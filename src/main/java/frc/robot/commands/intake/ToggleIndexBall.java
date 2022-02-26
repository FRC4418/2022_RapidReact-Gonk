package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class ToggleIndexBall extends CommandBase {
	// ----------------------------------------------------------
	// Constants

	private final double DELAY_TIME = 0.25d;
	
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;
	private final Manipulator m_manipulator;

	// delay to keep the intake running for a few seconds, even after we trip the whisker sensor
	private final Timer m_timer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public ToggleIndexBall(Intake intake, Manipulator manipulator) {
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
		if (m_intake.whiskerSensorIsActive()) {
			m_timer.start();
		}

		if (m_timer.hasElapsed(DELAY_TIME)) {
			m_timer.stop();
			m_timer.reset();
			return true;
		}

		return false;
	}
}
