package frc.robot.displays.drivetrain;


import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;


public class PolynomialDriveRampsDisplay extends DrivingDisplay {
	private final Drivetrain m_drivetrain;

	// ax^b --> multiplier * x^exponential

    private NetworkTableEntry
		arcadeForwardMultiplierTextView,
		arcadeForwardExponentialTextView,

		arcadeTurnMultiplierTextView,
		arcadeTurnExponentialTextView,

		curvatureForwardMultiplierTextView,
		curvatureForwardExponentialTextView,

		curvatureRotationMultiplierTextView,
		curvatureRotationExponentialTextView,

		tankForwardMultiplierTextView,
		tankForwardExponentialTextView;

    public PolynomialDriveRampsDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Polynomial Ramps (ax^b)", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 5, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			{ var arcadeForwardLayout = layout
				.getLayout("Arcade Forward", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeForwardMultiplierTextView = arcadeForwardLayout
					.addPersistent("Multiplier", Constants.Drivetrain.ArcadePolynomial.kForwardMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				arcadeForwardExponentialTextView = arcadeForwardLayout
					.addPersistent("Exponential", Constants.Drivetrain.ArcadePolynomial.kForwardExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var arcadeTurnLayout = layout
				.getLayout("Arcade Turn", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeTurnMultiplierTextView = arcadeTurnLayout
					.addPersistent("Multiplier", Constants.Drivetrain.ArcadePolynomial.kTurnMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				arcadeTurnExponentialTextView = arcadeTurnLayout
					.addPersistent("Exponential", Constants.Drivetrain.ArcadePolynomial.kTurnExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var curvatureForwardLayout = layout
				.getLayout("Curvature Forward", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
				
				curvatureForwardMultiplierTextView = curvatureForwardLayout
					.addPersistent("Multiplier", Constants.Drivetrain.CurvaturePolynomial.kForwardMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				curvatureForwardExponentialTextView = curvatureForwardLayout
					.addPersistent("Exponential", Constants.Drivetrain.CurvaturePolynomial.kForwardExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var curvatureRotationLayout = layout
				.getLayout("Curvature Rotation", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				curvatureRotationMultiplierTextView = curvatureRotationLayout
					.addPersistent("Multiplier", Constants.Drivetrain.CurvaturePolynomial.kRotationMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				curvatureRotationExponentialTextView = curvatureRotationLayout
					.addPersistent("Exponential", Constants.Drivetrain.CurvaturePolynomial.kRotationExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var tankForwardLayout = layout
				.getLayout("Tank Forward", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				tankForwardMultiplierTextView = tankForwardLayout
					.addPersistent("Multiplier", Constants.Drivetrain.TankPolynomial.kForwardMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				tankForwardExponentialTextView = tankForwardLayout
					.addPersistent("Exponential", Constants.Drivetrain.TankPolynomial.kForwardExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		{ // Arcade forward
			arcadeForwardMultiplierTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			arcadeForwardExponentialTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardExponential(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Arcade turn
			arcadeTurnMultiplierTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			arcadeTurnExponentialTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnExponential(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Curvature forward
			curvatureForwardMultiplierTextView.addListener(event -> {
				m_drivetrain.setCurvatureForwardMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Tank forward
			tankForwardMultiplierTextView.addListener(event -> {
				m_drivetrain.setTankForwardMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankForwardExponentialTextView.addListener(event -> {
				m_drivetrain.setTankForwardExponential(event.value.getDouble());
			}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
	}
}