package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		DEFAULT_REVERSE_FEEDER_OUTPUT_PERCENT = -0.5d,
		DEFAULT_FEEDER_OUTPUT_PERCENT = 0.5d,
		
		DEFAULT_RETRACTOR_POSITION = 90.d;
	
		
	// ----------------------------------------------------------
	// Private constants


	private final int
		FEEDER_CAN_ID = 11,
		RETRACT_CAN_ID = 12;

	private final int WHISKER_SENSOR_DIO_PORT = 0;

	private final double
		// units in seconds
		FEEDER_MOTOR_RAMP_TIME = 0.25d;


	// ----------------------------------------------------------
	// Resources


	private final DigitalInput m_whiskerSensor = new DigitalInput(WHISKER_SENSOR_DIO_PORT);

	private final WPI_TalonSRX m_feederMotor = new WPI_TalonSRX(FEEDER_CAN_ID);
	private final WPI_TalonFX m_retractorMotor = new WPI_TalonFX(RETRACT_CAN_ID);


	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		m_feederMotor.configOpenloopRamp(FEEDER_MOTOR_RAMP_TIME);
		m_feederMotor.setInverted(true);
	}


	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor


	public double getRetractorPosition() { return m_retractorMotor.getSelectedSensorPosition(); }

	public Intake setRetractMotorPosition(double position) {
		m_retractorMotor.set(ControlMode.Position, position);
		return this;
	}

	// TODO: P1 Figure out how the intake's retractor is supposed to work

	public Intake retractIntakeArm() {
		// setRetractMotorPosition(RETRACTED_INTAKE_ARM_RETRACTOR_MOTOR_POSITION);
		return this;
	}

	public Intake extendIntakeArm() {
		// setRetractMotorPosition(EXTENDED_INTAKE_ARM_RETRACTOR_MOTOR_POSITION);
		return this;
	}


	// ----------------------------------------------------------
	// Feeder motor


	public double getFeederSpeed() { return m_feederMotor.get(); }

	public Intake setFeederMotorPercent(double percentOutput) {
		m_feederMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	public Intake runReverseFeeder() {
		setFeederMotorPercent(DEFAULT_REVERSE_FEEDER_OUTPUT_PERCENT);
		return this;
	}

	public Intake runFeeder() {
		setFeederMotorPercent(DEFAULT_FEEDER_OUTPUT_PERCENT);
		return this;
	}

	public Intake stopFeeder() {
		setFeederMotorPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}