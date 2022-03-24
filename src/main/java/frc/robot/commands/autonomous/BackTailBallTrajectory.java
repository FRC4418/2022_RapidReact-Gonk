package frc.robot.commands.autonomous;


import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class BackTailBallTrajectory extends CommandBase {
	// ----------------------------------------------------------
	// Private resources

	private final Drivetrain m_drivetrain;

	private final boolean m_reversePath;

	private Trajectory exampleTrajectory;
	private RamseteCommand ramseteCommand;

	// ----------------------------------------------------------
	// Constructor

	public BackTailBallTrajectory(Drivetrain drivetrain, boolean reversePath) {
		m_drivetrain = drivetrain;
		m_reversePath = reversePath;

		// Create a voltage constraint to ensure we don't accelerate too fast
		var autoVoltageConstraint =
			new DifferentialDriveVoltageConstraint(
				new SimpleMotorFeedforward(
					Constants.Drivetrain.ksVolts,
					Constants.Drivetrain.kvVoltSecondsPerMeter,
					Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
					Drivetrain.kDriveKinematics,
				10);
	
		// Create config for trajectory
		var config =
			new TrajectoryConfig(
				Constants.Drivetrain.kMaxSpeedMetersPerSecond,
				Constants.Drivetrain.kMaxAccelerationMetersPerSecondSquared)
				// Add kinematics to ensure max speed is actually obeyed
				.setKinematics(Drivetrain.kDriveKinematics)
				// Apply the voltage constraint
				.addConstraint(autoVoltageConstraint);
		
		double xDistance = Constants.inchesToMeters(154.459 + 3.);
		double yDistance = Constants.inchesToMeters(19.102);

		if (m_reversePath) {
			xDistance *= -1;
			yDistance *= -1;
		}

		var waypoints = List.of(
			new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
			new Pose2d(xDistance, yDistance, Rotation2d.fromDegrees(7.05))
		);

		// An example trajectory to follow. All units in meters.
		exampleTrajectory = TrajectoryGenerator.generateTrajectory(waypoints, config);
	
		ramseteCommand =
			new RamseteCommand(
				exampleTrajectory,
				m_drivetrain::getPose,
				new RamseteController(Constants.Drivetrain.kRamseteB, Constants.Drivetrain.kRamseteZeta),
				new SimpleMotorFeedforward(
					Constants.Drivetrain.ksVolts,
					Constants.Drivetrain.kvVoltSecondsPerMeter,
					Constants.Drivetrain.kaVoltSecondsSquaredPerMeter),
					Drivetrain.kDriveKinematics,
					m_drivetrain::getWheelSpeeds,
				new PIDController(Constants.Drivetrain.kLeftVelocityGains.kP, Constants.Drivetrain.kLeftVelocityGains.kI, Constants.Drivetrain.kLeftVelocityGains.kD),
				new PIDController(Constants.Drivetrain.kRightVelocityGains.kP, Constants.Drivetrain.kRightVelocityGains.kI, Constants.Drivetrain.kRightVelocityGains.kD),
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
	public void end(boolean interrupted) {
		m_drivetrain.tankDriveVolts(0., 0.);
	}

	@Override
	public boolean isFinished() {
		return ramseteCommand.isFinished();
	}
}
