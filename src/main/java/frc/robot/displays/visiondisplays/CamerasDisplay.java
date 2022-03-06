package frc.robot.displays.visiondisplays;


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
	protected VisionDisplay createEntriesArray() {
		
		return this;
	}

	@Override
	protected VisionDisplay createDisplayAt(int column, int row) {
		{ var camerasLayout = tab
			.getLayout("Cameras", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);
			
			// Front-center
			{ var frontCenterCameraLayout = camerasLayout
				.getLayout("Front-Center", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				enableFrontCenterCameraToggleSwitch = frontCenterCameraLayout
					.add("On-Off", Constants.Vision.kDefaultEnableFrontCenterCamera)
					.withWidget(BuiltInWidgets.kToggleSwitch)
					.getEntry();

				frontCenterCameraLayout
					.add(" ", Vision.frontCenterCameraServer.getSource())
					.withWidget(BuiltInWidgets.kCameraStream);
			}

			// Back-center
			{ var backCenterCameraLayout = camerasLayout
				.getLayout("Back-Center", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

				enableBackCenterCameraToggleSwitch = backCenterCameraLayout
					.add("On-Off", Constants.Vision.kDefaultEnableBackCenterCamera)
					.withWidget(BuiltInWidgets.kToggleSwitch)
					.getEntry();

				backCenterCameraLayout
					.add(" ", Vision.backCenterCameraServer.getSource())
					.withWidget(BuiltInWidgets.kCameraStream);
			}
		}
		return this;
	}

	@Override
	public VisionDisplay addEntryListeners() {
		{ // Front-center
			enableFrontCenterCameraToggleSwitch.addListener(event -> {
				m_vision.enableFrontCenterCameraStream(event.value.getBoolean());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Back-center
			enableBackCenterCameraToggleSwitch.addListener(event -> {
				m_vision.enableBackCenterCameraStream(event.value.getBoolean());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}
