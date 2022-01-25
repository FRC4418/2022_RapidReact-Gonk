package frc.robot.subsystems;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Climber extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants
	
	private final int MOTOR_CAN_ID = -1;

	// ----------------------------------------------------------
	// Resources

	private WPI_TalonSRX climbMotor;
	private Encoder climbEncoder;

	// ----------------------------------------------------------
	// Constructor and actions

	public Climber() {
		climbMotor = new WPI_TalonSRX(MOTOR_CAN_ID);
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
	// Scheduler functions

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}