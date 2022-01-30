package frc.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.Constants.XboxController;
import frc.robot.Constants.X3D;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.IntakeTesting;
import frc.robot.commands.ManipulatorTesting;
import frc.robot.commands.ToggleIntake;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.RunRoller;
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


	private final boolean usingV2Drivetrain = true;

	private final boolean enableDiagnostics = true;
	
	private final boolean enableJoystickConnectionWarnings = false;


	// ----------------------------------------------------------
	// Public constants


	public enum JoystickModes {
		ARCADE,
		TANK
	}


    // ----------------------------------------------------------
    // Resources


	private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = true;

    // private Joystick[] joysticks = new Joystick[5];
	private Joystick 
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		xboxController = new Joystick(Constants.XboxController.JOYSTICK_ID);

	public DriverControls driverControls;
	public SpotterControls spotterControls;
    

    // ----------------------------------------------------------
    // Subsystems
	

	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	public final Manipulator manipulator = new Manipulator();
	
	public static Sensory sensory = new Sensory();
	public final Autonomous autonomous = new Autonomous();
	
	public final HUD hud = new HUD();


    // ----------------------------------------------------------
    // Runtime resources for Robot


    public Command getDefaultAutonomousCommand() {
        return new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);
    }

    public Command getIntakeTesting() {
        return new IntakeTesting(intake, hud);
    }

    public Command getManipulatorTesting() {
        return new ManipulatorTesting(manipulator, hud);
    }

    public Command getDriveStraightWhileHeldCommand() {
        return new DriveStraightWhileHeld(drivetrain);
    }


    // ----------------------------------------------------------
    // Constructor and methods


    public RobotContainer() {
        driverControls = new DriverControls();
        driverControls.configButtonBindings();

		spotterControls = new SpotterControls();
        spotterControls.configureButtonBindings();
        
        hud.initializeHUD();
		if (enableDiagnostics) {
			hud.initializeDiagnostics();
		}

		DriverStation.silenceJoystickConnectionWarning(!enableJoystickConnectionWarnings);
    }

	// TODO: If-joystick-plugged in function here
	// maybe use boolean array to store states of the joystick ports and to detect boolean flips in the function

	public RobotContainer configureRobotSpecificDrivetrain() {
		if (usingV2Drivetrain) {
			// true means flip the left side
			drivetrain.flipLeftOrRightMotors(true);
		} else {
			drivetrain.flipLeftOrRightMotors(false);
		}
		return this;
	}

    public boolean driverIsInArcade() { return driverIsInArcadeMode; }
	
	public void toggleDriverDriveMode() {
		driverIsInArcadeMode = !driverIsInArcadeMode;
	}

    public boolean spotterIsInArcade() { return spotterIsInArcadeMode; }
	
	public void toggleSpotterDriveMode() {
		spotterIsInArcadeMode = !spotterIsInArcadeMode;
	}

    public double gamepadJoystickMagnitude(boolean isLeftJoystick) {
		if (isLeftJoystick) {
			return Math.sqrt(
				Math.pow(xboxController.getRawAxis(Constants.XboxController.LEFT_X_AXIS), 2)
				+ Math.pow(xboxController.getRawAxis(Constants.XboxController.LEFT_Y_AXIS), 2));
		} else {
			return Math.sqrt(
				Math.pow(xboxController.getRawAxis(Constants.XboxController.RIGHT_X_AXIS), 2)
				+ Math.pow(xboxController.getRawAxis(Constants.XboxController.RIGHT_Y_AXIS), 2));
		}
	}

    public void teleopDrive() {
		if (spotterIsInArcade()) {
			drivetrain.arcadeDrive(
				spotterControls.getForwardArcadeDriveAxis(),
				spotterControls.getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()) {
			drivetrain.tankDrive(
				spotterControls.getLeftTankDriveAxis(),
				spotterControls.getRightTankDriveAxis());
		}

		// TODO: Use a toggle to switch between driver or spotter driving the robot

		// if (driverIsInArcade()) {
		// 	drivetrain.arcadeDrive(
		// 		driverControls.getForwardArcadeDriveAxis(),	// forward
		// 		driverControls.getAngleArcadeDriveAxis());	// angle
		// } else {
		// 	drivetrain.tankDrive(
		// 		driverControls.getLeftTankDriveAxis(),		// left
		// 		driverControls.getRightTankDriveAxis());	// right
		// }
	}


    // ----------------------------------------------------------
    // Driver controls inner class


	// TODO: Different set of driver controls for tank mode

    public class DriverControls {
        // ----------------------------------------------------------
		// Resources

        private JoystickButton
            driveStraightButton = new JoystickButton(X3D_LEFT, X3D.GRIP_BUTTON_ID),
			
			toggleIntakeButton = new JoystickButton(X3D_LEFT, X3D.BUTTON_3_ID),
			runLaunchButton = new JoystickButton(X3D_LEFT, X3D.TRIGGER_BUTTON_ID);
    
        // ----------------------------------------------------------
		// Methods

        public DriverControls configButtonBindings() {
            driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
			
			toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));
			runLaunchButton.whenHeld(new RunLauncher(manipulator));
			
			return this;
        }

        // Tank drive axes

		@SuppressWarnings("unused")
		private double getLeftTankDriveAxis() {
			return X3D_LEFT.getRawAxis(X3D.PITCH_AXIS);
		}

		@SuppressWarnings("unused")
		private double getRightTankDriveAxis() {
			return X3D_RIGHT.getRawAxis(X3D.PITCH_AXIS);
		}

		// Arcade drive axes

		@SuppressWarnings("unused")
		private double getForwardArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(X3D.PITCH_AXIS);
		}

		@SuppressWarnings("unused")
		private double getAngleArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(X3D.ROLL_AXIS);
		}
    }


    // ----------------------------------------------------------
    // Spotter controls inner class


	// TODO: Different set of spotter controls for tank mode

    public class SpotterControls {
		// ----------------------------------------------------------
		// Resources
		
		public POVButton
			driveStraightButton = new POVButton(xboxController, XboxController.ANGLE_UP_POV);

		public JoystickButton
			runRollerButton = new JoystickButton(xboxController, XboxController.X_BUTTON_ID),
			// toggleIntakeButton = new JoystickButton(xboxController, XboxController.X_BUTTON_ID),
			runLauncherButton = new JoystickButton(xboxController, XboxController.Y_BUTTON_ID);

		// ----------------------------------------------------------
		// Methods

		public SpotterControls configureButtonBindings() {
			driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
			
			runRollerButton.whenHeld(new RunRoller(intake));
			// toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));
			runLauncherButton.whenHeld(new RunLauncher(manipulator));
			return this;
		}

		// Tank drive axes
		public double getLeftTankDriveAxis() {
			return xboxController.getRawAxis(XboxController.LEFT_Y_AXIS);
		}
		public double getRightTankDriveAxis() {
			return xboxController.getRawAxis(XboxController.RIGHT_Y_AXIS);
		}

		// Arcade drive axes
		public double getForwardArcadeDriveAxis() {
			return xboxController.getRawAxis(XboxController.LEFT_Y_AXIS);
		}
		public double getAngleArcadeDriveAxis() {
			return xboxController.getRawAxis(XboxController.LEFT_X_AXIS);
		}
	}
}