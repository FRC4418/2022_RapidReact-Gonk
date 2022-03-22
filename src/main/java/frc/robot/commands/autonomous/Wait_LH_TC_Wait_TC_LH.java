package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.commands.intake.ExtendIntakeArm;
import frc.robot.commands.intake.RetractIntakeArm;
import frc.robot.commands.intake.RunFeederAndIndexer;
import frc.robot.commands.intake.StopFeederAndIndexer;
import frc.robot.commands.manipulator.LaunchBalls;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_TC_Wait_TC_LH extends SequentialCommandGroup {
	public Wait_LH_TC_Wait_TC_LH(Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
		super(
			new WaitFor(Autonomous.getStartDelaySeconds()),
			new LaunchBalls(manipulator, 2),
			new ExtendIntakeArm(intake, false),
			new RunFeederAndIndexer(intake, manipulator, false),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS),
			new WaitFor(Autonomous.getTarmacReturnDelaySeconds()),
			new RunFeederAndIndexer(intake, manipulator, true),
			new SecondBallTrajectory(drivetrain, false),
			new StopFeederAndIndexer(intake, manipulator, false),
			new RetractIntakeArm(intake, false),
			new SecondBallTrajectory(drivetrain, true),
			new LaunchBalls(manipulator, 2)
		);
	}
}