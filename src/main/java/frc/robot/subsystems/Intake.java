package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Gains;
import frc.robot.Constants.Falcon500;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		kDefaultReverseFeederPercent = -0.5d,
		kDefaultFeederPercent = 0.5d;
		
	public static final int
		kDefaultRetractorDegree = 90;
	
		
	// ----------------------------------------------------------
	// Private constants


	private static class CAN_ID {
		private static final int
			kFeeder = 11,
			kRetractor = 12;
	}

	private final int kWhiskerSensorDIOPort = 8;

	private final double
		// in seconds
		kFeederRampTime = 0.25d;

		
	private final double
		kRetractorDegreesToTicks = ((double) Falcon500.ticksPerRevolution) / 360.d;
	
	// TODO: P1 Tune retractor extended and retracted degrees
	private final int
		kExtendedIntakeRetractorPosition = 0,
		kRetractedIntakeRetractorPosition = 100;
		
	private final int
		kRetractorPidIdx = 0,
		kRetractorSlotIdx = 0,
		kTimeoutMs = 30;

	private final Gains kRetractorPositionGains
		// = new Gains(0.1d,	0.001d,	5.d,	1023.d/20660.d,	300,	1.00d);
		// kP, kI, kD, kF, kIzone, kPeakOutput
		= new Gains(0.1d, 0.d, 0.d, 1023.d/20660.d, 300, 1.00d);

	// ----------------------------------------------------------
	// Resources


	private final DigitalInput m_whiskerSensor = new DigitalInput(kWhiskerSensorDIOPort);

	private final WPI_TalonSRX m_feederMotor = new WPI_TalonSRX(CAN_ID.kFeeder);
	private final WPI_TalonFX m_retractorMotor = new WPI_TalonFX(CAN_ID.kRetractor);


	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		// ----------------------------------------------------------
		// Feeder motor configuration

		m_feederMotor.configFactoryDefault();
		m_feederMotor.configOpenloopRamp(kFeederRampTime);
		m_feederMotor.setInverted(true);
		
		// ----------------------------------------------------------
		// Retractor motor configuration

		m_retractorMotor.configFactoryDefault();
		m_retractorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kRetractorPidIdx, kTimeoutMs);
		// m_retractorMotor.config_kF(kRetractorSlotIdx, kRetractorPositionGains.kF);
		m_retractorMotor.config_kP(kRetractorSlotIdx, kRetractorPositionGains.kP);
		// m_retractorMotor.config_kI(kRetractorSlotIdx, kRetractorPositionGains.kI);
        // m_retractorMotor.config_kD(kRetractorSlotIdx, kRetractorPositionGains.kD);
	}
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor


	public double getRetractorDegree() {
		return m_retractorMotor.getSelectedSensorPosition() / kRetractorDegreesToTicks;
	}

	// in degrees
	public Intake setRetractDegree(double positionDegrees) {
		m_retractorMotor.set(ControlMode.Position, positionDegrees * kRetractorDegreesToTicks);
		return this;
	}

	public Intake retractIntakeArm() {
		setRetractDegree(kRetractedIntakeRetractorPosition);
		return this;
	}

	public Intake extendIntakeArm() {
		setRetractDegree(kExtendedIntakeRetractorPosition);
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
		setFeederPercent(kDefaultReverseFeederPercent);
		return this;
	}

	public Intake runFeeder() {
		setFeederPercent(kDefaultFeederPercent);
		return this;
	}

	public Intake stopFeeder() {
		setFeederPercent(0.d);
		return this;
	}
}