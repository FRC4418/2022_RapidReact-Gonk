package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16448_IMU.IMUAxis;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants;
import frc.robot.Constants.Drivetrain.MotorGroup;
import frc.robot.RobotContainer.TeamRobot;


public class Drivetrain extends SubsystemBase {
	// ----------------------------------------------------------
	// Motors and drivetrain


	private final WPI_TalonFX
		m_frontLeftMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kFrontLeft),
		m_backLeftMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kBackLeft);
	private MotorControllerGroup m_leftGroup = new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);

	private final WPI_TalonFX
		m_frontRightMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kFrontRight),
		m_backRightMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kBackRight);
	private MotorControllerGroup m_rightGroup = new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);

	private boolean m_reverseDrivetrain = false;

	// 1 is not inverted, -1 is inverted
	// this multiplier is used to maintain the correct inversion when direct phoenix-level motor-setting is needed (like the setLeftMotors and setRightMotors functions)
	private double
		leftMotorsDirectionMultiplier = 1.,
		rightMotorsDirectionMultiplier = 1.;

	private DifferentialDrive m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);

	private boolean usingTeleopOpenLoopRamp = false;


	// ----------------------------------------------------------
	// Odometry, kinematics, and IMU


	private static DifferentialDriveOdometry m_odometry;

	public static DifferentialDriveKinematics kDriveKinematics;

	private final ADIS16448_IMU imu = new ADIS16448_IMU(ADIS16448_IMU.IMUAxis.kZ, SPI.Port.kMXP, ADIS16448_IMU.CalibrationTime._1s);
	
	private double
		m_filteredXAccelOffset = 0.,
		m_filteredYAccelOffset = 0.;


	// ----------------------------------------------------------
	// Polynomial ramp functions (ax^b)


	private double
		curvatureForwardMultiplier = Constants.Drivetrain.CurvaturePolynomial.kForwardMultiplier,
		curvatureForwardExponential = Constants.Drivetrain.CurvaturePolynomial.kForwardExponential,

		curvatureRotationMultiplier = Constants.Drivetrain.CurvaturePolynomial.kRotationMultiplier,
		curvatureRotationExponential = Constants.Drivetrain.CurvaturePolynomial.kRotationExponential,

		arcadeForwardMultiplier = Constants.Drivetrain.ArcadePolynomial.kForwardMultiplier,
		arcadeForwardExponential = Constants.Drivetrain.ArcadePolynomial.kForwardExponential,

		arcadeTurnMultiplier = Constants.Drivetrain.ArcadePolynomial.kTurnMultiplier,
		arcadeTurnExponential = Constants.Drivetrain.ArcadePolynomial.kTurnExponential,

		tankForwardMultiplier = Constants.Drivetrain.TankPolynomial.kForwardMultiplier,
		tankForwardExponential = Constants.Drivetrain.TankPolynomial.kForwardExponential;


	// ----------------------------------------------------------
	// Slew rate limiters

	
	private boolean useSlewRateLimiters = Constants.Drivetrain.kUseSlewRateLimiters;

	private SlewRateLimiter
		m_curvatureForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kCurvatureForward),
		m_curvatureRotationLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kCurvatureRotation),

		m_arcadeForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kArcadeForward),
		m_arcadeTurnLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kArcadeTurn),

		m_tankLeftForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kTankForward),
		m_tankRightForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.SlewRates.kTankForward);


	// ----------------------------------------------------------
	// Constructor


	public Drivetrain() {
		configureDriveKinematics();

		imu.setYawAxis(IMUAxis.kZ);

		m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(imu.getAngle()));

		// ----------------------------------------------------------
		// Left motor group configuration

		m_frontLeftMotor.configFactoryDefault();
		m_backLeftMotor.configFactoryDefault();
		m_backRightMotor.follow(m_frontRightMotor);
		m_frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Drivetrain.kLeftPidIdx, Constants.Drivetrain.kTimeoutMs);

		// ----------------------------------------------------------
		// Right motor group configuration

		m_frontRightMotor.configFactoryDefault();
		m_backRightMotor.configFactoryDefault();
		m_backLeftMotor.follow(m_frontLeftMotor);
		m_frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Drivetrain.kRightPidIdx, Constants.Drivetrain.kTimeoutMs);

		// ----------------------------------------------------------
		// Final setup

		configureMotorPIDs();
		resetEncoders();
	}


	// ----------------------------------------------------------
	// Constants-reconfiguration methods


	public static void configureDriveKinematics() {
		kDriveKinematics = new DifferentialDriveKinematics(Constants.Drivetrain.kTrackWidthMeters);
	}

	public Drivetrain configureMotorPIDs() {
		m_frontLeftMotor.config_kP(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kLeftVelocityGains.kP);
		m_frontLeftMotor.config_kI(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kLeftVelocityGains.kI);
        m_frontLeftMotor.config_kD(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kLeftVelocityGains.kD);
		// m_frontLeftMotor.config_kF(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kLeftVelocityGains.kF);

		m_frontRightMotor.config_kP(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kRightVelocityGains.kP);
		m_frontRightMotor.config_kI(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kRightVelocityGains.kI);
        m_frontRightMotor.config_kD(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kRightVelocityGains.kD);
		// m_frontRightMotor.config_kF(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kRightVelocityGains.kF);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		m_odometry.update(getRotation2d(), getLeftDistanceMeters(), getRightDistanceMeters());
	}


	// ----------------------------------------------------------
	// Motor methods


	public Drivetrain setDeadband(double deadband) {
		m_differentialDrive.setDeadband(deadband);
		return this;
	}

	public Drivetrain useDefaultMaxOutput() {
		m_differentialDrive.setMaxOutput(Constants.Drivetrain.kMaxOutput);
		return this;
	}

	public Drivetrain setMaxOutput(double maxOutput) {
		Constants.Drivetrain.kMaxOutput = maxOutput;
		m_differentialDrive.setMaxOutput(maxOutput);
		return this;
	}

	// weird name 'setONLYMotorGroupToInverted' means that ONLY the given motor group should be inverted
	public Drivetrain setOnlyMotorGroupToInverted(MotorGroup motorGroup) {
		switch (motorGroup) {
			default:
				DriverStation.reportError("Unsupported motor group detected in setOnlyMotorGroupToInverted", true);
				break;
			case kLeft:
				m_leftGroup.setInverted(true);
				m_rightGroup.setInverted(false);

				leftMotorsDirectionMultiplier = -1.;
				rightMotorsDirectionMultiplier = 1.;
				break;
			case kRight:
				m_leftGroup.setInverted(false);
				m_rightGroup.setInverted(true);

				leftMotorsDirectionMultiplier = 1.;
				rightMotorsDirectionMultiplier = -1.;
				break;
		}
		return this;
	}

	public boolean isReversed() {
		return m_reverseDrivetrain;
	}
	public Drivetrain reverseDrivetrain() {
		m_reverseDrivetrain = !m_reverseDrivetrain;
		return this;
	}

	public Drivetrain useTeleopOpenLoopRamp() {
		setOpenLoopRampTimes(Constants.Drivetrain.kTeleopOpenLoopRampTime);
		usingTeleopOpenLoopRamp = true;
		return this;
	}

	public Drivetrain updateTeleopOpenLoopRampTime(double openLoopRampTime) {
		Constants.Drivetrain.kTeleopOpenLoopRampTime = openLoopRampTime;
		if (usingTeleopOpenLoopRamp) {
			setOpenLoopRampTimes(openLoopRampTime);
		}
		return this;
	}

	public Drivetrain disableOpenLoopRamp() {
		setOpenLoopRampTimes(0.);
		usingTeleopOpenLoopRamp = false;
		return this;
	}

	public Drivetrain setOpenLoopRampTimes(double timeInSeconds) {
		m_frontLeftMotor.configOpenloopRamp(timeInSeconds);
		m_backLeftMotor.configOpenloopRamp(timeInSeconds);

		m_frontRightMotor.configOpenloopRamp(timeInSeconds);
		m_backRightMotor.configOpenloopRamp(timeInSeconds);
		return this;
	}

	public double getLeftMPS() {
		return m_frontLeftMotor.getSelectedSensorVelocity(Constants.Drivetrain.kLeftPidIdx) / Constants.Drivetrain.kMPSToTicksPer100ms * leftMotorsDirectionMultiplier;
	}

	public double getRightMPS() {
		return m_frontRightMotor.getSelectedSensorVelocity(Constants.Drivetrain.kRightPidIdx) / Constants.Drivetrain.kMPSToTicksPer100ms * rightMotorsDirectionMultiplier;
	}

	public Drivetrain brakeMotors() {
		m_frontLeftMotor.setNeutralMode(NeutralMode.Brake);
		m_backLeftMotor.setNeutralMode(NeutralMode.Brake);

		m_frontRightMotor.setNeutralMode(NeutralMode.Brake);
		m_backRightMotor.setNeutralMode(NeutralMode.Brake);
		return this;
	}

	public Drivetrain coastMotors() {
		m_frontLeftMotor.setNeutralMode(NeutralMode.Coast);
		m_backLeftMotor.setNeutralMode(NeutralMode.Coast);

		m_frontRightMotor.setNeutralMode(NeutralMode.Coast);
		m_backRightMotor.setNeutralMode(NeutralMode.Coast);
		return this;
	}


	// ----------------------------------------------------------
	// Odometry methods


	public Pose2d getPose() {
		return m_odometry.getPoseMeters();
	}

	public DifferentialDriveWheelSpeeds getWheelSpeeds() {
		return new DifferentialDriveWheelSpeeds(
			m_frontLeftMotor.getSelectedSensorVelocity(Constants.Drivetrain.kLeftSlotIdx),
			m_frontRightMotor.getSelectedSensorVelocity(Constants.Drivetrain.kRightSlotIdx));
	}

	public void resetOdometry(Pose2d pose) {
		resetEncoders();
		m_odometry.resetPosition(pose, getRotation2d());
	}


	// ----------------------------------------------------------
	// Drive methods


	public Drivetrain configureDrivetrain(TeamRobot teamRobot) {
		switch (teamRobot) {
			default:
				DriverStation.reportError("Unsupported robot selection found while configuring the robot-specific drivetrain", true);
				break;
			case VERSACHASSIS_TWO:
				setOnlyMotorGroupToInverted(MotorGroup.kLeft);
				break;
			case VERSACHASSIS_ONE:
				setOnlyMotorGroupToInverted(MotorGroup.kRight);
				break;
		}
		return this;
	}

	public void arcadeDrive(double forward, double turn) {
		forward = Math.pow(arcadeForwardMultiplier * forward, arcadeForwardExponential);
		turn = Math.pow(arcadeTurnMultiplier * turn, arcadeTurnExponential);

		if (!m_reverseDrivetrain) {
			m_differentialDrive.arcadeDrive(forward, turn);
		} else {
			m_differentialDrive.arcadeDrive(-forward, turn);
		}
		m_differentialDrive.feed();
	}

	public Drivetrain tankDriveMPS(double leftMPS, double rightMPS) {
		m_frontLeftMotor.set(ControlMode.Velocity,
			leftMPS * Constants.Drivetrain.kDrivetrainMPSReductionRatio
			* Constants.Drivetrain.kMPSToTicksPer100ms
			* leftMotorsDirectionMultiplier);
		m_frontRightMotor.set(ControlMode.Velocity,
			rightMPS * Constants.Drivetrain.kDrivetrainMPSReductionRatio
			* Constants.Drivetrain.kMPSToTicksPer100ms
			* rightMotorsDirectionMultiplier);
		
		m_differentialDrive.feed();
		return this;
	}

	public void tankDriveVolts(double leftVolts, double rightVolts) {
		leftVolts = Math.pow(tankForwardMultiplier * leftVolts, tankForwardExponential);
		rightVolts = Math.pow(tankForwardMultiplier * rightVolts, tankForwardExponential);

		if (!m_reverseDrivetrain) {
			m_leftGroup.setVoltage(leftVolts);
			m_rightGroup.setVoltage(rightVolts);
		} else {
			m_leftGroup.setVoltage(-rightVolts);
			m_rightGroup.setVoltage(-leftVolts);
		}
		m_differentialDrive.feed();
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed = Math.pow(tankForwardMultiplier * leftSpeed, tankForwardExponential);
		rightSpeed = Math.pow(tankForwardMultiplier * rightSpeed, tankForwardExponential);

		if (!m_reverseDrivetrain) {
			m_differentialDrive.tankDrive(leftSpeed, rightSpeed);
		} else {
			m_differentialDrive.tankDrive(-rightSpeed, -leftSpeed);
		}
		m_differentialDrive.feed();
	}

	public void curvatureDrive(double forward, double rotation, boolean allowTurnInPlace) {
		forward = Math.pow(curvatureForwardMultiplier * forward, curvatureForwardExponential);
		rotation = Math.pow(curvatureRotationMultiplier * rotation, curvatureRotationExponential);

		if (!m_reverseDrivetrain) {
			m_differentialDrive.curvatureDrive(forward, rotation, allowTurnInPlace);
		} else {
			m_differentialDrive.curvatureDrive(-forward, rotation, allowTurnInPlace);
		}
		m_differentialDrive.feed();
	}

	public void stopDrive() {
		tankDrive(0., 0.);
	}


	// ----------------------------------------------------------
	// Polynomial ramp setters


	public void setArcadeForwardMultiplier(double multiplier) {
		Constants.Drivetrain.ArcadePolynomial.kForwardMultiplier = multiplier;
		arcadeForwardMultiplier = multiplier;
	}

	public void setArcadeForwardExponential(double exponential) {
		arcadeForwardExponential = exponential;
	}

	public void setArcadeTurnMultiplier(double multiplier) {
		arcadeTurnMultiplier = multiplier;
	}

	public void setArcadeTurnExponential(double exponential) {
		arcadeForwardExponential = exponential;
	}

	public void setCurvatureForwardMultiplier(double multiplier) {
		curvatureForwardMultiplier = multiplier;
	}

	public void setCurvatureForwardExponential(double exponential) {
		curvatureForwardExponential = exponential;
	}

	public void setCurvatureRotationMultiplier(double multiplier) {
		curvatureRotationMultiplier = multiplier;
	}

	public void setCurvatureRotationExponential(double exponential) {
		curvatureRotationExponential = exponential;
	}

	public void setTankForwardMultiplier(double multiplier) {
		tankForwardMultiplier = multiplier;
	}

	public void setTankForwardExponential(double exponential) {
		tankForwardExponential = exponential;
	}


	// ----------------------------------------------------------
	// Slew rate limiters


	public boolean usingSlewRateLimiters() {
		return useSlewRateLimiters;
	}

	public Drivetrain setUseSlewRateLimiters(boolean bool) {
		useSlewRateLimiters = bool;
		return this;
	}

	// Output-mode configurations

	public Drivetrain useDefaultSlewRates() {
		setArcadeForwardLimiterRate(Constants.Drivetrain.SlewRates.kArcadeForward);
		setArcadeTurnLimiterRate(Constants.Drivetrain.SlewRates.kArcadeTurn);

		setTankLeftForwardLimiterRate(Constants.Drivetrain.SlewRates.kTankForward);
		setTankRightForwardLimiterRate(Constants.Drivetrain.SlewRates.kTankForward);
		return this;
	}

	// Curvature-drive slew-rate limiters

	public Drivetrain setCurvatureForwardLimiterRate(double rate) {
		m_curvatureForwardLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterCurvatureForward(double forward) {
		return m_curvatureForwardLimiter.calculate(forward);
	}

	public Drivetrain setCurvatureRotationLimiterRate(double rate) {
		m_curvatureRotationLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterCurvatureRotation(double rotation) {
		return m_curvatureRotationLimiter.calculate(rotation);
	}

	// Arcade-drive slew-rate limiters

	// there isn't a meethod in the SlewRateLimiter class in the WPILIB API to just change the rate :(
	public Drivetrain setArcadeForwardLimiterRate(double rate) {
		m_arcadeForwardLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterArcadeForward(double forward) {
		return m_arcadeForwardLimiter.calculate(forward);
	}

	public Drivetrain setArcadeTurnLimiterRate(double rate) {
		m_arcadeTurnLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterArcadeTurn(double turn) {
		return m_arcadeTurnLimiter.calculate(turn);
	}

	// Tank-drive slew-rate limiters

	public void setTankLeftForwardLimiterRate(double rate) {
		m_tankLeftForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterTankLeftForward(double forward) {
		return m_tankLeftForwardLimiter.calculate(forward);
	}

	public void setTankRightForwardLimiterRate(double rate) {
		m_tankRightForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterTankRightForward(double forward) {
		return m_tankRightForwardLimiter.calculate(forward);
	}


	// ----------------------------------------------------------
	// IMU methods


	// heading increases (positive) when turning counter-clockwise
	public double getHeading() {
		return imu.getAngle();
	}

	public Rotation2d getRotation2d() {
		return Rotation2d.fromDegrees(imu.getAngle());
	}

	public double getTurnRate() {
		return imu.getRate();	// degrees per second
	}

	// rounds to two decimals
	// private double getRounded(double input) {
	// 	return Math.round(input * 100.0) / 100.0;
	// }

	// private double getXAccelAngle() {
	// 	return imu.getXFilteredAccelAngle() - m_filteredXAccelOffset;
	// }
	
	// private double getYAccelAngle() {
	// 	return imu.getYFilteredAccelAngle() - m_filteredYAccelOffset;
	// }

	public Drivetrain calibrateIMU() {
		imu.calibrate();	// just filters out noise (robot must be still)
		
		m_filteredXAccelOffset = imu.getXFilteredAccelAngle();
		m_filteredYAccelOffset = imu.getYFilteredAccelAngle();
		
		return this;
	}

	public Drivetrain resetIMU() {
		// zeros out current measurements (basically sets all sensor readings at current location as the "origin")
		imu.reset();
		return this;
	}


	// ----------------------------------------------------------
	// Encoder methods
	

	public double getLeftDistanceMeters() {
		return 
			m_frontLeftMotor.getSelectedSensorPosition() 
			/ Constants.Drivetrain.kDrivetrainMPSReductionRatio
			* Constants.Drivetrain.kTicksToMeters
			* leftMotorsDirectionMultiplier;
	}

	public Drivetrain resetLeftEncoder() {
		m_frontLeftMotor.setSelectedSensorPosition(0.);
		return this;
	}

	public double getRightDistanceMeters() {
		return
			m_frontRightMotor.getSelectedSensorPosition()
			/ Constants.Drivetrain.kDrivetrainMPSReductionRatio
			* Constants.Drivetrain.kTicksToMeters
			* rightMotorsDirectionMultiplier;
	}

	public Drivetrain resetRightEncoder() {
		m_frontRightMotor.setSelectedSensorPosition(0.);
		return this;
	}

	public Drivetrain resetEncoders() {
		resetLeftEncoder();
		resetRightEncoder();
		return this;
	}

	// always returns a positive value
	public double getAverageDistanceMeters() {
		return (Math.abs(getLeftDistanceMeters()) + Math.abs(getRightDistanceMeters())) / 2.0;
	}
}