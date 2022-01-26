package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraightWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Constants

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
	// Scheduler actions

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		dt
			.coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		dt
			.setLeftMotors(motorOutputPercent)
			.setRightMotors(motorOutputPercent);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		dt.stopDrive();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
