package frc.robot;


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
import frc.robot.joystickcontrols.singlejoystickcontrols.arcade.SpotterXboxArcadeControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.arcade.SpotterXboxLoneTankControls;
import frc.robot.joystickcontrols.singlejoystickcontrols.lonetank.DriverXboxLoneTankControls;
import frc.robot.commands.autonomous.Wait_LH_LT;
import frc.robot.commands.autonomous.Wait_LH_PC_LH;
import frc.robot.commands.autonomous.Wait_LH_RC_LT;
import frc.robot.commands.autonomous.LH_Wait_LT;
import frc.robot.commands.autonomous.WaitAndLeaveTarmac;
import frc.robot.commands.drivetrain.DriveWithJoysticks;
import frc.robot.commands.intake.RunFeederAndIndexerWithTrigger;
import frc.robot.displays.DisplaysGrid;
import frc.robot.displays.autonomousdisplays.PremadeAutoRoutineDisplay;
import frc.robot.displays.drivingdisplays.OpenLoopDrivetrainDisplay;
import frc.robot.displays.drivingdisplays.PolynomialDriveRampsDisplay;
import frc.robot.displays.drivingdisplays.SlewRateLimiterTuningDisplay;
import frc.robot.displays.generaldisplays.JoysticksDisplay;
import frc.robot.displays.generaldisplays.RobotChooserDisplay;
import frc.robot.displays.motortuningdisplays.MotorTestingDisplay;
import frc.robot.displays.visiondisplays.CamerasDisplay;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class RobotContainer {
	// ----------------------------------------------------------
    // Singleton instance


	public static RobotContainer instance;


	// ----------------------------------------------------------
    // Robot-configuration constants


	public static final TeamRobot defaultRobot = TeamRobot.VERSACHASSIS_TWO;

	public static final boolean
		// initial value is the start-up configuration
		usingKidsSafetyMode = false,
		
		enableDeveloperMode = true,
		
		disableJoystickConnectionWarnings = true;


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


	private static final int[] driverJoystickPorts = {0, 1};
	private static final int[] spotterJoystickPorts = {2, 3};


	// ----------------------------------------------------------
    // Publicly static resources


	// joystick control resources are publicly static because 
	public static JoystickControls driverJoystickControls;
	public static final JoystickMode defaultDriverJoystickMode = JoystickMode.ARCADE;
	
	public static JoystickControls spotterJoystickControls;
	public static final JoystickMode defaultSpotterJoystickMode = JoystickMode.ARCADE;


	// ----------------------------------------------------------
    // Public resources

	
	public static TeamRobot teamRobot = defaultRobot;

	public static JoystickMode driverJoystickMode = defaultDriverJoystickMode;
	public static JoystickMode spotterJoystickMode = defaultSpotterJoystickMode;
	
	
    // ----------------------------------------------------------
    // Private resources
	

	private static AutonomousRoutine autoRoutine;
	private static Command autoCommand;

	private DisplaysGrid
		generalDisplaysGrid = new DisplaysGrid(),
		drivingDisplaysGrid = new DisplaysGrid(),
		autonomousDisplaysGrid = new DisplaysGrid(),
		visionDisplaysGrid = new DisplaysGrid(),
		motorTuningDisplaysGrid = new DisplaysGrid();

	private final RobotChooserDisplay robotChooserDisplay;
	private final JoysticksDisplay joysticksDisplay;
	private final PremadeAutoRoutineDisplay autonomousDisplay;

	// has default USB values
	private JoystickDeviceType
		driverJoystickDeviceType = JoystickDeviceType.XboxController,
		spotterJoystickDeviceType = JoystickDeviceType.XboxController;
    

    // ----------------------------------------------------------
    // Subsystems and commands
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	
	public final Manipulator manipulator = new Manipulator();

	public final Climber climber = new Climber();
	
	public final Autonomous autonomous = new Autonomous();

	public final Vision vision = new Vision();

	public final Lights lights = new Lights();


    // ----------------------------------------------------------
    // Constructor, display helpers, and constants-dependencies configuration
	

    public RobotContainer() {
		DriverStation.silenceJoystickConnectionWarning(disableJoystickConnectionWarnings);
		
		// add new display grids here
		DisplaysGrid[] displaysGrids = {
			generalDisplaysGrid,
			drivingDisplaysGrid,
			autonomousDisplaysGrid,
			visionDisplaysGrid,
			motorTuningDisplaysGrid
		};

		generalDisplaysGrid
			.makeOriginWith(robotChooserDisplay = new RobotChooserDisplay(2, 1))
			.reserveNextColumnAtRow(0, joysticksDisplay = new JoysticksDisplay(3, 2));
			
		drivingDisplaysGrid
			.makeOriginWith(new OpenLoopDrivetrainDisplay(drivetrain, 3, 1))
			.reserveNextRowAtColumn(0, new PolynomialDriveRampsDisplay(drivetrain, 3, 2))
			.reserveNextRowAtColumn(0, new SlewRateLimiterTuningDisplay(drivetrain, 3, 4));

		autonomousDisplaysGrid
			.makeOriginWith(autonomousDisplay = new PremadeAutoRoutineDisplay(autonomous, 2, 3));
			// TODO: !P1! Add the auto-routine maker display here
		
		visionDisplaysGrid
			.makeOriginWith(new CamerasDisplay(vision, 6, 2));
			// TODO: !P1! Add the Jevois-parameters adjuster display here

		motorTuningDisplaysGrid
			.makeOriginWith(new MotorTestingDisplay(intake, manipulator, 8, 3));

		for (var grid: displaysGrids) {
			grid.initialize().addEntryListeners();
		}

		// hudDisplaysGrid
		// 	.reserveNextColumnAtRow(0, new KidsSafetyDisplay(drivetrain, 2, 2))
		// if (RobotContainer.enableCameras) {
		// 	hudDisplaysGrid.reserveNextRowAtColumn(1, new CamerasDisplay(6, 2))
		
		// if (enableDiagnostics) {
		// 	diagnosticDisplaysGrid
		// 		.reserveNextColumnAtRow(0, new SlewRateLimiterTuningDisplay(drivetrain, 3, 4))
		// 		.reserveNextRowAtColumn(0, new DrivetrainOpenLoopRampTimeDisplay(drivetrain, 3, 1))

		setupDriverJoystickControls();
		setupSpotterJoystickControls();

		drivetrain.setDefaultCommand(new DriveWithJoysticks(drivetrain));
		intake.setDefaultCommand(new RunFeederAndIndexerWithTrigger(intake, manipulator));

		assert instance == null;
		instance = this;
    }

	public static void configureConstantsDependencies() {
		Drivetrain.configureDriveKinematics();
	}

	public RobotContainer configureNonStaticConstantsDependencies() {
		drivetrain.configureMotorPIDs();

		intake.configurePIDs();

		manipulator.configurePIDs();
		return this;
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

		// SmartDashboard.putNumber("Raw Left Trigger", m_printOutjoystick.getRawAxis(2));
		// SmartDashboard.putNumber("Raw Right Trigger", m_printOutjoystick.getRawAxis(3));

		SmartDashboard.putNumber("WPI Mag", m_printOutjoystick.getMagnitude());
		return this;
	}


	// ----------------------------------------------------------
    // Robot-selection listeners


	public static void configureConstants() {
		switch (RobotContainer.teamRobot) {
			default:
				assert 0 == 1;
				break;
			case VERSACHASSIS_ONE:
				Constants.useV1Constants();
				break;
			case VERSACHASSIS_TWO:
				Constants.useV2Constants();
				break;
		}

		configureConstantsDependencies();
		if (instance != null) {
			instance.configureNonStaticConstantsDependencies();
		}
	}

	public RobotContainer listenForRobotSelection() {
		var newRobotSelection = robotChooserDisplay.teamRobotChooser.getSelected();
		if (teamRobot != newRobotSelection) {
			teamRobot = newRobotSelection;
			configureConstants();
			drivetrain.configureDrivetrain(teamRobot);
		}
		return this;
	}


	// ----------------------------------------------------------
	// Autonomous-routine listeners


	public Command getAutoCommand() {
		return autoCommand;
	}

	public RobotContainer setAutoCommand(AutonomousRoutine autoRoutine) {
		switch (autoRoutine) {
			default:
				DriverStation.reportError("Unsupported auto routine detected in listenForAutoRoutine", true);
				break;
			case WAIT_AND_LEAVE_TARMAC:
				autoCommand = new WaitAndLeaveTarmac(drivetrain);
				break;
			case WAIT_SCORE_LH_AND_LEAVE_TARMAC:
				autoCommand = new Wait_LH_LT(drivetrain, manipulator);
				break;
			case SCORE_LH_AND_WAIT_AND_LEAVE_TARMAC:
				autoCommand = new LH_Wait_LT(drivetrain, manipulator);
				break;
			case WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_SCORE_LH:
				autoCommand = new Wait_LH_PC_LH(drivetrain, intake, manipulator);
				break;
			case WAIT_AND_SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC:
				autoCommand = new Wait_LH_RC_LT(drivetrain, intake, manipulator, vision);
				break;
		}
		return this;
	}

	public RobotContainer remakeAutoCommand() {
		setAutoCommand(autoRoutine);
		return this;
	}

	public RobotContainer listenForPremadeAutoRoutine() {
		if (!autonomous.usingPremadeRoutine()) {
			return this;
		}
		var newAutoRoutineSelection = autonomousDisplay.autoRoutineChooser.getSelected();
		if (autoRoutine != newAutoRoutineSelection) {
			autoRoutine = newAutoRoutineSelection;
			setAutoCommand(autoRoutine);
		}
		return this;
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
					DriverStation.reportWarning("Scanning for joystick device changes found an unrecognized device type", true);
					return JoystickDeviceType.NULL;
				case X3D.USB_DEVICE_NAME:
					return JoystickDeviceType.X3D;
			}
		}
	}


	// ----------------------------------------------------------
    // Joystick-device (ex. Xbox, X3D, etc) listeners

	
	public RobotContainer listenForJoystickDevices() {
		int[] pilotPrimaryPorts = {driverJoystickPorts[0], spotterJoystickPorts[0]};
		JoystickDeviceType[] pilotJoystickTypes = {driverJoystickDeviceType, spotterJoystickDeviceType};
		Runnable[] setupPilotJoystickControls = {() -> setupDriverJoystickControls(), () -> setupSpotterJoystickControls()};

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
		return;
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
						driverJoystickControls = new DriverXboxArcadeControls(primaryJoystick, drivetrain, intake, manipulator, climber);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DArcadeControls(primaryJoystick, drivetrain, intake, manipulator, climber);
						break;
				}
				break;
			case LONE_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						driverJoystickControls = new DriverXboxLoneTankControls(primaryJoystick, drivetrain, intake, manipulator, climber);
						break;
				}
				break;
			case DUAL_TANK:
				switch (driverJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up driver joystick controls for dual-tank mode", true);
						break;
					case X3D:
						driverJoystickControls = new DriverX3DDualTankControls(primaryJoystick, secondaryJoystick, drivetrain, intake, manipulator, climber);
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
						spotterJoystickControls = new SpotterXboxArcadeControls(firstJoystick, drivetrain, intake, manipulator, climber);
						break;
				}
				break;
			case LONE_TANK:
				switch (spotterJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up spotter joystick controls for lone-tank mode", true);
						break;
					case XboxController:
						spotterJoystickControls = new SpotterXboxLoneTankControls(firstJoystick, drivetrain, intake, manipulator, climber);
						break;
				}
				break;
			case DUAL_TANK:
				switch (spotterJoystickDeviceType) {
					default:
						DriverStation.reportError("Unsupported joystick device type while setting up spotter joystick controls for dual-tank mode", true);
						break;
				}
				break;
		}
		return this;
	}
}