package frc.robot.displays.huddisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.displays.Display;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class AutonomousDisplay extends HUDDisplay {
    private SendableChooser<AutonomousRoutine> autoRoutineChooser = new SendableChooser<>();
    
    public AutonomousDisplay(int width, int height) {
		super(width, height);
    }

	@Override
	protected Display createEntriesArray() {
		
		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var autonomousLayout = hudTab
			.getLayout("Autonomous", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			autoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
			autoRoutineChooser.addOption("Drive Straight to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
			autonomousLayout
				.add("Autonomous Routine", autoRoutineChooser)
				.withWidget(BuiltInWidgets.kComboBoxChooser);
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		
		return this;
	}
}
