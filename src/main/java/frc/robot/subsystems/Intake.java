package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final DigitalInput m_whiskerSensor = new DigitalInput(Constants.Intake.kWhiskerSensorDIOPort);

	private final WPI_TalonSRX m_feederMotor = new WPI_TalonSRX(Constants.Intake.CAN_ID.kFeeder);
	private final WPI_TalonFX m_retractorMotor = new WPI_TalonFX(Constants.Intake.CAN_ID.kRetractor);


	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		// ----------------------------------------------------------
		// Feeder motor configuration

		m_feederMotor.configFactoryDefault();
		m_feederMotor.configOpenloopRamp(Constants.Intake.kFeederRampTime);
		m_feederMotor.setInverted(true);
		
		// ----------------------------------------------------------
		// Retractor motor configuration

		m_retractorMotor.configFactoryDefault();
		m_retractorMotor.setNeutralMode(NeutralMode.Brake);
		m_retractorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Intake.kRetractorPidIdx, Constants.Intake.kTimeoutMs);
		// m_retractorMotor.config_kF(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kF);
		m_retractorMotor.config_kP(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kP);
		// m_retractorMotor.config_kI(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kI);
        // m_retractorMotor.config_kD(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kD);
	}
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor


	public double getRetractorDegree() {
		return m_retractorMotor.getSelectedSensorPosition() / Constants.Intake.kRetractorDegreesToTicks;
	}

	// in degrees
	public Intake setRetractDegree(double positionDegrees) {
		m_retractorMotor.set(ControlMode.Position, positionDegrees * Constants.Intake.kRetractorDegreesToTicks);
		return this;
	}

	public Intake retractIntakeArm() {
		setRetractDegree(Constants.Intake.kRetractedIntakeRetractorPosition);
		return this;
	}

	public Intake extendIntakeArm() {
		setRetractDegree(Constants.Intake.kExtendedIntakeRetractorPosition);
		return this;
	}


	// ----------------------------------------------------------
	// Feeder motor


	// -1 to 1
	public double getFeederPercent() {
		return m_feederMotor.get();
	}

	// -1 to 1
	public Intake setFeederPercent(double percentOutput) {
		m_feederMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	public Intake runReverseFeeder() {
		setFeederPercent(Constants.Intake.kDefaultReverseFeederPercent);
		return this;
	}

	public Intake runFeeder() {
		setFeederPercent(Constants.Intake.kDefaultFeederPercent);
		return this;
	}

	public Intake stopFeeder() {
		setFeederPercent(0.d);
		return this;
	}
}