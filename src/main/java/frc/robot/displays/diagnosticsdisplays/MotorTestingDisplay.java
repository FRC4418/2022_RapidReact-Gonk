package frc.robot.displays.diagnosticsdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class MotorTestingDisplay extends DiagnosticsDisplay {
	private final Intake m_intake;
	private final Manipulator m_manipulator;

    private NetworkTableEntry motorTestingModeToggleSwitch;

	private NetworkTableEntry indexerToggleSwitch;
	private NetworkTableEntry indexerRPMNumberSlider;

	private NetworkTableEntry launcherToggleSwitch;
	private NetworkTableEntry launcherRPMNumberSlider;

	private NetworkTableEntry feederToggleSwitch;
	private NetworkTableEntry feederPercentNumberSlider;

	private NetworkTableEntry retractorToggleSwitch;
	private NetworkTableEntry retractorDegreesNumberSlider;

    public MotorTestingDisplay(Intake intake, Manipulator manipulator, int width, int height) {
		super(width, height);

		m_intake = intake;
		m_manipulator = manipulator;
    }

	@Override
	protected DiagnosticsDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			indexerToggleSwitch,
			indexerRPMNumberSlider,

			launcherToggleSwitch,
			launcherRPMNumberSlider,

			feederToggleSwitch,
			feederPercentNumberSlider,

			retractorToggleSwitch,
			retractorDegreesNumberSlider
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
						
						retractorDegreesNumberSlider = retractorLayout
							.add("Degrees", Constants.Intake.kExtendedIntakeRetractorTicks * Constants.Falcon500.kDegreesToTicks)
							.withWidget(BuiltInWidgets.kTextView)
							// .withProperties(Map.of("Min", -360., "Max", 360., "Block increment", 0.5))
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
						
						feederPercentNumberSlider = feederLayout
							.add("Percentage", Constants.Intake.kDefaultFeederPercent)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", -1., "Max", 1., "Block increment", 0.05))
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

						indexerRPMNumberSlider = indexerLayout
							.add("RPM", Constants.Manipulator.kDefaultIndexerRPM)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", -Constants.Falcon500.kMaxRPM, "Max", Constants.Falcon500.kMaxRPM, "Block increment", 50))
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

						launcherRPMNumberSlider = launcherLayout
							.add("RPM", Constants.Manipulator.kDefaultLauncherRPM)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", -Constants.Falcon500.kMaxRPM, "Max", Constants.Falcon500.kMaxRPM, "Block increment", 50))
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
	
				retractorDegreesNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& retractorToggleSwitch.getBoolean(false)) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
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

				feederPercentNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& feederToggleSwitch.getBoolean(false)) {
						m_intake.setFeederPercent(event.value.getDouble());
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

				indexerRPMNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& indexerToggleSwitch.getBoolean(false)
					&& indexerToggleSwitch.getBoolean(false)) {
						m_manipulator.setIndexerRPM((int) event.value.getDouble());
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

				launcherRPMNumberSlider.addListener(event -> {
					if (motorTestingModeToggleSwitch.getBoolean(false)
					&& launcherToggleSwitch.getBoolean(false)) {
						m_manipulator.setLauncherRPM((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}
		return this;
	}
}
