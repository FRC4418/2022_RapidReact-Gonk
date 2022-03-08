package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;


public class RetractIntakeArm extends CommandBase {
	private final Intake m_intake;
	private final boolean m_runWhenDisabled;

	private final Timer m_timer = new Timer();

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
		if (m_intake.retractorIsLocked()) {
			end(true);
		} else {
			m_intake
				.lockRetractor()
				.retractIntakeArm();
		}
	}

	@Override
	public boolean isFinished() {
		if (m_intake.armIsRetracted()) {
			m_timer.start();
			if (m_timer.hasElapsed(Constants.Intake.kRetractorLockEndDelaySeconds)) {
				m_timer.stop();
				m_timer.reset();
				
				m_intake.unlockRetractor();
				return true;
			}
		}
		return false;
	}
}
