/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import frc.robot.teamlibraries.Gains;


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
			THREE_BUTTON_ID = 3,
			FOUR_BUTTON_ID = 4,
			FIVE_BUTTON_ID = 5,
			SIX_BUTTON_ID = 6,
			SEVEN_BUTTON_ID = 7,
			EIGHT_BUTTON_ID = 8,
			NINE_BUTTON_ID = 9,
			TEN_BUTTON_ID = 10,
			ELEVEN_BUTTON_ID = 11,
			TWELVE_BUTTON_ID = 12;
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
			TOGGLE_ARCADE_DRIVE_BUTTON_ID = X3D.FIVE_BUTTON_ID,	// does not toggle drive mode for spotter
			DRIVE_STRAIGHT_BUTTON_ID = X3D.GRIP_BUTTON_ID;
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

	public static class Drive {
		public static class TalonFX {
			public static final int
				FRONT_LEFT_ID = 4,
				BACK_LEFT_ID = 3,
				FRONT_RIGHT_ID = 2,
				BACK_RIGHT_ID = 1;
		}
		
		public static class Encoder {
			public static final double
				// 2048 ticks in 1 revolution for Falcon 500s
				// wheel diameter * pi = circumference of 1 revolution
				// `box is 44:30 ratio
				TICKS_TO_INCHES_CONVERSION  = ( (6.0d * Math.PI) / 2048.0d ) * (1.0d / 7.33d);
		}

		public static class OpenLoopControl {
			public static final double
				// units in seconds
				SHARED_RAMP_TIME = 1.5d;	// TODO: Config open-loop ramp time
		}

		// AKA PID stuff
		public static class ClosedLoopControl {
			/**
			 * Which PID slot to pull gains from. Starting 2018, you can choose from
			 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
			 * configuration.
			 */
			public static final int kSlotIdx = 0;

			/**
			 * Talon FX supports multiple (cascaded) PID loops. For
			 * now we just want the primary one.
			 */
			public static final int kIdx = 0;

			/**
			 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
			 * report to DS if action fails.
			 */
			public static final int kTimeoutMs = 30;

			/**
			 * PID Gains may have to be adjusted based on the responsiveness of control loop.
			 * kF: 1023 represents output value to Talon at 100%, 20660 represents Velocity units at 100% output
			 */
			public final static Gains kLeftMotorVelocityGains = new Gains(
				0.1,				// kP
				0.001,				// kI
				5,					// kD
				1023.0/20660.0,		// kF
				300,				// Iz
				1.00);				// Peakout
			
			public final static Gains kRightMotorVelocityGains = new Gains(
				0.1,				// kP
				0.001,				// kI
				5,					// kD
				1023.0/20660.0,		// kF
				300,				// Iz
				1.00);				// Peakout
		}
	}

	public static class Manipulator {
		public static class Intake {
			// TODO: Set acutal intake motors to have these IDs
			public static final int
				MOTOR_0_ID = 21,
				MOTOR_1_ID = 22,
				MOTOR_2_ID = 23;

			public static final double
				PERCENT_OUTPUT = 0.5d;
		}
		
		public static class ConveyorShooter {
			public static final int
				LOWER_MOTOR_ID = 5,
				HIGHER_MOTOR_ID = 7;

			public static final double
				DEFAULT_LOWER_MOTOR_PERCENT = 0.3,
				DEFAULT_HIGHER_MOTOR_PERCENT = 0.3;
		}

		public static class HighGoalShooter {
			public static final int
				MOTOR_775_ID = 11;
			
			public static final double
				TARGET_PERCENTAGE = 0.8; // high shooter, RPMs are changed to units/100ms in motor commands
		}
		
	}


// RIO Post Info
// public static int[] expectedTalonIDs = {
// 	DRIVE_LEFT_A_TALON_SRX_ID,
// 	DRIVE_LEFT_B_TALON_SRX_ID,
// 	DRIVE_RIGHT_A_TALON_SRX_ID,
// 	DRIVE_RIGHT_B_TALON_SRX_ID
// };

// public static int[] expectedDIOEncoders = {
// 	DRIVE_LEFT_ENCODER_CHANNELA_ID,
// 	DRIVE_LEFT_ENCODER_CHANNELB_ID,
// 	DRIVE_RIGHT_A_TALON_SRX_ID,
// 	DRIVE_RIGHT_B_TALON_SRX_ID
// };

// public static int expectedGyro = DRIVE_GYRO_ID;

// public static int[] expectedDIOUltrasonic = {
// 	DRIVE_FRONT_DISTANCE_PING_ID,
// 	DRIVE_FRONT_DISTANCE_ECHO_ID,
// 	DRIVE_BACK_DISTANCE_PING_ID,
// 	DRIVE_BACK_DISTANCE_ECHO_ID
// };
}
