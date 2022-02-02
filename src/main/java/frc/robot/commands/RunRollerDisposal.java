package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RunRollerDisposal extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake it;

	// ----------------------------------------------------------
	// Constructor

	public RunRollerDisposal(Intake intake) {
		it = intake;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		it.runRollerDisposal();
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void end(boolean interrupted) {
		it.stopRoller();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
