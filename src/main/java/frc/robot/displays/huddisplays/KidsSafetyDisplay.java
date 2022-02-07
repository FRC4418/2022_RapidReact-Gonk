package frc.robot.displays.huddisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.displays.Display;
import frc.robot.subsystems.Drivetrain;


public class KidsSafetyDisplay extends HUDDisplay {
    // ----------------------------------------------------------
    // Resources

	private final Drivetrain m_drivetrain;

    private NetworkTableEntry kidsSafetyModeToggleSwitch;
	private NetworkTableEntry kidsSafetyMaxOutputTextField;

    // ----------------------------------------------------------
    // Constructor (initializes the display the same time)
    
    public KidsSafetyDisplay(Drivetrain drivetrain, int column, int row) {
		super(column, row);

		m_drivetrain = drivetrain;
    }

	@Override
	protected Display createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			kidsSafetyModeToggleSwitch
		));
		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var kidsSafetyLayout = hudTab
			.getLayout("Kid's Safety", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(2, 2);
			
			kidsSafetyModeToggleSwitch = kidsSafetyLayout
				.add("On-Off", false)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();
			
			kidsSafetyMaxOutputTextField = kidsSafetyLayout
				.add("Max Output", Drivetrain.DEFAULT_KIDS_SAFETY_MAXIMUM_OUTPUT)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
				.getEntry();
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		kidsSafetyModeToggleSwitch.addListener(event -> {
			if (event.value.getBoolean()) {
				m_drivetrain.setMaximumOutput(kidsSafetyMaxOutputTextField.getDouble(Drivetrain.DEFAULT_KIDS_SAFETY_MAXIMUM_OUTPUT));
			} else {
				m_drivetrain.useNormalMaximumOutput();
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		kidsSafetyMaxOutputTextField.addListener(event -> {
			if (kidsSafetyModeToggleSwitch.getBoolean(false)) {
				m_drivetrain.setMaximumOutput(event.value.getDouble());
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		return this;
	}
}
