package frc.robot.displays.drivingdisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class SlewRateLimiterTuningDisplay extends DrivingDisplay {
	private final Drivetrain m_drivetrain;

	private NetworkTableEntry
		useSlewRateLimitersToggleSwitch,

    	arcadeDriveForwardLimiterTextView,
		arcadeDriveTurnLimiterTextView,

		tankDriveLeftForwardLimiterTextView,
		tankDriveRightForwardLimiterTextView;

    public SlewRateLimiterTuningDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DrivingDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			useSlewRateLimitersToggleSwitch,

			arcadeDriveForwardLimiterTextView,
			arcadeDriveTurnLimiterTextView,

			tankDriveLeftForwardLimiterTextView
		));
		return this;
	}

	@Override
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Slew Rate Limiters", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 3, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			useSlewRateLimitersToggleSwitch = layout
				.add("On-Off", Constants.Drivetrain.kDefaultUseSlewRateLimiters)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();

			{ var arcadeDriveLayout = layout
				.getLayout("Arcade Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeDriveForwardLimiterTextView = arcadeDriveLayout
					.add("Forward", Constants.Drivetrain.SlewRates.kDefaultArcadeForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				arcadeDriveTurnLimiterTextView = arcadeDriveLayout
					.add("Turn", Constants.Drivetrain.SlewRates.kDefaultArcadeTurn)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var tankDriveLayout = layout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
			
				tankDriveLeftForwardLimiterTextView = tankDriveLayout
					.add("Left Forward", Constants.Drivetrain.SlewRates.kDefaultTankForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				tankDriveRightForwardLimiterTextView = tankDriveLayout
					.add("Right Forward", Constants.Drivetrain.SlewRates.kDefaultTankForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public DrivingDisplay addEntryListeners() {
		useSlewRateLimitersToggleSwitch.addListener(event -> {
			m_drivetrain.setUseSlewRateLimiters(event.value.getBoolean());
		}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		{	// Arcade drive
			arcadeDriveForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeDriveForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			arcadeDriveTurnLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeDriveTurnLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{	// Tank drive
			tankDriveLeftForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankDriveLeftForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankDriveRightForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankDriveRightForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}