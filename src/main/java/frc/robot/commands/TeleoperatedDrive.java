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

	public TeleoperatedDrive(Drivetrain drivetrain) {
		dt = drivetrain;
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
		// TODO: Figure out a way to unscuff using a static reference to RobotContainer
		Robot.robotContainer.teleopDrive();
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
