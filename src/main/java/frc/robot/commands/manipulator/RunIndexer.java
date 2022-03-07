package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunIndexer extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator m_manipulator;

	// ----------------------------------------------------------
	// Constructor
	
	public RunIndexer(Manipulator manipulator) {
		m_manipulator = manipulator;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_manipulator
			.lockIndexer()
			.runIndexer();
	}

	@Override
	public void end(boolean interrupted) {
		m_manipulator
			.stopIndexer()
			.unlockIndexer();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
