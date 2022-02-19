package frc.robot.displays.huddisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

import frc.robot.displays.Display;
import frc.robot.subsystems.Vision;


public class CamerasDisplay extends HUDDisplay {
    // ----------------------------------------------------------
    // Resources

    

    // ----------------------------------------------------------
    // Constructor (initializes the display the same time)
    
    public CamerasDisplay(int column, int row) {
		super(column, row);
    }

	@Override
	protected Display createEntriesArray() {
		
		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var camerasLayout = hudTab
			.getLayout("Cameras", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(3, 4);
			
			camerasLayout.add("Front-Center", Vision.frontCenterCamera);
			camerasLayout.add("Back-Center", Vision.backCenterCamera);
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		
		return this;
	}
}
