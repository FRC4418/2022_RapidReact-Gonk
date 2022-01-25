package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Telemetry;
import frc.robot.subsystems.TeleopInput;


public class TeleoperatedDrive extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain dt;
	private final TeleopInput ti;

	// ----------------------------------------------------------
	// Construtor and actions

	public TeleoperatedDrive() {
		dt = Robot.drivetrain;
		ti = Robot.teleopInput;
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
		ti.teleopDrive();
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
