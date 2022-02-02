package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		DEFAULT_ROLLER_DISPOSAL_OUTPUT_PERCENT = -0.5d,
		DEFAULT_ROLLER_INTAKE_OUTPUT_PERCENT = 0.5d,
		
		DEFAULT_RETRACTOR_POSITION = 90.d;
	
		
	// ----------------------------------------------------------
	// Private constants


	private final int
		ROLLER_CAN_ID = 11,
		RETRACT_CAN_ID = 12;

	private final double
		// units in seconds
		ROLLER_MOTOR_RAMP_TIME = 0.25d;


	// ----------------------------------------------------------
	// Resources


	public WPI_TalonSRX rollerMotor;
	public WPI_TalonFX retractorMotor;
	

	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		rollerMotor = new WPI_TalonSRX(ROLLER_CAN_ID);
		retractorMotor = new WPI_TalonFX(RETRACT_CAN_ID);

		rollerMotor.configOpenloopRamp(ROLLER_MOTOR_RAMP_TIME);
	}


	// ----------------------------------------------------------
	// Retractor motor


	public double getRetractorPosition() { return retractorMotor.getSelectedSensorPosition(); }

	public Intake setRetractMotorPosition(double position) {
		retractorMotor.set(ControlMode.Position, position);
		return this;
	}

	// TODO: Figure out how the intake's retractor is supposed to work

	public Intake retractIntakeArm() {
		// setRetractMotorPosition(RETRACTED_INTAKE_ARM_RETRACTOR_MOTOR_POSITION);
		return this;
	}

	public Intake extendIntakeArm() {
		// setRetractMotorPosition(EXTENDED_INTAKE_ARM_RETRACTOR_MOTOR_POSITION);
		return this;
	}


	// ----------------------------------------------------------
	// Roller motor


	public double getRollerSpeed() { return rollerMotor.get(); }

	public Intake setRollerMotorPercent(double percentOutput) {
		rollerMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	public Intake runRollerDisposal() {
		setRollerMotorPercent(DEFAULT_ROLLER_DISPOSAL_OUTPUT_PERCENT);
		return this;
	}

	public Intake runRollerIntake() {
		setRollerMotorPercent(DEFAULT_ROLLER_INTAKE_OUTPUT_PERCENT);
		return this;
	}

	public Intake stopRoller() {
		setRollerMotorPercent(0.d);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}