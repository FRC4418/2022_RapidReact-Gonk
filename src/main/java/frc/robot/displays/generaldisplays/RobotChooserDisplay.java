package frc.robot.displays.generaldisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.RobotContainer;
import frc.robot.RobotContainer.TeamRobot;


public class RobotChooserDisplay extends GeneralDisplay {
    public SendableChooser<TeamRobot> teamRobotChooser = new SendableChooser<>();

    public RobotChooserDisplay(int width, int height) {
		super(width, height);
    }

	@Override
	protected GeneralDisplay createEntriesArray() {

		return this;
	}

	@Override
	protected GeneralDisplay createDisplayAt(int column, int row) {
		{ var robotSelectionLayout = tab
			.getLayout("Robot Chooser", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			if (RobotContainer.defaultRobot == TeamRobot.VERSACHASSIS_ONE) {
				teamRobotChooser.setDefaultOption("Versa-One", TeamRobot.VERSACHASSIS_ONE);
				teamRobotChooser.addOption("Versa-Two", TeamRobot.VERSACHASSIS_TWO);
			} else if (RobotContainer.defaultRobot == TeamRobot.VERSACHASSIS_TWO) {
				teamRobotChooser.setDefaultOption("Versa-Two", TeamRobot.VERSACHASSIS_TWO);
				teamRobotChooser.addOption("Versa-One", TeamRobot.VERSACHASSIS_ONE);
			}
			robotSelectionLayout
				.add("Sendable Chooser", teamRobotChooser)
				.withWidget(BuiltInWidgets.kSplitButtonChooser);
		}
		return this;
	}

	@Override
	public GeneralDisplay addEntryListeners() {

		return this;
	}
}
