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

		indexerTuningPercentTextField,
		indexerFinalPercentTextField,

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

			indexerTuningPercentTextField,
			indexerFinalPercentTextField,

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
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);

			// Enable/disable motor testing
			tuningModeToggleSwitch = layout
				.add("CLICK ME! (Red = Tuning Mode)", false)
				.withWidget(BuiltInWidgets.kToggleButton)
				.getEntry();

			// Horizontal stack
			{ var hstack = layout
				.getLayout("H-stack", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// Tuning column
				{ var tuningColumn = hstack
					.getLayout("Tuning Mode", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 1, "Number of rows", 5, "Label position", "TOP"));

					launcherTuningRPMTextField = tuningColumn
						.add("Launcher RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					indexerTuningPercentTextField = tuningColumn
						.add("Indexer Percent", Constants.Manipulator.kIndexerPercent)
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
					
					indexerFinalPercentTextField = finalColumn
						.add("Indexer Percent", Constants.Manipulator.kIndexerPercent)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorFinalUpDegreeTextField = finalColumn
						.add("Retractor Up Degree", Constants.Intake.kRetractedIntakeRetractorDegree)
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
			Constants.kUsingTuningMode = event.value.getBoolean();
			if (!Constants.kUsingTuningMode) {
				m_intake.retractIntakeArm();
				m_intake.stopFeeder();
				
				m_manipulator.stopIndexer();
				m_manipulator.stopLauncher();
			}
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		
		{ // Launcher motor
			launcherTuningRPMTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_manipulator.setLauncherRPM((int) event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			launcherFinalRPMTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					Constants.Manipulator.kLauncherRPM = (int) event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Indexer motor
			indexerTuningPercentTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_manipulator.setIndexerPercent(event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			indexerFinalPercentTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					Constants.Manipulator.kIndexerPercent = event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Retractor motor
			{
				retractorTuningUpDegreeTextField.addListener(event -> {
					if (Constants.kUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalUpDegreeTextField.addListener(event -> {
					if (!Constants.kUsingTuningMode) {
						Constants.Intake.kRetractedIntakeRetractorDegree = (int) event.value.getDouble();
					}

					// update to the new angle if we're already retracted
					if (m_intake.armIsRetracted()) {
						m_intake.retractIntakeArm();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
			
			{
				retractorTuningDownDegreeTextField.addListener(event -> {
					if (Constants.kUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalDownDegreeTextField.addListener(event -> {
					if (!Constants.kUsingTuningMode) {
						Constants.Intake.kExtendedIntakeRetractorDegree = (int) event.value.getDouble();
					}

					// update to the new angle if we're already extended
					if (m_intake.armIsExtended()) {
						m_intake.extendIntakeArm();
					}
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}

		{ // Feeder motor
			feederTuningPercentTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_intake.setFeederPercent(event.value.getDouble());
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			feederFinalPercentTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					Constants.Intake.kFeederPercent = event.value.getDouble();
				}
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}