package frc.robot.displays.autonomousdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.Constants;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class PremadeAutoRoutineDisplay extends AutonomousDisplay {
	private final Autonomous m_autonomous;

    public SendableChooser<AutonomousRoutine> autoRoutineChooser = new SendableChooser<>();

	private NetworkTableEntry
		usePremadeRoutineToggleSwitch,
		startDelayTimeTextView,
		drivingMPSTextView,
		launcherFiringDurationTextView,
		// how far to drive (inches instead of meters to help dirty American pigs like us visualize our distance estimates) to leave the tarmac
		tarmacLeavingDistanceTextView;
    
    public PremadeAutoRoutineDisplay(Autonomous autonomous, int width, int height) {
		super(width, height);

		m_autonomous = autonomous;
    }

	@Override
	protected AutonomousDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			usePremadeRoutineToggleSwitch,
			startDelayTimeTextView,
			tarmacLeavingDistanceTextView
		));
		return this;
	}

	@Override
	protected AutonomousDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Autonomous", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);
			
			// Column 1
			{ var column1 = layout
				.getLayout("Column 1", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 5, "Label position", "TOP"));

				usePremadeRoutineToggleSwitch = column1
					.add("Use Premade Routine", Autonomous.usingPremadeRoutine())
					.withWidget(BuiltInWidgets.kToggleSwitch)
					.getEntry();
				
				startDelayTimeTextView = column1
					.add("Start Delay [s]", Autonomous.getStartDelaySeconds())
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				drivingMPSTextView = column1
					.addPersistent("Driving Velocity [ft/s]", Constants.metersToFeet(Autonomous.getDrivingMPS()) / 1.)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				launcherFiringDurationTextView = column1
					.addPersistent("Launcher-Firing Duration [s]", Autonomous.getLauncherFiringDurationSeconds())
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				tarmacLeavingDistanceTextView = column1
					.add("Leave-Tarmac Distance [in]", Constants.metersToInches(Autonomous.getTarmacLeavingMeters()))
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			// Column 2
			{ var column2 = layout
				.getLayout("Column 2", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"));

				// setting default options for sendable choosers also adds the label-value pair as an option
				autoRoutineChooser.setDefaultOption("Wait LH PC LH", AutonomousRoutine.WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_SCORE_LH);
				autoRoutineChooser.addOption("Wait LT", AutonomousRoutine.WAIT_AND_LEAVE_TARMAC);
				autoRoutineChooser.addOption("Wait LH LT", AutonomousRoutine.WAIT_SCORE_LH_AND_LEAVE_TARMAC);
				autoRoutineChooser.addOption("LH Wait LT", AutonomousRoutine.SCORE_LH_AND_WAIT_AND_LEAVE_TARMAC);
				// autoRoutineChooser.addOption("LH RC LT", AutonomousRoutine.SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC);
				column2
					.add("Routine", autoRoutineChooser)
					.withWidget(BuiltInWidgets.kComboBoxChooser);
			}
		}
		return this;
	}

	@Override
	public AutonomousDisplay addEntryListeners() {
		{ // Column 1
			usePremadeRoutineToggleSwitch.addListener(event -> {
				m_autonomous.setUsePremadeRoutine(event.value.getBoolean());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			startDelayTimeTextView.addListener(event -> {
				m_autonomous.setStartDelaySeconds(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			drivingMPSTextView.addListener(event -> {
				m_autonomous.setDrivingMPS(Constants.feetToMeters(event.value.getDouble()));
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			launcherFiringDurationTextView.addListener(event -> {
				m_autonomous.setLauncherFiringDurationSeconds(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			tarmacLeavingDistanceTextView.addListener(event -> {
				m_autonomous.setTarmacLeavingMeters(Constants.inchesToMeters(event.value.getDouble()));
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Column 2

		}
		return this;
	}
}