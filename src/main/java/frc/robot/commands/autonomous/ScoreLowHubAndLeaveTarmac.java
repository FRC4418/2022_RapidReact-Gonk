package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.manipulator.AutoRunLauncherDemo;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class ScoreLowHubAndLeaveTarmac extends CommandBase {
	private final Drivetrain m_drivetrain;
	private final Manipulator m_manipulator;

	private SequentialCommandGroup command;

	public ScoreLowHubAndLeaveTarmac(Drivetrain drivetrain, Manipulator manipulator) {
		m_drivetrain = drivetrain;
		m_manipulator = manipulator;

		command = new SequentialCommandGroup(
			new AutoRunLauncherDemo(m_manipulator, 1.5d),
			new DriveStraightForDistance(m_drivetrain, 3d, DriveStraightDirection.BACKWARDS)
		);
	}

	@Override
	public void initialize() {
		command.schedule();
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
