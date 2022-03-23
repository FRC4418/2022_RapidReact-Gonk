package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraightWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Public constants

	public enum DriveStraightDirection {
		FORWARDS,
		BACKWARDS
	}

	// ----------------------------------------------------------
	// Private constants

	// protected final double kP = 0.02;
	
	// ----------------------------------------------------------
	// Resources
	
	protected final Drivetrain m_drivetrain;
	
	protected final DriveStraightDirection m_direction;
	
	protected final double m_maxMotorPercent;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraightWhileHeld(Drivetrain drivetrain, DriveStraightDirection direction, double maxMotorPercent) {
		m_direction = direction;
		m_drivetrain = drivetrain;
		m_maxMotorPercent = maxMotorPercent;
		
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain.disableOpenLoopRamp();
		m_drivetrain.resetIMU();
		m_drivetrain.resetEncoders();

		// we do this so that using the tank drive functions with positive speeds still works (only if we're driving backwards)
		if (m_direction == DriveStraightDirection.BACKWARDS) {
			m_drivetrain.reverseDrivetrain();
		}
	}

	@Override
	public void execute() {
		// var error = m_drivetrain.getHeading();

		// var leftSpeed = m_maxMotorPercent + kP * error;
		// var rightSpeed = m_maxMotorPercent - kP * error;

		m_drivetrain.tankDrive(m_maxMotorPercent, m_maxMotorPercent);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
		
		m_drivetrain.useTeleopOpenLoopRamp();

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