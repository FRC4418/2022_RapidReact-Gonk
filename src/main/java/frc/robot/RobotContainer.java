package frc.robot;


import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.joystickcontrols.IO.JoystickDeviceType;
import frc.robot.joystickcontrols.IO.X3D;
import frc.robot.joystickcontrols.dualjoystickcontrols.dualtank.DriverX3DDualTankControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.arcade.DriverX3DArcadeControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.arcade.DriverXboxArcadeControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.arcade.SpotterXboxControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.lonetank.DriverXboxLoneTankControls;
import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveWithJoysticks;
import frc.robot.commands.drivetrain.DriveStraightForDistance.DriveStraightDirection;
import frc.robot.commands.intake.RunFeederWithTrigger;
import frc.robot.displays.diagnosticsdisplays.DiagnosticsDisplay;
import frc.robot.displays.diagnosticsdisplays.MotorTestingDisplay;
import frc.robot.displays.diagnosticsdisplays.SlewRateLimiterTuningDisplay;
import frc.robot.displays.huddisplays.AutonomousDisplay;
import frc.robot.displays.huddisplays.JoysticksDisplay;
import frc.robot.displays.huddisplays.RobotChooserDisplay;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Sensory;


public class RobotContainer {
	// ----------------------------------------------------------
    // Robot-configuration constants


	private final boolean enableDiagnostics = true;
	
	private final boolean disableJoystickConnectionWarnings = true;


	// ----------------------------------------------------------
	// Public constants

	public enum TeamRobot {
		VERSACHASSIS_ONE,
		VERSACHASSIS_TWO
	}

	public enum JoystickMode {
		ARCADE,
		LONE_TANK,	// tank drive that uses just one joystick (ex. Xbox with two thumbsticks)
		DUAL_TANK	// tank drive that uses two joysticsks (ex. two X3Ds, respectively for the left and right motors)
	}

	public static final JoystickMode defaultDriverJoystickMode = JoystickMode.ARCADE;
	public static final JoystickMode defaultSpotterJoystickMode = JoystickMode.ARCADE;


	// ----------------------------------------------------------
	// Private constants


	private final int[] driverJoystickPorts = new int[] {0, 1};
	private final int[] spotterJoystickPorts = new int[] {2, 3};


	// ----------------------------------------------------------
    // Public resources


	public static TeamRobot teamRobot;

	public static JoystickMode driverJoystickMode = defaultDriverJoystickMode;
	public static JoystickMode spotterJoystickMode = defaultSpotterJoystickMode;


    // ----------------------------------------------------------
    // Private resources


	private final ShuffleboardTab HUDTab = Shuffleboard.getTab("HUD");

	private final RobotChooserDisplay robotChooserDisplay;
	private final JoysticksDisplay joysticksDisplay;

	private final ArrayList<DiagnosticsDisplay> diagnosticsDisplays = new ArrayList<>();

	// has default USB values
	private JoystickDeviceType driverJoystickDeviceType = JoystickDeviceType.XboxController;
	private JoystickDeviceType spotterJoystickDeviceType = JoystickDeviceType.XboxController;

	private JoystickControls driverJoystickControls;
	private JoystickControls spotterJoystickControls;
    

    // ----------------------------------------------------------
    // Subsystems and commands
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	
	public final Manipulator manipulator = new Manipulator();
	
	public final Sensory sensory = new Sensory();

	public final Autonomous autonomous = new Autonomous();
	private final DriveStraightForDistance autoDriveStraightForDistance;


    // ----------------------------------------------------------
    // Constructor


    public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(disableJoystickConnectionWarnings);

		robotChooserDisplay = new RobotChooserDisplay(HUDTab, 0, 0);
		joysticksDisplay = new JoysticksDisplay(HUDTab, 2, 0);
		new AutonomousDisplay(HUDTab, 0, 1);

		if (enableDiagnostics) {
			diagnosticsDisplays.addAll(Arrays.asList(
				new MotorTestingDisplay(intake, manipulator, 0, 0),
				new SlewRateLimiterTuningDisplay(drivetrain, 7, 0)
			));
		}

		setupDriverJoystickControls();
		setupSpotterJoystickControls();

		autoDriveStraightForDistance = new DriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);

		drivetrain.setDefaultCommand(new DriveWithJoysticks(drivetrain, driverJoystickControls));
		intake.setDefaultCommand(new RunFeederWithTrigger(intake, driverJoystickControls));
    }


	// ----------------------------------------------------------
    // Command getters


	public Command defaultAutoCommand() {
		return autoDriveStraightForDistance;
	}

	
	// ----------------------------------------------------------
    // Methods


	public RobotContainer addDiagnosticsEntryListeners() {
		for (var display: diagnosticsDisplays) {
			display.addEntryListeners();
		}
		return this;
	}

	public RobotContainer removeDiagnosticsEntryListeners() {
		for (var display: diagnosticsDisplays) {
			display.removeEntryListeners();
		}
		return this;
	} 

	public RobotContainer listenForRobotSelection() {
		var newRobotSelection = robotChooserDisplay.teamRobotChooser.getSelected();
		if (teamRobot != newRobotSelection) {
			teamRobot = newRobotSelection;
			configureRobotSpecificDrivetrain();
		}
		return this;
	}
	
	private void configureRobotSpecificDrivetrain() {
		switch (teamRobot) {
			default:
				DriverStation.reportError("Unsupported robot selection found while configuring the robot-specific drivetrain", true);
				break;
			case VERSACHASSIS_TWO:
				// true means flip the left side
				drivetrain.invertLeftOrRightMotors(true);
				break;
			case VERSACHASSIS_ONE:
				drivetrain.invertLeftOrRightMotors(false);
				break;
		}
	}

	public RobotContainer listenForJoystickModes() {
		var newDriverJoystickMode = joysticksDisplay.driverJoystickModeChooser.getSelected();
		if (driverJoystickMode != newDriverJoystickMode) {
			driverJoystickMode = newDriverJoystickMode;
			setupDriverJoystickControls();
		}

		var newSpotterJoystickMode = joysticksDisplay.spotterJoystickModeChooser.getSelected();
		if (spotterJoystickMode != newSpotterJoystickMode) {
			spotterJoystickMode = newSpotterJoystickMode;
			setupSpotterJoystickControls();
		}
		return this;
	}

	private JoystickDeviceType getJoystickDeviceTypeFor(int port) {
		if (DriverStation.getJoystickIsXbox(port)) {
			return JoystickDeviceType.XboxController;
		} else {
			switch (DriverStation.getJoystickName(port)) {
				default:
					// DriverStation.reportWarning("Scanning for joystick device changes found an unrecognized device type", true);
					return JoystickDeviceType.NULL;
				case X3D.USB_DEVICE_NAME:
					return JoystickDeviceType.X3D;
			}
		}
	}
	
	public RobotContainer listenForJoystickDevices() {
		// code is repetitive for driver and spotter, but this is on purpose so that we can use the different setup functions
		// only same-type dual controls are supported, so here we are just looking at the first port for the driver's and spotter's port ranges

		var newDriverJoystickDeviceType = getJoystickDeviceTypeFor(driverJoystickPorts[0]);
		if (newDriverJoystickDeviceType != JoystickDeviceType.NULL && driverJoystickDeviceType != newDriverJoystickDeviceType) {
			driverJoystickDeviceType = newDriverJoystickDeviceType;
			setupDriverJoystickControls();
		}

		var newSpotterJoystickDeviceType = getJoystickDeviceTypeFor(spotterJoystickPorts[0]);
		if (newSpotterJoystickDeviceType != JoystickDeviceType.NULL && spotterJoystickDeviceType != newSpotterJoystickDeviceType) {
			spotterJoystickDeviceType = newSpotterJoystickDeviceType;
			setupSpotterJoystickControls();
		}
		return this;
	}

	// using a different setup function for the driver and the spotter allows special switch cases for each person, meaning that there can be a unique driver and spotter configuration for each joystick setup (ex. one Xbox controller, two X3Ds, etc)
	
	private void setupDriverJoystickControls() {
		var firstJoystick = new Joystick(driverJoystickPorts[0]);
		var secondJoystick = new Joystick(driverJoystickPorts[1]);

		switch (driverJoystickMode) {
			default:
				DriverStation.reportError("Unsupported joystick mode detected while setting up driver joystick controls", true);
				break;
			case ARCADE:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for arcade mode", true);
						break;
					case XboxController:
						driverJoystickControls = new DriverXboxArcadeControls(firstJoystick, drivetrain, intake, manipulator);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DArcadeControls(firstJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case LONE_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						SmartDashboard.putString("Reached Lone Tank", "Xbox");
						driverJoystickControls = new DriverXboxLoneTankControls(firstJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case DUAL_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for dual-tank mode", true);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DDualTankControls(firstJoystick, secondJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
		}
	}

	// TODO: P1 Figure out a way to switch between the driver and spotter controlling the robot

	private void setupSpotterJoystickControls() {
		var firstJoystick = new Joystick(spotterJoystickPorts[0]);
		var secondJoystick = new Joystick(spotterJoystickPorts[1]);

		spotterJoystickControls = new SpotterXboxControls(firstJoystick, drivetrain, intake, manipulator);

		// TODO: P1 If spotter should be allowed to drive, implement setup switch cases for spotter joystick modes
		switch (spotterJoystickMode) {
			default:
				DriverStation.reportError("Unsupported joystick mode detected while setting up spotter joystick controls", true);
				break;
			case ARCADE:
				switch (spotterJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up spotter joystick controls for arcade mode", true);
						break;
					case XboxController:
						
						break;
					case X3D:
						
						break;
				}
				break;
			case LONE_TANK:
				switch (spotterJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up spotter joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						SmartDashboard.putString("Reached Lone Tank", "Xbox");
						
						break;
				}
				break;
			case DUAL_TANK:
				switch (spotterJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up spotter joystick controls for dual-tank mode", true);
						break;
					case X3D:

						break;
				}
				break;
		}
	}
}