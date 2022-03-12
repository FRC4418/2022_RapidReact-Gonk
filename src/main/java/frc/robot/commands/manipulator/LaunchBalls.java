package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonomous.WaitFor;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Manipulator;


public class LaunchBalls extends CommandBase {
	private final Manipulator m_manipulator;
	private final int m_numberOfBalls;

	public LaunchBalls(Manipulator manipulator, int numberOfBalls) {
		m_manipulator = manipulator;
		m_numberOfBalls = numberOfBalls;
	}

	@Override
	public void initialize() {
		if (m_numberOfBalls == 1) {
			new SequentialCommandGroup(
				new RunLauncher(m_manipulator, Autonomous.getLauncherAutoRPM()),
				new WaitFor(Autonomous.getTwoBallFiringDurationSeconds()),
				new IdleLauncher(m_manipulator)
			).schedule();
		} else if (m_numberOfBalls == 2) {
			new SequentialCommandGroup(
				new RunLauncher(m_manipulator, Autonomous.getLauncherAutoRPM()),
				new WaitFor(Autonomous.getTwoBallFiringDurationSeconds()),
				new IdleLauncher(m_manipulator)
			).schedule();
		}
	}
}