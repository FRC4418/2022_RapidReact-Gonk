package frc.robot.displays.huddisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.displays.Display;
import frc.robot.subsystems.Vision;


public class CamerasDisplay extends HUDDisplay {    
    public CamerasDisplay(int column, int row, int width, int height) {
		super(column, row, width, height);
    }

	@Override
	protected Display createEntriesArray() {
		
		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var camerasLayout = hudTab
			.getLayout("Cameras", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);
			
			camerasLayout
				.add("Front-Center", Vision.frontCenterCameraServer.getSource())
				.withWidget(BuiltInWidgets.kCameraStream);
			
			camerasLayout
				.add("Back-Center", Vision.backCenterCameraServer.getSource())
				.withWidget(BuiltInWidgets.kCameraStream);
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		
		return this;
	}
}
