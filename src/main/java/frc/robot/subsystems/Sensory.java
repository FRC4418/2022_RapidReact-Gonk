package frc.robot.subsystems;


import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Sensory extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources

	private final ADIS16448_IMU imu = new ADIS16448_IMU();

	// use .get() to get back 0 or 1 limit switch output
	private final DigitalInput limitSwitch1 = new DigitalInput(0);
	private final DigitalInput limitSwitch2 = new DigitalInput(1);

	private int counter = 0;


	// ----------------------------------------------------------
	// Constructor and methods


	public Sensory() {

	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		// Just so we know this periodic function is running
		SmartDashboard.putNumber("COUNTER", counter);

		SmartDashboard.putBoolean("Limit Switch 1", limitSwitch1.get());
		SmartDashboard.putBoolean("Limit Switch 2", limitSwitch2.get());

		SmartDashboard.putNumber("IMU Angle", imu.getAngle());

		// SmartDashboard.putNumber("Temperature from IMU", imu.getTemperature());
			
		// SmartDashboard.putNumber("Accel X", imu.getAccelX());
		// SmartDashboard.putNumber("Accel Y", imu.getAccelY());
		// SmartDashboard.putNumber("Accel Z", imu.getAccelZ());

		// SmartDashboard.putNumber("Gyro X", imu.getGyroAngleX());
		// SmartDashboard.putNumber("Gyro Y", imu.getGyroAngleY());
		// SmartDashboard.putNumber("Gyro Z", imu.getGyroAngleZ());

		counter++;
	}
}
