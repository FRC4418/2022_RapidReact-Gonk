package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT = 1.0d,
		DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT = 1.0d;

		
	// ----------------------------------------------------------
	// Private constants


	private final int
		INDEXER_CAN_ID = 21,
		LAUNCHER_CAN_ID = 22;
	

	// ----------------------------------------------------------
	// Resources
	

	private final WPI_TalonSRX indexerMotor = new WPI_TalonSRX(INDEXER_CAN_ID);
	private final WPI_TalonFX launcherMotor = new WPI_TalonFX(LAUNCHER_CAN_ID);
	
	/* Encoder.getRate() returns distance per second
	distance per second * distance per pulse = pulse per second
	pulse per second * decoding factor = degrees per second
	degrees per second / 360 degrees = revolutions per second
	revolutions per second * 60 seconds = revolutions per minute (RPM) */
	// private static double distPerSecToRPM = 
	//   Constants.DRIVE_ENCODER_DISTANCE_PER_PULSE
	//   * (double) Constants.DRIVE_ENCODER_DECODING_SCALE_FACTOR 
	//   / 60.0;


	// ----------------------------------------------------------
	// Constructor


	public Manipulator() {

	}


	// ----------------------------------------------------------
	// Indexer motor
	

	public double getIndexerSpeed() { return indexerMotor.get(); }

	public Manipulator setIndexerToPercent(double percentOutput) {
		indexerMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	// runs the indexer motor at the default output percent
	public Manipulator runIndexer() {
		setIndexerToPercent(DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT);
		return this;
	}

	public Manipulator stopIndexer() {
		setIndexerToPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Launcher motor


	public double getLauncherSpeed() { return launcherMotor.get(); }

	public Manipulator setLauncherToPercent(double percentOutput) {
		launcherMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	// runs the launcher motor at the default output percent
	public Manipulator runLauncher() {
		setLauncherToPercent(DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT);
		return this;
	}

	public Manipulator stopLauncher() {
		setLauncherToPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}