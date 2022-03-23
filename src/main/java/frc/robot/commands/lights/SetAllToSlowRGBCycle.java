package frc.robot.commands.lights;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Lights;


public class SetAllToSlowRGBCycle extends CommandBase {
	private final Lights m_lights;

	public SetAllToSlowRGBCycle(Lights lights) {
		m_lights = lights;

		addRequirements(m_lights);
	}

	@Override
	public void initialize() {
		m_lights.setAllToSlowRGBCycle();
	}
	
	@Override
	public boolean isFinished() {
		return true;
	}
}
