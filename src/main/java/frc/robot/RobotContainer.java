package frc.robot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.joystickcontrols.IO.JoystickDeviceType;
import frc.robot.joystickcontrols.IO.X3D;
import frc.robot.joystickcontrols.IO.XboxController;
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
import frc.robot.displays.huddisplays.HUDDisplay;
import frc.robot.displays.huddisplays.JoysticksDisplay;
import frc.robot.displays.huddisplays.KidsSafetyDisplay;
import frc.robot.displays.huddisplays.RobotChooserDisplay;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Sensory;
import frc.robot.subsystems.Drivetrain.MotorGroup;


public class RobotContainer {
	// ----------------------------------------------------------
    // Robot-configuration constants


	// initial value is the start-up configuration
	public static final boolean usingKidsSafetyMode = false;

	public static final boolean enableDiagnostics = true;
	
	private final boolean disableJoystickConnectionWarnings = true;


	// ----------------------------------------------------------
	// Public constants


	public enum TeamRobot {
		VERSACHASSIS_ONE,
		VERSACHASSIS_TWO
	}

	public enum Pilot {
		DRIVER,
		SPOTTER
	}

	public enum JoystickMode {
		ARCADE,
		LONE_TANK,	// tank drive that uses just one joystick (ex. Xbox with two thumbsticks)
		DUAL_TANK	// tank drive that uses two joysticsks (ex. two X3Ds, respectively for the left and right motors)
	}


	// ----------------------------------------------------------
	// Private constants


	private static final int[] driverJoystickPorts = new int[] {0, 1};
	private static final int[] spotterJoystickPorts = new int[] {2, 3};


	// ----------------------------------------------------------
    // Publicly static resources


	public static boolean usingV1Drivetrain = false;

	// joystick control resources are publicly static because 
	public static JoystickControls driverJoystickControls;
	public static final JoystickMode defaultDriverJoystickMode = JoystickMode.ARCADE;
	
	public static JoystickControls spotterJoystickControls;
	public static final JoystickMode defaultSpotterJoystickMode = JoystickMode.ARCADE;


	// ----------------------------------------------------------
    // Public resources


	public static TeamRobot teamRobot;

	public static JoystickMode driverJoystickMode = defaultDriverJoystickMode;
	public static JoystickMode spotterJoystickMode = defaultSpotterJoystickMode;


    // ----------------------------------------------------------
    // Private resources


	private final RobotChooserDisplay robotChooserDisplay;
	private final JoysticksDisplay joysticksDisplay;

	private final ArrayList<HUDDisplay> hudDisplays = new ArrayList<>();
	private final ArrayList<DiagnosticsDisplay> diagnosticsDisplays = new ArrayList<>();

	// has default USB values
	private JoystickDeviceType driverJoystickDeviceType = JoystickDeviceType.XboxController;
	private JoystickDeviceType spotterJoystickDeviceType = JoystickDeviceType.XboxController;
    

    // ----------------------------------------------------------
    // Subsystems and commands
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	
	public final Manipulator manipulator = new Manipulator();
	
	public final Sensory sensory = new Sensory();

	public final Autonomous autonomous = new Autonomous();


    // ----------------------------------------------------------
    // Constructor


    public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(disableJoystickConnectionWarnings);

		hudDisplays.addAll(Arrays.asList(
			robotChooserDisplay = new RobotChooserDisplay(0, 0),
			joysticksDisplay = new JoysticksDisplay(2, 0),
			new KidsSafetyDisplay(drivetrain, 5, 0),
			new AutonomousDisplay(0, 1)
		));
		for (var display: hudDisplays) {
			display.initialize();
			display.addEntryListeners();
		}

		if (enableDiagnostics) {
			diagnosticsDisplays.addAll(Arrays.asList(
				new MotorTestingDisplay(intake, manipulator, 0, 0),
				new SlewRateLimiterTuningDisplay(drivetrain, 7, 0)
			));
			for (var display: diagnosticsDisplays) {
				display.initialize();
			}
		}

		setupDriverJoystickControls();
		setupSpotterJoystickControls();

		drivetrain.setDefaultCommand(new DriveWithJoysticks(drivetrain));
		intake.setDefaultCommand(new RunFeederWithTrigger(intake, manipulator));
    }


	// ----------------------------------------------------------
    // Command getters


	public Command defaultAutoCommand() {
		return new DriveStraightForDistance(drivetrain, 3.0d, DriveStraightDirection.BACKWARDS);
	}

	
	// ----------------------------------------------------------
    // Print-out joystick for debugging

	
	private final Joystick m_printOutjoystick = new Joystick(0);
	public RobotContainer initializeJoystickValues() {
		m_printOutjoystick.setXChannel(0);
		m_printOutjoystick.setYChannel(1);
		return this;
	}

	// just a print-out function to help with joystick controls debugging
	public RobotContainer printJoystickValues() {
		SmartDashboard.putNumber("Get X", m_printOutjoystick.getX());
		SmartDashboard.putNumber("Get Y", m_printOutjoystick.getY());

		SmartDashboard.putNumber("Raw Left Trigger", m_printOutjoystick.getRawAxis(2));
		SmartDashboard.putNumber("Raw Right Trigger", m_printOutjoystick.getRawAxis(3));

		SmartDashboard.putNumber("WPI Mag", m_printOutjoystick.getMagnitude());
		return this;
	}


	// ----------------------------------------------------------
    // Diagnostic-entry listeners


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


	// ----------------------------------------------------------
    // Robot-drivetrain listeners


	public RobotContainer listenForRobotSelection() {
		var newRobotSelection = robotChooserDisplay.teamRobotChooser.getSelected();
		if (teamRobot != newRobotSelection) {
			teamRobot = newRobotSelection;
			configureRobotSpecificDrivetrain();
			usingV1Drivetrain = true;
		}
		return this;
	}
	
	private void configureRobotSpecificDrivetrain() {
		switch (teamRobot) {
			default:
				DriverStation.reportError("Unsupported robot selection found while configuring the robot-specific drivetrain", true);
				break;
			case VERSACHASSIS_TWO:
				drivetrain.setOnlyMotorGroupToInverted(MotorGroup.LEFT);
				usingV1Drivetrain = false;
				break;
			case VERSACHASSIS_ONE:
				drivetrain.setOnlyMotorGroupToInverted(MotorGroup.RIGHT);
				usingV1Drivetrain = true;
				break;
		}
	}


	// ----------------------------------------------------------
    // Joystick-mode (ex. arcade, lone tank, etc) listeners


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


	// ----------------------------------------------------------
    // Joystick-device (ex. Xbox, X3D, etc) listeners

	
	public RobotContainer listenForJoystickDevices() {
		var pilotPrimaryPorts = new int[] {driverJoystickPorts[0], spotterJoystickPorts[0]};
		var pilotJoystickTypes = new JoystickDeviceType[] {driverJoystickDeviceType, spotterJoystickDeviceType};
		Runnable[] setupPilotJoystickControls = new Runnable[] {() -> setupDriverJoystickControls(), () -> setupSpotterJoystickControls()};

		for (int pilotIndex: new int[] {0, 1}) {
			var newJoystickDeviceType = getJoystickDeviceTypeFor(pilotPrimaryPorts[pilotIndex]);
			if (newJoystickDeviceType != JoystickDeviceType.NULL && pilotJoystickTypes[pilotIndex] != newJoystickDeviceType) {
				pilotJoystickTypes[pilotIndex] = newJoystickDeviceType;

				switch (pilotJoystickTypes[pilotIndex]) {
					default:
						DriverStation.reportError("Unrecognized device type found while setting robot drive's deadband", true);
						break;
					case XboxController:
						drivetrain.setDeadband(XboxController.JOYSTICK_DEADBAND);
						break;
					case X3D:
						drivetrain.setDeadband(X3D.JOYSTICK_DEADBAND);
						break;
				}

				setupPilotJoystickControls[pilotIndex].run();
			}
		}
		return this;
	}


	// ----------------------------------------------------------
    // Joystick mode and device setups


	public static void swapJoysticksFor(Pilot pilot) {
		JoystickControls joystickControls;
		JoystickMode joystickMode;
		int[] joystickPorts;
		BiConsumer<Joystick, Joystick> setupJoystickControls;

		if (pilot == Pilot.DRIVER) {
			joystickControls = driverJoystickControls;
			joystickMode = driverJoystickMode;
			joystickPorts = driverJoystickPorts;
			setupJoystickControls = (joy1, joy2) -> Robot.robotContainer.setupDriverJoystickControls(joy1, joy2);
		} else {
			joystickControls = spotterJoystickControls;
			joystickMode = spotterJoystickMode;
			joystickPorts = spotterJoystickPorts;
			setupJoystickControls = (joy1, joy2) -> Robot.robotContainer.setupSpotterJoystickControls(joy1, joy2);
		}

		// account for the case where this is called even though initial driver joystick controls aren't set up yet
		if (joystickControls == null) {
			return;
		}

		int tempPrimaryJoystickPort = joystickPorts[0];
		joystickPorts[0] = joystickPorts[1];
		joystickPorts[1] = tempPrimaryJoystickPort;

		// TODO: P3 Figure out how to use non-static RobotContainer resources from static call

		switch (joystickMode) {
			default:
				DriverStation.reportError("Unsupported joystick mode detected while swapping the left and right joysticks for the driver", true);
				break;
			case ARCADE:
			case LONE_TANK:
				// the arcade and lone-tank modes only need one joystick
				setupJoystickControls.accept(new Joystick(joystickPorts[0]), null);
				break;
			case DUAL_TANK:
				setupJoystickControls.accept(new Joystick(joystickPorts[0]), new Joystick(joystickPorts[1]));
				break;
		}
	}

	// using a different setup function for the driver and the spotter allows special switch cases for each person, meaning that there can be a unique driver and spotter configuration for each joystick setup (ex. one Xbox controller, two X3Ds, etc)

	private RobotContainer setupDriverJoystickControls() {
		return setupDriverJoystickControls(new Joystick(driverJoystickPorts[0]), new Joystick(driverJoystickPorts[1]));
	}

	private RobotContainer setupDriverJoystickControls(Joystick primaryJoystick, Joystick secondaryJoystick) {
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
						driverJoystickControls = new DriverXboxArcadeControls(primaryJoystick, drivetrain, intake, manipulator);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DArcadeControls(primaryJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case LONE_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						driverJoystickControls = new DriverXboxLoneTankControls(primaryJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case DUAL_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for dual-tank mode", true);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DDualTankControls(primaryJoystick, secondaryJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
		}
		return this;
	}

	private RobotContainer setupSpotterJoystickControls() {
		return setupSpotterJoystickControls(new Joystick(spotterJoystickPorts[0]), new Joystick(spotterJoystickPorts[1]));
	}

	private RobotContainer setupSpotterJoystickControls(Joystick firstJoystick, Joystick secondJoystick) {
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
		return this;
	}
}