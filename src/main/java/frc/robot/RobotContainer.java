package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.IO.DriverControls;
import frc.robot.IO.SpotterControls;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.IntakeTesting;
import frc.robot.commands.ManipulatorTesting;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Sensory;
import frc.robot.subsystems.HUD;


public class RobotContainer {
	// ----------------------------------------------------------
    // Robot-configuration constants


	private final boolean usingV2Drivetrain = false;

	private final boolean enableDiagnostics = true;
	
	private final boolean enableJoystickConnectionWarnings = false;


	// ----------------------------------------------------------
	// Public constants


	public enum JoystickModes {
		ARCADE,
		LONE_TANK,	// tank drive that uses just one joystick (ex. Xbox with two thumbsticks)
		DUAL_TANK	// tank drive that uses two joysticsks (ex. two X3Ds, respectively for the left and right motors)
	}


    // ----------------------------------------------------------
    // Resources


    private boolean[] connectedJoystickPorts = new boolean[6];

	private DriverControls driverControls;
	private SpotterControls spotterControls;
    

    // ----------------------------------------------------------
    // Subsystems
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	public final Manipulator manipulator = new Manipulator();
	
	public final Sensory sensory = new Sensory();
	public final Autonomous autonomous = new Autonomous();
	
	public final HUD hud = new HUD();


    // ----------------------------------------------------------
    // Runtime resources for Robot


    public Command getDefaultAutonomousCommand() {
        return new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);
    }

	public Command getDriveStraightWhileHeldCommand() {
		return new DriveStraightWhileHeld(drivetrain);
	}

    public Command getIntakeTesting() {
		if (enableDiagnostics) {
			return new IntakeTesting(intake, hud);
		}
		return null;
    }

    public Command getManipulatorTesting() {
		if (enableDiagnostics) {
			return new ManipulatorTesting(manipulator, hud);
		}
		return null;
    }


    // ----------------------------------------------------------
    // Constructor


    public RobotContainer() {
        driverControls = new DriverControls(this);
		spotterControls = new SpotterControls();
        
        hud.initializeHUD();
		if (enableDiagnostics) {
			hud.initializeDiagnostics();
		}

		DriverStation.silenceJoystickConnectionWarning(!enableJoystickConnectionWarnings);
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

	// TODO: Figure out a way to switch between the driver and spotter controlling the robot

    public RobotContainer periodicTeleop() {
		driverControls
			.periodicTeleopDrive()
			.periodicTeleopIntakeRoller();
		return this;
	}
}