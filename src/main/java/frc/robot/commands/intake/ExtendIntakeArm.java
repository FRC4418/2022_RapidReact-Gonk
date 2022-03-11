package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ExtendIntakeArm extends CommandBase {
	private final Intake m_intake;
	private final boolean m_runWhenDisabled;

	public ExtendIntakeArm(Intake intake, boolean runWhenDisabled) {
		m_intake = intake;
		m_runWhenDisabled = runWhenDisabled;
	}

	@Override
	public boolean runsWhenDisabled() {
		return m_runWhenDisabled;
	}

	@Override
	public void initialize() {
		m_intake.extendIntakeArm();
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
