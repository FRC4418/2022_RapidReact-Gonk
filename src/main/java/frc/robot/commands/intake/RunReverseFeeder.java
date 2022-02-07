package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RunReverseFeeder extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;

	// ----------------------------------------------------------
	// Constructor

	public RunReverseFeeder(Intake intake) {
		m_intake = intake;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_intake.runReverseFeeder();
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.stopFeeder();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
