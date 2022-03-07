package frc.robot.subsystems;


import java.util.HashMap;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Lights extends SubsystemBase {
	// ----------------------------------------------------------
	// Private constants

	public enum Pattern {
		FRONT_UPPER_ON(5),
		BACK_UPPER_ON(6),
		UNDERGLOW_RED(7),
		UNDERGLOW_BLUE(8),
		UNDERGLOW_OFF(9),
		IDLE_ON(10),
		IDLE_OFF(11);

		private int value;
		private static HashMap<Integer, Pattern> map = new HashMap<>();

		private Pattern(int value) {
			this.value = value;
		}

		static {
			for (Pattern pattern : Pattern.values()) {
				map.put(pattern.value, pattern);
			}
		}

		public static Pattern valueOf(int pattern) {
			return (Pattern) map.get(pattern);
		}

		public int value() {
			return value;
		}
	}


	// ----------------------------------------------------------
	// Resources


	private final I2C arduino = new I2C(Port.kOnboard, Constants.Lights.I2C_DEVICE_ADDRESS);


	// ----------------------------------------------------------
	// Command-sending methods


	public void sendCommand(int command) {
		arduino.write(Constants.Lights.VALUE_REGISTER, command);
 	}
}