package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;
	private final JoystickControls m_driverJoystickControls;
	private final JoystickControls m_spotterJoystickControls;

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain, JoystickControls driverJoystickControls, JoystickControls spotterControls) {
		m_drivetrain = drivetrain;
		m_driverJoystickControls = driverJoystickControls;
		m_spotterJoystickControls = spotterControls;

		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		JoystickMode activePilotJoystickMode;
		JoystickControls activePilotJoystickControls;

		// driver's driving takes priority over the spotter's driving
		if (!m_driverJoystickControls.isActivelyDriving()) {
			SmartDashboard.putString("ACTIVE", "spotter");
			activePilotJoystickMode = RobotContainer.spotterJoystickMode;
			activePilotJoystickControls = m_spotterJoystickControls;
		} else {
			SmartDashboard.putString("ACTIVE", "driver");
			activePilotJoystickMode = RobotContainer.driverJoystickMode;
			activePilotJoystickControls = m_driverJoystickControls;
		}

		switch (activePilotJoystickMode) {
			case ARCADE:
				m_drivetrain.arcadeDrive(
					m_drivetrain.filterArcadeDriveForward(activePilotJoystickControls.getArcadeDriveForwardAxis()),
					m_drivetrain.filterArcadeDriveTurn(activePilotJoystickControls.getArcadeDriveTurnAxis()));
				break;
			case LONE_TANK:
			case DUAL_TANK:
				m_drivetrain.tankDrive(
					m_drivetrain.filterTankDriveForward(activePilotJoystickControls.getTankDriveLeftAxis()),
					m_drivetrain.filterTankDriveForward(activePilotJoystickControls.getTankDriveRightAxis()));
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
