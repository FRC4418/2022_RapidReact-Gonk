package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.simulation.DriverStationSim;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants.AxisDominanceThresholds;
import frc.robot.RobotContainer.DriverControls;
import frc.robot.RobotContainer.SpotterControls;
// import frc.robot.Constants.Drive.PID;
import frc.robot.RobotContainer;
import frc.robot.commands.TeleoperatedDrive;
import frc.robot.teamlibraries.DriveInputPipeline;
import frc.robot.teamlibraries.Gains;
import frc.robot.teamlibraries.DriveInputPipeline.InputMapModes;


public class Drivetrain extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants
	
	public final int
		FRONT_LEFT_ID = 4,
		BACK_LEFT_ID = 3,
		FRONT_RIGHT_ID = 2,
		BACK_RIGHT_ID = 1;

	public final double
		// 2048 ticks in 1 revolution for Falcon 500s
		// wheel diameter * pi = circumference of 1 revolution
		// 1 to 7.33 gearbox is big to small gear (means more speed)
		TICKS_TO_INCHES_CONVERSION  = ( (6.0d * Math.PI) / 2048.0d ) / 7.33d;

	public final double
		// units in seconds
		SHARED_RAMP_TIME = 0.75d;	// TODO: Config open-loop ramp time

	// the PID slot to pull gains from. Starting 2018, there is 0,1,2 or 3. Only 0 and 1 are visible in web-based configuration
	public final int kSlotIdx = 0;

	// Talon FX supports multiple (cascaded) PID loops. For now we just want the primary one.
	public final int kIdx = 0;

	// Set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails.
	public final int kTimeoutMs = 30;

	// ID Gains may have to be adjusted based on the responsiveness of control loop. kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output

	public final Gains kLeftMotorVelocityGains
		//			kP		kI		kD		kF				Iz		Peakout
		= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
	
	public final Gains kRightMotorVelocityGains 
		//			kP		kI		kD		kF				Iz		Peakout
		= new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);

	// ----------------------------------------------------------
	// Subsystem resources

	private WPI_TalonFX frontLeftMotor;
	private WPI_TalonFX backLeftMotor;
	private WPI_TalonFX frontRightMotor;
	private WPI_TalonFX backRightMotor;

	private DifferentialDrive robotDrive;

	private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

	// ----------------------------------------------------------
	// Constructor and actions

	public Drivetrain() {
		// ----------------------------------------------------------
		// Initialize motor controllers and followers
		frontLeftMotor = new WPI_TalonFX(FRONT_LEFT_ID);
		backLeftMotor = new WPI_TalonFX(BACK_LEFT_ID);
		frontRightMotor = new WPI_TalonFX(FRONT_RIGHT_ID);
		backRightMotor = new WPI_TalonFX(BACK_RIGHT_ID);

		backLeftMotor.follow(frontLeftMotor);
		backRightMotor.follow(frontRightMotor);

		frontLeftMotor.configFactoryDefault();
		backLeftMotor.configFactoryDefault();
		frontRightMotor.configFactoryDefault();
		backRightMotor.configFactoryDefault();

		// ----------------------------------------------------------

		frontRightMotor.setInverted(true);
		backRightMotor.setInverted(InvertType.FollowMaster);

		// ----------------------------------------------------------

		// Integrated sensors (built-in encoders)
		frontLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		frontRightMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 10);
		resetEncoders();

		// frontLeftDriveMotor.config_kF(PID.kIdx, PID.kLeftMotorVelocityGains.kF, PID.kTimeoutMs);
		// frontLeftDriveMotor.config_kP(PID.kIdx, PID.kLeftMotorVelocityGains.kP, PID.kTimeoutMs);
		// frontLeftDriveMotor.config_kI(PID.kIdx, PID.kLeftMotorVelocityGains.kI, PID.kTimeoutMs);
        // frontLeftDriveMotor.config_kD(PID.kIdx, PID.kLeftMotorVelocityGains.kD, PID.kTimeoutMs);

		// frontRightDriveMotor.config_kF(PID.kIdx, PID.kRightMotorVelocityGains.kF, PID.kTimeoutMs);
		// frontRightDriveMotor.config_kP(PID.kIdx, PID.kRightMotorVelocityGains.kP, PID.kTimeoutMs);
		// frontRightDriveMotor.config_kI(PID.kIdx, PID.kRightMotorVelocityGains.kI, PID.kTimeoutMs);
        // frontRightDriveMotor.config_kD(PID.kIdx, PID.kRightMotorVelocityGains.kD, PID.kTimeoutMs);

		// ----------------------------------------------------------

		// Drive system
		coastOrBrakeMotors(false, false);

		robotDrive = new DifferentialDrive(frontLeftMotor, frontRightMotor);
	}

	// ----------------------------------------------------------

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


	// Automatically set the breaks on when the robot is not moving and disables them when the robot is moving
	public Drivetrain breakTankDriveIfNotMoving(double[] values) {
		// brake motors if value is 0, else coast
		coastOrBrakeMotors(values[0] == 0.d, values[1] == 0.d);
		return this;
	}

	// stop driving
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
			.inputMapWrapper(InputMapModes.IMM_LINEAR, InputMapModes.IMM_LINEAR)	// TODO: Config joystick map functions
			.applyDeadzones();
		
		double[] values = pipeline.getValues();
		breakTankDriveIfNotMoving(pipeline.convertArcadeDriveToTank(values));
		robotDrive.arcadeDrive(-values[0], values[1]);

		return this;
	}


	// ----------------------------------------------------------


	// spotter overrides driver for dominant controls for emergencies
	public Drivetrain driveWithDominantControls() {
		if (spotterIsInArcade()
		&& (RobotContainer.gamepadJoystickMagnitude(true) > AxisDominanceThresholds.ARCADE)) {
			arcadeDrive(
				SpotterControls.getForwardArcadeDriveAxis(),
				SpotterControls.getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()
		&& (RobotContainer.gamepadJoystickMagnitude(true) > AxisDominanceThresholds.TANK
		|| RobotContainer.gamepadJoystickMagnitude(false) > AxisDominanceThresholds.TANK)) {
			tankDrive(
				SpotterControls.getLeftTankDriveAxis(),
				SpotterControls.getRightTankDriveAxis());
		}

		if (driverIsInArcade()) {
			arcadeDrive(
				DriverControls.getForwardArcadeDriveAxis(), // forward
				DriverControls.getAngleArcadeDriveAxis());  // angle
		} else {
			tankDrive(
				DriverControls.getLeftTankDriveAxis(),  // left
				DriverControls.getRightTankDriveAxis());  // right
		}

		return this;
	}

	private boolean driverIsInArcade() { return driverIsInArcadeMode; }
	public Drivetrain toggleDriverDriveMode() {
		driverIsInArcadeMode = !driverIsInArcadeMode;
		return this;
	}
	
	private boolean spotterIsInArcade() { return spotterIsInArcadeMode; }
	public Drivetrain toggleSpotterDriveMode() {
		spotterIsInArcadeMode = !spotterIsInArcadeMode;
		return this;
	}


	// ----------------------------------------------------------

	
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

	// resets both
	public Drivetrain resetEncoders() {
		resetLeftEncoder();
		resetRightEncoder();
		return this;
	}


	// ----------------------------------------------------------
	

	@Override
	public void periodic() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new TeleoperatedDrive());
	}
}
