package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.subsystems.Drivetrain;


public class SlewRateLimiterTuningDisplay {
    // ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;

    private NetworkTableEntry arcadeDriveForwardLimiterTextField;
	private NetworkTableEntry arcadeDriveTurnLimiterTextField;
	private NetworkTableEntry tankDriveForwardLimiterTextField;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public SlewRateLimiterTuningDisplay(Drivetrain drivetrain, ShuffleboardTab diagnosticsTab, int column, int row) {
		m_drivetrain = drivetrain;

        var slewRateLimiterLayout = diagnosticsTab
			.getLayout("Slew Rate Limiters", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 3, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(1, 3);

			var arcadeDriveLayout = slewRateLimiterLayout
				.getLayout("Arcade Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				arcadeDriveForwardLimiterTextField = arcadeDriveLayout
					.add("Forward", 0.5d)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				arcadeDriveForwardLimiterTextField.addListener(event -> {
					m_drivetrain.setArcadeDriveForwardLimiterRate(event.value.getDouble());
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

				arcadeDriveTurnLimiterTextField = arcadeDriveLayout
					.add("Turn", 0.5d)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				arcadeDriveTurnLimiterTextField.addListener(event -> {
					m_drivetrain.setArcadeDriveTurnLimiterRate(event.value.getDouble());
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

			var tankDriveLayout = slewRateLimiterLayout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"));
			
				tankDriveForwardLimiterTextField = tankDriveLayout
					.add("Forward", 0.d)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				tankDriveForwardLimiterTextField.addListener(event -> {
					m_drivetrain.setTankDriveForwardLimiterRate(event.value.getDouble());
				}, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }
}
