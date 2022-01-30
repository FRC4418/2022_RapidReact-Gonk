package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunIndexer extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator ms;

	// ----------------------------------------------------------
	// Constructor
	
	public RunIndexer(Manipulator manipulator) {
		ms = manipulator;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		ms.runIndexer();
	}

	@Override
	public void end(boolean interrupted) {
		ms.stopIndexer();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
