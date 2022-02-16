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

	private double filteredXAccelOffset = 0.d;
	private double filteredYAccelOffset = 0.d;


	// ----------------------------------------------------------
	// Constructor and methods


	public Sensory() {
		imu.setYawAxis(IMUAxis.kZ);
	}
	
	public Sensory calibrateIMU() {
		imu.calibrate();	// just filters out noise (robot must be still)
		
		filteredXAccelOffset = imu.getXFilteredAccelAngle();
		filteredYAccelOffset = imu.getYFilteredAccelAngle();
		
		return this;
	}
	
	public Sensory resetIMU() {
		imu.reset();		// zeros out current measurements (basically sets all sensor readings at current location as the "origin")
		return this;
	}

	// rounds to two decimals
	private double getRounded(double input) {
		return Math.round(input * 100.0d) / 100.0d;
	}

	private double getXFilteredAccelAngle() {
		return imu.getXFilteredAccelAngle() - filteredXAccelOffset;
	}
	
	private double getYFilteredAccelAngle() {
		return imu.getYFilteredAccelAngle() - filteredYAccelOffset;
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		SmartDashboard.putNumber("Yaw Axis", getRounded(imu.getAngle()));
		SmartDashboard.putNumber("Temperature from IMU", getRounded(imu.getTemperature()));
			
		SmartDashboard.putNumber("Filtered Accel X", getRounded(getXFilteredAccelAngle()));
		SmartDashboard.putNumber("Filtered Accel Y", getRounded(getYFilteredAccelAngle()));

		SmartDashboard.putNumber("Gyro X", getRounded(imu.getGyroAngleX()));
		SmartDashboard.putNumber("Gyro Y", getRounded(imu.getGyroAngleY()));
		SmartDashboard.putNumber("Gyro Z", getRounded(imu.getGyroAngleZ()));
	}
}