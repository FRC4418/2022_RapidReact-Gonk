package frc.robot.subsystems;


import java.math.BigInteger;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.shuffleboard.SendableCameraWrapper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Lights extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final int I2C_DEVICE_ADDRESS = 0x44;
	private final I2C arduino = new I2C(Port.kOnboard, I2C_DEVICE_ADDRESS);
	

	// ----------------------------------------------------------
	// Constructor


	public Lights() {
		
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		byte[] sendData = (BigInteger.valueOf(4)).toByteArray();
		// arduino.writeBulk(sendData);
		arduino.write(2, 4);
	}
}