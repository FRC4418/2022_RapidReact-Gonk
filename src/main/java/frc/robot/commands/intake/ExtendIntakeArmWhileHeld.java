package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ExtendIntakeArmWhileHeld extends CommandBase {
	private final Intake m_intake;

	public ExtendIntakeArmWhileHeld(Intake intake) {
		m_intake = intake;
	}

	@Override
	public void initialize() {
		m_intake.extendIntakeArm();
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.retractIntakeArm();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
