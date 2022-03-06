package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ExtendIntakeArmThenCoast extends CommandBase {
	private final Intake m_intake;

	private final Timer m_timer = new Timer();
	private final double endDelaySeconds = 1.5;

	public ExtendIntakeArmThenCoast(Intake intake) {
		m_intake = intake;

		addRequirements(m_intake);
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}

	@Override
	public void initialize() {
		m_intake.extendIntakeArm();
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.coastRetractor();
	}

	@Override
	public boolean isFinished() {
		if (m_intake.intakeArmIsExtended()) {
			m_timer.start();
			if (m_timer.hasElapsed(endDelaySeconds)) {
				m_timer.stop();
				m_timer.reset();
				return true;
			}
		}
		return false;
	}
}
