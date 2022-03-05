package frc.robot.commands.autonomous;


import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class ExampleTrajectory extends CommandBase {
	// ----------------------------------------------------------
	// Private resources

	private final Drivetrain m_drivetrain;

	private Trajectory exampleTrajectory;
	private RamseteCommand ramseteCommand;

	// ----------------------------------------------------------
	// Constructor

	public ExampleTrajectory(Drivetrain drivetrain) {
		m_drivetrain = drivetrain;

		// Create a voltage constraint to ensure we don't accelerate too fast
		var autoVoltageConstraint =
			new DifferentialDriveVoltageConstraint(
				new SimpleMotorFeedforward(
					Constants.Drivetrain.ksVolts,
					Constants.Drivetrain.kvVoltSecondsPerMeter,
					Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
					Drivetrain.kDriveKinematicsV2,
				10);
	
		// Create config for trajectory
		var config =
			new TrajectoryConfig(
				Constants.Drivetrain.kMaxSpeedMetersPerSecond,
				Constants.Drivetrain.kMaxAccelerationMetersPerSecondSquared)
				// Add kinematics to ensure max speed is actually obeyed
				.setKinematics(Drivetrain.kDriveKinematicsV2)
				// Apply the voltage constraint
				.addConstraint(autoVoltageConstraint);
	
		// An example trajectory to follow.  All units in meters.
		exampleTrajectory =
			TrajectoryGenerator.generateTrajectory(
				// Start at the origin facing the +X direction
				new Pose2d(0, 0, new Rotation2d(0)),
				// Pass through these two interior waypoints, making an 's' curve path
				List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
				// End 3 meters straight ahead of where we started, facing forward
				new Pose2d(3, 0, new Rotation2d(0)),
				// Pass config
				config);
	
		ramseteCommand =
			new RamseteCommand(
				exampleTrajectory,
				m_drivetrain::getPose,
				new RamseteController(Constants.Drivetrain.kRamseteB, Constants.Drivetrain.kRamseteZeta),
				new SimpleMotorFeedforward(
					Constants.Drivetrain.ksVolts,
					Constants.Drivetrain.kvVoltSecondsPerMeter,
					Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
					Drivetrain.kDriveKinematicsV2,
					m_drivetrain::getWheelSpeeds,
				new PIDController(Constants.Drivetrain.kLeftVelocityGains.kP, 0, 0),
				new PIDController(Constants.Drivetrain.kRightVelocityGains.kP, 0, 0),
				// RamseteCommand passes volts to the callback
				m_drivetrain::tankDriveVolts,
				m_drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		// Reset odometry to the starting pose of the trajectory.
		m_drivetrain.resetOdometry(exampleTrajectory.getInitialPose());

		ramseteCommand.schedule();
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.tankDriveVolts(0., 0.);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
