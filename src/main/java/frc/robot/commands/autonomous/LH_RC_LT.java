package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.manipulator.RunLauncherForTime;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;


public class LH_RC_LT extends SequentialCommandGroup {
	public LH_RC_LT(Drivetrain drivetrain, Intake intake, Manipulator manipulator, Vision vision) {
		super(
			new Wait(Autonomous.startDelayTime),
			new RunLauncherForTime(manipulator, 1.5d),
			new LocateAndCollectClosestCargo(drivetrain, intake, manipulator, vision),
			new DriveStraightForDistance(drivetrain, Constants.inchesToMeters(10), DriveStraightDirection.FORWARDS)
		);
	}
}