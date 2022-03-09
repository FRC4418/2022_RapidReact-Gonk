package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;


public class RetractIntakeArm extends CommandBase {
	private final Intake m_intake;
	private final boolean m_runWhenDisabled;

	private final Timer m_timer = new Timer();

	private boolean m_retractingQueued = false;

	public RetractIntakeArm(Intake intake, boolean runWhenDisabled) {
		m_intake = intake;
		m_runWhenDisabled = runWhenDisabled;
	}

	@Override
	public boolean runsWhenDisabled() {
		return m_runWhenDisabled;
	}

	// @Override
	// public void initialize() {
	// 	if (!m_intake.retractorIsLocked()) {
	// 		m_intake
	// 			.lockRetractor()
	// 			.retractIntakeArm();
	// 	}
	// }

	@Override
	public void execute() {
		if (!m_intake.retractorIsLocked()) {
			m_intake
				.lockRetractor()
				.retractIntakeArm();
		} else if (!m_retractingQueued) {
			SmartDashboard.putString("Trying to retract", "he");

			switch (m_intake.getRetractorState()) {
				default:
					DriverStation.reportError("Unsupported retractor state found while retracting intake arm", true);
					assert 1 == 0;
					break;
				case IDLE:
					m_intake
						.lockRetractor()
						.retractIntakeArm();
					break;
				case RETRACTING:
					end(true);
					break;
			}
			m_retractingQueued = true;
		}
	}

	@Override
	public boolean isFinished() {
		if (m_intake.armIsRetracted()) {
			m_timer.start();
			if (m_timer.hasElapsed(Constants.Intake.kRetractorLockEndDelaySeconds)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		if (!interrupted) {
			m_timer.stop();
			m_timer.reset();
			
			m_intake
				.idleRetractorState()
				.unlockRetractor();
		}
	}
}