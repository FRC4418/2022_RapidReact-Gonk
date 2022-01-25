package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants

	// TODO: Set acutal intake motors to have these IDs
	public final int
		ROLLER_CAN_ID = 21,
		RETRACT_CAN_ID = 22;

	public final double
		ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT = 0.5d,
		RETRACT_MOTOR_DEFAULT_POSITION = 90.d;

	// ----------------------------------------------------------
	// Resources

	public WPI_TalonSRX rollerMotor;
	public WPI_TalonFX retractMotor;

	public NetworkTableEntry intakePercentOutputTextField;
	
	// ----------------------------------------------------------
	// Constructor and actions
	
	public Intake() {
		rollerMotor = new WPI_TalonSRX(ROLLER_CAN_ID);
		retractMotor = new WPI_TalonFX(RETRACT_CAN_ID);
	}

	public double getRollerMotor() { return rollerMotor.get(); }
	public double getRetractMotor() { return retractMotor.get(); }

	public void setRollerMotorPercent(double percentOutput) { rollerMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setRetractMotorPosition(double position) { retractMotor.set(ControlMode.Position, position); }

	// ----------------------------------------------------------
	// Scheduler functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}