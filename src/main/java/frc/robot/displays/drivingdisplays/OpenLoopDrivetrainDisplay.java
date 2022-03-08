package frc.robot.displays.drivingdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class OpenLoopDrivetrainDisplay extends DrivingDisplay {
	private final Drivetrain m_drivetrain;

    private NetworkTableEntry
		rampTimeTextView,
		maxOutputTextView;

    public OpenLoopDrivetrainDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DrivingDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			rampTimeTextView,
			maxOutputTextView
		));
		return this;
	}

	@Override
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Open-Loop", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			rampTimeTextView = layout
				.add("Ramp Time", Constants.Drivetrain.kOpenLoopRampTime)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();
			
			maxOutputTextView = layout
				.add("Max Output", Constants.Drivetrain.kMaxOutput)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();
		}
		return this;
	}

	@Override
	public DrivingDisplay addEntryListeners() {
		rampTimeTextView.addListener(event -> {
			m_drivetrain.setOpenLoopRampTimes(event.value.getDouble());
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		maxOutputTextView.addListener(event -> {
			m_drivetrain.setMaxOutput(event.value.getDouble());
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		return this;
	}
}