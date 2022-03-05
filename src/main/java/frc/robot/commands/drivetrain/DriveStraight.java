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

	protected final double kMotorMPS = 3.;
	protected final double kP = 0.03;

	// ----------------------------------------------------------
	// Resources

	protected final Drivetrain m_drivetrain;

	protected final DriveStraightDirection m_direction;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraight(Drivetrain drivetrain, DriveStraightDirection direction) {
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

		// we do this so that using the tank drive functions with positive speeds still works (only if we're driving backwards)
		if (m_direction == DriveStraightDirection.BACKWARDS) {
			m_drivetrain.reverseDrivetrain();
		}
	}

	@Override
	public void execute() {
		var error = m_drivetrain.getHeading();

		var leftTankMPS = kMotorMPS + kP * error;
		var rightTankMPS = kMotorMPS - kP * error;

		m_drivetrain
			.setLeftMPS(leftTankMPS)
			.setRightMPS(rightTankMPS);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
		m_drivetrain.useJoystickDrivingOpenLoopRamp();

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