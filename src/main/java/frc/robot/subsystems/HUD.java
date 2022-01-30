package frc.robot.subsystems;


import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.RobotContainer.JoystickModes;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class HUD extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


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
	// Motor testing resources


	public NetworkTableEntry motorTestingModeToggleSwitch;

	public NetworkTableEntry manipulatorIndexerToggleSwitch;
	public NetworkTableEntry manipulatorIndexerOutputPercentTextView;

	public NetworkTableEntry manipulatorLauncherToggleSwitch;
	public NetworkTableEntry manipulatorLauncherOutputPercentTextView;

	public NetworkTableEntry intakeRollerToggleSwitch;
	public NetworkTableEntry intakeRollerOutputPercentTextView;

	public NetworkTableEntry intakeRetractorToggleSwitch;
	public NetworkTableEntry intakeRetractorPositionTextView;


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

		// ----------------------------------------------------------
		// Motor testing

		var motorTestingLayout = diagnosticsTab
			.getLayout("Motor Testing", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(0, 0)
			.withSize(4, 3);

			// Enable/disable motor testing
			motorTestingModeToggleSwitch = motorTestingLayout
				.add("Enabled", false)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();

			// put into the 2nd slot of motorTestingLayout's vertical stack
			var horizontalStack = motorTestingLayout
				.getLayout("Horizontal Stack", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// ----------------------------------------------------------
				// Testing the intake motors

				var intakeLayout = horizontalStack
					.getLayout("Intake", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					// Intake retractor motor

					var retractorLayout = intakeLayout
						.getLayout("Retractor", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						// I have no fucking clue why the textView entry is always added before (to the 2nd row) the toggleSwitch but it's good enough
						intakeRetractorToggleSwitch = retractorLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						intakeRetractorPositionTextView = retractorLayout
							.add("Position", Intake.RETRACT_MOTOR_DEFAULT_POSITION)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

					// intake roller motor
					
					var rollerLayout = intakeLayout
						.getLayout("Roller", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						intakeRollerToggleSwitch = rollerLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						intakeRollerOutputPercentTextView = rollerLayout
							.add("% Output", Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

				// ----------------------------------------------------------
				// Testing the conveyor-shooter motors

				var manipulatorLayout = horizontalStack
					.getLayout("Manipulator", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					var indexerLayout = manipulatorLayout
						.getLayout("Indexer", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						manipulatorIndexerToggleSwitch = indexerLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						manipulatorIndexerOutputPercentTextView = indexerLayout
							.add("% Output", Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

					// Manipulator launcher motor
					
					var launcherLayout = manipulatorLayout
						.getLayout("Launcher", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						manipulatorLauncherToggleSwitch = launcherLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						manipulatorLauncherOutputPercentTextView = launcherLayout
							.add("% Output", Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}