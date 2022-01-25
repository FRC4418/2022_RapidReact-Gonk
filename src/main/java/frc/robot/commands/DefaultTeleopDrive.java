package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TeleopInput;


public class DefaultTeleopDrive extends CommandBase {
	private TeleopInput ti;

	public DefaultTeleopDrive() {

	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ti.teleopDrive();
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
