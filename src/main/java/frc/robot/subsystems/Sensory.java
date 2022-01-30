package frc.robot.subsystems;


// import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Sensory extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources

	
	// private final ADIS16448_IMU imu = new ADIS16448_IMU();


	// ----------------------------------------------------------
	// Constructor and methods


	public Sensory() {

	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		// SmartDashboard.putNumber("IMU Angle", imu.getAngle());

		// SmartDashboard.putNumber("Temperature from IMU", imu.getTemperature());
			
		// SmartDashboard.putNumber("Accel X", imu.getAccelX());
		// SmartDashboard.putNumber("Accel Y", imu.getAccelY());
		// SmartDashboard.putNumber("Accel Z", imu.getAccelZ());

		// SmartDashboard.putNumber("Gyro X", imu.getGyroAngleX());
		// SmartDashboard.putNumber("Gyro Y", imu.getGyroAngleY());
		// SmartDashboard.putNumber("Gyro Z", imu.getGyroAngleZ());
	}
}
