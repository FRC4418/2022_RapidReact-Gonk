package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;


public class TeleoperatedDrive extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain dt;

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
		dt.setOpenLoopRampTimes(dt.SHARED_RAMP_TIME);
	}

	@Override
	public void execute() {
		
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
