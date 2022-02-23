package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Falcon500;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		kDefaultIndexerPercent = 1.0d;
	
	// TODO: P1 Tune launcher RPM
	public static final int
		kDefaultLauncherRPM = 800;

		
	// ----------------------------------------------------------
	// Private constants


	private static class CAN_ID {
		private static final int
			kIndexer = 21,
			kLauncher = 22;
	}

	private final int kLauncherPidIdx = 0;

	private final double kRpmToTicksPer100ms = ((double) Falcon500.ticksPerRevolution) / 600.d;
	

	// ----------------------------------------------------------
	// Resources
	

	private final WPI_TalonSRX indexerMotor = new WPI_TalonSRX(CAN_ID.kIndexer);
	private final WPI_TalonFX launcherMotor = new WPI_TalonFX(CAN_ID.kLauncher);


	// ----------------------------------------------------------
	// Indexer motor
	

	// -1 to 1
	public double getIndexerPercent() {
		return indexerMotor.get();
	}

	public Manipulator setIndexerPercent(double percentOutput) {
		indexerMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	// runs the indexer motor at the default output percent
	public Manipulator runIndexer() {
		setIndexerPercent(kDefaultIndexerPercent);
		return this;
	}

	public Manipulator stopIndexer() {
		setIndexerPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Launcher motor


	public double getLauncherRPM() {
		return launcherMotor.getSelectedSensorVelocity(kLauncherPidIdx) / kRpmToTicksPer100ms;
	}

	public Manipulator setLauncherRPM(double rpm) {
		launcherMotor.set(ControlMode.Velocity, rpm * kRpmToTicksPer100ms);
		return this;
	}

	// runs the launcher motor at the default output percent
	public Manipulator runLauncher() {
		setLauncherRPM(kDefaultLauncherRPM);
		return this;
	}

	public Manipulator stopLauncher() {
		setLauncherRPM(0.d);
		return this;
	}
}