package frc.robot.subsystems;


import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Lights extends SubsystemBase {
	private final int I2C_DEVICE_ADDRESS = 0x44;
	private final I2C arduino = new I2C(Port.kOnboard, I2C_DEVICE_ADDRESS);
	

	public Lights() {
		
	}

	@Override
	public void periodic() {
		arduino.write(0, 4);
	}
}
