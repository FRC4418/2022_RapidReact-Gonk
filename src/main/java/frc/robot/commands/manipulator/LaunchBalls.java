package frc.robot.commands.manipulator;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.autonomous.WaitFor;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Manipulator;


public class LaunchBalls extends SequentialCommandGroup {
	public LaunchBalls(Manipulator manipulator) {
		super(
			new RunLauncher(manipulator, Autonomous.getLauncherAutoRPM()),
			new WaitFor(Autonomous.getLauncherFiringDurationSeconds()),
			new IdleLauncher(manipulator)
		);
	}
}