package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {
	private final double motorOutputPercent = 0.7d;
	private final Manipulator ms;

	public RunLauncher(Manipulator manipulator) {	
		ms = manipulator; 
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}	

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ms
			.setHighMotorPercent(motorOutputPercent)
			.setLowMotorPercent(motorOutputPercent);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ms
			.setLowMotorPercent(0.d)
			.setHighMotorPercent(0.d);
	}	

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
