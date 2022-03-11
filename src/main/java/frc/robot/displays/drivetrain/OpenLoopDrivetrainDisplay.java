package frc.robot.displays.drivetrain;


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
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Open-Loop", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			rampTimeTextView = layout
				.addPersistent("Ramp Time", Constants.Drivetrain.kTeleopOpenLoopRampTime)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();
			
			maxOutputTextView = layout
				.addPersistent("Max Output", Constants.Drivetrain.kMaxOutput)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		rampTimeTextView.addListener(event -> {
			m_drivetrain.updateTeleopOpenLoopRampTime(event.value.getDouble());
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		maxOutputTextView.addListener(event -> {
			m_drivetrain.setMaxOutput(event.value.getDouble());
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	}
}