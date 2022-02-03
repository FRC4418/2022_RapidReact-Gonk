package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer.JoystickMode;
import frc.robot.displays.JoysticksDisplay;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Drivetrain;


public class DriveWithJoysticks extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

	private final JoystickControls m_joystickControls;
	private final JoysticksDisplay m_joysticksDisplay;
	private JoystickMode m_joystickMode;

	// ----------------------------------------------------------
	// Constructor

	public DriveWithJoysticks(Drivetrain drivetrain, JoystickControls joystickControls, JoysticksDisplay joysticksDisplay) {
		this.m_joystickControls = joystickControls;
		this.m_drivetrain = drivetrain;
		this.m_joysticksDisplay = joysticksDisplay;

		addRequirements(drivetrain);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		m_joystickMode = m_joysticksDisplay.driverJoystickModeChooser.getSelected();
		
		switch (m_joystickMode) {
			case ARCADE:
				m_drivetrain.arcadeDrive(
					m_joystickControls.arcadeDriveForwardAxis(),
					m_joystickControls.arcadeDriveAngleAxis());
				break;
			case LONE_TANK:
			case DUAL_TANK:
				m_drivetrain.tankDrive(
					m_joystickControls.tankDriveLeftAxis(),
					m_joystickControls.tankDriveRightAxis()
				);
		}
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
