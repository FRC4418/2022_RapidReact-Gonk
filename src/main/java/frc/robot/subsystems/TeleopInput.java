package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.commands.DriveStraightWhileHeld;


public class TeleopInput extends SubsystemBase {
	public DriverControls driverControls;
	public SpotterControls spotterControls;
	
	public TeleopInput() {
		this.driverControls = new DriverControls();
		this.spotterControls = new SpotterControls();
	}

	// Create joysticks
	private final Joystick
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		GAMEPAD = new Joystick(Constants.Gamepad.JOYSTICK_ID);

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

	// Climber axes or up/down buttons (buttons take priority over joystick)
	// public static double getClimberAxis() {
	// 	return GAMEPAD.getRawAxis(Constants.DriverControlIDs.CLIMBER_JOYSTICK_AXIS_ID);
	// }

	public class DriverControls {
		public JoystickButton
			driveStraightButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.DRIVE_STRAIGHT_BUTTON_ID);
			
		public void configureButtonBindings() {
			driveStraightButton.whileHeld(new DriveStraightWhileHeld());
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
	
	public class SpotterControls {
		public POVButton
			driveStraightButton = new POVButton(GAMEPAD, Constants.SpotterControlIDs.DRIVE_STRAIGHT_POV_ANGLE);

		public JoystickButton
			intakeButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.INTAKE_BUTTON_ID),
			feederButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.FEEDER_BUTTON_ID),
			extendClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.EXTEND_CLIMBER_BUTTON_ID),
			lowerClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.LOWER_CLIMBER_BUTTON_ID);

		public void configureButtonBindings() {
			driveStraightButton.whileHeld(new DriveStraightWhileHeld());
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

	@Override
	public void periodic() {
		
	}
}
