package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
		m_drivetrain.invertAndFlipBothMotorSides();
	}

	int counter = 0;

	@Override
	public void execute() {
		SmartDashboard.putNumber("REVERSING", counter++);
	}

	@Override
	public void end(boolean interrupted) {
		m_drivetrain.invertAndFlipBothMotorSides();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
