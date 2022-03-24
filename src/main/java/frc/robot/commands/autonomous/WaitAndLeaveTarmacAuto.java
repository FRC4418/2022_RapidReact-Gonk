package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;


public class WaitAndLeaveTarmacAuto extends SequentialCommandGroup {
	public WaitAndLeaveTarmacAuto(Drivetrain drivetrain) {
		super(
			new WaitFor(Autonomous.getStartDelaySeconds()),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS)
		);
	}
}