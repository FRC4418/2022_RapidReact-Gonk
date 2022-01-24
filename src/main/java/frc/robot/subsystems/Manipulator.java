package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
//import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// import frc.robot.Robot;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Constants

	public final int
		LOWER_MOTOR_ID = 5,
		HIGHER_MOTOR_ID = 7;

	public final double
		DEFAULT_LOWER_MOTOR_PERCENT = 0.3,
		DEFAULT_HIGHER_MOTOR_PERCENT = 0.3;

	// High-goal shooter
	public final int
		MOTOR_775_ID = 11;
	
	public final double
		TARGET_PERCENTAGE = 0.8; // high shooter, RPMs are changed to units/100ms in motor commands
	
	// ----------------------------------------------------------
	// Subsystem resources

	public boolean inTuningMode;
	
	private WPI_TalonSRX lowerConveyorMotor;
	private WPI_TalonSRX higherConveyorMotor;
	

	public NetworkTableEntry highShooterPercentageTextField;

	/* Encoder.getRate() returns distance per second
	distance per second * distance per pulse = pulse per second
	pulse per second * decoding factor = degrees per second
	degrees per second / 360 degrees = revolutions per second
	revolutions per second * 60 seconds = revolutions per minute (RPM) */
	// private static double distPerSecToRPM = 
	//   Constants.DRIVE_ENCODER_DISTANCE_PER_PULSE
	//   * (double) Constants.DRIVE_ENCODER_DECODING_SCALE_FACTOR 
	//   / 60.0;

	// ----------------------------------------------------------
	// Constructor and actions

	public Manipulator() {
		lowerConveyorMotor = new WPI_TalonSRX(LOWER_MOTOR_ID);
		higherConveyorMotor = new WPI_TalonSRX(HIGHER_MOTOR_ID);

		higherConveyorMotor.setInverted(true);
	}
	
	public double getLowMotor() { return lowerConveyorMotor.get(); }
	public double getHighMotor() { return higherConveyorMotor.get(); }

	public void setLowMotor(double percentOutput) { lowerConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }
	public void setHighMotor(double percentOutput) { higherConveyorMotor.set(ControlMode.PercentOutput, percentOutput); }

	// ----------------------------------------------------------
	// Cycle functions

	@Override
	public void periodic() {
		inTuningMode = true;
	}
}