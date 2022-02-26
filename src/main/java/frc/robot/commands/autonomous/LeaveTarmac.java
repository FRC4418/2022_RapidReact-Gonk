package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;


public class LeaveTarmac extends SequentialCommandGroup {
	public LeaveTarmac(Drivetrain drivetrain) {
		super(
			new Wait(Autonomous.startDelayTime),
			new DriveStraightForDistance(drivetrain, Autonomous.tarmacLeavingDistanceMeters, DriveStraightDirection.FORWARDS)
		);
	}
}