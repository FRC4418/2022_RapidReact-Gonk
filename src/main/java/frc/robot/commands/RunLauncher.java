package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {

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
		ms.setLauncherToPercent(Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT);

		SmartDashboard.putString("reee", "goober gabber");
	}

	@Override
	public void end(boolean interrupted) {
		ms.setLauncherToPercent(0.d);
	}	

	@Override
	public boolean isFinished() {
		return false;
	}
}
