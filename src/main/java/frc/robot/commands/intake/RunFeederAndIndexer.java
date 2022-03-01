package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederAndIndexer extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;
	private final Manipulator m_manipulator;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederAndIndexer(Intake intake, Manipulator manipulator) {
		m_intake = intake;
		m_manipulator = manipulator;

		addRequirements(m_intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_intake.runFeeder();
		m_manipulator.runIndexer();
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.stopFeeder();
		m_manipulator.stopIndexer();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
