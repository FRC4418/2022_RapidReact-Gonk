package frc.robot.displays.motortuning;


import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class MotorPrintoutDisplay extends MotorTuningDisplay {
	private final Intake m_intake;
	private final Manipulator m_manipulator;

	private NetworkTableEntry
		retractorDegreeNumberBar,
		launcherRPMNumberBar;

    public MotorPrintoutDisplay(Intake intake, Manipulator manipulator, int width, int height) {
		super(width, height);

		m_intake = intake;
		m_manipulator = manipulator;
    }

	@Override
	protected MotorPrintoutDisplay createDisplayAt(int column, int row) {
        { var layout = tab
			.getLayout("Printouts", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

			retractorDegreeNumberBar = layout
				.add("Retractor degree", m_intake.getRetractorDegree())
				.withWidget(BuiltInWidgets.kNumberBar)
				.withProperties(Map.of(
					"Min", Constants.Intake.kRetractorMinDegree,
					"Max", Constants.Intake.kRetractorMaxDegree,
					"Center", m_intake.getRetractorDegree()))
				.getEntry();
			
			launcherRPMNumberBar = layout
				.add("Launcher RPM", m_manipulator.getLauncherRPM())
				.withWidget(BuiltInWidgets.kNumberBar)
				.withProperties(Map.of(
					"Min", -Constants.Falcon500.kMaxRPM,
					"Max", Constants.Falcon500.kMaxRPM,
					"Center", m_manipulator.getLauncherRPM()))
				.getEntry();
		}
		return this;
	}

	@Override
	public void updatePrintouts() {
		retractorDegreeNumberBar.setDouble(m_intake.getRetractorDegree());

		launcherRPMNumberBar.setDouble((double) m_manipulator.getLauncherRPM());
	}
}