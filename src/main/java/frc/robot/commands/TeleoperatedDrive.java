package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;


public class TeleoperatedDrive extends CommandBase {
	// ----------------------------------------------------------
	// Constants
	public final double
		// units in seconds
		SHARED_RAMP_TIME = 0.75d;	// TODO: Config open-loop ramp time

	// ----------------------------------------------------------
	// Subsystem resources

	private Drivetrain dt;

	// ----------------------------------------------------------
	// Construtor and actions

	public TeleoperatedDrive() {
		dt = Robot.drivetrain;
		addRequirements(dt);
	}

	// ----------------------------------------------------------
	// Scheduler actions

	@Override
	public void initialize() {
		dt.setOpenLoopRampTimes(SHARED_RAMP_TIME);
	}

	@Override
	public void execute() {
		dt.driveWithDominantControls();
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
