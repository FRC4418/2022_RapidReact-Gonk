package frc.robot.subsystems;


// import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Sensory extends SubsystemBase {
	// private static final ADIS16448_IMU imu = new ADIS1644s8_IMU();
	DigitalInput limitSwitch = new DigitalInput(0);
	DigitalInput limitSwitch2 = new DigitalInput(1);

	Counter counter = new Counter(limitSwitch);

	
	public Sensory() {

	}

	@Override
	public void periodic() {
		SmartDashboard.putBoolean("Limit switch value", limitSwitch.get());
		SmartDashboard.putBoolean("Limit switch value2", limitSwitch2.get());
	}
}
