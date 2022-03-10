package frc.robot.commands.intake;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class RunFeederAndIndexer extends CommandBase {
	private final Intake m_intake;
	private final Manipulator m_manipulator;
	private final boolean m_runReverse;

	public RunFeederAndIndexer(Intake intake, Manipulator manipulator, boolean runReverse) {
		m_intake = intake;
		m_manipulator = manipulator;
		m_runReverse = runReverse;
	}

	@Override
	public void initialize() {
		if (!m_runReverse) {
			m_intake.runFeeder();
			m_manipulator.runIndexer();
		} else {
			m_intake.runReverseFeeder();
			m_manipulator.runReverseIndexer();
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
