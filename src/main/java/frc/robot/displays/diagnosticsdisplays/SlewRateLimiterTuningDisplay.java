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

    private NetworkTableEntry arcadeDriveForwardLimiterNumberSlider;
	private NetworkTableEntry arcadeDriveTurnLimiterNumberSlider;

	private NetworkTableEntry tankDriveLeftForwardLimiterNumberSlider;
	private NetworkTableEntry tankDriveRightForwardLimiterNumberSlider;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public SlewRateLimiterTuningDisplay(Drivetrain drivetrain, int column, int row) {
		super(column, row);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DiagnosticsDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			arcadeDriveForwardLimiterNumberSlider,
			arcadeDriveTurnLimiterNumberSlider,

			tankDriveLeftForwardLimiterNumberSlider
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

				arcadeDriveForwardLimiterNumberSlider = arcadeDriveLayout
					.add("Forward", 1.5d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", Drivetrain.MAXIMUM_SLEW_RATE_ALLOWED, "Block increment", 0.05d))
					.getEntry();

				arcadeDriveTurnLimiterNumberSlider = arcadeDriveLayout
					.add("Turn", 1.25d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", Drivetrain.MAXIMUM_SLEW_RATE_ALLOWED, "Block increment", 0.05d))
					.getEntry();
			}

			{ var tankDriveLayout = slewRateLimiterLayout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
			
				tankDriveLeftForwardLimiterNumberSlider = tankDriveLayout
					.add("Left Forward", 1.0d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", 2.0d, "Block increment", 0.05d))
					.getEntry();

				tankDriveRightForwardLimiterNumberSlider = tankDriveLayout
					.add("Right Forward", 1.0d)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", 0.d, "Max", Drivetrain.MAXIMUM_SLEW_RATE_ALLOWED, "Block increment", 0.05d))
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public DiagnosticsDisplay addEntryListeners() {
		{	// Arcade drive
			arcadeDriveForwardLimiterNumberSlider.addListener(event -> {
				m_drivetrain.setArcadeDriveForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			arcadeDriveTurnLimiterNumberSlider.addListener(event -> {
				m_drivetrain.setArcadeDriveTurnLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{	// Tank drive
			tankDriveLeftForwardLimiterNumberSlider.addListener(event -> {
				m_drivetrain.setTankDriveLeftForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankDriveRightForwardLimiterNumberSlider.addListener(event -> {
				m_drivetrain.setTankDriveRightForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}