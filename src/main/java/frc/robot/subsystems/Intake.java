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

	private double
		m_retractorSetDegree = 0.,
		m_feederSetPercent = 0.;


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


	public void configurePIDs() {
		m_retractorMotor.config_kP(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kP);
		m_retractorMotor.config_kI(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kI);
        m_retractorMotor.config_kD(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kD);
		// m_retractorMotor.config_kF(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGains.kF);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	// @Override
	// public void periodic() {
	// 	updateRetractorOrigin();
	// }
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor

	
	public void updateRetractorOrigin() {
		if (getRetractorDegree() < 0) {
			resetRetractorEncoder();
		}
	}

	public void resetRetractorEncoder() {
		m_retractorMotor.setSelectedSensorPosition(0.);
	}

	public void brakeRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Brake);
	}

	public void coastRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Coast);
	}

	public boolean retractorIsRetracting() {
		return m_retractorSetDegree == Constants.Intake.kRetractorUpDegree;
	}

	public boolean retractorIsExtending() {
		return m_retractorSetDegree == Constants.Intake.kRetractorDownDegree;
	}

	public double getRetractorDegree() {
		return m_retractorMotor.getSelectedSensorPosition(Constants.Intake.kRetractorPidIdx) / Constants.Intake.kRetractorOutputDegreesToInputTicks;
	}

	private boolean withinRetractorDegreeRange(double degree) {
		return (degree >= Constants.Intake.kRetractorMinDegree && degree <= Constants.Intake.kRetractorMaxDegree);
	}

	public void setRetractorDegree(double degree) {
		if (withinRetractorDegreeRange(degree)) {
			m_retractorMotor.set(ControlMode.Position, degree * Constants.Intake.kRetractorOutputDegreesToInputTicks);
			m_retractorSetDegree = degree;
		}
	}

	public void retractIntakeArm() {
		brakeRetractor();
		setRetractorDegree(Constants.Intake.kRetractorUpDegree);
	}

	// true means it is satisfiably close to the retracted-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is extended
	public boolean armIsWithinRetractedTolerance() {
		return Math.abs(getRetractorDegree() - Constants.Intake.kRetractorUpDegree) <= Constants.Intake.kRetractorDegreeTolerance;
	}

	public void extendIntakeArm() {
		brakeRetractor();
		setRetractorDegree(Constants.Intake.kRetractorDownDegree);
	}

	// true means it is satisfiably close to the extended-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is retracted
	// public boolean armIsWithinExtendedTolerance() {
	// 	return Math.abs(getRetractorDegree() - Constants.Intake.kRetractorDownDegree) <= Constants.Intake.kRetractorDegreeTolerance;
	// }


	// ----------------------------------------------------------
	// Feeder motor


	public boolean feederIsRunning() {
		return m_feederSetPercent == Constants.Intake.kFeederPercent;
	}

	// -1 to 1
	public double getFeederPercent() {
		return m_feederMotor.get();
	}

	// -1 to 1
	public void setFeederPercent(double percent) {
		m_feederMotor.set(ControlMode.PercentOutput, percent);
		m_feederSetPercent = percent;
	}

	public void runFeeder() {
		setFeederPercent(Constants.Intake.kFeederPercent);
	}

	public void runReverseFeeder() {
		setFeederPercent(Constants.Intake.kReverseFeederPercent);
	}

	public void stopFeeder() {
		setFeederPercent(0.);
	}
}