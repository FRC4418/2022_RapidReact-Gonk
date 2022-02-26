package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class Wait extends CommandBase {
	private final Timer m_timer = new Timer();

	private double m_delayTimeSeconds;

	public Wait(double delayTimeSeconds) {
		m_delayTimeSeconds = delayTimeSeconds;
	}

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
