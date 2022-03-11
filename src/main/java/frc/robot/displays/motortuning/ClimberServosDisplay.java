package frc.robot.displays.motortuning;


import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.subsystems.Climber;


public class ClimberServosDisplay extends MotorTuningDisplay {
	private final Climber m_climber;

	private NetworkTableEntry
		leftServoAngleTextView,
		rightServoAngleTextView,

		printLeftServoAngleTextView,
		printRightServoAngleTextView;

	public ClimberServosDisplay(Climber climber, int width, int height) {
		super(width, height);

		m_climber = climber;
	}

	@Override
	protected ClimberServosDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Climber Servos", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);

			{ var writeLayout = layout
				.getLayout("Set Angles", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

				leftServoAngleTextView = writeLayout
					.add("Left Servo Angle", 0)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
				
				rightServoAngleTextView = writeLayout
					.add("Right Servo Angle", 0)
					.withWidget(BuiltInWidgets.kTextView)
					.getEntry();
			}

			{ var printLayout = layout
				.getLayout("Print Angles", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));
				
				printLeftServoAngleTextView = printLayout
					.add("Left Angle", m_climber.getLeftServoAngle())
					.withWidget(BuiltInWidgets.kNumberBar)
					.getEntry();
				
				printRightServoAngleTextView = printLayout
					.add("Right Angle", m_climber.getRightServoAngle())
					.withWidget(BuiltInWidgets.kNumberBar)
					.getEntry();
			}
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		leftServoAngleTextView.addListener(event -> {
			m_climber.setLeftServoAngle((int) event.value.getDouble());
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		rightServoAngleTextView.addListener(event -> {
			m_climber.setRightServoAngle((int) event.value.getDouble());
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	}

	@Override
	public void updatePrintouts() {
		printLeftServoAngleTextView.setDouble(m_climber.getLeftServoAngle());
		printRightServoAngleTextView.setDouble(m_climber.getRightServoAngle());
	}
}