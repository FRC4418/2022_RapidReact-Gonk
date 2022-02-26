package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class RetractIntakeArm extends CommandBase {
	private final Intake m_intake;

	public RetractIntakeArm(Intake intake) {
		m_intake = intake;
	}

	@Override
	public void initialize() {
		m_intake.retractIntakeArm();
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.extendIntakeArm();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
