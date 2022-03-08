package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraight.DriveStraightDirection;
import frc.robot.commands.intake.ExtendIntakeArm;
import frc.robot.commands.intake.RetractIntakeArm;
import frc.robot.commands.intake.RunFeederAndIndexer;
import frc.robot.commands.intake.StopFeederAndIndexer;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_PC_LH extends SequentialCommandGroup {
	public Wait_LH_PC_LH(Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
		super(
			new WaitFor(),
			new RunLauncherForTime(manipulator),
			new ParallelCommandGroup(
				new ExtendIntakeArm(intake, false),
				new RunFeederAndIndexer(intake, manipulator),
				new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS)
			),
			new StopFeederAndIndexer(intake, manipulator),
			new RetractIntakeArm(intake, false),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.BACKWARDS),
			new RunLauncherForTime(manipulator)
		);
	}
}