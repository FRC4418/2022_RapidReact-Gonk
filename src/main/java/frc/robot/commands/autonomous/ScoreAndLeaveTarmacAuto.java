package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.commands.manipulator.LaunchOneBall;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class ScoreAndLeaveTarmacAuto extends SequentialCommandGroup {
	public ScoreAndLeaveTarmacAuto(Drivetrain drivetrain, Manipulator manipulator) {
		super(
			new WaitFor(Autonomous.getStartDelaySeconds()),
			new LaunchOneBall(manipulator),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS)
		);
	}
}