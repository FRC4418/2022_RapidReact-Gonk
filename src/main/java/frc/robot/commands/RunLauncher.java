package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {
	// ----------------------------------------------------------
	// Resource

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
		ms.runLauncher();
	}

	int coutner = 0;

	@Override
	public void execute() {
		SmartDashboard.putNumber("Coutner", coutner++);;
	}

	@Override
	public void end(boolean interrupted) {
		ms.stopLauncher();
	}	

	@Override
	public boolean isFinished() {
		return false;
	}
}
