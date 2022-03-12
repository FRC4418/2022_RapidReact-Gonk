package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.subsystems.Drivetrain;


public class WaitAndLeaveTarmac extends SequentialCommandGroup {
	public WaitAndLeaveTarmac(Drivetrain drivetrain) {
		super(
			new WaitFor(),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS)
		);
	}
}