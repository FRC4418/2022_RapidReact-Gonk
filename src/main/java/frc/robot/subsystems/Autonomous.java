package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Conversion;


public class Autonomous extends SubsystemBase {
	// ----------------------------------------------------------
	// Publicly static resources


	// in seconds
	public static double startDelayTime = 0.d;

	// in meters
	public static double tarmacLeavingDistance = Conversion.inchesToMeters(50.d);


	// ----------------------------------------------------------
	// Public constants


	public enum AutonomousRoutine {
		// 'PICKUP_CARGO' just directly runs intake to collect the cargo that's assumed to be right behind us
		// 'RETRIEVE_CARGO' uses vision and the IMU to autonomously find the closest cargo and collect it 
		// LT = leave tarmac
		// LH = score low hub
		// PC = pickup cargo
		// RC = retrieve cargo
		LEAVE_TARMAC									(1),	// LT
		SCORE_LH_AND_LEAVE_TARMAC						(2),	// LH LT
		SCORE_LH_AND_PICKUP_CARGO_AND_LEAVE_TARMAC		(3),	// LH PC LT
		SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC	(4);	// LH RC LT

		private final int value;

		private AutonomousRoutine(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}


	// ----------------------------------------------------------
	// Constructor

	
	public Autonomous() {
		
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}
