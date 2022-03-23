package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class IdleLauncher extends CommandBase {
	private final Manipulator m_manipulator;

	public IdleLauncher(Manipulator manipulator) {
		m_manipulator = manipulator;
	}

	@Override
	public void initialize() {
		m_manipulator.idleLauncher();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
