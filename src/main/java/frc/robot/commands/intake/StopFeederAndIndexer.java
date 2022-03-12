package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class StopFeederAndIndexer extends CommandBase {
	private final Intake m_intake;
	private final Manipulator m_manipulator;
	private final boolean m_runWhenDisabled;

	public StopFeederAndIndexer(Intake intake, Manipulator manipulator, boolean runWhenDisabled) {
		m_intake = intake;
		m_manipulator = manipulator;
		m_runWhenDisabled = runWhenDisabled;
	}

	@Override
	public boolean runsWhenDisabled() {
		return m_runWhenDisabled;
	}

	@Override
	public void initialize() {
		m_intake.stopFeeder();
		m_manipulator.stopIndexer();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
