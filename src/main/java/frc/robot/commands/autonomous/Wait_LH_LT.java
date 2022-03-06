package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraight.DriveStraightDirection;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_LT extends SequentialCommandGroup {
	public Wait_LH_LT(Drivetrain drivetrain, Manipulator manipulator) {
		super(
			new Wait(Autonomous.getStartDelaySeconds()),
			new RunLauncherForTime(manipulator, 1.5),
			new DriveStraightForDistance(drivetrain, Autonomous.getTarmacLeavingMeters(), DriveStraightDirection.FORWARDS, 3.)
		);
	}
}