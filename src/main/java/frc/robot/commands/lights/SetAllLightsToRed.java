package frc.robot.commands.lights;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Lights;


public class SetAllLightsToRed extends CommandBase {
	private final Lights m_lights;

	public SetAllLightsToRed(Lights lights) {
		m_lights = lights;

		addRequirements(m_lights);
	}

	@Override
	public void initialize() {
		m_lights.setAllToRed();
	}
	
	@Override
	public boolean isFinished() {
		return true;
	}
}
