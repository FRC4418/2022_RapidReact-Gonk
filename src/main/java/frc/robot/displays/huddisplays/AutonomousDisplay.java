package frc.robot.displays.huddisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.displays.Display;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class AutonomousDisplay extends HUDDisplay {
    private SendableChooser<AutonomousRoutine> autoRoutineChooser = new SendableChooser<>();

	private NetworkTableEntry startDelayTimeNumberSlider;
	
	// how far to drive (inches instead of meters to help dirty American pigs like us visualize our distance estimates) to leave the tarmac
	private NetworkTableEntry tarmacLeavingDistanceInchesNumberSlider;
    
    public AutonomousDisplay(int width, int height) {
		super(width, height);
    }

	@Override
	protected Display createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			startDelayTimeNumberSlider,

			tarmacLeavingDistanceInchesNumberSlider
		));
		return this;
	}

	@Override
	protected Display createDisplayAt(int column, int row) {
		{ var autonomousLayout = hudTab
			.getLayout("Autonomous", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 3, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			autoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
			autoRoutineChooser.addOption("Drive Straight to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
			autonomousLayout
				.add("Routine", autoRoutineChooser)
				.withWidget(BuiltInWidgets.kComboBoxChooser);

			startDelayTimeNumberSlider = autonomousLayout
				.add("Start Delay (seconds)", 0.d)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 0.d, "Max", 15.0d, "Block increment", 0.5d))
				.getEntry();

			tarmacLeavingDistanceInchesNumberSlider = autonomousLayout
				.add("Leave-Tarmac Distance (inches)", 50)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 35, "Max", 80, "Block increment", 1))
				.getEntry();
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		
		return this;
	}
}
