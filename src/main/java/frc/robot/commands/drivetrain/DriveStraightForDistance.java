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
	private final double kP = 0.1d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	private final double m_distanceInInches;
	private final DriveStraightDirection m_direction;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightForDistance(Drivetrain drivetrain, double distanceInInches, DriveStraightDirection direction) {
		m_distanceInInches = distanceInInches;
		m_direction = direction;
		m_drivetrain = drivetrain;
		
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain
			// .coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d)
			.resetEncoders();
	}

	@Override
	public void execute() {
		double error = m_drivetrain.getLeftDistance() - m_drivetrain.getRightDistance();

		if (m_direction == DriveStraightDirection.FORWARDS) {
			m_drivetrain.tankDrive(MOTOR_OUTPUT_PERCENT + kP * error, MOTOR_OUTPUT_PERCENT - kP * error);
			// m_drivetrain.tankDrive(MOTOR_OUTPUT_PERCENT, MOTOR_OUTPUT_PERCENT);
		} else {
			m_drivetrain.tankDrive(-(MOTOR_OUTPUT_PERCENT + kP * error), -(MOTOR_OUTPUT_PERCENT - kP * error));
			// m_drivetrain.tankDrive(-MOTOR_OUTPUT_PERCENT, -MOTOR_OUTPUT_PERCENT);
		}

		SmartDashboard.putNumber("Left Encoder", m_drivetrain.getLeftDistance());
		SmartDashboard.putNumber("Right Encoder", m_drivetrain.getRightDistance());
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
	}

	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("traveled average distance", m_drivetrain.getAverageDistance());

		return Math.abs(m_drivetrain.getAverageDistance()) >= m_distanceInInches;
	}
}
