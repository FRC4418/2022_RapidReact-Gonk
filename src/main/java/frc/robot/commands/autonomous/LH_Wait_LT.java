package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class LH_Wait_LT extends SequentialCommandGroup {
	public LH_Wait_LT(Drivetrain drivetrain, Manipulator manipulator) {
		super(
			new RunLauncherForTime(manipulator, 1.5d),
			new Wait(Autonomous.startDelayTime),
			new DriveStraightForDistance(drivetrain, Autonomous.tarmacLeavingDistanceMeters, DriveStraightDirection.FORWARDS)
		);
	}
}