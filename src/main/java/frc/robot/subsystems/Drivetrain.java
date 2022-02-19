package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16448_IMU.IMUAxis;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Gains;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain.NormalOutputMode.SlewRates;


public class Drivetrain extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public enum MotorGroup {
		LEFT,
		RIGHT
	}

	// Open-loop control constants
	public static final double
		// units in seconds
		JOYSTICK_DRIVING_OPEN_LOOP_TIME = 0.7d;

	public static final double MAXIMUM_SLEW_RATE_ALLOWED = 3.d;

	public static class NormalOutputMode {
		public static final double DEFAULT_MAXIMUM_OUTPUT = 1.d;

		public static class SlewRates {
			public static final double
				DEFAULT_ARCADE_DRIVE_FORWARD = 1.58d,
				DEFAULT_ARCADE_DRIVE_TURN = 2.08d,
	
				DEFAULT_TANK_DRIVE_FORWARD = 1.0d;
		}
	}

	public static class KidsSafetyOutputMode {
		public static final double DEFAULT_MAXIMUM_OUTPUT = 0.1d;

		public static class SlewRates {
			public static final double
				DEFAULT_ARCADE_DRIVE_FORWARD = 2.d,
				DEFAULT_ARCADE_DRIVE_TURN = 2.d,

				DEFAULT_TANK_DRIVE_FORWARD = 2.d;
		}
	}


	// ----------------------------------------------------------
	// ID constants


	private static class CAN_IDs {
		// These are how it's SUPPOSED to be on both V1 and V2
		private static final int
			FRONT_LEFT = 3,
			BACK_LEFT = 2,
			FRONT_RIGHT = 4,
			BACK_RIGHT = 5;
	}


	// ----------------------------------------------------------
	// Odometry constants


	private static DifferentialDriveOdometry m_odometry;


	// ----------------------------------------------------------
	// Kinematics constants


	// horizontal distance between the left and right-side wheels
	private static final double kTrackWidthMeters = 0.62484;
	private static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);

	private static final double
		kMaxSpeedMetersPerSecond = 3.d,
    	kMaxAccelerationMetersPerSecondSquared = 3.d;


	// ----------------------------------------------------------
	// Ramsete controller constsants


	private static final double
		kRamseteB = 2,
		kRamseteZeta = 0.7;


	// ----------------------------------------------------------
	// Conversion constants


	private static final double
		// 2048 ticks in 1 revolution for Falcon 500s
		// wheel diameter * pi = circumference of 1 revolution
		// wheel diameter is 6 inches (which is 0.1524 meters)
		// 7.33 to 1 gearbox is big to small gear (means more torque)
		kTicksToMeters  = ( (0.1524d * Math.PI) / 2048.0d ) / 7.33d,
		kMetersPerSecondToTicksPer100ms = 427.7550177d;
	

	// ----------------------------------------------------------
	// Closed-loop control constants


	private static final double
		// Feedforward gains
		ksVolts = 0.67701,
		kvVoltSecondsPerMeter = 0.041828,
		kaVoltSecondsSquaredPerMeter = 0.020568,
		
		// Feedback gains
		kPDriveVel = 0.96111;

	private static final int
		kLeftSlotIdx = 0,
		kRightSlotIdx = 0,
		// Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
		kTimeoutMs = 30;

	// ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output

	private static final Gains kLeftMotorVelocityGains
		// = new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
		// kP, kI, kD, kF, kIzone, kPeakOutput
		= new Gains(0.96111d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d);
	
	private static final Gains kRightMotorVelocityGains 
		// = new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
		// kP, kI, kD, kF, kIzone, kPeakOutput
		= new Gains(0.96111d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d);


	// ----------------------------------------------------------
	// Resources


	private final WPI_TalonFX
		m_frontLeftMotor = new WPI_TalonFX(CAN_IDs.FRONT_LEFT),
		m_backLeftMotor = new WPI_TalonFX(CAN_IDs.BACK_LEFT);
	private MotorControllerGroup m_leftGroup = new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);

	private final WPI_TalonFX
		m_frontRightMotor = new WPI_TalonFX(CAN_IDs.FRONT_RIGHT),
		m_backRightMotor = new WPI_TalonFX(CAN_IDs.BACK_RIGHT);
	private MotorControllerGroup m_rightGroup = new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);

	private DifferentialDrive m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);

	private final ADIS16448_IMU imu = new ADIS16448_IMU(ADIS16448_IMU.IMUAxis.kZ, SPI.Port.kMXP, ADIS16448_IMU.CalibrationTime._1s);
	private double
		m_filteredXAccelOffset = 0.d,
		m_filteredYAccelOffset = 0.d;

	private SlewRateLimiter
		m_arcadeDriveForwardLimiter = new SlewRateLimiter(SlewRates.DEFAULT_ARCADE_DRIVE_FORWARD),
		m_arcadeDriveTurnLimiter = new SlewRateLimiter(SlewRates.DEFAULT_ARCADE_DRIVE_TURN),

		m_tankDriveLeftForwardLimiter = new SlewRateLimiter(SlewRates.DEFAULT_TANK_DRIVE_FORWARD),
		m_tankDriveRightForwardLimiter = new SlewRateLimiter(SlewRates.DEFAULT_TANK_DRIVE_FORWARD);


	// ----------------------------------------------------------
	// Constructor


	public Drivetrain() {
		imu.setYawAxis(IMUAxis.kZ);

		m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(imu.getAngle()));

		// ----------------------------------------------------------
		// Initialize motor controllers and followers

		m_frontLeftMotor.configFactoryDefault();
		m_backLeftMotor.configFactoryDefault();
		m_frontRightMotor.configFactoryDefault();
		m_backRightMotor.configFactoryDefault();

		m_backLeftMotor.follow(m_frontLeftMotor);
		m_backRightMotor.follow(m_frontRightMotor);

		m_frontLeftMotor.setInverted(true);

		// ----------------------------------------------------------
		// Config closed-loop controls

		m_frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		// m_frontLeftMotor.config_kF(kLeftSlotIdx, kLeftMotorVelocityGains.kF);
		m_frontLeftMotor.config_kP(kLeftSlotIdx, kLeftMotorVelocityGains.kP);
		// m_frontLeftMotor.config_kI(kLeftSlotIdx, kLeftMotorVelocityGains.kI);
        // m_frontLeftMotor.config_kD(kLeftSlotIdx, kLeftMotorVelocityGains.kD);

		m_frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		// m_frontRightMotor.config_kF(kLeftSlotIdx, kRightMotorVelocityGains.kF);
		m_frontRightMotor.config_kP(kLeftSlotIdx, kRightMotorVelocityGains.kP);
		// m_frontRightMotor.config_kI(kLeftSlotIdx, kRightMotorVelocityGains.kI);
        // m_frontRightMotor.config_kD(kLeftSlotIdx, kRightMotorVelocityGains.kD);

		resetEncoders();
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		m_odometry.update(getRotation2d(), getLeftDistanceMeters(), getRightDistanceMeters());

		SmartDashboard.putNumber("Yaw Axis", getRounded(imu.getAngle()));
		SmartDashboard.putNumber("Temperature from IMU", getRounded(imu.getTemperature()));
			
		SmartDashboard.putNumber("Filtered Accel X", getRounded(getXFilteredAccelAngle()));
		SmartDashboard.putNumber("Filtered Accel Y", getRounded(getYFilteredAccelAngle()));

		SmartDashboard.putNumber("Gyro X", getRounded(imu.getGyroAngleX()));
		SmartDashboard.putNumber("Gyro Y", getRounded(imu.getGyroAngleY()));
		SmartDashboard.putNumber("Gyro Z", getRounded(imu.getGyroAngleZ()));
	}


	// ----------------------------------------------------------
	// Motor methods


	public Drivetrain setDeadband(double deadband) {
		m_differentialDrive.setDeadband(deadband);
		return this;
	}

	public Drivetrain useNormalMaximumOutput() {
		m_differentialDrive.setMaxOutput(Drivetrain.NormalOutputMode.DEFAULT_MAXIMUM_OUTPUT);
		return this;
	}

	public Drivetrain setMaxOutput(double maxOutput) {
		m_differentialDrive.setMaxOutput(maxOutput);
		return this;
	}

	public Drivetrain setOnlyMotorGroupToInverted(MotorGroup motorGroup) {
		if (motorGroup == MotorGroup.LEFT) {	// for V2
			m_leftGroup.setInverted(true);
			m_rightGroup.setInverted(false);
		} else {								// for V1
			m_leftGroup.setInverted(false);
			m_rightGroup.setInverted(true);
		}
		return this;
	}

	public Drivetrain swapMotorGroups() {
		var tempLeftGroup = m_leftGroup;
		m_leftGroup = m_rightGroup;
		m_rightGroup = tempLeftGroup;

		m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);
		return this;
	}

	public Drivetrain invertLeftAndRightMotorGroups() {
		m_leftGroup.setInverted(!m_leftGroup.getInverted());
		m_rightGroup.setInverted(!m_rightGroup.getInverted());
		return this;
	}

	public Drivetrain useJoystickDrivingOpenLoopRamp() {
		setOpenLoopRampTimes(JOYSTICK_DRIVING_OPEN_LOOP_TIME);
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

	public Drivetrain setLeftMotors(double velocity) {
		m_frontLeftMotor.set(ControlMode.Velocity, velocity);
		return this;
	}

	public Drivetrain setRightMotors(double velocity) {
		m_frontRightMotor.set(ControlMode.Velocity, velocity);
		return this;
	}

	public double getLeftPercent() {
		return m_frontLeftMotor.getMotorOutputPercent();
	}

	public double getRightPercent() {
		return m_frontRightMotor.getMotorOutputPercent();
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
			m_frontLeftMotor.getSelectedSensorVelocity(kLeftSlotIdx),
			m_frontRightMotor.getSelectedSensorVelocity(kRightSlotIdx));
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
		// TODO: P1 Why do the V1 motor groups need to be swapped???

		if (RobotContainer.usingV1Drivetrain) {
			m_differentialDrive.tankDrive(rightSpeed, leftSpeed);
		} else {
			m_differentialDrive.tankDrive(leftSpeed, rightSpeed);
		}
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace) {
		m_differentialDrive.curvatureDrive(xSpeed, zRotation, allowTurnInPlace);
	}

	public void stopDrive() {
		tankDrive(0.d, 0.d);
		m_leftGroup.stopMotor();
		m_rightGroup.stopMotor();
	}


	// ----------------------------------------------------------
	// Slew rate limiters


	// Output-mode configurations

	public Drivetrain useNormalOutputModeSlewRates() {
		setArcadeDriveForwardLimiterRate(NormalOutputMode.SlewRates.DEFAULT_ARCADE_DRIVE_FORWARD);
		setArcadeDriveTurnLimiterRate(NormalOutputMode.SlewRates.DEFAULT_ARCADE_DRIVE_TURN);

		setTankDriveLeftForwardLimiterRate(NormalOutputMode.SlewRates.DEFAULT_TANK_DRIVE_FORWARD);
		setTankDriveRightForwardLimiterRate(NormalOutputMode.SlewRates.DEFAULT_TANK_DRIVE_FORWARD);
		return this;
	}

	public Drivetrain useKidsSafetyModeSlewRates() {
		setArcadeDriveForwardLimiterRate(KidsSafetyOutputMode.SlewRates.DEFAULT_ARCADE_DRIVE_FORWARD);
		setArcadeDriveTurnLimiterRate(KidsSafetyOutputMode.SlewRates.DEFAULT_ARCADE_DRIVE_TURN);

		setTankDriveLeftForwardLimiterRate(KidsSafetyOutputMode.SlewRates.DEFAULT_TANK_DRIVE_FORWARD);
		setTankDriveRightForwardLimiterRate(KidsSafetyOutputMode.SlewRates.DEFAULT_TANK_DRIVE_FORWARD);
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


	public Drivetrain calibrateIMU() {
		imu.calibrate();	// just filters out noise (robot must be still)
		
		m_filteredXAccelOffset = imu.getXFilteredAccelAngle();
		m_filteredYAccelOffset = imu.getYFilteredAccelAngle();
		
		return this;
	}

	public Drivetrain resetIMU() {
		imu.reset();		// zeros out current measurements (basically sets all sensor readings at current location as the "origin")
		return this;
	}

	public Rotation2d getRotation2d() {
		return Rotation2d.fromDegrees(imu.getAngle());
	}

	// rounds to two decimals
	private double getRounded(double input) {
		return Math.round(input * 100.0d) / 100.0d;
	}

	private double getXFilteredAccelAngle() {
		return imu.getXFilteredAccelAngle() - m_filteredXAccelOffset;
	}
	
	private double getYFilteredAccelAngle() {
		return imu.getYFilteredAccelAngle() - m_filteredYAccelOffset;
	}


	// ----------------------------------------------------------
	// Encoder methods
	

	public double getLeftDistanceMeters() {
		return m_frontLeftMotor.getSelectedSensorPosition() * kTicksToMeters;
	}

	public Drivetrain resetLeftEncoder() {
		m_frontLeftMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public double getRightDistanceMeters() {
		return m_frontRightMotor.getSelectedSensorPosition() * kTicksToMeters;
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

	public double getAverageDistance() {
		return (getRightDistanceMeters() + getLeftDistanceMeters()) / 2.0d;
	}
}