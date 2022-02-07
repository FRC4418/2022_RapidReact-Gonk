package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


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
		// TODO: P1 Put drivetrain open-loop ramp time in a diagnostics display
		SHARED_RAMP_TIME = 2.d;


	// ----------------------------------------------------------
	// Private constants


	// ID and encoder constants
	// These are how it's SUPPOSED to be on both V1 and V2
	private static final int
		FRONT_LEFT_CAN_ID = 3,
		BACK_LEFT_CAN_ID = 2,
		FRONT_RIGHT_CAN_ID = 4,
		BACK_RIGHT_CAN_ID = 5;

	private static final double
		// 2048 ticks in 1 revolution for Falcon 500s
		// wheel diameter * pi = circumference of 1 revolution
		// 1 to 7.33 gearbox is big to small gear (means more speed)
		TICKS_TO_INCHES_CONVERSION  = ( (6.0d * Math.PI) / 2048.0d ) / 7.33d;
	

	// Closed-loop control constants

	// // the PID slot to pull gains from. Starting 2018, there is 0,1,2 or 3. Only 0 and 1 are visible in web-based configuration
	// private static final int kSlotIdx = 0;

	// // Talon FX supports multiple (cascaded) PID loops. For now we just want the primary one.
	// private static final int kIdx = 0;

	// // Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
	// private static final int kTimeoutMs = 30;

	// // ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output

	// private static final Gains kLeftMotorVelocityGains
	// 	//			kP		kI		kD		kF				Iz		Peakout
	// 	= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
	
	// private static final Gains kRightMotorVelocityGains 
	// 	//			kP		kI		kD		kF				Iz		Peakout
	// 	= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);


	// ----------------------------------------------------------
	// Resources


	private final WPI_TalonFX m_frontLeftMotor = new WPI_TalonFX(FRONT_LEFT_CAN_ID);
	private final WPI_TalonFX m_backLeftMotor = new WPI_TalonFX(BACK_LEFT_CAN_ID);
	private MotorControllerGroup m_leftGroup = new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);

	private final WPI_TalonFX m_frontRightMotor = new WPI_TalonFX(FRONT_RIGHT_CAN_ID);
	private final WPI_TalonFX m_backRightMotor = new WPI_TalonFX(BACK_RIGHT_CAN_ID);
	private MotorControllerGroup m_rightGroup = new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);

	private DifferentialDrive m_differentialDrive = new DifferentialDrive(m_leftGroup, m_rightGroup);

	private SlewRateLimiter m_arcadeDriveForwardLimiter = new SlewRateLimiter(0.5d);
	private SlewRateLimiter m_arcadeDriveTurnLimiter = new SlewRateLimiter(0.5d);

	private SlewRateLimiter m_tankDriveForwardLimiter = new SlewRateLimiter(0.d);


	// ----------------------------------------------------------
	// Constructor


	public Drivetrain() {
		// ----------------------------------------------------------
		// Initialize motor controllers and followers

		m_frontLeftMotor.configFactoryDefault();
		m_backLeftMotor.configFactoryDefault();
		m_frontRightMotor.configFactoryDefault();
		m_backRightMotor.configFactoryDefault();

		m_backLeftMotor.follow(m_frontLeftMotor);
		m_backRightMotor.follow(m_frontRightMotor);

		// ----------------------------------------------------------
		// Config open-loop controls

		// m_frontLeftMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		// m_backLeftMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		// m_frontRightMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		// m_backRightMotor.configOpenloopRamp(SHARED_RAMP_TIME);

		// ----------------------------------------------------------
		// Config closed-loop controls

		// frontLeftDriveMotor.config_kF(PID.kIdx, PID.kLeftMotorVelocityGains.kF, PID.kTimeoutMs);
		// frontLeftDriveMotor.config_kP(PID.kIdx, PID.kLeftMotorVelocityGains.kP, PID.kTimeoutMs);
		// frontLeftDriveMotor.config_kI(PID.kIdx, PID.kLeftMotorVelocityGains.kI, PID.kTimeoutMs);
        // frontLeftDriveMotor.config_kD(PID.kIdx, PID.kLeftMotorVelocityGains.kD, PID.kTimeoutMs);

		// frontRightDriveMotor.config_kF(PID.kIdx, PID.kRightMotorVelocityGains.kF, PID.kTimeoutMs);
		// frontRightDriveMotor.config_kP(PID.kIdx, PID.kRightMotorVelocityGains.kP, PID.kTimeoutMs);
		// frontRightDriveMotor.config_kI(PID.kIdx, PID.kRightMotorVelocityGains.kI, PID.kTimeoutMs);
        // frontRightDriveMotor.config_kD(PID.kIdx, PID.kRightMotorVelocityGains.kD, PID.kTimeoutMs);

		// ----------------------------------------------------------
		// Config integrated sensors (built-in encoders)

		m_frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		m_frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		resetEncoders();

		// ----------------------------------------------------------
		// Set up drivetrain

		// coastOrBrakeMotors(false, false);
	}


	// ----------------------------------------------------------
	// Low-level methods


	public Drivetrain setDeadband(double deadband) {
		m_differentialDrive.setDeadband(deadband);
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

	public Drivetrain setOpenLoopRampTimes(double timeInSeconds) {
		m_frontLeftMotor.configOpenloopRamp(timeInSeconds);
		m_frontRightMotor.configOpenloopRamp(timeInSeconds);
		return this;
	}

	public Drivetrain setLeftMotors(double negToPosPercentage) {
		m_frontLeftMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public Drivetrain setRightMotors(double negToPosPercentage) {
		m_frontRightMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public double getLeftPercent() {
		return m_frontLeftMotor.getMotorOutputPercent();
	}

	public double getRightPercent() {
		return m_frontRightMotor.getMotorOutputPercent();
	}


	// ----------------------------------------------------------
	// High-level methods

	
	public void arcadeDrive(double xSpeed, double zRotation) {
		m_differentialDrive.arcadeDrive(xSpeed, zRotation);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		m_differentialDrive.tankDrive(leftSpeed, rightSpeed);
	}

	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace) {
		m_differentialDrive.curvatureDrive(xSpeed, zRotation, allowTurnInPlace);
	}

	public void stopDrive() {
		tankDrive(0.d, 0.d);
	}


	// ----------------------------------------------------------
	// Slew rate limiters


	// Arcade-drive limiters

	// there isn't a meethod in the SlewRateLimiter class in the WPILIB API to just change the rate :(
	public void setArcadeDriveForwardLimiterRate(double rate) {
		m_arcadeDriveForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterArcadeDriveForward(double inputSpeed) {
		return m_arcadeDriveForwardLimiter.calculate(inputSpeed);
	}

	public void setArcadeDriveTurnLimiterRate(double rate) {
		m_arcadeDriveTurnLimiter = new SlewRateLimiter(rate);
	}
	public double filterArcadeDriveTurn(double inputSpeed) {
		return m_arcadeDriveTurnLimiter.calculate(inputSpeed);
	}

	// Tank-drive limiters

	public void setTankDriveForwardLimiterRate(double rate) {
		m_tankDriveForwardLimiter = new SlewRateLimiter(rate);
	}
	public double filterTankDriveForward(double inputSpeed) {
		return m_tankDriveForwardLimiter.calculate(inputSpeed);
	}


	// ----------------------------------------------------------
	// Encoder methods
	

	public double getLeftDistance() {
		return m_frontLeftMotor.getSelectedSensorPosition() * TICKS_TO_INCHES_CONVERSION;
	}

	public Drivetrain resetLeftEncoder() {
		m_frontLeftMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public double getRightDistance() {
		return m_frontRightMotor.getSelectedSensorPosition() * TICKS_TO_INCHES_CONVERSION;
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
		return (getRightDistance() + getLeftDistance()) / 2.0d;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}