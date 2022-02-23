package frc.robot.subsystems;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Gains;
import frc.robot.Constants.Falcon500;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		kDefaultIndexerPercent = 1.0d;
	
	// TODO: P1 Tune launcher RPM
	public static final int
		kDefaultLauncherRPM = 5_000;

		
	// ----------------------------------------------------------
	// Private constants


	private static class CAN_ID {
		private static final int
			kIndexer = 21,
			kLauncher = 22;
	}

	private final int
		kLauncherPidIdx = 0,
		kTimeoutMs = 30;

	// Falcon 500s have a free speed of 6380 RPM, which means a maximum of 21,777 ticks per 100ms
	private final double kRpmToTicksPer100ms = ((double) Falcon500.ticksPerRevolution) / 600.d;

	// TODO: !!!P1!!! tune launcher RPM PID gains
	private final Gains kLauncherRPMGains
		// = new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
		// kP, kI, kD, kF, kIzone, kPeakOutput
		= new Gains(0.1d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d);
	

	// ----------------------------------------------------------
	// Resources
	

	private final WPI_TalonSRX m_indexerMotor = new WPI_TalonSRX(CAN_ID.kIndexer);
	private final WPI_TalonFX m_launcherMotor = new WPI_TalonFX(CAN_ID.kLauncher);


	// ----------------------------------------------------------
	// Constructor


	public Manipulator() {
		m_launcherMotor.configFactoryDefault();
		m_launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kLauncherPidIdx, kTimeoutMs);
		// m_launcherMotor.config_kF(kLauncherPidIdx, kLauncherRPMGains.kF);
		m_launcherMotor.config_kP(kLauncherPidIdx, kLauncherRPMGains.kP);
		// m_launcherMotor.config_kI(kLauncherPidIdx, kLauncherRPMGains.kI);
        // m_launcherMotor.config_kD(kLauncherPidIdx, kLauncherRPMGains.kD);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		SmartDashboard.putNumber("Launcher RPM", getLauncherRPM());
	}


	// ----------------------------------------------------------
	// Indexer motor
	

	// -1 to 1
	public double getIndexerPercent() {
		return m_indexerMotor.get();
	}

	public Manipulator setIndexerPercent(double percentOutput) {
		m_indexerMotor.set(ControlMode.PercentOutput, percentOutput);
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
		return m_launcherMotor.getSelectedSensorVelocity(kLauncherPidIdx) / kRpmToTicksPer100ms;
	}

	public Manipulator setLauncherRPM(double rpm) {
		m_launcherMotor.set(ControlMode.Velocity, rpm * kRpmToTicksPer100ms);
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