package frc.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.Constants.AxisDominanceThresholds;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.IntakeDemo;
import frc.robot.commands.ManipulatorDemo;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Sensory;
import frc.robot.subsystems.Telemetry;


public class RobotContainer {
    // ----------------------------------------------------------
    // Resources

    // TODO: PERSISTENT CONFIG - enable robot's tuning tools or don't
	public boolean enableTuningTools = true;

    private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

    private final Joystick
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		GAMEPAD = new Joystick(Constants.Gamepad.JOYSTICK_ID);

	public DriverControls driverControls;
	public SpotterControls spotterControls;
    
    // ----------------------------------------------------------
    // Subsystems
	
	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	public final Manipulator manipulator = new Manipulator();
	
	public static Sensory sensory = new Sensory();
	public final Autonomous autonomous = new Autonomous();
	
	public final Telemetry telemetry = new Telemetry();

    // ----------------------------------------------------------
    // Runtime resources for Robot

    public boolean getTuningModeToggleSwitch() {
        return telemetry.tuningModeToggleSwitch.getBoolean(false);
    }

    public Command getDefaultAutonomousCommand() {
        return new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);
    }

    public Command getIntakeDemo() {
        return new IntakeDemo(intake, telemetry);
    }

    public Command getManipulatorDemo() {
        return new ManipulatorDemo(manipulator, telemetry);
    }

    public Command getDriveStraightWhileHeldCommand() {
        return new DriveStraightWhileHeld(drivetrain);
    }

    // ----------------------------------------------------------
    // Constructor and actions

    public RobotContainer() {
        driverControls = new DriverControls();
        driverControls.configButtonBindings();

		spotterControls = new SpotterControls();
        spotterControls.configureButtonBindings();
        
        telemetry.initializeTelemetry();
		if (enableTuningTools) {
			telemetry.initializeTuningTools();
		}
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
				Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.LEFT_X_AXIS), 2)
				+ Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.LEFT_Y_AXIS), 2));
		} else {
			return Math.sqrt(
				Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.RIGHT_X_AXIS), 2)
				+ Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.RIGHT_Y_AXIS), 2));
		}
	}

    public void teleopDrive() {
		if (spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.ARCADE)) {
			drivetrain.arcadeDrive(
				spotterControls.getForwardArcadeDriveAxis(),
				spotterControls.getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.TANK
		|| gamepadJoystickMagnitude(false) > AxisDominanceThresholds.TANK)) {
			drivetrain.tankDrive(
				spotterControls.getLeftTankDriveAxis(),
				spotterControls.getRightTankDriveAxis());
		}

		if (driverIsInArcade()) {
			drivetrain.arcadeDrive(
				driverControls.getForwardArcadeDriveAxis(),	// forward
				driverControls.getAngleArcadeDriveAxis());	// angle
		} else {
			drivetrain.tankDrive(
				driverControls.getLeftTankDriveAxis(),		// left
				driverControls.getRightTankDriveAxis());	// right
		}
	}

    // ----------------------------------------------------------
    // Driver controls inner class

    public class DriverControls {
        // ----------------------------------------------------------
		// Resources

        public JoystickButton
            driveStraightButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.DRIVE_STRAIGHT_BUTTON_ID);
    
        // ----------------------------------------------------------
		// Actions

        // TODO: Move driver button bindings to RobotContainer
        public void configButtonBindings() {
            driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
        }

        // Tank drive axes
		public double getLeftTankDriveAxis() {
			return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
		}
		public double getRightTankDriveAxis() {
			return X3D_RIGHT.getRawAxis(Constants.DriverControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
		}

		// Arcade drive axes
		public double getForwardArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
		}

		public double getAngleArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
    }

    // ----------------------------------------------------------
    // Spotter controls inner class

    public class SpotterControls {
		// ----------------------------------------------------------
		// Resources
		
		public POVButton
			driveStraightButton = new POVButton(GAMEPAD, Constants.SpotterControlIDs.DRIVE_STRAIGHT_POV_ANGLE);
		
		// public JoystickButton
		// 	intakeButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.INTAKE_BUTTON_ID),
		// 	feederButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.FEEDER_BUTTON_ID),
		// 	extendClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.EXTEND_CLIMBER_BUTTON_ID),
		// 	lowerClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.LOWER_CLIMBER_BUTTON_ID);

		// ----------------------------------------------------------
		// Actions

		// TODO: Move spotter button bindings to RobotContainer
		public void configureButtonBindings() {
			spotterControls.driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
		}

		// Tank drive axes
		public double getLeftTankDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
		}
		public double getRightTankDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
		}

		// Arcade drive axes
		public double getForwardArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
		}
		public double getAngleArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
	}
}
