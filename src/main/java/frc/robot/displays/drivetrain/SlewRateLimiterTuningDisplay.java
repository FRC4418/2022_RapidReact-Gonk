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

		curvatureForwardLimiterTextView,
		curvatureRotationLimiterTextView,

    	arcadeForwardLimiterTextView,
		arcadeTurnLimiterTextView,

		tankLeftForwardLimiterTextView,
		tankRightForwardLimiterTextView;

    public SlewRateLimiterTuningDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Slew Rate Limiters", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 4, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			useSlewRateLimitersToggleSwitch = layout
				.addPersistent("On-Off", Constants.Drivetrain.kUseSlewRateLimiters)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();
			
			{ var curvatureLayout = layout
				.getLayout("Curvature", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				curvatureForwardLimiterTextView = curvatureLayout
					.addPersistent("Forward", Constants.Drivetrain.SlewRates.kCurvatureForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				curvatureRotationLimiterTextView = curvatureLayout
					.addPersistent("Rotation", Constants.Drivetrain.SlewRates.kCurvatureRotation)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}
			
			{ var arcadeLayout = layout
				.getLayout("Arcade", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeForwardLimiterTextView = arcadeLayout
					.addPersistent("Forward", Constants.Drivetrain.SlewRates.kArcadeForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				arcadeTurnLimiterTextView = arcadeLayout
					.addPersistent("Turn", Constants.Drivetrain.SlewRates.kArcadeTurn)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var tankLayout = layout
				.getLayout("Tank", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
			
				tankLeftForwardLimiterTextView = tankLayout
					.addPersistent("Left Forward", Constants.Drivetrain.SlewRates.kTankForward)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

				tankRightForwardLimiterTextView = tankLayout
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

		{ // Curvature drive
			curvatureForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setCurvatureForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			curvatureRotationLimiterTextView.addListener(event -> {
				m_drivetrain.setCurvatureRotationLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Arcade drive
			arcadeForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	
			arcadeTurnLimiterTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Tank drive
			tankLeftForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankLeftForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankRightForwardLimiterTextView.addListener(event -> {
				m_drivetrain.setTankRightForwardLimiterRate(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
	}
}