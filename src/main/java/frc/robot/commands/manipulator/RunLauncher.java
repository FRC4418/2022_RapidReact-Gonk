package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Manipulator;


public class RunLauncher extends CommandBase {
	private final Manipulator m_manipulator;
	private final int customRPM;

	public RunLauncher(Manipulator manipulator, int rpm) {
		m_manipulator = manipulator;
		customRPM = rpm;
	}

	public RunLauncher(Manipulator manipulator) {
		this(manipulator, 0);
	}

	@Override
	public void initialize() {
		// sentinel value of 0 means we're just using the default teleop launcher RPM
		if (customRPM == 0) {
			m_manipulator.runLauncher();
		} else {
			m_manipulator.setLauncherRPM(customRPM);
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
