package frc.robot.subsystems;


import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Sensory extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources
	
	
	private final ADIS16448_IMU imu = new ADIS16448_IMU(ADIS16448_IMU.IMUAxis.kZ, SPI.Port.kMXP, ADIS16448_IMU.CalibrationTime._1s);


	// ----------------------------------------------------------
	// Constructor and methods


	public Sensory() {
		imu.calibrate();
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		SmartDashboard.putNumber("IMU Angle", imu.getAngle());
		SmartDashboard.putNumber("Temperature from IMU", imu.getTemperature());
			
		SmartDashboard.putNumber("Accel X", imu.getAccelX());
		SmartDashboard.putNumber("Accel Y", imu.getAccelY());
		SmartDashboard.putNumber("Accel Z", imu.getAccelZ());

		SmartDashboard.putNumber("Gyro X", Math.round(imu.getGyroAngleX()*100.0) / 100.0);
		SmartDashboard.putNumber("Gyro Y", Math.round(imu.getGyroAngleY()*100.0) / 100.0);
		SmartDashboard.putNumber("Gyro Z", Math.round(imu.getGyroAngleZ()*100.0) / 100.0);
	}
}