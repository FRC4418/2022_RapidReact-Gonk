package frc.robot.displays.writedisplays.vision;


import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Vision;


public class CamerasDisplay extends VisionDisplay {
	private final Vision m_vision;

	private NetworkTableEntry
		enableFrontCenterCameraToggleSwitch,
		enableBackCenterCameraToggleSwitch;

    public CamerasDisplay(Vision vision, int width, int height) {
		super(width, height);

		m_vision = vision;
    }

	@Override
	protected VisionDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Cameras", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			// Front-center
			if (Constants.Vision.kEnableFrontCenterCamera) {
				layout
					.add(" ", Vision.frontCenterCameraServer.getSource())
					.withWidget(BuiltInWidgets.kCameraStream);
			}

			// Back-center
			if (Constants.Vision.kEnableBackCenterCamera) {
				layout
					.add(" ", Vision.backCenterCameraServer.getSource())
					.withWidget(BuiltInWidgets.kCameraStream);
			}
		}
		return this;
	}

	@Override
	public VisionDisplay addEntryListeners() {
		{ // Front-center
			if (Constants.Vision.kEnableFrontCenterCamera) {
				enableFrontCenterCameraToggleSwitch.addListener(event -> {
					m_vision.enableFrontCenterCameraStream(event.value.getBoolean());
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}

		{ // Back-center
			if (Constants.Vision.kEnableBackCenterCamera) {
				enableBackCenterCameraToggleSwitch.addListener(event -> {
					m_vision.enableBackCenterCameraStream(event.value.getBoolean());
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}
		return this;
	}
}
