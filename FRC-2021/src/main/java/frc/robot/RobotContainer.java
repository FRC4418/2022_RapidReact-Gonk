/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ManipulatorSubsystem;
import frc.robot.subsystems.TeleopInput;
// import frc.robot.subsystems.SensorsSubsystem;


public class RobotContainer {
	public static DriveSubsystem driveSubsystem = new DriveSubsystem();
	public static ManipulatorSubsystem manipulatorsubsystem = new ManipulatorSubsystem();
	public static ClimberSubsystem climbSubsystem = new ClimberSubsystem();
	public static TeleopInput teleopSensitivitySubsystem = new TeleopInput();
	// public static SensorsSubsystem sensorsSubsystem = new SensorsSubsystem();

	// Create joysticks
	private static final Joystick
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		// X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		GAMEPAD = new Joystick(Constants.Gamepad.JOYSTICK_ID);

	public static double gamepadJoystickMagnitude(boolean isLeftJoystick) {
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

	public static class DriverControls {
		public static JoystickButton
			toggleArcadeDriveButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.TOGGLE_ARCADE_DRIVE_BUTTON_ID),
			driveStraightButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.DRIVE_STRAIGHT_BUTTON_ID),
		
			backwardIntakeButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.BACKWARD_INTAKE_BUTTON_ID),
			forwardIntakeButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.FORWARD_INTAKE_BUTTON_ID),
			feederButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.FEEDER_BUTTON_ID),
			shooterButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.SHOOT_BUTTON_ID),

			panelSpinButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.PANEL_SPIN_BUTTON_ID),
			reversePanelSpinButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.PANEL_SPIN_REVERSE_BUTTON_ID),

			extendClimberButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.EXTEND_CLIMBER_BUTTON_ID),
			lowerClimberButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.LOWER_CLIMBER_BUTTON_ID),

			spoolWinchButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.SPOOL_WINCH_BUTTON_ID),
			unspoolWinchButton = new JoystickButton(X3D_LEFT, Constants.DriverControlIDs.UNSPOOL_WINCH_BUTTON_ID);

		private static void configureButtonBindings() {
			toggleArcadeDriveButton.whenPressed(new ToggleDriverArcadeDriveCommand());
			driveStraightButton.whileHeld(new DriveStraightCommand());
			
			backwardIntakeButton.whileHeld(new IntakeCommand(true));
			forwardIntakeButton.whileHeld(new IntakeCommand(false));
			feederButton.whileHeld(new FeederCommand());
			shooterButton.whileHeld(new ShooterCommand());
			
			
			// true is extending, false is lowering
			extendClimberButton.whileHeld(new MoveClimberCommand(true));
			lowerClimberButton.whileHeld(new MoveClimberCommand(false));
	
			
		}
		
		// Tank drive axes
		// public static double getLeftTankDriveAxis() {
		// 	return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
		// }
		// public static double getRightTankDriveAxis() {
		// 	return X3D_RIGHT.getRawAxis(Constants.DriverControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
		// }

		// Arcade drive axes
		public static double getForwardArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
		}

		public static double getAngleArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(Constants.DriverControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
	}
	
	public static class SpotterControls {
		public static POVButton
			driveStraightButton = new POVButton(GAMEPAD, Constants.SpotterControlIDs.DRIVE_STRAIGHT_POV_ANGLE);

		public static JoystickButton
			toggleArcadeDriveButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.TOGGLE_ARCADE_DRIVE_BUTTON_ID),
			toggleSensitivityButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.TOGGLE_SENSITIVITY_BUTTON_ID),
			intakeButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.INTAKE_BUTTON_ID),
			feederButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.FEEDER_BUTTON_ID),
			extendClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.EXTEND_CLIMBER_BUTTON_ID),
			lowerClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.LOWER_CLIMBER_BUTTON_ID);

		private static void configureButtonBindings() {
			toggleArcadeDriveButton.whenPressed(new ToggleSpotterArcadeDriveCommand());
			driveStraightButton.whileHeld(new DriveStraightCommand());
			toggleSensitivityButton.whenPressed(new ToggleTeleopSensitivityMode());
			
			intakeButton.whileHeld(new IntakeCommand(false));
			feederButton.whileHeld(new ShooterCommand());
			
			
			// true is extending, false is lowering
			extendClimberButton.whileHeld(new MoveClimberCommand(true));
			lowerClimberButton.whileHeld(new MoveClimberCommand(false));
	

		}

		// Tank drive axes
		public static double getLeftTankDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.LEFT_TANK_DRIVE_AXIS_ID);
		}
		public static double getRightTankDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.RIGHT_TANK_DRIVE_AXIS_ID);
		}

		// Arcade drive axes
		public static double getForwardArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_FORWARD_AXIS_ID);
		}
		public static double getAngleArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(Constants.SpotterControlIDs.ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
	}
		

	/**
	 * The container for the robot.  Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		// Configure the button bindings
		DriverControls.configureButtonBindings();
		SpotterControls.configureButtonBindings();
	}
}
