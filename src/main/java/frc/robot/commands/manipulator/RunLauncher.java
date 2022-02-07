package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Manipulator m_manipulator;

	// ----------------------------------------------------------
	// Constructor

	public RunLauncher(Manipulator manipulator) {	
		m_manipulator = manipulator; 
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_manipulator.runLauncher();
	}

	int coutner = 0;

	@Override
	public void execute() {
		SmartDashboard.putNumber("Coutner", coutner++);;
	}

	@Override
	public void end(boolean interrupted) {
		m_manipulator.stopLauncher();
	}	

	@Override
	public boolean isFinished() {
		return false;
	}
}
