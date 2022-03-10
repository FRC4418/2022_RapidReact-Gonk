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
			case CURVATURE:
				if (m_drivetrain.usingSlewRateLimiters()) {
					m_drivetrain.curvatureDrive(
						m_drivetrain.filterCurvatureForward(activePilotJoystickControls.getCurvatureForwardAxis()),
						m_drivetrain.filterCurvatureRotation(activePilotJoystickControls.getCurvatureRotationAxis()),
						true);
				} else {
					m_drivetrain.curvatureDrive(
						activePilotJoystickControls.getCurvatureForwardAxis(),
						activePilotJoystickControls.getCurvatureRotationAxis(),
						true);
				}
				break;
			case ARCADE:
				if (m_drivetrain.usingSlewRateLimiters()) {
					m_drivetrain.arcadeDrive(
						m_drivetrain.filterArcadeForward(activePilotJoystickControls.getArcadeForwardAxis()),
						m_drivetrain.filterArcadeTurn(activePilotJoystickControls.getArcadeTurnAxis()));
				} else {
					m_drivetrain.arcadeDrive(
						activePilotJoystickControls.getArcadeForwardAxis(),
						activePilotJoystickControls.getArcadeTurnAxis());
				}
				break;
			case LONE_TANK:
			case DUAL_TANK:
				if (m_drivetrain.usingSlewRateLimiters()) {
					m_drivetrain.tankDrive(
						m_drivetrain.filterTankLeftForward(activePilotJoystickControls.getTankLeftAxis()),
						m_drivetrain.filterTankRightForward(activePilotJoystickControls.getTankRightAxis()));
				} else {
					m_drivetrain.tankDrive(
						activePilotJoystickControls.getTankLeftAxis(),
						activePilotJoystickControls.getTankRightAxis());
				}
				break;
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
