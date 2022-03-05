package frc.robot.subsystems;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources
	

	private final WPI_TalonSRX m_indexerMotor = new WPI_TalonSRX(Constants.Manipulator.CAN_ID.kIndexer);
	private final WPI_TalonFX m_launcherMotor = new WPI_TalonFX(Constants.Manipulator.CAN_ID.kLauncher);


	// ----------------------------------------------------------
	// Constructor


	public Manipulator() {
		// ----------------------------------------------------------
		// Indexer motor configuration

		m_indexerMotor.configFactoryDefault();
		m_indexerMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Manipulator.kIndexerPidIdx, Constants.Manipulator.kTimeoutMs);

		// ----------------------------------------------------------
		// Launcher motor configuration

		m_launcherMotor.configFactoryDefault();
		m_launcherMotor.setInverted(true);
		m_launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kTimeoutMs);

		// ----------------------------------------------------------
		// Final setup
		
		configurePIDs();
	}


	// ----------------------------------------------------------
	// Constants-reconfiguration methods


	public Manipulator configurePIDs() {
		m_launcherMotor.config_kP(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGains.kP);
		m_launcherMotor.config_kI(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGains.kI);
        m_launcherMotor.config_kD(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGains.kD);
		// m_launcherMotor.config_kF(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGains.kF);

		m_indexerMotor.config_kP(Constants.Manipulator.kIndexerPidIdx, Constants.Manipulator.kIndexerRPMGains.kP);
		m_indexerMotor.config_kI(Constants.Manipulator.kIndexerPidIdx, Constants.Manipulator.kIndexerRPMGains.kI);
        m_indexerMotor.config_kD(Constants.Manipulator.kIndexerPidIdx, Constants.Manipulator.kIndexerRPMGains.kD);
		// m_indexerMotor.config_kF(Constants.Manipulator.kIndexerPidIdx, Constants.Manipulator.kIndexerRPMGains.kF);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		// TODO: Remove this print once useless
		SmartDashboard.putNumber("Launcher RPM", getLauncherRPM());
	}


	// ----------------------------------------------------------
	// Indexer motor
	

	public int getIndexerRPM() {
		return (int) (
			m_indexerMotor.getSelectedSensorVelocity(Constants.Manipulator.kIndexerPidIdx)
			/ Constants.Manipulator.kIndexerTicksReductionRatio
			/ Constants.Falcon500.kRpmToTicksPer100ms);
	}

	private boolean withinIndexerRPMRange(int rpm) {
		return (rpm >= Constants.Manipulator.kIndexerMinRPM && rpm <= Constants.Manipulator.kIndexerMaxRPM);
	}

	public Manipulator setIndexerRPM(int rpm) {
		if (withinIndexerRPMRange(rpm)) {
			m_indexerMotor.set(ControlMode.Velocity,
				rpm * Constants.Manipulator.kIndexerTicksReductionRatio
				* Constants.Falcon500.kRpmToTicksPer100ms);
		}
		return this;
	}

	public Manipulator setIndexerPercent(double percent) {
		if (withinIndexerRPMRange((int) (Constants.Falcon500.kMaxRPM * percent))) {
			m_indexerMotor.set(ControlMode.PercentOutput, percent);
		}
		return this;
	}

	public Manipulator runIndexer() {
		setIndexerRPM(Constants.Manipulator.kDefaultIndexerRPM);
		return this;
	}

	public Manipulator stopIndexer() {
		setIndexerRPM(0);
		return this;
	}


	// ----------------------------------------------------------
	// Launcher motor


	public int getLauncherRPM() {
		return (int) (
			m_launcherMotor.getSelectedSensorVelocity(Constants.Manipulator.kLauncherPidIdx)
			/ Constants.Manipulator.kLauncherTicksReductionRatio
			/ Constants.Falcon500.kRpmToTicksPer100ms);
	}

	private boolean withinLauncherRPMRange(int rpm) {
		return (rpm >= Constants.Manipulator.kLauncherMinRPM && rpm <= Constants.Manipulator.kLauncherMaxRPM);
	}

	public Manipulator setLauncherRPM(int rpm) {
		if (withinLauncherRPMRange(rpm)) {
			m_launcherMotor.set(ControlMode.Velocity,
				rpm * Constants.Manipulator.kLauncherTicksReductionRatio
				* Constants.Falcon500.kRpmToTicksPer100ms);
		}
		return this;
	}

	public Manipulator setLauncherPercent(double percent) {
		if (withinLauncherRPMRange((int) (Constants.Falcon500.kMaxRPM * percent))) {
			m_launcherMotor.set(ControlMode.PercentOutput, percent);
		}
		return this;
	}

	public Manipulator runLauncher() {
		setLauncherRPM(Constants.Manipulator.kDefaultLauncherRPM);
		return this;
	}

	public Manipulator stopLauncher() {
		setLauncherRPM(0);
		return this;
	}
}