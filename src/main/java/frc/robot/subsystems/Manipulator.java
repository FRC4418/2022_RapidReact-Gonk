package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
//import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


// import frc.robot.Robot;


public class Manipulator extends SubsystemBase {
	// ----------------------------------------------------------
	// Private constants


	private final int
		INDEXER_CAN_ID = 21,
		LAUNCHER_CAN_ID = 22;


	// ----------------------------------------------------------
	// Public constants


	public static final double
		LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT = 0.3,
		HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT = 0.3;
	

	// ----------------------------------------------------------
	// Resources
	

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
	// Constructor and methods


	public Manipulator() {
		lowerConveyorMotor = new WPI_TalonSRX(INDEXER_CAN_ID);
		higherConveyorMotor = new WPI_TalonSRX(LAUNCHER_CAN_ID);

		higherConveyorMotor.setInverted(true);
	}
	
	public double getLowMotor() { return lowerConveyorMotor.get(); }
	public double getHighMotor() { return higherConveyorMotor.get(); }

	public Manipulator setLowMotorPercent(double percentOutput) {
		lowerConveyorMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}
	public Manipulator setHighMotorPercent(double percentOutput) {
		higherConveyorMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods

	
	@Override
	public void periodic() {
		
	}
}