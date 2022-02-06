package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;


public class ReverseDrivetrainWhileHeld extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	// ----------------------------------------------------------
	// Constructor

	public ReverseDrivetrainWhileHeld(Drivetrain drivetrain) {
		m_drivetrain = drivetrain;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain.invertLeftAndRightMotorGroups();
		m_drivetrain.swapMotorGroups();
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.invertLeftAndRightMotorGroups();
		m_drivetrain.swapMotorGroups();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
