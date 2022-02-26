package frc.robot.displays.huddisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants;
import frc.robot.displays.Display;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class AutonomousDisplay extends HUDDisplay {
    public SendableChooser<AutonomousRoutine> autoRoutineChooser = new SendableChooser<>();

	private NetworkTableEntry startDelayTimeNumberSlider;
	
	// how far to drive (inches instead of meters to help dirty American pigs like us visualize our distance estimates) to leave the tarmac
	private NetworkTableEntry tarmacLeavingDistanceNumberSlider;
    
    public AutonomousDisplay(int width, int height) {
		super(width, height);
    }

	@Override
	protected Display createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			startDelayTimeNumberSlider,

			tarmacLeavingDistanceNumberSlider
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
			autoRoutineChooser.setDefaultOption("LH PC LT", AutonomousRoutine.SCORE_LH_AND_PICKUP_CARGO_AND_LEAVE_TARMAC);
			autoRoutineChooser.addOption("LT", AutonomousRoutine.LEAVE_TARMAC);
			autoRoutineChooser.addOption("LH LT", AutonomousRoutine.SCORE_LH_AND_LEAVE_TARMAC);
			// autoRoutineChooser.addOption("LH RC LT", AutonomousRoutine.SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC);
			autonomousLayout
				.add("Routine", autoRoutineChooser)
				.withWidget(BuiltInWidgets.kComboBoxChooser);

			startDelayTimeNumberSlider = autonomousLayout
				.add("Start Delay (seconds)", Autonomous.startDelayTime)
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 0.d, "Max", 15.0d, "Block increment", 0.5d))
				.getEntry();

			tarmacLeavingDistanceNumberSlider = autonomousLayout
				.add("Leave-Tarmac Distance (inches)", Constants.metersToInches(Autonomous.tarmacLeavingDistanceMeters))
				.withWidget(BuiltInWidgets.kNumberSlider)
				.withProperties(Map.of("Min", 35, "Max", 80, "Block increment", 1))
				.getEntry();
		}
		return this;
	}

	@Override
	public Display addEntryListeners() {
		startDelayTimeNumberSlider.addListener(event -> {
			Autonomous.setStartDelayTime(event.value.getDouble());
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		tarmacLeavingDistanceNumberSlider.addListener(event -> {
			Autonomous.setTarmacLeavingDistance(Constants.inchesToMeters(event.value.getDouble()));
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		return this;
	}
}