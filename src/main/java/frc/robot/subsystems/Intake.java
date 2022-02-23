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
		kDefaultReverseFeederPercent = -0.5d,
		kDefaultFeederPercent = 0.5d;
		
	public static final int
		kDefaultRetractorPositionDegrees = 90;
	
		
	// ----------------------------------------------------------
	// Private constants


	private static class CAN_IDs {
		private static final int
			kFeeder = 11,
			kRetractor = 12;
	}

	private final int kWhiskerSensorDIOPort = 8;

	private final double
		// in seconds
		kFeederRampTime = 0.25d;

	private final double
		// 2048 ticks for every 360 degrees in one revolution
		kRetractorDegreesToTicks = 2048.d / 360.d;

	// TODO: P1 Figure out what the retractor's extended and retracted positions should be
	private final int
		kExtendedIntakeRetractorPosition = 0,
		kRetractedIntakeRetractorPosition = 100;


	// ----------------------------------------------------------
	// Resources


	private final DigitalInput m_whiskerSensor = new DigitalInput(kWhiskerSensorDIOPort);

	private final WPI_TalonSRX m_feederMotor = new WPI_TalonSRX(CAN_IDs.kFeeder);
	private final WPI_TalonFX m_retractorMotor = new WPI_TalonFX(CAN_IDs.kRetractor);


	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		m_feederMotor.configOpenloopRamp(kFeederRampTime);
		m_feederMotor.setInverted(true);
	}
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor


	// in degrees
	public double getRetractorPosition() {
		return m_retractorMotor.getSelectedSensorPosition() / kRetractorDegreesToTicks;
	}

	// in degrees
	public Intake setRetractMotorPosition(double positionDegrees) {
		m_retractorMotor.set(ControlMode.Position, positionDegrees * kRetractorDegreesToTicks);
		return this;
	}

	public Intake retractIntakeArm() {
		setRetractMotorPosition(kRetractedIntakeRetractorPosition);
		return this;
	}

	public Intake extendIntakeArm() {
		setRetractMotorPosition(kExtendedIntakeRetractorPosition);
		return this;
	}


	// ----------------------------------------------------------
	// Feeder motor


	// arbitrary -1 to 1
	public double getFeederSpeed() {
		return m_feederMotor.get();
	}

	// arbitrary -1 to 1
	public Intake setFeederMotorPercent(double percentOutput) {
		m_feederMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	public Intake runReverseFeeder() {
		setFeederMotorPercent(kDefaultReverseFeederPercent);
		return this;
	}

	public Intake runFeeder() {
		setFeederMotorPercent(kDefaultFeederPercent);
		return this;
	}

	public Intake stopFeeder() {
		setFeederMotorPercent(0.d);
		return this;
	}
}