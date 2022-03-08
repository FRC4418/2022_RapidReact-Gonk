package frc.robot.subsystems;


import java.util.HashMap;

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
		sendCommand(SETME1);
	}

	public void setAllToSlowRGBCycle() {
		sendCommand(SETME2);
	}

	public void setAllToGreen() {
		sendCommand(SETME3);
	}

	public void turnOffAll() {
		sendCommand(SETME5);
	}

	// ----------------------------------------------------------
	// Setters for the underglow lights

	public void setUnderglowToRed() {
		sendCommand(7);
	}

	public void setUnderglowToBlue() {
		sendCommand(8);
	}

	public void turnOffUnderglow() {
		sendCommand(9);
	}

	// ----------------------------------------------------------
	// Setters for the upper lights

	public void turnOffUpper() {
		sendCommand(SETME4);
	}
}