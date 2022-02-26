package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;


public class Autonomous extends SubsystemBase {
	// ----------------------------------------------------------
	// Publicly static resources


	// in seconds
	public static double startDelayTime = 0.d;

	// in meters
	public static double tarmacLeavingDistanceMeters = Constants.inchesToMeters(72.d);


	// ----------------------------------------------------------
	// Public constants


	public enum AutonomousRoutine {
		// 'PICKUP_CARGO' just directly runs intake to collect the cargo that's assumed to be right behind us
		// 'RETRIEVE_CARGO' uses vision and the IMU to autonomously find the closest cargo and collect it 
		// LT = leave tarmac
		// LH = score low hub
		// PC = pickup cargo
		// RC = retrieve cargo
		WAIT_AND_LEAVE_TARMAC									(1),	// Wait LT
		WAIT_SCORE_LH_AND_LEAVE_TARMAC							(2),	// Wait LH LT
		SCORE_LH_AND_WAIT_AND_LEAVE_TARMAC						(3),	// LH Wait LT
		WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_LEAVE_TARMAC		(4),	// Wait LH PC LT
		WAIT_AND_SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC	(5);	// Wait LH RC LT

		private final int value;

		private AutonomousRoutine(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static void setStartDelayTime(double newDelayTime) {
		startDelayTime = newDelayTime;
		RobotContainer.instance.remakeAutoCommand();
	}

	public static void setTarmacLeavingDistance(double newDistance) {
		tarmacLeavingDistanceMeters = newDistance;
		RobotContainer.instance.remakeAutoCommand();
	}
}
