package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class AutoDriveStraightForDistance extends CommandBase {
	// ----------------------------------------------------------
	// Public constants

	public enum DriveStraightDirection {
		FORWARDS,
		BACKWARDS
	}

	// ----------------------------------------------------------
	// Private constants

	private final double MOTOR_OUTPUT_PERCENT = 0.45d;
	private final double kP = 0.1d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain drivetrain;

	private double distanceInInches;
	private DriveStraightDirection direction;

	// ----------------------------------------------------------
	// Constructor

	public AutoDriveStraightForDistance(Drivetrain drivetrain, double distanceInInches, DriveStraightDirection direction) {
		this.distanceInInches = distanceInInches;
		this.direction = direction;
		this.drivetrain = drivetrain;
		
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		drivetrain
			// .coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d)
			.resetEncoders();
	}

	@Override
	public void execute() {
		double error = drivetrain.getLeftDistance() - drivetrain.getRightDistance();

		if (direction == DriveStraightDirection.FORWARDS) {
			drivetrain.tankDrive(MOTOR_OUTPUT_PERCENT + kP * error, MOTOR_OUTPUT_PERCENT - kP * error);
			// dt.tankDrive(MOTOR_OUTPUT_PERCENT, MOTOR_OUTPUT_PERCENT);
		} else {
			drivetrain.tankDrive(-(MOTOR_OUTPUT_PERCENT + kP * error), -(MOTOR_OUTPUT_PERCENT - kP * error));
			// dt.tankDrive(-MOTOR_OUTPUT_PERCENT, -MOTOR_OUTPUT_PERCENT);
		}

		SmartDashboard.putNumber("Left Encoder", drivetrain.getLeftDistance());
		SmartDashboard.putNumber("Right Encoder", drivetrain.getRightDistance());
	}

	@Override
	public void end(boolean interrupted) {
		drivetrain.stopDrive();
	}

	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("traveled average distance", drivetrain.getAverageDistance());

		return Math.abs(drivetrain.getAverageDistance()) >= distanceInInches;
	}
}
