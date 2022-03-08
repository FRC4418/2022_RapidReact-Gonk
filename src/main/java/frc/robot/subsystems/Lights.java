package frc.robot.subsystems;


import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Lights extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final I2C arduino = new I2C(Port.kOnboard, Constants.Lights.I2C_DEVICE_ADDRESS);


	// ----------------------------------------------------------
	// Command-sending methods


	private void sendCommand(int command) {
		arduino.write(Constants.Lights.VALUE_REGISTER, command);
	}

	// ----------------------------------------------------------
	// Setters for all lights

	public void setAllToFastRGBCycle() {
		sendCommand(5);
	}

	public void setAllToSlowRGBCycle() {
		sendCommand(6);
	}

	public void setAllToGreen() {
		sendCommand(7);
	}

	public void turnOffAll() {
		sendCommand(8);
	}

	// ----------------------------------------------------------
	// Setters for the underglow lights

	public void setUnderglowToRed() {
		sendCommand(9);
	}

	public void setUnderglowToBlue() {
		sendCommand(9);
	}

	public void turnOffUnderglow() {
		sendCommand(10);
	}

	// ----------------------------------------------------------
	// Setters for the upper lights

	public void turnOffUpper() {
		sendCommand(11);
	}
}