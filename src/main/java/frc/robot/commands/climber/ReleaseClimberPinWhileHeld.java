package frc.robot.commands.climber;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;


public class ReleaseClimberPinWhileHeld extends CommandBase {
	private final Climber m_climber;

	public ReleaseClimberPinWhileHeld(Climber climber) {
		m_climber = climber;
	
	}

	@Override
	public void initialize() {
		m_climber.releasePin();
	}

	@Override
	public void end(boolean interrupted) {
		m_climber.attachPin();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}