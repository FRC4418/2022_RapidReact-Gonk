package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Constants;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.intake.RunFeederAndIndexerForTime;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_PC_LT extends SequentialCommandGroup {
	public Wait_LH_PC_LT(Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
		super(
			new Wait(Autonomous.startDelayTime),
			new RunLauncherForTime(manipulator, 1.5d),
			new ParallelCommandGroup(
				new RunFeederAndIndexerForTime(intake, manipulator, 4.d),
				new DriveStraightForDistance(drivetrain, Autonomous.tarmacLeavingDistanceMeters, DriveStraightDirection.FORWARDS)
			),
			new Wait(0.25d),
			new DriveStraightForDistance(drivetrain, Constants.inchesToMeters(10), DriveStraightDirection.FORWARDS)
		);
	}
}