/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.simulation.DriverStationSim;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.teamlibraries.DriveInputPipeline;


public class Drivetrain extends SubsystemBase {
	private WPI_TalonFX frontLeftDriveMotor;
	private WPI_TalonFX backLeftDriveMotor;
	private WPI_TalonFX frontRightDriveMotor;
	private WPI_TalonFX backRightDriveMotor;

	private Encoder leftDriveEncoder;
	private Encoder rightDriveEncoder;

	private DifferentialDrive robotDrive;

	private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

	public Drivetrain() {
		// Drive Motor
		frontLeftDriveMotor = new WPI_TalonFX(Constants.Drive.FRONT_LEFT_TALON_SRX_ID);
		backLeftDriveMotor = new WPI_TalonFX(Constants.Drive.BACK_LEFT_TALON_SRX_ID);
		frontRightDriveMotor = new WPI_TalonFX(Constants.Drive.FRONT_RIGHT_TALON_SRX_ID);
		backRightDriveMotor = new WPI_TalonFX(Constants.Drive.BACK_RIGHT_TALON_SRX_ID);

		backLeftDriveMotor.follow(frontLeftDriveMotor);
		backRightDriveMotor.follow(frontRightDriveMotor);

		frontLeftDriveMotor.configFactoryDefault();
		backLeftDriveMotor.configFactoryDefault();
		frontRightDriveMotor.configFactoryDefault();
		backRightDriveMotor.configFactoryDefault();

		// leftDriveMotorA.config_kF(Constants.Drive.PID.kIdx, Constants.Drive.PID.kLeftMotorVelocityGains.kF, Constants.Drive.PID.kTimeoutMs);
		// leftDriveMotorA.config_kP(Constants.Drive.PID.kIdx, Constants.Drive.PID.kLeftMotorVelocityGains.kP, Constants.Drive.PID.kTimeoutMs);
		// leftDriveMotorA.config_kI(Constants.Drive.PID.kIdx, Constants.Drive.PID.kLeftMotorVelocityGains.kI, Constants.Drive.PID.kTimeoutMs);
        // leftDriveMotorA.config_kD(Constants.Drive.PID.kIdx, Constants.Drive.PID.kLeftMotorVelocityGains.kD, Constants.Drive.PID.kTimeoutMs);

		// rightDriveMotorA.config_kF(Constants.Drive.PID.kIdx, Constants.Drive.PID.kRightMotorVelocityGains.kF, Constants.Drive.PID.kTimeoutMs);
		// rightDriveMotorA.config_kP(Constants.Drive.PID.kIdx, Constants.Drive.PID.kRightMotorVelocityGains.kP, Constants.Drive.PID.kTimeoutMs);
		// rightDriveMotorA.config_kI(Constants.Drive.PID.kIdx, Constants.Drive.PID.kRightMotorVelocityGains.kI, Constants.Drive.PID.kTimeoutMs);
        // rightDriveMotorA.config_kD(Constants.Drive.PID.kIdx, Constants.Drive.PID.kRightMotorVelocityGains.kD, Constants.Drive.PID.kTimeoutMs);

		// ----------------------------------------------------------

		// Drive system
		coastOrBrakeMotors(false, false);

		robotDrive = new DifferentialDrive(frontLeftDriveMotor, frontRightDriveMotor);

		// ----------------------------------------------------------

		// Encoders
		// leftDriveEncoder = new Encoder(Constants.DRIVE_LEFT_ENCODER_CHANNELA_ID, Constants.DRIVE_LEFT_ENCODER_CHANNELB_ID);
		// rightDriveEncoder = new Encoder(Constants.DRIVE_RIGHT_ENCODER_CHANNELA_ID, Constants.DRIVE_RIGHT_ENCODER_CHANNELB_ID);

		// leftDriveEncoder.setDistancePerPulse(Constants.DRIVE_ENCODER_DISTANCE_PER_PULSE);
		// rightDriveEncoder.setDistancePerPulse(Constants.DRIVE_ENCODER_DISTANCE_PER_PULSE);

		// leftDriveEncoder.reset();
		// rightDriveEncoder.reset();
	}

	public Drivetrain setLeftMotors(double negToPosPercentage) {
		frontLeftDriveMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public Drivetrain setRightMotors(double negToPosPercentage) {
		frontRightDriveMotor.set(ControlMode.PercentOutput, negToPosPercentage);
		return this;
	}

	public double getLeftPercent() {
		return frontLeftDriveMotor.getMotorOutputPercent();
	}

	public double getRightPercent() {
		return frontRightDriveMotor.getMotorOutputPercent();
	}

	// brake or coast left and right motors (true for braking)
	public Drivetrain coastOrBrakeMotors(boolean leftIsBraking, boolean rightIsBraking) {
		if (leftIsBraking) {
			frontLeftDriveMotor.setNeutralMode(NeutralMode.Brake);
			backLeftDriveMotor.setNeutralMode(NeutralMode.Brake);
		} else {
			frontLeftDriveMotor.setNeutralMode(NeutralMode.Coast);
			backLeftDriveMotor.setNeutralMode(NeutralMode.Coast);
		}

		if (rightIsBraking) {
			frontRightDriveMotor.setNeutralMode(NeutralMode.Brake);
			backRightDriveMotor.setNeutralMode(NeutralMode.Brake);
		} else {
			frontRightDriveMotor.setNeutralMode(NeutralMode.Coast);
			backRightDriveMotor.setNeutralMode(NeutralMode.Coast);
		}

		return this;
	}


	// ----------------------------------------------------------


	// Automatically set the breaks on when the robot is not moving and disables them when the robot is moving
	public Drivetrain breakTankDriveIfNotMoving(double[] values) {
		// brake motors if value is 0, else coast
		coastOrBrakeMotors(values[0] == 0.0, values[1] == 0.0);
		return this;
	}

	// stop driving
	public Drivetrain stopDrive() {
		frontLeftDriveMotor.set(ControlMode.PercentOutput, 0);
		frontRightDriveMotor.set(ControlMode.PercentOutput, 0);
		return this;
	}

	public Drivetrain tankDrive(double leftValue, double rightValue) {
		var pipeline = new DriveInputPipeline(leftValue, rightValue);
		pipeline
			.inputMapWrapper(DriveInputPipeline.InputMapModes.IMM_SQUARE)
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
			.inputMapWrapper(DriveInputPipeline.InputMapModes.IMM_CUBE, DriveInputPipeline.InputMapModes.IMM_CUBE)
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
		&& (RobotContainer.gamepadJoystickMagnitude(true) > Constants.AxisDominanceThresholds.ARCADE)) {
			arcadeDrive(
				RobotContainer.SpotterControls.getForwardArcadeDriveAxis(),
				RobotContainer.SpotterControls.getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()
		&& (RobotContainer.gamepadJoystickMagnitude(true) > Constants.AxisDominanceThresholds.TANK
		|| RobotContainer.gamepadJoystickMagnitude(false) > Constants.AxisDominanceThresholds.TANK)) {
			tankDrive(
				RobotContainer.SpotterControls.getLeftTankDriveAxis(),
				RobotContainer.SpotterControls.getRightTankDriveAxis());
		}

		if (driverIsInArcade()) {
			arcadeDrive(
				RobotContainer.DriverControls.getForwardArcadeDriveAxis(), // forward
				RobotContainer.DriverControls.getAngleArcadeDriveAxis());  // angle
		} else {
			tankDrive(
				RobotContainer.DriverControls.getLeftTankDriveAxis(),  // left
				RobotContainer.DriverControls.getRightTankDriveAxis());  // right
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

	
	public double getLeftDistance() { return -leftDriveEncoder.getDistance(); }

	public double getRightDistance() { return rightDriveEncoder.getDistance(); }

	// two-sided encoder distance (average one-sided encoder distance)
	public double getDistance() { return (getRightDistance() + getLeftDistance()) / 2.0; }

	public Drivetrain resetLeftDriveEncoder() {
		leftDriveEncoder.reset();
		return this;
	}

	public Drivetrain resetRightEncoder() {
		rightDriveEncoder.reset();
		return this;
	}

	// resets both
	public Drivetrain resetEncoders() {
		resetLeftDriveEncoder(); resetRightEncoder();
		return this;
	}


	// ----------------------------------------------------------
	

	@Override
	public void periodic() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new TeleopDriveCommand());
	}
}
