package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunIndexer extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator manipulator;

	// ----------------------------------------------------------
	// Constructor
	
	public RunIndexer(Manipulator manipulator) {
		this.manipulator = manipulator;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		manipulator.runIndexer();
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void end(boolean interrupted) {
		manipulator.stopIndexer();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
