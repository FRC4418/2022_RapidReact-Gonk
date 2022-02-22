package frc.robot.displays.huddisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.displays.Display;
import frc.robot.subsystems.Drivetrain;


public class KidsSafetyDisplay extends HUDDisplay {
	private final Drivetrain m_drivetrain;

    private NetworkTableEntry kidsSafetyModeToggleSwitch;
	private NetworkTableEntry kidsSafetyMaxOutputNumberSlider;

    public KidsSafetyDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

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
			
			kidsSafetyMaxOutputNumberSlider = kidsSafetyLayout
				.add("Max Output", Drivetrain.KidsSafetyOutputMode.DEFAULT_MAXIMUM_OUTPUT)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
				.getEntry();
		}
		return this;
	}

	int counter = 0;

	@Override
	public Display addEntryListeners() {
		kidsSafetyModeToggleSwitch.addListener(event -> {
			if (event.value.getBoolean()) {
				m_drivetrain
					.setMaxOutput(kidsSafetyMaxOutputNumberSlider.getDouble(Drivetrain.KidsSafetyOutputMode.DEFAULT_MAXIMUM_OUTPUT))
					.useKidsSafetyModeSlewRates();
			} else {
				m_drivetrain
					.useNormalMaximumOutput()
					.useNormalOutputModeSlewRates();
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		kidsSafetyMaxOutputNumberSlider.addListener(event -> {
			if (kidsSafetyModeToggleSwitch.getBoolean(false)) {
				m_drivetrain.setMaxOutput(event.value.getDouble());
				SmartDashboard.putNumber("DOING THING", counter++);
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		return this;
	}
}