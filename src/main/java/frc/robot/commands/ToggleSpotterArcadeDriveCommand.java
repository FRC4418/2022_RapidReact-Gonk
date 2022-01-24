package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;


public class ToggleSpotterArcadeDriveCommand extends CommandBase {
	public ToggleSpotterArcadeDriveCommand() {

	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		RobotContainer.drivetrain.toggleSpotterDriveMode();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return true;
	}
}
