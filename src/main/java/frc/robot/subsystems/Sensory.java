package frc.robot.subsystems;


import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16448_IMU.IMUAxis;
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
		imu.setYawAxis(IMUAxis.kZ);
	}

	// rounds to two decimals
	private double getRounded(double input) {
		return Math.round(input * 100.0d) / 100.0d;
	}

	public Sensory calibrateIMU() {
		imu.calibrate();	// just filters out noise (robot must be still)
		return this;
	}

	public Sensory resetIMU() {
		imu.reset();		// zeros out current measurements (basically sets all sensor readings at current location as the "origin")
		return this;
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		SmartDashboard.putNumber("Yaw Axis", getRounded(imu.getAngle()));
		SmartDashboard.putNumber("Temperature from IMU", getRounded(imu.getTemperature()));
			
		SmartDashboard.putNumber("Filtered Accel X", getRounded(imu.getXFilteredAccelAngle()));
		SmartDashboard.putNumber("Filtered Accel Y", getRounded(imu.getYFilteredAccelAngle()));

		SmartDashboard.putNumber("Gyro X", getRounded(imu.getGyroAngleX()));
		SmartDashboard.putNumber("Gyro Y", getRounded(imu.getGyroAngleY()));
		SmartDashboard.putNumber("Gyro Z", getRounded(imu.getGyroAngleZ()));
	}
}