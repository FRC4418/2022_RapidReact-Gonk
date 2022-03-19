package frc.robot.commands.climber;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;


public class LowerClimberWhileHeld extends CommandBase {
	private final Climber m_climber;

	private boolean activated = false;

	public LowerClimberWhileHeld(Climber climber) {
		m_climber = climber;
	}

	@Override
	public void initialize() {
		m_climber.releasePin();
	}

	@Override
	public void execute() {
		if (!activated && m_climber.pinIsReleased()) {
			m_climber.setWinchToLowerPercent();
			activated = true;
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_climber.stopWinchMotor();
		m_climber.attachPin();
		activated = false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
