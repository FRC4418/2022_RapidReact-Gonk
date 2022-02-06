package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	private final JoystickControls m_joystickControls;

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain, JoystickControls joystickControls) {
		m_joystickControls = joystickControls;
		m_drivetrain = drivetrain;

		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		switch (RobotContainer.driverJoystickMode) {
			case ARCADE:
				m_drivetrain.arcadeDrive(
					m_drivetrain.filterArcadeDriveForward(m_joystickControls.getArcadeDriveForwardAxis()),
					m_drivetrain.filterArcadeDriveTurn(m_joystickControls.getArcadeDriveTurnAxis()));
				break;
			case LONE_TANK:
			case DUAL_TANK:
				m_drivetrain.tankDrive(
					m_drivetrain.filterTankDriveForward(m_joystickControls.getTankDriveLeftAxis()),
					m_drivetrain.filterTankDriveForward(m_joystickControls.getTankDriveRightAxis()));
				break;
		}
	}

	@Override
	public void end(boolean interrupted) {
		
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
