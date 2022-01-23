// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



// import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensory extends SubsystemBase {
	// private static final ADIS16448_IMU imu = new ADIS1644s8_IMU();
	DigitalInput limitSwitch = new DigitalInput(0);
	DigitalInput limitSwitch2 = new DigitalInput(1);

	Counter counter = new Counter(limitSwitch);

	
	/** Creates a new SensorsSubsystem. */
	public Sensory() {
		//Very basic while loop that will run until the switch is pressed.
		//This is probably impractical because it does not do anything with these values.
		//But because we do not have the motors for the limit switches programed we will need to wait. 
		 while (limitSwitch.get()) {
         Timer.delay(10);
      }
	  while (limitSwitch2.get()) {
		Timer.delay(10);
	 }
	}

	@Override
	public void periodic() {
		SmartDashboard.putBoolean("Limit switch value", limitSwitch.get());
		SmartDashboard.putBoolean("Limit switch value", limitSwitch2.get());
	}
}
