package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;


public class Autonomous extends SubsystemBase {
	// ----------------------------------------------------------
	// Private resources	

	private static boolean
		usingPremadeRoutine = true;

	private static double
		// converting from feet per second (FPS) to meters per second (MPS)
		driveStraightMPS = Constants.feetToMeters(1.5) / 1.,

		startDelaySeconds = 0.,
		tarmacLeavingMeters = Constants.inchesToMeters(72.);

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
		WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_SCORE_LH			(4),	// Wait LH PC LH
		WAIT_AND_SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC	(5);	// Wait LH RC LT

		private final int value;

		private AutonomousRoutine(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}


	// ----------------------------------------------------------
	// Routine-parameter setters


	public static double getDriveStraightMPS() {
		return driveStraightMPS;
	}
	public Autonomous setDriveStraightMPS(double mps) {
		driveStraightMPS = mps;
		RobotContainer.instance.remakeAutoCommand();
		return this;
	}

	public static boolean usingPremadeRoutine() {
		return usingPremadeRoutine;
	}
	public Autonomous setUsePremadeRoutine(boolean bool) {
		usingPremadeRoutine = bool;
		RobotContainer.instance.remakeAutoCommand();
		return this;
	}

	public static double getStartDelaySeconds() {
		return startDelaySeconds;
	}
	public Autonomous setStartDelaySeconds(double delaySeconds) {
		startDelaySeconds = delaySeconds;
		RobotContainer.instance.remakeAutoCommand();
		return this;
	}

	public static double getTarmacLeavingMeters() {
		return tarmacLeavingMeters;
	}
	public Autonomous setTarmacLeavingMeters(double distance) {
		tarmacLeavingMeters = distance;
		RobotContainer.instance.remakeAutoCommand();
		return this;
	}
}
