package frc.robot.subsystems;


import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Lights extends SubsystemBase {
	// ----------------------------------------------------------
	// Private constants


	private final int I2C_DEVICE_ADDRESS = 0x44;

	private final byte VALUE_REGISTER = 0x0;

	private final byte
		STARTUP_EFFECT = 0x5,
		BLUE = 0x4,
		GREEN = 0x3,
		SECOND_HALF_IS_FORWARD_COLOR = 0x2,
		FIRST_HALF_IS_FORWARD_COLOR = 0x1;


	// ----------------------------------------------------------
	// Resources


	private final I2C arduino = new I2C(Port.kOnboard, I2C_DEVICE_ADDRESS);
	

	// ----------------------------------------------------------
	// Constructor


	public Lights() {
		sendCommand(VALUE_REGISTER, BLUE);
	}
	
	
	// ----------------------------------------------------------
	// Scheduler methods
	
	
	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Command-sending methods


	private void sendCommand(int register, byte command) {
		arduino.write(register, command);

		// one byte for the register address, one byte for the integer-value command
		ByteBuffer data = ByteBuffer.allocateDirect(2);
		data.put(VALUE_REGISTER);
		data.put(command);

		arduino.writeBulk(data, 2);
 	}

	// private void sendSingleCommands(int register, int... commands) {
	// 	for (int iii = 0; iii < commands.length; iii++) {
	// 		sendCommand(register, (byte) commands[iii]);
	// 	}
	// }

	// private void sendBulkCommand(byte register, byte... commands) {
	// 	ByteBuffer data = ByteBuffer.allocateDirect(commands.length);

	// 	data.put(register);
	// 	for (int iii = 0; iii < commands.length; iii++) {
	// 		data.put(commands[iii]);
	// 	}

	// 	arduino.writeBulk(data, commands.length);
	// }
}