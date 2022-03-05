package frc.robot.commands.climber;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;


public class ExtendClimber extends CommandBase {
	private final Climber m_climber;

	public ExtendClimber(Climber climber) {
		m_climber = climber;
		
		addRequirements(m_climber);
	}

	@Override
	public void initialize() {
		m_climber.extend();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
