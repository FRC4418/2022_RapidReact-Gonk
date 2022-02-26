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
		// m_launcherMotor.config_kF(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kF);
		m_launcherMotor.config_kP(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kP);
		// m_launcherMotor.config_kI(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kI);
        // m_launcherMotor.config_kD(Constants.Manipulator.kLauncherPidIdx, Constants.Manipulator.kLauncherRPMGainsV2.kD);
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
		setIndexerPercent(Constants.Manipulator.kDefaultIndexerPercent);
		return this;
	}

	public Manipulator stopIndexer() {
		setIndexerPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Launcher motor


	public double getLauncherRPM() {
		return m_launcherMotor.getSelectedSensorVelocity(Constants.Manipulator.kLauncherPidIdx) / Constants.Manipulator.kRpmToTicksPer100ms;
	}

	public Manipulator setLauncherRPM(double rpm) {
		m_launcherMotor.set(ControlMode.Velocity, rpm * Constants.Manipulator.kRpmToTicksPer100ms);
		return this;
	}

	public Manipulator setLauncherPercent(double percent) {
		m_launcherMotor.set(ControlMode.PercentOutput, percent);
		return this;
	}

	// runs the launcher motor at the default output percent
	public Manipulator runLauncher() {
		// setLauncherPercent(Constants.Manipulator.kDefaultLauncherPercent);
		setLauncherRPM(Constants.Manipulator.kDefaultLauncherRPM);
		return this;
	}

	public Manipulator stopLauncher() {
		setLauncherPercent(0.d);
		return this;
	}
}