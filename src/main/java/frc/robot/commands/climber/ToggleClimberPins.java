package frc.robot.commands.climber;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

	int counter = 0;
	@Override
	public void initialize() {
		SmartDashboard.putNumber("Toggled climber pins", counter++);
		m_climber.toggleClimberPins();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}