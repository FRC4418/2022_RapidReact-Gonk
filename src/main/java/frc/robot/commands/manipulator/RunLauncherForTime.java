package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunLauncherForTime extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Manipulator m_manipulator;
	
	private final double m_duration;
	private final Timer m_timer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public RunLauncherForTime(Manipulator manipulator, double duration) {	
		m_manipulator = manipulator;
		m_duration = duration;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_timer.start();
		m_manipulator.runLauncher();
	}

	@Override
	public void end(boolean interrupted) {
		m_manipulator.stopLauncher();
		m_timer.stop();
		m_timer.reset();
	}	

	@Override
	public boolean isFinished() {
		return m_timer.hasElapsed(m_duration);
	}
}
