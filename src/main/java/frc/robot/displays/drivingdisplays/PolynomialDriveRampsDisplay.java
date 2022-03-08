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


public class PolynomialDriveRampsDisplay extends DrivingDisplay {
	private final Drivetrain m_drivetrain;

	// ax^b --> multiplier * x^exponential

    private NetworkTableEntry
		arcadeForwardMultiplierTextView,
		arcadeForwardExponentialTextView,

		arcadeTurnMultiplierTextView,
		arcadeTurnExponentialTextView,

		tankForwardMultiplierTextView,
		tankForwardExponentialTextView;

    public PolynomialDriveRampsDisplay(Drivetrain drivetrain, int width, int height) {
		super(width, height);

		m_drivetrain = drivetrain;
    }

	@Override
	protected DrivingDisplay createEntriesArray() {
		entries = new ArrayList<>(Arrays.asList(
			arcadeForwardMultiplierTextView,
			arcadeForwardExponentialTextView,

			arcadeTurnMultiplierTextView,
			arcadeTurnExponentialTextView,

			tankForwardMultiplierTextView,
			tankForwardExponentialTextView
		));
		return this;
	}

	@Override
	protected DrivingDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Polynomial Ramps (ax^b)", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 3, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			{ var arcadeForwardLayout = layout
				.getLayout("Arcade Forward", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeForwardMultiplierTextView = arcadeForwardLayout
					.addPersistent("Multiplier", Constants.Drivetrain.ArcadePolynomial.kDefaultForwardMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				arcadeForwardExponentialTextView = arcadeForwardLayout
					.addPersistent("Exponential", Constants.Drivetrain.ArcadePolynomial.kDefaultForwardExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var arcadeTurnLayout = layout
				.getLayout("Arcade Turn", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeTurnMultiplierTextView = arcadeTurnLayout
					.addPersistent("Multiplier", Constants.Drivetrain.ArcadePolynomial.kDefaultTurnMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				arcadeTurnExponentialTextView = arcadeTurnLayout
					.addPersistent("Exponential", Constants.Drivetrain.ArcadePolynomial.kDefaultTurnExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var tankForwardLayout = layout
				.getLayout("Tank Forward", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				tankForwardMultiplierTextView = tankForwardLayout
					.addPersistent("Multiplier", Constants.Drivetrain.TankPolynomial.kDefaultForwardMultiplier)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				tankForwardExponentialTextView = tankForwardLayout
					.addPersistent("Exponential", Constants.Drivetrain.TankPolynomial.kDefaultForwardExponential)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public DrivingDisplay addEntryListeners() {
		{ // Arcade forward
			arcadeForwardMultiplierTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			arcadeForwardExponentialTextView.addListener(event -> {
				m_drivetrain.setArcadeForwardExponential(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Arcade turn
			arcadeTurnMultiplierTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			arcadeTurnExponentialTextView.addListener(event -> {
				m_drivetrain.setArcadeTurnExponential(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}

		{ // Tank forward
			tankForwardMultiplierTextView.addListener(event -> {
				m_drivetrain.setTankForwardMultiplier(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			tankForwardExponentialTextView.addListener(event -> {
				m_drivetrain.setTankForwardExponential(event.value.getDouble());
			}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
		}
		return this;
	}
}