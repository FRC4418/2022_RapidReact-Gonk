package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants

	// TODO: Set acutal intake motors to have these IDs
	public static final int
		MOTOR_0_ID = 21,
		MOTOR_1_ID = 22,
		MOTOR_2_ID = 23;

	public static final double
		PERCENT_OUTPUT = 0.5d;

	// ----------------------------------------------------------
	// Subsystem resources

	// TODO: Change intake motor names after learning of functionality
	public WPI_TalonSRX motor0;
	public WPI_TalonSRX motor1;
	public WPI_TalonSRX motor2;

	public NetworkTableEntry intakePercentOutputTextField;
	
	// ----------------------------------------------------------
	// Constructor and actions
	
	public Intake() {
		motor0 = new WPI_TalonSRX(MOTOR_0_ID);
		motor1 = new WPI_TalonSRX(MOTOR_1_ID);
		motor2 = new WPI_TalonSRX(MOTOR_2_ID);
	}

	public double getMotor0() { return motor0.get(); }
	public double getMotor1() { return motor1.get(); }
	public double getMotor2() { return motor2.get(); }

	public void setMotor0(double percentOutput) { motor0.set(ControlMode.PercentOutput, percentOutput); }
	public void setMotor1(double percentOutput) { motor1.set(ControlMode.PercentOutput, percentOutput); }
	public void setMotor2(double percentOutput) { motor2.set(ControlMode.PercentOutput, percentOutput); }

	// ----------------------------------------------------------
	// Cycle functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}