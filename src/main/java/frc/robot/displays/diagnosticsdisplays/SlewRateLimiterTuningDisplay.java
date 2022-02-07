package frc.robot.displays.diagnosticsdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.subsystems.Drivetrain;


public class SlewRateLimiterTuningDisplay extends DiagnosticsDisplay {
    // ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

    private NetworkTableEntry arcadeDriveForwardLimiterTextField;
	private NetworkTableEntry arcadeDriveTurnLimiterTextField;

	private NetworkTableEntry tankDriveForwardLimiterTextField;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public SlewRateLimiterTuningDisplay(Drivetrain drivetrain, int column, int row) {
		super(column, row);

		m_drivetrain = drivetrain;

		this.column = column;
		this.row = row;
    }

	@Override
	protected DiagnosticsDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			arcadeDriveForwardLimiterTextField,
			arcadeDriveTurnLimiterTextField,

			tankDriveForwardLimiterTextField
		));
		return this;
	}

	@Override
	protected DiagnosticsDisplay createDisplayAt(int column, int row) {
		{ var slewRateLimiterLayout = diagnosticsTab
			.getLayout("Slew Rate Limiters", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(2, 3);

			{ var arcadeDriveLayout = slewRateLimiterLayout
				.getLayout("Arcade Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeDriveForwardLimiterTextField = arcadeDriveLayout
					.add("Forward", 1.5d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", 2.0d, "Block increment", 0.05d))
					.getEntry();

				arcadeDriveTurnLimiterTextField = arcadeDriveLayout
					.add("Turn", 1.25d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", 2.0d, "Block increment", 0.05d))
					.getEntry();
			}

			{ var tankDriveLayout = slewRateLimiterLayout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"));
			
				tankDriveForwardLimiterTextField = tankDriveLayout
					.add("Forward", 1.0d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", 2.0d, "Block increment", 0.05d))
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public DiagnosticsDisplay addEntryListeners() {
		{	// Arcade drive
			arcadeDriveForwardLimiterTextField.addListener(event -> {
				m_drivetrain.setArcadeDriveForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			arcadeDriveTurnLimiterTextField.addListener(event -> {
				m_drivetrain.setArcadeDriveTurnLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{	// Tank drive
			tankDriveForwardLimiterTextField.addListener(event -> {
				m_drivetrain.setTankDriveForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}