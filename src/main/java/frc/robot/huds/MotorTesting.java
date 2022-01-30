package frc.robot.huds;


import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class MotorTesting {
    // ----------------------------------------------------------
	// Resources

    public NetworkTableEntry motorTestingModeToggleSwitch;

	public NetworkTableEntry manipulatorIndexerToggleSwitch;
	public NetworkTableEntry manipulatorIndexerOutputPercentTextView;

	public NetworkTableEntry manipulatorLauncherToggleSwitch;
	public NetworkTableEntry manipulatorLauncherOutputPercentTextView;

	public NetworkTableEntry intakeRollerToggleSwitch;
	public NetworkTableEntry intakeRollerOutputPercentTextView;

	public NetworkTableEntry intakeRetractorToggleSwitch;
	public NetworkTableEntry intakeRetractorPositionTextView;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public MotorTesting(ShuffleboardTab diagnosticsTab, int column, int width) {
        var motorTestingLayout = diagnosticsTab
			.getLayout("Motor Testing", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(column, width)
			.withSize(4, 3);

			// Enable/disable motor testing
			motorTestingModeToggleSwitch = motorTestingLayout
				.add("Enabled", false)
				.withWidget(BuiltInWidgets.kToggleSwitch)
				.getEntry();

			// put into the 2nd slot of motorTestingLayout's vertical stack
			var horizontalStack = motorTestingLayout
				.getLayout("Horizontal Stack", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// ----------------------------------------------------------
				// Testing the intake motors

				var intakeLayout = horizontalStack
					.getLayout("Intake", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					// Intake retractor motor

					var retractorLayout = intakeLayout
						.getLayout("Retractor", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						// I have no fucking clue why the textView entry is always added before (to the 2nd row) the toggleSwitch but it's good enough
						intakeRetractorToggleSwitch = retractorLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						intakeRetractorPositionTextView = retractorLayout
							.add("Position", Intake.RETRACT_MOTOR_DEFAULT_POSITION)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

					// intake roller motor
					
					var rollerLayout = intakeLayout
						.getLayout("Roller", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						intakeRollerToggleSwitch = rollerLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						intakeRollerOutputPercentTextView = rollerLayout
							.add("% Output", Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

				// ----------------------------------------------------------
				// Testing the conveyor-shooter motors

				var manipulatorLayout = horizontalStack
					.getLayout("Manipulator", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					var indexerLayout = manipulatorLayout
						.getLayout("Indexer", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						manipulatorIndexerToggleSwitch = indexerLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						manipulatorIndexerOutputPercentTextView = indexerLayout
							.add("% Output", Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();

					// Manipulator launcher motor
					
					var launcherLayout = manipulatorLayout
						.getLayout("Launcher", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						manipulatorLauncherToggleSwitch = launcherLayout
							.add("Enabled", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						manipulatorLauncherOutputPercentTextView = launcherLayout
							.add("% Output", Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT)
							.withWidget(BuiltInWidgets.kTextView)
							.getEntry();
    }
}
