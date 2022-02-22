package frc.robot.displays.diagnosticsdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.subsystems.Drivetrain;


public class DrivetrainOpenLoopRampTimeDisplay extends DiagnosticsDisplay {
	private final Drivetrain m_drivetrain;

    private NetworkTableEntry rampTimeNumberSlider;

    public DrivetrainOpenLoopRampTimeDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DiagnosticsDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			rampTimeNumberSlider
		));
		return this;
	}

	@Override
	protected DiagnosticsDisplay createDisplayAt(int column, int row) {
		{ var drivetrainOpenLoopRampTimeLayout = diagnosticsTab
			.getLayout("Drivetrain Open-Loop", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(2, 1);

			rampTimeNumberSlider = drivetrainOpenLoopRampTimeLayout
				.add("Ramp Time", Drivetrain.JOYSTICK_DRIVING_OPEN_LOOP_TIME)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 0.d, "Max", 5.0d, "Block increment", 0.05d))
				.getEntry();
		}
		return this;
	}

	@Override
	public DiagnosticsDisplay addEntryListeners() {
		rampTimeNumberSlider.addListener(event -> {
			m_drivetrain.setOpenLoopRampTimes(event.value.getDouble());
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		return this;
	}
}