package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Constants;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.commands.manipulator.RunLauncherAutoRPMForAutoTime;
import frc.robot.commands.vision.CollectClosestCargo;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;


public class Wait_LH_RC_LT extends SequentialCommandGroup {
	public Wait_LH_RC_LT(Drivetrain drivetrain, Intake intake, Manipulator manipulator, Vision vision) {
		super(
			new WaitFor(),
			new RunLauncherAutoRPMForAutoTime(manipulator),
			new CollectClosestCargo(drivetrain, intake, manipulator, vision, false),
			new DriveStraightForDistance(drivetrain, Constants.inchesToMeters(10), DriveStraightDirection.FORWARDS)
		);
	}
}