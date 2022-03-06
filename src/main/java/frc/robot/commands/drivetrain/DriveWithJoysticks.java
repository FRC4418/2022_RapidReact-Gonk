package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain) {
		m_drivetrain = drivetrain;

		addRequirements(m_drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain.useJoystickDrivingOpenLoopRamp();
	}

	@Override
	public void execute() {
		JoystickMode activePilotJoystickMode;
		JoystickControls activePilotJoystickControls;

		// driver's driving takes priority over the spotter's driving
		if (!RobotContainer.driverJoystickControls.isActivelyDriving()) {
			activePilotJoystickMode = RobotContainer.spotterJoystickMode;
			activePilotJoystickControls = RobotContainer.spotterJoystickControls;
		} else {
			activePilotJoystickMode = RobotContainer.driverJoystickMode;
			activePilotJoystickControls = RobotContainer.driverJoystickControls;
		}

		switch (activePilotJoystickMode) {
			case ARCADE:
				if (m_drivetrain.usingSlewRateLimiters()) {
					m_drivetrain.arcadeDrive(
						m_drivetrain.filterArcadeDriveForward(activePilotJoystickControls.getArcadeDriveForwardAxis()),
						m_drivetrain.filterArcadeDriveTurn(activePilotJoystickControls.getArcadeDriveTurnAxis()));
				} else {
					m_drivetrain.arcadeDrive(
						activePilotJoystickControls.getArcadeDriveForwardAxis(),
						activePilotJoystickControls.getArcadeDriveTurnAxis());
				}
				break;
			case LONE_TANK:
			case DUAL_TANK:
				if (m_drivetrain.usingSlewRateLimiters()) {
					m_drivetrain.tankDrive(
						m_drivetrain.filterTankDriveLeftForward(activePilotJoystickControls.getTankDriveLeftAxis()),
						m_drivetrain.filterTankDriveRightForward(activePilotJoystickControls.getTankDriveRightAxis()));
				} else {
					m_drivetrain.tankDrive(
						activePilotJoystickControls.getTankDriveLeftAxis(),
						activePilotJoystickControls.getTankDriveRightAxis());
				}
				break;
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
