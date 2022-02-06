package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;


public class SlewRateLimiterTuningDisplay {
    // ----------------------------------------------------------
	// Resources

	// TODO: P3 If using event listeners, make slew rate limiter tuning resources private

    public NetworkTableEntry arcadeDriveForwardLimiterTextField;
	public NetworkTableEntry arcadeDriveTurnLimiterTextField;
	public NetworkTableEntry tankDriveForwardLimiterTextField;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public SlewRateLimiterTuningDisplay(ShuffleboardTab diagnosticsTab, int column, int row) {
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
				arcadeDriveTurnLimiterTextField = arcadeDriveLayout
					.add("Turn", 0.5d)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();

			var tankDriveLayout = slewRateLimiterLayout
				.getLayout("Tank Drive", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"));
			
				tankDriveForwardLimiterTextField = tankDriveLayout
					.add("Forward", 0.d)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
    }
}
