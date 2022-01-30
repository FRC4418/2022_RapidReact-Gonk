package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.RobotContainer.JoystickModes;
import frc.robot.huds.MotorTesting;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class HUD extends SubsystemBase {
	// ----------------------------------------------------------
	// Public resources

	public MotorTesting motorTesting;
	
	// ----------------------------------------------------------
	// Private resources


	private ShuffleboardTab HUDTab;

	private ShuffleboardTab diagnosticsTab;


	// ----------------------------------------------------------
	// HUD resources


	private SendableChooser<AutonomousRoutine> sendableAutoRoutineChooser = new SendableChooser<>();

	private SendableChooser<JoystickModes> sendableDriverJoystickModeChooser = new SendableChooser<>();
	private SendableChooser<Joystick> sendableDriverJoystickDeviceChooser = new SendableChooser<>();

	private SendableChooser<JoystickModes> sendableSpotterJoystickModeChooser = new SendableChooser<>();
	private SendableChooser<Joystick> sendableSpotterJoystickDeviceChooser = new SendableChooser<>();


	// ----------------------------------------------------------
	// Constructor and methods

	
	public HUD() {

	}

	public void initializeHUD() {
		HUDTab = Shuffleboard.getTab("HUD");

		// ----------------------------------------------------------
		// Choose autonomous routine

		// setting default options for sendable choosers also adds the label-value pair as an option
		sendableAutoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
		sendableAutoRoutineChooser.addOption("Drive Straight to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
		HUDTab
			.add("Autonomous Routine", sendableAutoRoutineChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(0, 0)
			.withSize(2, 1);

		// ----------------------------------------------------------
		// Choose driver joystick mode and devices

		sendableDriverJoystickModeChooser.setDefaultOption("Arcade", JoystickModes.ARCADE);
		sendableDriverJoystickModeChooser.addOption("Tank", JoystickModes.TANK);
		HUDTab
			.add("Driver Joystick Mode",sendableDriverJoystickModeChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(0, 2)
			.withSize(2, 1);
		// sendableDriverJoystickDeviceChooser.setDefaultOption("ex. Xbox1", SOME_JOYSTICK);
		// sendableDriverJoystickDeviceChooser.addOption("Tank", SOME_JOYSTICK);
		// HUDTab
		// 	.add("Driver Joystick Device",sendableDriverJoystickDeviceChooser)
		// 	.withWidget(BuiltInWidgets.kComboBoxChooser)
		// 	.withPosition(2, 2)
		// 	.withSize(2, 1);

		// ----------------------------------------------------------
		// Choose spotter joystick mode and devices

		sendableSpotterJoystickModeChooser.setDefaultOption("Arcade", JoystickModes.ARCADE);
		sendableSpotterJoystickModeChooser.addOption("Tank", JoystickModes.TANK);
		HUDTab
			.add("Spotter Joystick Mode",sendableSpotterJoystickModeChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(0, 3)
			.withSize(2, 1);
		// sendableSpotterJoystickDeviceChooser.setDefaultOption("ex. Xbox1", SOME_JOYSTICK);
		// sendableSpotterJoystickDeviceChooser.addOption("Tank", SOME_JOYSTICK);
		// HUDTab
		// 	.add("Spotter Joystick Device",sendableSpotterJoystickDeviceChooser)
		// 	.withWidget(BuiltInWidgets.kComboBoxChooser)
		// 	.withPosition(2, 3)
		// 	.withSize(2, 1);
	
	}

	public void initializeDiagnostics() {
		diagnosticsTab = Shuffleboard.getTab("Diagnostics");
		
		motorTesting = new MotorTesting(diagnosticsTab, 0, 0);
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}