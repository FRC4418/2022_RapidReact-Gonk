package frc.robot;


public final class Constants {
	public static class X3D {
		public static final int
			// ---Joystick IDs---
			LEFT_JOYSTICK_ID = 0,
			RIGHT_JOYSTICK_ID = 1,

			/// ---Axes---
			ROLL_AXIS = 0, 
			PITCH_AXIS = 1,
			YAW_AXIS = 2,
			// OTHER_AXIS = 3,

			// ---Button IDs---
			TRIGGER_BUTTON_ID = 1,
			GRIP_BUTTON_ID = 2,
			BUTTON_3_ID = 3,
			BUTTON_4_ID = 4,
			BUTTON_5_ID = 5,
			BUTTON_6_ID = 6,
			BUTTON_7_ID = 7,
			BUTTON_8_ID = 8,
			BUTTON_9_ID = 9,
			BUTTON_10_ID = 10,
			BUTTON_11_ID = 11,
			BUTTON_12_ID = 12;
	}

	public static class Gamepad {
		public static final int
			JOYSTICK_ID = 1,

			// ---Axes---
			LEFT_X_AXIS = 0,
			LEFT_Y_AXIS = 1,
			RIGHT_X_AXIS = 4,
			RIGHT_Y_AXIS = 5,
			// LEFT_TRIGGER_AXIS = 2,
			// RIGHT_TRIGGER_AXIS = 3,

			// ---Button IDs---
			A_BUTTON_ID = 1,
			B_BUTTON_ID = 2,
			X_BUTTON_ID = 3,
			Y_BUTTON_ID = 4,
			LEFT_BUMPER_BUTTON_ID = 5,
			RIGHT_BUMPER_BUTTON_ID = 6,
			BACK_BUTTON_ID = 7,
			START_BUTTON_ID = 8,
			// LOGITECH_BUTTON_ID = 9,
			LEFT_JOYSTICK_BUTTON_ID = 10,
			RIGHT_JOYSTICK_BUTTON_ID = 11,

			// ---POV Indices---
			ANGLE_UP_POV = 0;
	}

	public static class AxisDominanceThresholds {
		public static final double
			// Drive axis dominance thresholds
			ARCADE = 0.35,
			TANK = 0.21;
	}

	public static class DriverControlIDs {
		public static final int
			// Tank drive axis
			LEFT_TANK_DRIVE_AXIS_ID = X3D.PITCH_AXIS,
			RIGHT_TANK_DRIVE_AXIS_ID = X3D.PITCH_AXIS,

			// Arcade drive axis
			ARCADE_DRIVE_FORWARD_AXIS_ID = X3D.PITCH_AXIS,
			ARCADE_DRIVE_ANGLE_AXIS_ID = X3D.YAW_AXIS,

			TOGGLE_ARCADE_DRIVE_BUTTON_ID = X3D.BUTTON_5_ID,	// does not toggle drive mode for spotter
			DRIVE_STRAIGHT_BUTTON_ID = X3D.GRIP_BUTTON_ID,
			TOGGLE_INTAKE_BUTTON_ID = X3D.BUTTON_3_ID;
	}

	public static class SpotterControlIDs {
		public static final int
			// Tank drive axis
			LEFT_TANK_DRIVE_AXIS_ID = Gamepad.LEFT_Y_AXIS,
			RIGHT_TANK_DRIVE_AXIS_ID = Gamepad.RIGHT_Y_AXIS,

			// Arcade drive axis
			ARCADE_DRIVE_FORWARD_AXIS_ID = Gamepad.LEFT_Y_AXIS,
			ARCADE_DRIVE_ANGLE_AXIS_ID = Gamepad.LEFT_X_AXIS,
			
			// Drive mode function buttons
			DRIVE_STRAIGHT_POV_ANGLE = Gamepad.ANGLE_UP_POV,
			TOGGLE_ARCADE_DRIVE_BUTTON_ID = Gamepad.LEFT_JOYSTICK_BUTTON_ID,	// does not toggle drive mode for driver
			TOGGLE_SENSITIVITY_BUTTON_ID = Gamepad.RIGHT_JOYSTICK_BUTTON_ID,	// does not toggle motor sensitivities for driver

			// Manipulator buttons
			INTAKE_BUTTON_ID = Gamepad.X_BUTTON_ID,
			FEEDER_BUTTON_ID = Gamepad.B_BUTTON_ID,

			// Climber buttons
			EXTEND_CLIMBER_BUTTON_ID = Gamepad.Y_BUTTON_ID,
			LOWER_CLIMBER_BUTTON_ID = Gamepad.A_BUTTON_ID;

	}
}
