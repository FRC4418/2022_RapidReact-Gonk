package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;


public class TeleoperatedDrive extends CommandBase {
	private Drivetrain dt;

	public TeleoperatedDrive() {
		dt = RobotContainer.drivetrain;
		addRequirements(dt);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		dt.setOpenLoopRampTimes(dt.SHARED_RAMP_TIME);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		// SmartDashboard.putNumber("Gamepad Left Joy Magnitude", RobotContainer.gamepadJoystickMagnitude(true));

		dt.driveWithDominantControls();
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
