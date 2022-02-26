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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16448_IMU.IMUAxis;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants;
import frc.robot.Constants.Drivetrain.MotorGroup;


public class Drivetrain extends SubsystemBase {
	// ----------------------------------------------------------
	// Motor group constants


	// 1 is not inverted, -1 is inverted
	// this multiplier is used to maintain the correct inversion when direct phoenix-level motor-setting is needed (like the setLeftMotors and setRightMotors functions)
	private double leftMotorsDirectionMultiplier = 1.d;
	private double rightMotorsDirectionMultiplier = 1.d;


	// ----------------------------------------------------------
	// Odometry constants


	private static DifferentialDriveOdometry m_odometry;


	// ----------------------------------------------------------
	// Kinematics constants


	// public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);
	public static final DifferentialDriveKinematics kDriveKinematicsV2 = new DifferentialDriveKinematics(Constants.Drivetrain.kTrackWidthMetersV2);


	// ----------------------------------------------------------
	// Resources


	private final WPI_TalonFX
		m_frontLeftMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kFrontLeft),
		m_backLeftMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kBackLeft);
	private MotorControllerGroup m_leftGroup = new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);

	private final WPI_TalonFX
		m_frontRightMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kFrontRight),
		m_backRightMotor = new WPI_TalonFX(Constants.Drivetrain.CAN_ID.kBackRight);
	private MotorControllerGroup m_rightGroup = new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);

	private DifferentialDrive m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);

	private final ADIS16448_IMU imu = new ADIS16448_IMU(ADIS16448_IMU.IMUAxis.kZ, SPI.Port.kMXP, ADIS16448_IMU.CalibrationTime._1s);
	private double
		m_filteredXAccelOffset = 0.d,
		m_filteredYAccelOffset = 0.d;

	private SlewRateLimiter
		m_arcadeDriveForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultArcadeForward),
		m_arcadeDriveTurnLimiter = new SlewRateLimiter(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultArcadeTurn),

		m_tankDriveLeftForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultTankForward),
		m_tankDriveRightForwardLimiter = new SlewRateLimiter(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultTankForward);


	// ----------------------------------------------------------
	// Constructor


	public Drivetrain() {
		imu.setYawAxis(IMUAxis.kZ);

		m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(imu.getAngle()));

		// ----------------------------------------------------------
		// Left motor group configuration

		m_frontLeftMotor.configFactoryDefault();
		m_backLeftMotor.configFactoryDefault();
		m_backRightMotor.follow(m_frontRightMotor);
		m_frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Drivetrain.kLeftPidIdx, Constants.Drivetrain.kTimeoutMs);
		// m_frontLeftMotor.config_kF(kLeftSlotIdx, kLeftMotorVelocityGainsV2.kF);
		m_frontLeftMotor.config_kP(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kLeftVelocityGainsV2.kP);
		// m_frontLeftMotor.config_kI(kLeftSlotIdx, kLeftMotorVelocityGainsV2.kI);
        // m_frontLeftMotor.config_kD(kLeftSlotIdx, kLeftMotorVelocityGainsV2.kD);

		// ----------------------------------------------------------
		// Right motor group configuration

		m_frontRightMotor.configFactoryDefault();
		m_backRightMotor.configFactoryDefault();
		m_backLeftMotor.follow(m_frontLeftMotor);
		m_frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Drivetrain.kRightPidIdx, Constants.Drivetrain.kTimeoutMs);
		// m_frontRightMotor.config_kF(kLeftSlotIdx, kRightMotorVelocityGainsV2.kF);
		m_frontRightMotor.config_kP(Constants.Drivetrain.kLeftSlotIdx, Constants.Drivetrain.kRightVelocityGainsV2.kP);
		// m_frontRightMotor.config_kI(kLeftSlotIdx, kRightMotorVelocityGainsV2.kI);
        // m_frontRightMotor.config_kD(kLeftSlotIdx, kRightMotorVelocityGainsV2.kD);

		resetEncoders();
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		m_odometry.update(getRotation2d(), getLeftDistanceMeters(), getRightDistanceMeters());

		SmartDashboard.putNumber("Yaw Axis", getRounded(imu.getAngle()));

		SmartDashboard.putNumber("Left Encoder", getLeftDistanceMeters());
		SmartDashboard.putNumber("Right Encoder", getRightDistanceMeters());

		SmartDashboard.putBoolean("Left is inverted", m_leftGroup.getInverted());
		SmartDashboard.putBoolean("Right is inverted", m_rightGroup.getInverted());
	}


	// ----------------------------------------------------------
	// Motor methods


	public Drivetrain setDeadband(double deadband) {
		m_differentialDrive.setDeadband(deadband);
		return this;
	}

	public Drivetrain useNormalMaximumOutput() {
		m_differentialDrive.setMaxOutput(Constants.Drivetrain.NormalOutputMode.kDefaultMaxOutput);
		return this;
	}

	public Drivetrain setMaxOutput(double maxOutput) {
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

				leftMotorsDirectionMultiplier = -1.d;
				rightMotorsDirectionMultiplier = 1.d;
				break;
			case kRight:
				m_leftGroup.setInverted(false);
				m_rightGroup.setInverted(true);

				leftMotorsDirectionMultiplier = 1.d;
				rightMotorsDirectionMultiplier = -1.d;
				break;
		}
		return this;
	}

	public Drivetrain reverseDrivetrain() {
		invertMotors();
		swapMotorGroups();
		return this;
	}

	private Drivetrain swapMotorGroups() {
		var tempLeftGroup = m_leftGroup;
		m_leftGroup = m_rightGroup;
		m_rightGroup = tempLeftGroup;

		m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);
		return this;
	}

	private Drivetrain invertMotors() {
		m_leftGroup.setInverted(!m_leftGroup.getInverted());
		m_rightGroup.setInverted(!m_rightGroup.getInverted());
		return this;
	}

	public Drivetrain useJoystickDrivingOpenLoopRamp() {
		setOpenLoopRampTimes(Constants.Drivetrain.kJoystickOpenLoopRampTime);
		return this;
	}

	public Drivetrain disableOpenLoopRamp() {
		setOpenLoopRampTimes(0.d);
		return this;
	}

	public Drivetrain setOpenLoopRampTimes(double timeInSeconds) {
		m_frontLeftMotor.configOpenloopRamp(timeInSeconds);
		m_backLeftMotor.configOpenloopRamp(timeInSeconds);

		m_frontRightMotor.configOpenloopRamp(timeInSeconds);
		m_backRightMotor.configOpenloopRamp(timeInSeconds);
		return this;
	}

	public Drivetrain setLeftMPS(double mps) {
		m_frontLeftMotor.set(ControlMode.Velocity, mps * Constants.Drivetrain.kMPSToTicksPer100msV2 * leftMotorsDirectionMultiplier);
		return this;
	}

	public Drivetrain setRightMPS(double mps) {
		m_frontRightMotor.set(ControlMode.Velocity, mps * Constants.Drivetrain.kMPSToTicksPer100msV2 * rightMotorsDirectionMultiplier);
		return this;
	}

	public double getLeftMPS() {
		return m_frontLeftMotor.getSelectedSensorVelocity(Constants.Drivetrain.kLeftPidIdx) / Constants.Drivetrain.kMPSToTicksPer100msV2 * leftMotorsDirectionMultiplier;
	}

	public double getRightMPS() {
		return m_frontRightMotor.getSelectedSensorVelocity(Constants.Drivetrain.kRightPidIdx) / Constants.Drivetrain.kMPSToTicksPer100msV2 * rightMotorsDirectionMultiplier;
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

	
	public void arcadeDrive(double forward, double rotation) {
		m_differentialDrive.arcadeDrive(forward, rotation);
	}

	public void tankDriveVolts(double leftVolts, double rightVolts) {
		m_leftGroup.setVoltage(leftVolts);
		m_rightGroup.setVoltage(rightVolts);
		m_differentialDrive.feed();
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		m_differentialDrive.tankDrive(leftSpeed, rightSpeed);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace) {
		m_differentialDrive.curvatureDrive(xSpeed, zRotation, allowTurnInPlace);
	}

	public void stopDrive() {
		tankDrive(0.d, 0.d);
		// m_leftGroup.stopMotor();
		// m_rightGroup.stopMotor();
	}


	// ----------------------------------------------------------
	// Slew rate limiters


	// Output-mode configurations

	public Drivetrain useNormalOutputModeSlewRates() {
		setArcadeDriveForwardLimiterRate(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultArcadeForward);
		setArcadeDriveTurnLimiterRate(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultArcadeTurn);

		setTankDriveLeftForwardLimiterRate(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultTankForward);
		setTankDriveRightForwardLimiterRate(Constants.Drivetrain.NormalOutputMode.SlewRates.kDefaultTankForward);
		return this;
	}

	public Drivetrain useKidsSafetyModeSlewRates() {
		setArcadeDriveForwardLimiterRate(Constants.Drivetrain.KidsSafetyOutputMode.SlewRates.kDefaultArcadeForward);
		setArcadeDriveTurnLimiterRate(Constants.Drivetrain.KidsSafetyOutputMode.SlewRates.kDefaultArcadeTurn);

		setTankDriveLeftForwardLimiterRate(Constants.Drivetrain.KidsSafetyOutputMode.SlewRates.kDefaultTankForward);
		setTankDriveRightForwardLimiterRate(Constants.Drivetrain.KidsSafetyOutputMode.SlewRates.kDefaultTankForward);
		return this;
	}

	// Arcade-drive limiters

	// there isn't a meethod in the SlewRateLimiter class in the WPILIB API to just change the rate :(
	public Drivetrain setArcadeDriveForwardLimiterRate(double rate) {
		m_arcadeDriveForwardLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterArcadeDriveForward(double inputSpeed) {
		return m_arcadeDriveForwardLimiter.calculate(inputSpeed);
	}

	public Drivetrain setArcadeDriveTurnLimiterRate(double rate) {
		m_arcadeDriveTurnLimiter = new SlewRateLimiter(rate);
		return this;
	}
	public double filterArcadeDriveTurn(double inputSpeed) {
		return m_arcadeDriveTurnLimiter.calculate(inputSpeed);
	}

	// Tank-drive limiters

	public void setTankDriveLeftForwardLimiterRate(double rate) {
		m_tankDriveLeftForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterTankDriveLeftForward(double inputSpeed) {
		return m_tankDriveLeftForwardLimiter.calculate(inputSpeed);
	}

	public void setTankDriveRightForwardLimiterRate(double rate) {
		m_tankDriveRightForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterTankDriveRightForward(double inputSpeed) {
		return m_tankDriveRightForwardLimiter.calculate(inputSpeed);
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
	private double getRounded(double input) {
		return Math.round(input * 100.0d) / 100.0d;
	}

	private double getXAccelAngle() {
		return imu.getXFilteredAccelAngle() - m_filteredXAccelOffset;
	}
	
	private double getYAccelAngle() {
		return imu.getYFilteredAccelAngle() - m_filteredYAccelOffset;
	}

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
		return m_frontLeftMotor.getSelectedSensorPosition() * Constants.Drivetrain.kTicksToMetersV2 * leftMotorsDirectionMultiplier;
	}

	public Drivetrain resetLeftEncoder() {
		m_frontLeftMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public double getRightDistanceMeters() {
		return m_frontRightMotor.getSelectedSensorPosition() * Constants.Drivetrain.kTicksToMetersV2 * rightMotorsDirectionMultiplier;
	}

	public Drivetrain resetRightEncoder() {
		m_frontRightMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public Drivetrain resetEncoders() {
		resetLeftEncoder();
		resetRightEncoder();
		return this;
	}

	// always returns a positive value
	public double getAverageDistance() {
		return (Math.abs(getLeftDistanceMeters()) + Math.abs(getRightDistanceMeters())) / 2.0d;
	}
}