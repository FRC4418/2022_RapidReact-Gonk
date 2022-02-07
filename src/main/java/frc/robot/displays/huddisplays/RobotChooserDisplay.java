package frc.robot.displays.huddisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.RobotContainer.TeamRobot;
import frc.robot.displays.Display;


public class RobotChooserDisplay extends HUDDisplay {
    // ----------------------------------------------------------
    // Resources

    public SendableChooser<TeamRobot> teamRobotChooser = new SendableChooser<>();

    // ----------------------------------------------------------
    // Constructor (initializes the display the same time)
    
    public RobotChooserDisplay(int column, int row) {
		super(column, row);
    }

	@Override
	protected Display createEntriesArray() {

		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var robotSelectionLayout = hudTab
			.getLayout("Robot Chooser", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(2, 1);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			teamRobotChooser.setDefaultOption("Versa-Two", TeamRobot.VERSACHASSIS_TWO);
			teamRobotChooser.addOption("Versa-One", TeamRobot.VERSACHASSIS_ONE);
			robotSelectionLayout
				.add("Sendable Chooser", teamRobotChooser)
				.withWidget(BuiltInWidgets.kSplitButtonChooser);
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {

		return this;
	}
}
