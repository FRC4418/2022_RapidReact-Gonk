package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class RetractIntakeArmWithFeedbackInDisabled extends CommandBase {
	private final Intake m_intake;

	public RetractIntakeArmWithFeedbackInDisabled(Intake intake) {
		m_intake = intake;

		addRequirements(m_intake);
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}

	@Override
	public void initialize() {
		m_intake.retractIntakeArm();
	}

	@Override
	public boolean isFinished() {
		return m_intake.armIsWithinRetractedTolerance();
	}
}