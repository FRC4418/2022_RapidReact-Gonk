package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.manipulator.AutoRunLauncherDemo;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class LH_LT extends SequentialCommandGroup {
	public LH_LT(Drivetrain drivetrain, Manipulator manipulator) {
		super(
			new Wait(Autonomous.startDelayTime),
			new AutoRunLauncherDemo(manipulator, 1.5d),
			new DriveStraightForDistance(drivetrain, Autonomous.tarmacLeavingDistanceMeters, DriveStraightDirection.FORWARDS)
		);
	}
}