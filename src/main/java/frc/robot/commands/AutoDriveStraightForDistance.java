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

	private final double motorPercentOutput = 0.45d;
	private final double kP = 0.1d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain dt;

	private double distanceInInches;
	private DriveStraightDirection direction;

	// ----------------------------------------------------------
	// Constructor

	public AutoDriveStraightForDistance(Drivetrain drivetrain, double distanceInInches, DriveStraightDirection direction) {
		this.distanceInInches = distanceInInches;
		this.direction = direction;

		dt = drivetrain;
		addRequirements(dt);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		dt
			.coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d)
			.resetEncoders();
	}

	@Override
	public void execute() {
		double error = dt.getLeftDistance() - dt.getRightDistance();

		if (direction == DriveStraightDirection.FORWARDS) {
			dt.tankDrive(motorPercentOutput + kP * error, motorPercentOutput - kP * error);
			// dt.tankDrive(motorPercentOutput, motorPercentOutput);
		} else {
			dt.tankDrive(-(motorPercentOutput + kP * error), -(motorPercentOutput - kP * error));
			// dt.tankDrive(-motorPercentOutput, -motorPercentOutput);
		}

		SmartDashboard.putNumber("Left Encoder", dt.getLeftDistance());
		SmartDashboard.putNumber("Right Encoder", dt.getRightDistance());
	}

	@Override
	public void end(boolean interrupted) {
		dt.stopDrive();
	}

	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("traveled average distance", dt.getAverageDistance());

		return Math.abs(dt.getAverageDistance()) >= distanceInInches;
	}
}
