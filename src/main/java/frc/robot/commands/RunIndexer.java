package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunIndexer extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	private final double INDEXER_OUTPUT_PERCENT = 0.7d;
	
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
		ms.setIndexerMotorPercent(INDEXER_OUTPUT_PERCENT);
	}

	@Override
	public void end(boolean interrupted) {
		ms.setIndexerMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
