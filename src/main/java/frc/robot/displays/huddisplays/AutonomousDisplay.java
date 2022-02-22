package frc.robot.displays.huddisplays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.displays.Display;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class AutonomousDisplay extends HUDDisplay {
    // ----------------------------------------------------------
    // Resources

    private SendableChooser<AutonomousRoutine> autoRoutineChooser = new SendableChooser<>();

    // ----------------------------------------------------------
    // Constructor (initializes the display the same time)
    
    public AutonomousDisplay(int column, int row) {
		super(column, row);
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
			.withSize(2, 1);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			autoRoutineChooser.setDefaultOption("LH PC LT", AutonomousRoutine.SCORE_LH_AND_PICKUP_CARGO_AND_LEAVE_TARMAC);
			autoRoutineChooser.addOption("LT", AutonomousRoutine.LEAVE_TARMAC);
			autoRoutineChooser.addOption("LH LT", AutonomousRoutine.SCORE_LH_AND_LEAVE_TARMAC);
			autoRoutineChooser.addOption("LH RC LT", AutonomousRoutine.SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC);
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
