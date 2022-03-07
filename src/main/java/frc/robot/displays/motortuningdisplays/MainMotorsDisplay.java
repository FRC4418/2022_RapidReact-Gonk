package frc.robot.displays.motortuningdisplays;


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


public class MainMotorsDisplay extends MotorTuningDisplay {
	private final Intake m_intake;
	private final Manipulator m_manipulator;

    private NetworkTableEntry
		// ex. when tuning mode is enabled, retractorTuningDegreesTextField controls the retractor. When disabled, retractorFinalDegreesTextField controls the retractor
		tuningModeToggleSwitch,

		launcherTuningRPMTextField,
		launcherFinalRPMTextField,

		indexerTuningRPMTextField,
		indexerFinalRPMTextField,

		retractorTuningUpDegreeTextField,
		retractorFinalUpDegreeTextField,

		retractorTuningDownDegreeTextField,
		retractorFinalDownDegreeTextField,

		feederTuningPercentTextField,
		feederFinalPercentTextField;

    public MainMotorsDisplay(Intake intake, Manipulator manipulator, int width, int height) {
		super(width, height);

		m_intake = intake;
		m_manipulator = manipulator;
    }

	@Override
	protected MotorTuningDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			tuningModeToggleSwitch,

			launcherTuningRPMTextField,
			launcherFinalRPMTextField,

			indexerTuningRPMTextField,
			indexerFinalRPMTextField,

			retractorTuningUpDegreeTextField,
			retractorFinalUpDegreeTextField,

			feederTuningPercentTextField,
			feederFinalPercentTextField
		));
		return this;
	}

	@Override
	protected MotorTuningDisplay createDisplayAt(int column, int row) {
        { var layout = tab
			.getLayout("Motor Testing", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			// Enable/disable motor testing
			tuningModeToggleSwitch = layout
				.add("CLICK ME! Red = enabled", false)
				.withWidget(BuiltInWidgets.kToggleButton)
				.getEntry();

			// Horizontal stack
			{ var hstack = layout
				.getLayout(" ", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// Tuning column
				{ var tuningColumn = hstack
					.getLayout("Tuning Mode", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 1, "Number of rows", 5, "Label position", "TOP"));

					launcherTuningRPMTextField = tuningColumn
						.add("Launcher RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					indexerTuningRPMTextField = tuningColumn
						.add("Indexer RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorTuningUpDegreeTextField = tuningColumn
						.add("Retractor Up Degree", Constants.Intake.kRetractedIntakeRetractorDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorTuningDownDegreeTextField = tuningColumn
						.add("Retractor Down Degree", Constants.Intake.kExtendedIntakeRetractorDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					feederTuningPercentTextField = tuningColumn
						.add("Feeder Percent", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
				}

				// Final column
				{ var finalColumn = hstack
					.getLayout("Final Mode", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 1, "Number of rows", 5, "Label position", "TOP"));

					launcherFinalRPMTextField = finalColumn
						.add("Launcher RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					indexerFinalRPMTextField = finalColumn
						.add("Indexer RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorFinalUpDegreeTextField = finalColumn
						.add("Retractor Degree", Constants.Intake.kRetractedIntakeRetractorDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorFinalDownDegreeTextField = finalColumn
						.add("Retractor Down Degree", Constants.Intake.kExtendedIntakeRetractorDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					feederFinalPercentTextField = finalColumn
						.add("Feeder Percent", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
				}
			}
		}
		return this;
	}

	@Override
	public MainMotorsDisplay addEntryListeners() {
		tuningModeToggleSwitch.addListener(event -> {
			Constants.kDefaultUsingTuningMode = event.value.getBoolean();
			if (!Constants.kDefaultUsingTuningMode) {
				m_intake.retractIntakeArm();
				m_intake.stopFeeder();
				
				m_manipulator.stopIndexer();
				m_manipulator.stopLauncher();
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		
		{ // Launcher motor
			launcherTuningRPMTextField.addListener(event -> {
				if (Constants.kDefaultUsingTuningMode) {
					m_manipulator.setLauncherRPM((int) event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			launcherFinalRPMTextField.addListener(event -> {
				if (!Constants.kDefaultUsingTuningMode) {
					Constants.Manipulator.kDefaultLauncherRPM = (int) event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Indexer motor
			indexerTuningRPMTextField.addListener(event -> {
				if (Constants.kDefaultUsingTuningMode) {
					m_manipulator.setIndexerRPM((int) event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			indexerFinalRPMTextField.addListener(event -> {
				if (!Constants.kDefaultUsingTuningMode) {
					Constants.Manipulator.kDefaultIndexerRPM = (int) event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Retractor motor
			{
				retractorTuningUpDegreeTextField.addListener(event -> {
					if (Constants.kDefaultUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalUpDegreeTextField.addListener(event -> {
					if (!Constants.kDefaultUsingTuningMode) {
						Constants.Intake.kDefaultRetractedIntakeRetractorDegree = (int) event.value.getDouble();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
			
			{
				retractorTuningDownDegreeTextField.addListener(event -> {
					if (Constants.kDefaultUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalDownDegreeTextField.addListener(event -> {
					if (!Constants.kDefaultUsingTuningMode) {
						Constants.Intake.kDefaultExtendedIntakeRetractorDegree = (int) event.value.getDouble();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}

		{ // Feeder motor
			feederTuningPercentTextField.addListener(event -> {
				if (Constants.kDefaultUsingTuningMode) {
					m_intake.setFeederPercent(event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			feederFinalPercentTextField.addListener(event -> {
				if (!Constants.kDefaultUsingTuningMode) {
					Constants.Intake.kDefaultFeederPercent = event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}