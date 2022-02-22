package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraight extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	// in meters per second
	private final double MOTOR_SPEED = 0.7d;

	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	// ----------------------------------------------------------
	// Constructor

	public DriveStraight(Drivetrain drivetrain) {
		m_drivetrain = drivetrain;
		
		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain.disableOpenLoopRamp();
	}

	@Override
	public void execute() {
		m_drivetrain.tankDrive(MOTOR_SPEED, MOTOR_SPEED);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
		m_drivetrain.useJoystickDrivingOpenLoopRamp();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
