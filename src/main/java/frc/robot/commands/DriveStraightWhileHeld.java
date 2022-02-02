package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraightWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	private final double motorOutputPercent = 0.5d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain drivetrain;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightWhileHeld(Drivetrain drivetrain) {
		this.drivetrain = drivetrain;
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		drivetrain
			// .coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d);
	}

	@Override
	public void execute() {
		drivetrain
			.setLeftMotors(motorOutputPercent)
			.setRightMotors(motorOutputPercent);
	}

	@Override
	public void end(boolean interrupted) {
		drivetrain.stopDrive();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
