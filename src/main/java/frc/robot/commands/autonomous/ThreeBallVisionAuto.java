package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Constants;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.commands.manipulator.LaunchOneBall;
import frc.robot.commands.vision.CollectClosestCargo;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;


public class ThreeBallVisionAuto extends SequentialCommandGroup {
	public ThreeBallVisionAuto(Drivetrain drivetrain, Intake intake, Manipulator manipulator, Vision vision) {
		super(
			new WaitFor(Autonomous.getStartDelaySeconds()),
			new LaunchOneBall(manipulator),
			new CollectClosestCargo(drivetrain, intake, manipulator, vision, false),
			new CollectClosestCargo(drivetrain, intake, manipulator, vision, false),
			new DriveStraightForDistance(drivetrain, Constants.inchesToMeters(10), DriveStraightDirection.FORWARDS)
		);
	}
}