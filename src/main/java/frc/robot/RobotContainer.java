package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.joystickcontrols.X3DArcadeControls;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveStraightWhileHeld;
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


    // ----------------------------------------------------------
    // Resources


	private final ShuffleboardTab HUDTab = Shuffleboard.getTab("HUD");
	private final ShuffleboardTab diagnosticsTab = enableDiagnostics ? Shuffleboard.getTab("Diagnostics"): null;

	private JoystickControls joystickControls;
    

    // ----------------------------------------------------------
    // Subsystems
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	private final IntakeTesting intakeTesting;
	
	public final Manipulator manipulator = new Manipulator();
	private final ManipulatorTesting manipulatorTesting;
	
	public final Sensory sensory = new Sensory();

	public final Autonomous autonomous = new Autonomous();
	private final AutoDriveStraightForDistance autoDriveStraightForDistance;


    // ----------------------------------------------------------
    // Constructor


    public RobotContainer() {
		new JoysticksDisplay(HUDTab, 2, 0);
		new AutonomousDisplay(HUDTab, 0, 0);

		if (enableDiagnostics) {
			var motorTestingDisplay = new MotorTestingDisplay(diagnosticsTab, 0, 0);
			intakeTesting = new IntakeTesting(intake, motorTestingDisplay);
			manipulatorTesting = new ManipulatorTesting(manipulator, motorTestingDisplay);
		}

		joystickControls = new X3DArcadeControls(new Joystick(0), drivetrain, intake, manipulator);

		autoDriveStraightForDistance = new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);

		DriverStation.silenceJoystickConnectionWarning(disableJoystickConnectionWarnings);
    }


	// ----------------------------------------------------------
    // Command getters


	public IntakeTesting intakeTestingCommand() {
		return intakeTesting;
	}

	public ManipulatorTesting manipulatorTestingCommand() {
		return manipulatorTesting;
	}

	public Command defaultAutoCommand() {
		return autoDriveStraightForDistance;
	}


	// ----------------------------------------------------------
    // Methods

	
	public RobotContainer configureRobotSpecificDrivetrain() {
		if (usingV2Drivetrain) {
			// true means flip the left side
			drivetrain.flipLeftOrRightMotors(true);
		} else {
			drivetrain.flipLeftOrRightMotors(false);
		}
		return this;
	}


	// ----------------------------------------------------------
    // Scheduler methods


	// TODO: Figure out a way to switch between the driver and spotter controlling the robot

    public RobotContainer teleopPeriodic() {
		// TODO: Create and use spotter controls

		// driverControls
		// 	.listenForJoystickMode()
		// 	.periodicTeleopDrive();

		// if (driverControls.deviceType == DeviceType.XboxController) {
		// 	driverControls.periodicTeleopIntakeFeeder();
		// }

		return this;
	}
}