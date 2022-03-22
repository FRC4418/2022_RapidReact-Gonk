package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederAndIndexerWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;
	private final Manipulator m_manipulator;
	private final boolean m_runReverse;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederAndIndexerWhileHeld(Intake intake, Manipulator manipulator, boolean runReverse) {
		m_intake = intake;
		m_manipulator = manipulator;
		m_runReverse = runReverse;

		addRequirements(m_intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		if (!m_runReverse) {
			(new RunFeederAndIndexer(m_intake, m_manipulator, false)).schedule();
		} else {
			(new RunFeederAndIndexer(m_intake, m_manipulator, true)).schedule();
		}
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
