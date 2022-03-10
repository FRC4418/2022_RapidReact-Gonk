package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RetractIntakeArm extends CommandBase {
	private final Intake m_intake;
	private final boolean m_runWhenDisabled;

	public RetractIntakeArm(Intake intake, boolean runWhenDisabled) {
		m_intake = intake;
		m_runWhenDisabled = runWhenDisabled;
	}

	@Override
	public boolean runsWhenDisabled() {
		return m_runWhenDisabled;
	}

	@Override
	public void initialize() {
		if (!m_intake.armIsRetracted()) {
			m_intake.retractIntakeArm();
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}