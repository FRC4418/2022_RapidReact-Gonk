package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.teamlibraries.DriveInputPipeline;
import frc.robot.teamlibraries.Gains;
import frc.robot.teamlibraries.DriveInputPipeline.InputMapModes;


public class Drivetrain extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	// Open-loop control constants
	public final double
		// units in seconds
		SHARED_RAMP_TIME = 2.d;	// TODO: Put drivetrain open-loop ramp time in a diagnostics display


	// ----------------------------------------------------------
	// Private constants


	// ID and encoder constants
	// These are how it's SUPPOSED to be on both V1 and V2
	private final int
		FRONT_LEFT_CAN_ID = 4,
		BACK_LEFT_CAN_ID = 5,
		FRONT_RIGHT_CAN_ID = 3,
		BACK_RIGHT_CAN_ID = 2;

	private final double
		// 2048 ticks in 1 revolution for Falcon 500s
		// wheel diameter * pi = circumference of 1 revolution
		// 1 to 7.33 gearbox is big to small gear (means more speed)
		TICKS_TO_INCHES_CONVERSION  = ( (6.0d * Math.PI) / 2048.0d ) / 7.33d;
	

	// Closed-loop control constants

	// the PID slot to pull gains from. Starting 2018, there is 0,1,2 or 3. Only 0 and 1 are visible in web-based configuration
	private final int kSlotIdx = 0;

	// Talon FX supports multiple (cascaded) PID loops. For now we just want the primary one.
	private final int kIdx = 0;

	// Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
	private final int kTimeoutMs = 30;

	// ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output

	private final Gains kLeftMotorVelocityGains
		//			kP		kI		kD		kF				Iz		Peakout
		= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
	
	private final Gains kRightMotorVelocityGains 
		//			kP		kI		kD		kF				Iz		Peakout
		= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);


	// ----------------------------------------------------------
	// Resources


	private WPI_TalonFX frontLeftMotor;
	private WPI_TalonFX backLeftMotor;
	private WPI_TalonFX frontRightMotor;
	private WPI_TalonFX backRightMotor;

	private DifferentialDrive robotDrive;


	// ----------------------------------------------------------
	// Constructor


	public Drivetrain() {
		// ----------------------------------------------------------
		// Initialize motor controllers and followers

		frontLeftMotor = new WPI_TalonFX(FRONT_LEFT_CAN_ID);
		backLeftMotor = new WPI_TalonFX(BACK_LEFT_CAN_ID);
		frontRightMotor = new WPI_TalonFX(FRONT_RIGHT_CAN_ID);
		backRightMotor = new WPI_TalonFX(BACK_RIGHT_CAN_ID);

		backLeftMotor.follow(frontLeftMotor);
		backRightMotor.follow(frontRightMotor);

		frontLeftMotor.configFactoryDefault();
		backLeftMotor.configFactoryDefault();
		frontRightMotor.configFactoryDefault();
		backRightMotor.configFactoryDefault();

		// ----------------------------------------------------------
		// Config open-loop controls

		frontLeftMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		backLeftMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		frontRightMotor.configOpenloopRamp(SHARED_RAMP_TIME);
		backRightMotor.configOpenloopRamp(SHARED_RAMP_TIME);

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

		frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		resetEncoders();

		// ----------------------------------------------------------
		// Set up drivetrain

		coastOrBrakeMotors(false, false);

		robotDrive = new DifferentialDrive(frontLeftMotor, frontRightMotor);
	}


	// ----------------------------------------------------------
	// Low-level drivetrain actions

	public Drivetrain flipLeftOrRightMotors(boolean flipLeftMotors) {
		if (flipLeftMotors) {
			frontLeftMotor.setInverted(true);
			backLeftMotor.setInverted(InvertType.FollowMaster);
		} else {
			frontRightMotor.setInverted(true);
			backRightMotor.setInverted(InvertType.FollowMaster);
		}
		return this;
	}

	public Drivetrain setOpenLoopRampTimes(double timeInSeconds) {
		frontLeftMotor.configOpenloopRamp(timeInSeconds);
		frontRightMotor.configOpenloopRamp(timeInSeconds);
		return this;
	}

	public Drivetrain setLeftMotors(double negToPosPercentage) {
		frontLeftMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public Drivetrain setRightMotors(double negToPosPercentage) {
		frontRightMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public double getLeftPercent() {
		return frontLeftMotor.getMotorOutputPercent();
	}

	public double getRightPercent() {
		return frontRightMotor.getMotorOutputPercent();
	}

	// brake or coast left and right motors (true for braking)
	public Drivetrain coastOrBrakeMotors(boolean leftIsBraking, boolean rightIsBraking) {
		if (leftIsBraking) {
			frontLeftMotor.setNeutralMode(NeutralMode.Brake);
			backLeftMotor.setNeutralMode(NeutralMode.Brake);
		} else {
			frontLeftMotor.setNeutralMode(NeutralMode.Coast);
			backLeftMotor.setNeutralMode(NeutralMode.Coast);
		}

		if (rightIsBraking) {
			frontRightMotor.setNeutralMode(NeutralMode.Brake);
			backRightMotor.setNeutralMode(NeutralMode.Brake);
		} else {
			frontRightMotor.setNeutralMode(NeutralMode.Coast);
			backRightMotor.setNeutralMode(NeutralMode.Coast);
		}

		return this;
	}


	// ----------------------------------------------------------
	// High-level drivetrain actions


	// Automatically set the breaks on when the robot is not moving and disables them when the robot is moving
	public Drivetrain breakTankDriveIfNotMoving(double[] values) {
		// brake motors if value is 0, else coast
		coastOrBrakeMotors(values[0] == 0.d, values[1] == 0.d);
		return this;
	}

	public Drivetrain stopDrive() {
		frontLeftMotor.set(ControlMode.PercentOutput, 0.d);
		frontRightMotor.set(ControlMode.PercentOutput, 0.d);
		return this;
	}

	public Drivetrain tankDrive(double leftValue, double rightValue) {
		var pipeline = new DriveInputPipeline(leftValue, rightValue);
		pipeline
			.inputMapWrapper(InputMapModes.IMM_SQUARE)
			.magnetizeTankDrive()
			.applyDeadzones();
		
		double[] values = pipeline.getValues();
		breakTankDriveIfNotMoving(values);
		robotDrive.tankDrive(values[0], values[1]);

		return this;
	}

	public Drivetrain arcadeDrive(double forwardValue, double angleValue) {
		var pipeline = new DriveInputPipeline(forwardValue, angleValue);
		pipeline
			.inputMapWrapper(InputMapModes.IMM_LINEAR, InputMapModes.IMM_SIGMOID)	// TODO: If using custom input pipelines, put joystick map functions in a diagnostics display
			.applyDeadzones();
		
		double[] values = pipeline.getValues();
		breakTankDriveIfNotMoving(pipeline.convertArcadeDriveToTank(values));
		robotDrive.arcadeDrive(-values[0], values[1]);

		return this;
	}


	// ----------------------------------------------------------
	// Encoder actions
	

	public double getLeftDistance() {
		return frontLeftMotor.getSelectedSensorPosition() * TICKS_TO_INCHES_CONVERSION;
	}

	public double getRightDistance() {
		return frontRightMotor.getSelectedSensorPosition() * TICKS_TO_INCHES_CONVERSION;
	}

	public double getAverageDistance() {
		return (getRightDistance() + getLeftDistance()) / 2.0d;
	}

	public Drivetrain resetLeftEncoder() {
		frontLeftMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public Drivetrain resetRightEncoder() {
		frontRightMotor.setSelectedSensorPosition(0.d);
		return this;
	}

	public Drivetrain resetEncoders() {
		resetLeftEncoder();
		resetRightEncoder();
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}