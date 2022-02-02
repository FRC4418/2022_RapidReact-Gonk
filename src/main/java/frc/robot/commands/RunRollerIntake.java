package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RunRollerIntake extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake intake;

	// ----------------------------------------------------------
	// Constructor

	public RunRollerIntake(Intake intake) {
		this.intake = intake;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		intake.runRollerIntake();
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void end(boolean interrupted) {
		intake.stopRoller();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
