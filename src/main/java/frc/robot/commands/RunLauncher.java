package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	private final double LAUNCHER_OUTPUT_PERCENT = 1.d;
	
	// ----------------------------------------------------------
	// Resources

	private final Manipulator ms;

	// ----------------------------------------------------------
	// Constructor

	public RunLauncher(Manipulator manipulator) {	
		ms = manipulator; 
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		ms.setLauncherMotorPercent(LAUNCHER_OUTPUT_PERCENT);
	}

	@Override
	public void end(boolean interrupted) {
		ms.setLauncherMotorPercent(0.d);
	}	

	@Override
	public boolean isFinished() {
		return false;
	}
}
