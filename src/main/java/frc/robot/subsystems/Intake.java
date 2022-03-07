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
		m_retractorMotor.configOpenloopRamp(Constants.Intake.kRetractorOpenLoopRampSeconds);
		m_retractorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Intake.kRetractorPidIdx, Constants.Intake.kTimeoutMs);

		// ----------------------------------------------------------
		// Final setup

		configurePIDs();
	}


	// ----------------------------------------------------------
	// Constants-reconfiguration methods


	public Intake configurePIDs() {
		m_retractorMotor.config_kP(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kP);
		m_retractorMotor.config_kI(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kI);
        m_retractorMotor.config_kD(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kD);
		// m_retractorMotor.config_kF(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kF);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		updateRetractorOrigin();

		
	}
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor

	
	public Intake updateRetractorOrigin() {
		if (getRetractorTicks() < 0) {
			m_retractorMotor.setSelectedSensorPosition(0.);
		}
		return this;
	}

	public Intake brakeRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Brake);
		return this;
	}

	public Intake coastRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Coast);
		return this;
	}

	public int getRetractorTicks() {
		return (int) (
			m_retractorMotor.getSelectedSensorPosition(Constants.Intake.kRetractorPidIdx)
			/ Constants.Intake.kRetractorTicksReductionRatio);
	}

	public double getRetractorDegree() {
		return (double) getRetractorTicks() / Constants.Falcon500.kDegreesToTicks;
	}

	private boolean withinRetractorDegreeRange(double degree) {
		return (degree >= Constants.Intake.kRetractorMinDegree && degree <= Constants.Intake.kRetractorMaxDegree);
	}

	public Intake setRetractorDegree(double positionDegrees) {
		if (withinRetractorDegreeRange(positionDegrees)) {
			setRetractorTicks((int) (positionDegrees * Constants.Falcon500.kDegreesToTicks));
		}
		return this;
	}

	public Intake setRetractorTicks(int positionTicks) {
		if (withinRetractorDegreeRange(positionTicks / Constants.Falcon500.kDegreesToTicks)) {
			m_retractorMotor.set(ControlMode.Position, positionTicks * Constants.Intake.kRetractorTicksReductionRatio);
		}
		return this;
	}

	public Intake retractIntakeArm() {
		brakeRetractor();
		setRetractorDegree(Constants.Intake.kDefaultRetractedIntakeRetractorDegree);
		return this;
	}

	// true means it is satisfiably close to the retracted-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is extended
	public boolean armIsRetracted() {
		return Math.abs(getRetractorDegree() - Constants.Intake.kDefaultRetractedIntakeRetractorDegree) <= Constants.Intake.kRetractorDegreeTolerance;
	}

	public Intake extendIntakeArm() {
		brakeRetractor();
		setRetractorDegree(Constants.Intake.kDefaultExtendedIntakeRetractorDegree);
		return this;
	}

	// true means it is satisfiably close to the extended-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is retracted
	public boolean armIsExtended() {
		return Math.abs(getRetractorTicks() - Constants.Intake.kDefaultExtendedIntakeRetractorDegree) <= Constants.Intake.kRetractorDegreeTolerance;
	}


	// ----------------------------------------------------------
	// Feeder motor


	// -1 to 1
	public double getFeederPercent() {
		return m_feederMotor.get();
	}

	// -1 to 1
	public Intake setFeederPercent(double percent) {
		m_feederMotor.set(ControlMode.PercentOutput, percent);
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
		setFeederPercent(0.);
		return this;
	}
}