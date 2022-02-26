package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Conversion;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.intake.RunFeederAndIndexerForTime;
import frc.robot.commands.manipulator.AutoRunLauncherDemo;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class LH_PC_LT extends SequentialCommandGroup {
	public LH_PC_LT(Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
		super(
			new Wait(Autonomous.startDelayTime),
			new AutoRunLauncherDemo(manipulator, 1.5d),
			new DriveStraightForDistance(drivetrain, Autonomous.tarmacLeavingDistanceMeters, DriveStraightDirection.FORWARDS),
			new Wait(0.5d),
			new RunFeederAndIndexerForTime(intake, manipulator, 2.d),
			new DriveStraightForDistance(drivetrain, Conversion.inchesToMeters(10), DriveStraightDirection.FORWARDS)
		);
	}
}