package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ExtendIntakeArm extends CommandBase {
	private final Intake m_intake;

	public ExtendIntakeArm(Intake intake) {
		m_intake = intake;

		addRequirements(m_intake);
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
