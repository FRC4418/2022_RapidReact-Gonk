package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Constants.AxisDominanceThresholds;
import frc.robot.commands.DriveStraightWhileHeld;


public class TeleopInput extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private Drivetrain dt;

	private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

	private final Joystick
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		GAMEPAD = new Joystick(Constants.Gamepad.JOYSTICK_ID);

	public POVButton
		SPOTTER_driveStraightButton = new POVButton(GAMEPAD, Constants.SpotterControlIDs.DRIVE_STRAIGHT_POV_ANGLE);

	public JoystickButton
		SPOTTER_intakeButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.INTAKE_BUTTON_ID),
		SPOTTER_feederButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.FEEDER_BUTTON_ID),
		SPOTTER_extendClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.EXTEND_CLIMBER_BUTTON_ID),
		SPOTTER_lowerClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.LOWER_CLIMBER_BUTTON_ID);


	// ----------------------------------------------------------
	// Constructor and actions
	

	public TeleopInput() {
		dt = Robot.drivetrain;
	}

	// TODO: Figure out how to split funcs and vars for driver and spotter

	public JoystickButton
		DRIVER_driveStraightButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.DRIVE_STRAIGHT_BUTTON_ID);

	public void configureButtonBindings() {
		SPOTTER_driveStraightButton.whenHeld(new DriveStraightWhileHeld());
		DRIVER_driveStraightButton.whenHeld(new DriveStraightWhileHeld());
	}

	private boolean driverIsInArcade() { return driverIsInArcadeMode; }
	
	public TeleopInput toggleDriverDriveMode() {
		driverIsInArcadeMode = !driverIsInArcadeMode;
		return this;
	}
	
	private boolean spotterIsInArcade() { return spotterIsInArcadeMode; }
	
	public TeleopInput toggleSpotterDriveMode() {
		spotterIsInArcadeMode = !spotterIsInArcadeMode;
		return this;
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


	// ----------------------------------------------------------
	// Driver controls inner class

	
	// Tank drive axes
	public double DRIVER_getLeftTankDriveAxis() {
		return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
	}
	public double DRIVER_getRightTankDriveAxis() {
		return X3D_RIGHT.getRawAxis(Constants.DriverControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
	}

	// Arcade drive axes
	public double DRIVER_getForwardArcadeDriveAxis() {
		return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
	}

	public double DRIVER_getAngleArcadeDriveAxis() {
		return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
	}
	

	// ----------------------------------------------------------
	// Spotter controls inner class

	// Tank drive axes
	public double SPOTTER_getLeftTankDriveAxis() {
		return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
	}
	public double SPOTTER_getRightTankDriveAxis() {
		return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
	}

	// Arcade drive axes
	public double SPOTTER_getForwardArcadeDriveAxis() {
		return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
	}
	public double SPOTTER_getAngleArcadeDriveAxis() {
		return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
	}

	public void teleopDrive() {
		if (spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.ARCADE)) {
			dt.arcadeDrive(
				SPOTTER_getForwardArcadeDriveAxis(),
				SPOTTER_getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.TANK
		|| gamepadJoystickMagnitude(false) > AxisDominanceThresholds.TANK)) {
			dt.tankDrive(
				SPOTTER_getLeftTankDriveAxis(),
				SPOTTER_getRightTankDriveAxis());
		}

		if (driverIsInArcade()) {
			dt.arcadeDrive(
				DRIVER_getForwardArcadeDriveAxis(),	// forward
				DRIVER_getAngleArcadeDriveAxis());	// angle
		} else {
			dt.tankDrive(
				DRIVER_getLeftTankDriveAxis(),		// left
				DRIVER_getRightTankDriveAxis());	// right
		}
	}


	// ----------------------------------------------------------
	// Scheduler actions


	@Override
	public void periodic() {

	}
}
