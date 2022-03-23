package frc.robot.commands.climber;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.Climber;


public class ExtendClimberWhileHeld extends CommandBase {
	private final Climber m_climber;

	private double m_startTime;

	public ExtendClimberWhileHeld(Climber climber) {
		m_climber = climber;
	}

	@Override
	public void initialize() {
		m_climber.releasePin();
		
		m_climber.setWinchToLowerPercent();
		m_startTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
		if (Timer.getFPGATimestamp() > m_startTime + Constants.Climber.kPinRollbackTimeSeconds) {
			m_climber.setWinchToExtendPercent();
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_climber.stopWinchMotor();
		m_climber.attachPin();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
