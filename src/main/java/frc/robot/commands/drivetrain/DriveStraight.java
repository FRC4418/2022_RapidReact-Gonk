package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraight extends CommandBase {
	// ----------------------------------------------------------
	// Public constants

	public enum DriveStraightDirection {
		FORWARDS,
		BACKWARDS
	}

	// ----------------------------------------------------------
	// Private constants

	protected final double kP = 0.03;
	
	// ----------------------------------------------------------
	// Resources
	
	protected final Drivetrain m_drivetrain;
	
	protected final DriveStraightDirection m_direction;
	
	protected final double m_motorMPS;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraight(Drivetrain drivetrain, DriveStraightDirection direction, double motorMPS) {
		m_direction = direction;
		m_drivetrain = drivetrain;
		m_motorMPS = motorMPS;
		
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

		// we do this so that using the tank drive functions with positive speeds still works (only if we're driving backwards)
		if (m_direction == DriveStraightDirection.BACKWARDS) {
			m_drivetrain.reverseDrivetrain();
		}
	}

	@Override
	public void execute() {
		var error = m_drivetrain.getHeading();

		var leftSpeed = 1.0 + kP * error;
		var rightSpeed = 1.0 - kP * error;

		m_drivetrain.tankDrive(leftSpeed, rightSpeed);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();

		// undo the drivetrain reversal we did in the initialization (only if we're driving backwards)
		if (m_direction == DriveStraightDirection.BACKWARDS) {
			m_drivetrain.reverseDrivetrain();
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}