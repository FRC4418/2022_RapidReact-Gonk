package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraightWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	private final double motorOutputPercent = 0.5d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain dt;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightWhileHeld(Drivetrain drivetrain) {
		dt = drivetrain;
		addRequirements(dt);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		dt
			// .coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d);
	}

	@Override
	public void execute() {
		dt
			.setLeftMotors(motorOutputPercent)
			.setRightMotors(motorOutputPercent);
	}

	@Override
	public void end(boolean interrupted) {
		dt.stopDrive();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
