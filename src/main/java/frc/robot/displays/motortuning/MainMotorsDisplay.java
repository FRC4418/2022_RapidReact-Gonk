package frc.robot.displays.motortuning;


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
		launcherFinalFiringRPMTextField,
		launcherFinalIdleRPMTextField,

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
	protected MainMotorsDisplay createDisplayAt(int column, int row) {
        { var layout = tab
			.getLayout("Motor Tuning", BuiltInLayouts.kGrid)
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
						.addPersistent("Launcher RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					indexerTuningPercentTextField = tuningColumn
						.addPersistent("Indexer Percent", Constants.Manipulator.kIndexerPercent)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorTuningUpDegreeTextField = tuningColumn
						.addPersistent("Retractor Up Degree", Constants.Intake.kRetractorUpDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorTuningDownDegreeTextField = tuningColumn
						.addPersistent("Retractor Down Degree", Constants.Intake.kRetractorDownDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					feederTuningPercentTextField = tuningColumn
						.addPersistent("Feeder Percent", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
				}

				// Final column
				{ var finalColumn = hstack
					.getLayout("Final Mode", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 1, "Number of rows", 6, "Label position", "TOP"));

					launcherFinalFiringRPMTextField = finalColumn
						.addPersistent("Launcher RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					launcherFinalIdleRPMTextField = finalColumn
						.addPersistent("Launcher Idle RPM", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					indexerFinalPercentTextField = finalColumn
						.addPersistent("Indexer Percent", Constants.Manipulator.kIndexerPercent)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorFinalUpDegreeTextField = finalColumn
						.addPersistent("Retractor Up Degree", Constants.Intake.kRetractorUpDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();

					retractorFinalDownDegreeTextField = finalColumn
						.addPersistent("Retractor Down Degree", Constants.Intake.kRetractorDownDegree)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
					
					feederFinalPercentTextField = finalColumn
						.addPersistent("Feeder Percent", 0)
						.withWidget(BuiltInWidgets.kTextView)
						.getEntry();
				}
			}
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		tuningModeToggleSwitch.addListener(event -> {
			Constants.kUsingTuningMode = event.value.getBoolean();
			if (!Constants.kUsingTuningMode) {
				m_intake.retractIntakeArm();
				m_intake.stopFeeder();
				
				m_manipulator.stopIndexer();
				m_manipulator.idleLauncher();
			}
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		
		{ // Launcher motor
			launcherTuningRPMTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_manipulator.setLauncherRPM((int) event.value.getDouble());
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			launcherFinalFiringRPMTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					boolean wasFiring = false;
					if (m_manipulator.launcherIsFiring()) {
						wasFiring = true;
					}
					Constants.Manipulator.kLauncherFiringRPM = (int) event.value.getDouble();
					if (wasFiring) {
						m_manipulator.runLauncher();
					}
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			launcherFinalIdleRPMTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					boolean wasIdling = false;
					if (m_manipulator.launcherIsIdling()) {
						wasIdling = true;
					}
					Constants.Manipulator.kLauncherIdleRPM = (int) event.value.getDouble();
					if (wasIdling) {
						m_manipulator.idleLauncher();
					}
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Indexer motor
			indexerTuningPercentTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_manipulator.setIndexerPercent(event.value.getDouble());
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			indexerFinalPercentTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					boolean wasRunning = false;
					if (m_manipulator.indexerIsRunning()) {
						wasRunning = true;
					}
					Constants.Manipulator.kIndexerPercent = event.value.getDouble();
					Constants.Manipulator.kReverseIndexerPercent = -Constants.Manipulator.kIndexerPercent;
					if (wasRunning) {
						m_manipulator.runIndexer();
					}
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Retractor motor
			{
				retractorTuningUpDegreeTextField.addListener(event -> {
					if (Constants.kUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalUpDegreeTextField.addListener(event -> {
					if (!Constants.kUsingTuningMode) {
						boolean wasRetracting = false;
						if (m_intake.retractorIsRetracting()) {
							wasRetracting = true;
						}
						Constants.Intake.kRetractorUpDegree = (int) event.value.getDouble();
						if (wasRetracting) {
							m_intake.retractIntakeArm();
						}
					}
				}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
			
			{
				retractorTuningDownDegreeTextField.addListener(event -> {
					if (Constants.kUsingTuningMode) {
						m_intake.setRetractorDegree((int) event.value.getDouble());
					}
				}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				retractorFinalDownDegreeTextField.addListener(event -> {
					if (!Constants.kUsingTuningMode) {
						boolean wasExtending = false;
						if (m_intake.retractorIsExtending()) {
							wasExtending = true;
						}
						Constants.Intake.kRetractorDownDegree = (int) event.value.getDouble();
						if (wasExtending) {
							m_intake.extendIntakeArm();
						}
					}
				}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
			}
		}

		{ // Feeder motor
			feederTuningPercentTextField.addListener(event -> {
				if (Constants.kUsingTuningMode) {
					m_intake.setFeederPercent(event.value.getDouble());
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			feederFinalPercentTextField.addListener(event -> {
				if (!Constants.kUsingTuningMode) {
					boolean wasRunning = false;
					if (m_intake.feederIsRunning()) {
						wasRunning = true;
					}
					Constants.Intake.kFeederPercent = event.value.getDouble();
					Constants.Intake.kReverseFeederPercent = -Constants.Intake.kFeederPercent;
					if (wasRunning) {
						m_intake.runFeeder();
					}
				}
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
	}
}