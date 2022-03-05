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
		m_launcherMotor.configFactoryDefault();
		m_launcherMotor.setInverted(true);
		m_launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kTimeoutMs);
		m_launcherMotor.config_kP(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kP);
		m_launcherMotor.config_kI(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kI);
        m_launcherMotor.config_kD(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kD);
		// m_launcherMotor.config_kF(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kF);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		// TODO: Remove these print once useless
		SmartDashboard.putNumber("Launcher RPM", getLauncherRPM());
		SmartDashboard.putNumber("Indexer RPM", getIndexerRPM());
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