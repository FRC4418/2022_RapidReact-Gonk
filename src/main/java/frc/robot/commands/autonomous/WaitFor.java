package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Autonomous;


public class WaitFor extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Timer m_timer = new Timer();

	private double m_delayTimeSeconds;

	// ----------------------------------------------------------
	// Constructors

	public WaitFor(double delayTimeSeconds) {
		m_delayTimeSeconds = delayTimeSeconds;
	}

	public WaitFor() {
		this(Autonomous.getStartDelaySeconds());
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_timer.start();
	}

	@Override
	public void end(boolean interrupted) {
		m_timer.stop();
		m_timer.reset();
	}

	@Override
	public boolean isFinished() {
		return m_timer.hasElapsed(m_delayTimeSeconds);
	}
}
