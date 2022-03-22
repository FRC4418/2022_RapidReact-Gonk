package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederAndIndexerForTime extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;
	private final Manipulator m_manipulator;
	private final double m_durationSeconds;

	private final Timer m_timer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public RunFeederAndIndexerForTime(Intake intake, Manipulator manipulator, double durationSeconds) {
		m_intake = intake;
		m_manipulator = manipulator;
		m_durationSeconds = durationSeconds;

		addRequirements(m_intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_timer.start();

		(new RunFeederAndIndexer(m_intake, m_manipulator, false)).schedule();
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.stopFeeder();
		m_manipulator.stopIndexer();
		
		m_timer.stop();
		m_timer.reset();
	}

	@Override
	public boolean isFinished() {
		return m_timer.hasElapsed(m_durationSeconds);
	}
}
