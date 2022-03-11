package frc.robot.commands.climber;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;


public class ToggleClimberPins extends CommandBase {
	private final Climber m_climber;

	public ToggleClimberPins(Climber climber) {
		m_climber = climber;
		
		addRequirements(m_climber);
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}

	@Override
	public void initialize() {
		m_climber.toggleClimberPins();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}