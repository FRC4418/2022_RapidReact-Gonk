package frc.robot.displays.drivetrain;


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
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Slew Rate Limiters", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 3, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			useSlewRateLimitersToggleSwitch = layout
				.addPersistent("On-Off", Constants.Drivetrain.kUseSlewRateLimiters)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();

			{ var arcadeDriveLayout = layout
				.getLayout("Arcade Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeDriveForwardLimiterTextView = arcadeDriveLayout
					.addPersistent("Forward", Constants.Drivetrain.SlewRates.kArcadeForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				arcadeDriveTurnLimiterTextView = arcadeDriveLayout
					.addPersistent("Turn", Constants.Drivetrain.SlewRates.kArcadeTurn)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var tankDriveLayout = layout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
			
				tankDriveLeftForwardLimiterTextView = tankDriveLayout
					.addPersistent("Left Forward", Constants.Drivetrain.SlewRates.kTankForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				tankDriveRightForwardLimiterTextView = tankDriveLayout
					.addPersistent("Right Forward", Constants.Drivetrain.SlewRates.kTankForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		useSlewRateLimitersToggleSwitch.addListener(event -> {
			m_drivetrain.setUseSlewRateLimiters(event.value.getBoolean());
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		{	// Arcade drive
			arcadeDriveForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			arcadeDriveTurnLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{	// Tank drive
			tankDriveLeftForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankLeftForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankDriveRightForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankRightForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
	}
}