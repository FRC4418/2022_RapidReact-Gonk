package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants
	
	private final int MOTOR_ID = -1;

	// ----------------------------------------------------------
	// Subsystem resources

	private WPI_TalonSRX climbMotor;
	private Encoder climbEncoder;

	// ----------------------------------------------------------
	// Constructor and actions

	public Climber() {
		climbMotor = new WPI_TalonSRX(MOTOR_ID);
	}

	public void setClimbMotor(double motorValue) {
		climbMotor.set(ControlMode.PercentOutput, motorValue);
	}

	public double getDistanceTraveled() {
		return climbEncoder.getDistance();
	}

	public Encoder getClimbEncoder() {
		return climbEncoder;
	}

	// ----------------------------------------------------------
	// Cycle functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}