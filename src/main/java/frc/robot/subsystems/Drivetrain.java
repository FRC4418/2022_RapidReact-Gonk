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

import frc.robot.Constants.AxisDominanceThresholds;
import frc.robot.Constants.Drive;
import frc.robot.RobotContainer.DriverControls;
import frc.robot.RobotContainer.SpotterControls;
// import frc.robot.Constants.Drive.PID;
import frc.robot.RobotContainer;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.teamlibraries.DriveInputPipeline;
import frc.robot.teamlibraries.DriveInputPipeline.InputMapModes;


public class Drivetrain extends SubsystemBase {
	private WPI_TalonFX frontLeftMotor;
	private WPI_TalonFX backLeftMotor;
	private WPI_TalonFX frontRightMotor;
	private WPI_TalonFX backRightMotor;

	public Encoder leftEncoder;
	public Encoder rightEncoder;

	private DifferentialDrive robotDrive;

	private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

	public Drivetrain() {
		// Drive Motor
		frontLeftMotor = new WPI_TalonFX(Drive.TalonFX.FRONT_LEFT_ID);
		backLeftMotor = new WPI_TalonFX(Drive.TalonFX.BACK_LEFT_ID);
		frontRightMotor = new WPI_TalonFX(Drive.TalonFX.FRONT_RIGHT_ID);
		backRightMotor = new WPI_TalonFX(Drive.TalonFX.BACK_RIGHT_ID);

		backLeftMotor.follow(frontLeftMotor);
		backRightMotor.follow(frontRightMotor);

		frontLeftMotor.configFactoryDefault();
		backLeftMotor.configFactoryDefault();
		frontRightMotor.configFactoryDefault();
		backRightMotor.configFactoryDefault();

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

		// ----------------------------------------------------------

		// Encoders
		leftEncoder = new Encoder(
			Drive.Encoder.LEFT_CHANNEL_A_ID,
			Drive.Encoder.LEFT_CHANNEL_B_ID,
			false,	// TODO: Figure out if left drivetrain encoder needs direction-flipping
			Drive.Encoder.ENCODING_TYPE);
		rightEncoder = new Encoder(
			Drive.Encoder.RIGHT_CHANNEL_A_ID,
			Drive.Encoder.RIGHT_CHANNEL_B_ID,
			false,	// TODO: Figure out if right drivetrain encoder needs direction-flipping
			Drive.Encoder.ENCODING_TYPE);

		leftEncoder.setDistancePerPulse(Drive.Encoder.DISTANCE_PER_PULSE);
		rightEncoder.setDistancePerPulse(Drive.Encoder.DISTANCE_PER_PULSE);

		leftEncoder.reset();
		rightEncoder.reset();
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
		coastOrBrakeMotors(values[0] == 0.0d, values[1] == 0.0d);
		return this;
	}

	// stop driving
	public Drivetrain stopDrive() {
		frontLeftMotor.set(ControlMode.PercentOutput, 0.0d);
		frontRightMotor.set(ControlMode.PercentOutput, 0.0d);
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
			.inputMapWrapper(InputMapModes.IMM_CUBE, InputMapModes.IMM_CUBE)
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

	
	public double getLeftDistance() { return leftEncoder.getDistance(); }

	public double getRightDistance() { return rightEncoder.getDistance(); }

	public double getAverageDistance() { return (getRightDistance() + getLeftDistance()) / 2.0; }

	public Drivetrain resetLeftEncoder() {
		leftEncoder.reset();
		return this;
	}

	public Drivetrain resetRightEncoder() {
		rightEncoder.reset();
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
		setDefaultCommand(new TeleopDriveCommand());
	}
}
