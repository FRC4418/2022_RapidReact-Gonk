package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Public constants


	public static final double
		ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT = 0.5d,
		RETRACT_MOTOR_DEFAULT_POSITION = 90.d;

		
	// ----------------------------------------------------------
	// Private constants


	private final int
		ROLLER_CAN_ID = 11,
		RETRACT_CAN_ID = 12;


	// ----------------------------------------------------------
	// Resources


	public WPI_TalonSRX rollerMotor;
	public WPI_TalonFX retractMotor;
	

	// ----------------------------------------------------------
	// Constructor and methods
	

	public Intake() {
		rollerMotor = new WPI_TalonSRX(ROLLER_CAN_ID);
		retractMotor = new WPI_TalonFX(RETRACT_CAN_ID);

		rollerMotor.setInverted(true);
	}

	public double getRollerMotorPercent() { return rollerMotor.get(); }
	public double getRetractMotorPercent() { return retractMotor.get(); }

	public void setRollerMotorPercent(double percentOutput) { rollerMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setRetractMotorPosition(double position) { retractMotor.set(ControlMode.Position, position); }


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}