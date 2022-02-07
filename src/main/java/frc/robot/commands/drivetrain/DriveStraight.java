package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class DriveStraight extends CommandBase {
	// ----------------------------------------------------------
	// Private constants

	private final double MOTOR_OUTPUT_PERCENT = 0.5d;

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
		m_drivetrain
			// .coastOrBrakeMotors(false, false)
			.setOpenLoopRampTimes(0.d);
	}

	@Override
	public void execute() {
		m_drivetrain
			.setLeftMotors(MOTOR_OUTPUT_PERCENT)
			.setRightMotors(MOTOR_OUTPUT_PERCENT);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.stopDrive();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
