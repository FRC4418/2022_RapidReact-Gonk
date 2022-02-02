package frc.robot.IO;


public final class Constants {
	public enum DeviceType {
		XboxController,
		X3D
	}

	public static class X3D {
		public static final String
			USB_DEVICE_NAME					= "Logitech Extreme 3D";

		public static final int
			// Joysticks
			LEFT_JOYSTICK_ID 	= 0,
			RIGHT_JOYSTICK_ID 	= 1,

			// Axes
			ROLL_AXIS 			= 0, 
			PITCH_AXIS 			= 1,
			YAW_AXIS 			= 2,
			SLIDER_AXIS			= 3,

			// Buttons
			TRIGGER_BUTTON_ID 	= 1,
			GRIP_BUTTON_ID 		= 2,
			BUTTON_3_ID 		= 3,
			BUTTON_4_ID 		= 4,
			BUTTON_5_ID 		= 5,
			BUTTON_6_ID 		= 6,
			BUTTON_7_ID 		= 7,
			BUTTON_8_ID 		= 8,
			BUTTON_9_ID 		= 9,
			BUTTON_10_ID 		= 10,
			BUTTON_11_ID 		= 11,
			BUTTON_12_ID 		= 12;
	}

	public static class XboxController {
		public static final String
			USB_DEVICE_NAME					= "Controller (Xbox One For Windows)";

		public static final int
			JOYSTICK_ID 				= 1,

			// Left joystick axes
			LEFT_X_AXIS 				= 0,
			LEFT_Y_AXIS 				= 1,

			// Trigger axes
			LEFT_TRIGGER_AXIS 			= 2,
			RIGHT_TRIGGER_AXIS 			= 3,

			// Joystick buttons (like when you press straight down on the left or right joystick)
			LEFT_JOYSTICK_BUTTON_ID 	= 9,
			RIGHT_JOYSTICK_BUTTON_ID 	= 10,
			
			// Right joystick axes
			RIGHT_X_AXIS 				= 4,
			RIGHT_Y_AXIS 				= 5,

			// Gamepad buttons
			A_BUTTON_ID 				= 1,
			B_BUTTON_ID 				= 2,
			X_BUTTON_ID 				= 3,
			Y_BUTTON_ID 				= 4,
			
			// Bumper buttons
			LEFT_BUMPER_BUTTON_ID 		= 5,
			RIGHT_BUMPER_BUTTON_ID 		= 6,
			
			// Center buttons
			VIEW_BUTTON_ID 				= 7,
			MENU_BUTTON_ID 				= 8,

			// ---POV Indices---
			ANGLE_UP_POV 				= 0;
	}
}
