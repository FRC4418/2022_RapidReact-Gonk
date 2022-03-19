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
		startDelaySeconds = 0.,
		tarmacReturnDelaySeconds = 0.,
		// converting from feet per second (FPS) to meters per second (MPS)
		maxMotorMPS = Constants.feetToMeters(1.25),
		launcherFiringDurationSeconds = 1.25,
		tarmacLeavingMeters = Constants.inchesToMeters(72.);

	// ----------------------------------------------------------
	// Public constants


	public enum AutonomousRoutine {
		// 'PICKUP_CARGO' just directly runs intake to collect the cargo that's assumed to be right behind us
		// 'RETRIEVE_CARGO' uses vision and the IMU to autonomously find the closest cargo and collect it 
		// LT = leave tarmac
		// LH = score low hub
		// PC = pickup cargo
		// TC = trajectory-collect cargo
		// RC = retrieve cargo
		WAIT_AND_LEAVE_TARMAC,												// Wait LT
		WAIT_SCORE_LH_AND_LEAVE_TARMAC,										// Wait LH LT
		SCORE_LH_AND_WAIT_AND_LEAVE_TARMAC,									// LH Wait LT
		WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_WAIT_AND_SCORE_LH,			// Wait LH PC LH
		WAIT_LH_AND_TRAJECTORY_COLLECT_ONE_AND_WAIT_AND_GET_SECOND_AND_LH,	// Wait LH TC WAIT TC LH
		WAIT_AND_SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC;				// Wait LH RC LT
	}


	// ----------------------------------------------------------
	// Routine-parameter setters


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

	public static double getTarmacReturnDelaySeconds() {
		return tarmacReturnDelaySeconds;
	}
	public void setTarmacReturnDelaySeconds(double seconds) {
		tarmacReturnDelaySeconds = seconds;
		RobotContainer.instance.remakeAutoCommand();
	}

	public static double getDrivingMaxMotorMPS() {
		return maxMotorMPS;
	}
	public Autonomous setDrivingMaxSpeedMPS(double maxSpeed) {
		Autonomous.maxMotorMPS = maxSpeed;
		RobotContainer.instance.remakeAutoCommand();
		return this;
	}

	public static double getLauncherFiringDurationSeconds() {
		return launcherFiringDurationSeconds;
	}
	public Autonomous setLauncherFiringDurationSeconds(double seconds) {
		launcherFiringDurationSeconds = seconds;
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
