package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraightForDistance extends CommandBase {
	// ----------------------------------------------------------
	// Public constants

	public enum DriveStraightDirection {
		FORWARDS,
		BACKWARDS
	}

	// ----------------------------------------------------------
	// Private constants

	private final double MOTOR_OUTPUT_PERCENT = 0.45d;
	private final double kP = 0.05d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	private final double m_distanceInMeters;
	private final DriveStraightDirection m_direction;
	private ReverseDrivetrain reverseDrivetrainCommand;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInMeters, DriveStraightDirection direction) {
		m_distanceInMeters = distanceInMeters;
		m_direction = direction;
		m_drivetrain = drivetrain;
		
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain
			.disableOpenLoopRamp()
			.resetIMU()
			.resetEncoders();

		if (m_direction == DriveStraightDirection.BACKWARDS) {
			reverseDrivetrainCommand = new ReverseDrivetrain(m_drivetrain);
			reverseDrivetrainCommand.schedule();
		}
	}

	@Override
	public void execute() {
		var error = m_drivetrain.getHeading();

		var leftTankSpeed = MOTOR_OUTPUT_PERCENT + kP * error;
		var rightTankSpeed = MOTOR_OUTPUT_PERCENT - kP * error;

		m_drivetrain.tankDrive(leftTankSpeed, rightTankSpeed);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
		m_drivetrain.useJoystickDrivingOpenLoopRamp();

		if (m_direction == DriveStraightDirection.BACKWARDS) {
			reverseDrivetrainCommand.cancel();
		}
	}

	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("traveled average distance", m_drivetrain.getAverageDistance());

		return m_drivetrain.getAverageDistance() >= m_distanceInMeters;
	}
}
