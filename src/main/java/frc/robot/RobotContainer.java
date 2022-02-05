package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.joystickcontrols.arcade.X3DArcadeControls;
import frc.robot.joystickcontrols.IO.JoystickDeviceType;
import frc.robot.joystickcontrols.IO.X3D;
import frc.robot.joystickcontrols.arcade.XboxArcadeControls;
import frc.robot.joystickcontrols.dualtank.X3DDualTankControls;
import frc.robot.joystickcontrols.lonetank.XboxLoneTankControls;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveWithJoysticks;
import frc.robot.commands.IntakeTesting;
import frc.robot.commands.ManipulatorTesting;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;
import frc.robot.displays.AutonomousDisplay;
import frc.robot.displays.JoysticksDisplay;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Sensory;


public class RobotContainer {
	// ----------------------------------------------------------
    // Robot-configuration constants


	private final boolean usingV2Drivetrain = false;

	private final boolean enableDiagnostics = true;
	
	private final boolean disableJoystickConnectionWarnings = true;


	// ----------------------------------------------------------
	// Public constants


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


	public static JoystickMode driverJoystickMode;
	public static JoystickMode spotterJoystickMode;


    // ----------------------------------------------------------
    // Private resources


	private final ShuffleboardTab HUDTab = Shuffleboard.getTab("HUD");
	private final ShuffleboardTab diagnosticsTab = enableDiagnostics ? Shuffleboard.getTab("Diagnostics"): null;

	private final JoysticksDisplay joysticksDisplay;

	private JoystickDeviceType driverJoystickDeviceType;
	private JoystickDeviceType spotterJoystickDeviceType;

	private JoystickControls driverJoystickControls;
	private JoystickControls spotterJoystickControls;
    

    // ----------------------------------------------------------
    // Subsystems
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	private final IntakeTesting m_intakeTesting;
	
	public final Manipulator manipulator = new Manipulator();
	private final ManipulatorTesting manipulatorTesting;
	
	public final Sensory sensory = new Sensory();

	public final Autonomous autonomous = new Autonomous();
	private final AutoDriveStraightForDistance autoDriveStraightForDistance;


    // ----------------------------------------------------------
    // Constructor


    public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(disableJoystickConnectionWarnings);

		joysticksDisplay = new JoysticksDisplay(HUDTab, 2, 0);
		new AutonomousDisplay(HUDTab, 0, 0);

		if (enableDiagnostics) {
			var motorTestingDisplay = new MotorTestingDisplay(diagnosticsTab, 0, 0);
			m_intakeTesting = new IntakeTesting(intake, motorTestingDisplay);
			manipulatorTesting = new ManipulatorTesting(manipulator, motorTestingDisplay);
		}

		setupDriverJoystickControls();
		setupSpotterJoystickControls();

		autoDriveStraightForDistance = new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);

		drivetrain.setDefaultCommand(new DriveWithJoysticks(drivetrain, driverJoystickControls));
    }


	// ----------------------------------------------------------
    // Command getters


	public IntakeTesting intakeTestingCommand() {
		return m_intakeTesting;
	}

	public ManipulatorTesting manipulatorTestingCommand() {
		return manipulatorTesting;
	}

	public Command defaultAutoCommand() {
		return autoDriveStraightForDistance;
	}


	// ----------------------------------------------------------
    // Methods

	
	public void configureRobotSpecificDrivetrain() {
		if (usingV2Drivetrain) {
			// true means flip the left side
			drivetrain.flipLeftOrRightMotors(true);
		} else {
			drivetrain.flipLeftOrRightMotors(false);
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
		if (DriverStation.getJoystickIsXbox(driverJoystickPorts[0])) {
			return JoystickDeviceType.XboxController;
		} else {
			switch (DriverStation.getJoystickName(driverJoystickPorts[0])) {
				default:
					DriverStation.reportError("Scanning for joystick device changes found an unrecognized device type", true);
					return null;
				case X3D.USB_DEVICE_NAME:
					return JoystickDeviceType.X3D;
			}
		}
	}
	
	public RobotContainer listenForJoystickDevices() {
		// code is repetitive for driver and spotter, but this is on purpose so that we can use the different setup functions
		// only same-type dual controls are supported, so here we are just looking at the first port for the driver's and spotter's port ranges

		var newDriverJoystickDeviceType = getJoystickDeviceTypeFor(driverJoystickPorts[0]);
		if (driverJoystickDeviceType != newDriverJoystickDeviceType) {
			driverJoystickDeviceType = newDriverJoystickDeviceType;
			setupDriverJoystickControls();
		}

		var newSpotterJoystickDeviceType = getJoystickDeviceTypeFor(spotterJoystickPorts[0]);
		if (spotterJoystickDeviceType != newSpotterJoystickDeviceType) {
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
						driverJoystickControls = new XboxArcadeControls(firstJoystick, drivetrain, intake, manipulator);
						break;
					case X3D:
						driverJoystickControls = new X3DArcadeControls(firstJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case LONE_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						driverJoystickControls = new XboxLoneTankControls(firstJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
			case DUAL_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for dual-tank mode", true);
						break;
					case X3D:
						driverJoystickControls = new X3DDualTankControls(firstJoystick, secondJoystick, drivetrain, intake, manipulator);
						break;
				}
				break;
		}
	}

	// TODO: P1 Figure out a way to switch between the driver and spotter controlling the robot

	private void setupSpotterJoystickControls() {
		var firstJoystick = new Joystick(spotterJoystickPorts[0]);
		var secondJoystick = new Joystick(spotterJoystickPorts[1]);

		switch (spotterJoystickMode) {
			default:
				DriverStation.reportError("Unsupported joystick mode detected while setting up spotter joystick controls", true);
				break;
			case ARCADE:
				break;
			case LONE_TANK:
				break;
			case DUAL_TANK:
				break;
		}
	}
}