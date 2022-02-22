package frc.robot.displays.diagnosticsdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class MotorTestingDisplay extends DiagnosticsDisplay {
	private final Intake m_intake;
	private final Manipulator m_manipulator;

    private NetworkTableEntry motorTestingModeToggleSwitch;

	private NetworkTableEntry indexerToggleSwitch;
	private NetworkTableEntry indexerOutputPercentNumberSlider;

	private NetworkTableEntry launcherToggleSwitch;
	private NetworkTableEntry launcherOutputPercentNumberSlider;

	private NetworkTableEntry feederToggleSwitch;
	private NetworkTableEntry feederOutputPercentNumberSlider;

	private NetworkTableEntry retractorToggleSwitch;
	private NetworkTableEntry retractorPositionNumberSlider;

    public MotorTestingDisplay(Intake intake, Manipulator manipulator, int column, int row, int width, int height) {
		super(column, row, width, height);

		m_intake = intake;
		m_manipulator = manipulator;
    }

	@Override
	protected DiagnosticsDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			indexerToggleSwitch,
			indexerOutputPercentNumberSlider,

			launcherToggleSwitch,
			launcherOutputPercentNumberSlider,

			feederToggleSwitch,
			feederOutputPercentNumberSlider,

			retractorToggleSwitch,
			retractorPositionNumberSlider
		));
		return this;
	}

	@Override
	protected DiagnosticsDisplay createDisplayAt(int column, int row) {
        { var motorTestingLayout = diagnosticsTab
			.getLayout("Motor Testing", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);

			// Enable/disable motor testing
			motorTestingModeToggleSwitch = motorTestingLayout
				.add("CLICK ME! Red = enabled", false)
				.withWidget(BuiltInWidgets.kToggleButton)
				.getEntry();

			// put into the 2nd slot of motorTestingLayout's vertical stack
			{ var horizontalStack = motorTestingLayout
				.getLayout("Horizontal Stack", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// ----------------------------------------------------------
				// Testing the intake motors

				{ var intakeLayout = horizontalStack
					.getLayout("Intake", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					// Intake retractor motor

					{ var retractorLayout = intakeLayout
						.getLayout("Retractor", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						// I have no fucking clue why the textView entry is always added before (to the 2nd row) the toggleSwitch but it's good enough
						retractorToggleSwitch = retractorLayout
							.add("On-Off", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						
						retractorPositionNumberSlider = retractorLayout
							.add("Position", Intake.DEFAULT_RETRACTOR_POSITION)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();
					}

					// intake feeder motor
					
					{ var feederLayout = intakeLayout
						.getLayout("Feeder", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						feederToggleSwitch = feederLayout
							.add("On-Off", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						
						feederOutputPercentNumberSlider = feederLayout
							.add("Percentage", Intake.DEFAULT_FEEDER_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();
					}
				}

				// ----------------------------------------------------------
				// Testing the conveyor-shooter motors

				{ var manipulatorLayout = horizontalStack
					.getLayout("Manipulator", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					{ var indexerLayout = manipulatorLayout
						.getLayout("Indexer", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						indexerToggleSwitch = indexerLayout
							.add("On-Off", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();

						indexerOutputPercentNumberSlider = indexerLayout
							.add("Percentage", Manipulator.DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();
					}

					// Manipulator launcher motor
					
					{ var launcherLayout = manipulatorLayout
						.getLayout("Launcher", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						launcherToggleSwitch = launcherLayout
							.add("On-Off", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();

						launcherOutputPercentNumberSlider = launcherLayout
							.add("Percentage", Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();
					}
				}
			}
		}
		return this;
	}

	@Override
	public MotorTestingDisplay addEntryListeners() {
		motorTestingModeToggleSwitch.addListener(event -> {
			// means if the toggle switch's boolean is false (AKA disabled)
			if (!event.value.getBoolean()) {
				m_intake.retractIntakeArm();
				m_intake.stopFeeder();

				m_manipulator.stopIndexer();
				m_manipulator.stopLauncher();
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		
		{ // Intake
			{ // Retractor motor
				retractorToggleSwitch.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& !event.value.getBoolean()) {
						m_intake.retractIntakeArm();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
				retractorPositionNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& retractorToggleSwitch.getBoolean(false)) {
						m_intake.setRetractMotorPosition(event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
			{ // Feeder motor
				feederToggleSwitch.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& !event.value.getBoolean()) {
						m_intake.stopFeeder();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				feederOutputPercentNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& feederToggleSwitch.getBoolean(false)) {
						m_intake.setFeederMotorPercent(event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}
		
		{ // Manipulator
			{ // Indexer motor
				indexerToggleSwitch.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& !event.value.getBoolean()) {
						m_manipulator.stopIndexer();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				indexerOutputPercentNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& indexerToggleSwitch.getBoolean(false)
					&& indexerToggleSwitch.getBoolean(false)) {
						m_manipulator.setIndexerToPercent(event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
			{ // Launcher motor
				launcherToggleSwitch.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& !event.value.getBoolean()) {
						m_manipulator.stopLauncher();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				launcherOutputPercentNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& launcherToggleSwitch.getBoolean(false)) {
						m_manipulator.setLauncherToPercent(event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}
		return this;
	}
}
