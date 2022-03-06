package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraight.DriveStraightDirection;
import frc.robot.commands.intake.RunFeederAndIndexerForTime;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_PC_LH extends SequentialCommandGroup {
	public Wait_LH_PC_LH(Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
		super(
			new Wait(Autonomous.getStartDelaySeconds()),
			new RunLauncherForTime(manipulator, 1.0),
			new ParallelCommandGroup(
				new RunFeederAndIndexerForTime(intake, manipulator, 4.),
				new DriveStraightForDistance(drivetrain, Autonomous.getTarmacLeavingMeters(), DriveStraightDirection.FORWARDS, 3.)
			),
			new DriveStraightForDistance(drivetrain, Autonomous.getTarmacLeavingMeters(), DriveStraightDirection.BACKWARDS, 3.),
			new RunLauncherForTime(manipulator, 1.0)
		);
	}
}