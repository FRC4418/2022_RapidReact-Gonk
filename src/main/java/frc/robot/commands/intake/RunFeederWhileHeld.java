package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;


public class RunFeederWhileHeld extends CommandBase {
	private final Intake m_intake;
	private final boolean m_runReverse;

	public RunFeederWhileHeld(Intake intake, boolean runReverse) {
		m_intake = intake;
		m_runReverse = runReverse;
	}

	@Override
	public void initialize() {
		if (!m_runReverse) {
			m_intake.runFeeder();
		} else {
			m_intake.runReverseFeeder();
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.stopFeeder();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
