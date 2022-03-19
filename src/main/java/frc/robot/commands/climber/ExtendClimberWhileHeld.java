package frc.robot.commands.climber;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;


public class ExtendClimberWhileHeld extends CommandBase {
	private final Climber m_climber;

	private boolean m_activated = false;

	private Timer m_rollbackTimer = new Timer();

	public ExtendClimberWhileHeld(Climber climber) {
		m_climber = climber;
	}

	@Override
	public void initialize() {
		m_climber.releasePin();
		
		m_climber.setWinchToLowerPercent();
		m_rollbackTimer.start();
	}

	@Override
	public void execute() {
		if (m_rollbackTimer.hasElapsed(Constants.Climber.kPinRollbackTimeSeconds) && !m_activated && m_climber.pinIsReleased()) {
			m_climber.setWinchToExtendPercent();
			
			m_activated = true;
			m_rollbackTimer.stop();
			m_rollbackTimer.reset();
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_climber.stopWinchMotor();
		m_climber.attachPin();
		m_activated = false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
