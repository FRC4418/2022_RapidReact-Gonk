package frc.robot.displays.motortuning;


import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;


public class ClimberServosDisplay extends MotorTuningDisplay {
	private final Climber m_climber;

	private NetworkTableEntry
		releasePinsAngle,
		attachPinsAngle,

		currentServoAnglesTextView;

	public ClimberServosDisplay(Climber climber, int width, int height) {
		super(width, height);

		m_climber = climber;
	}

	@Override
	protected ClimberServosDisplay createDisplayAt(int column, int row) {
		{ var layout = tab
			.getLayout("Climber Servos", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 3, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(width, height);

			releasePinsAngle = layout
				.add("Release-Pin Angle", Constants.Climber.kReleasePinAngle)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();
			
			attachPinsAngle = layout
				.add("Attach-Pin Angle", Constants.Climber.kAttachPinAngle)
				.withWidget(BuiltInWidgets.kTextView)
				.getEntry();

			currentServoAnglesTextView = layout
				.add("Current Angle", m_climber.getServoAngle())
				.withWidget(BuiltInWidgets.kNumberBar)
				.getEntry();
		}
		return this;
	}

	@Override
	public void addEntryListeners() {
		releasePinsAngle.addListener(event -> {
			boolean wasReleased = false;
			if (m_climber.pinIsReleased()) {
				wasReleased = true;
			}
			Constants.Climber.kReleasePinAngle = event.value.getDouble();
			if (wasReleased) {
				m_climber.releasePin();
			}
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

		attachPinsAngle.addListener(event -> {
			boolean wasAttached = false;
			if (m_climber.pinIsAttached()) {
				wasAttached = true;
			}
			Constants.Climber.kAttachPinAngle = event.value.getDouble();
			if (wasAttached) {
				m_climber.attachPin();
			}
		}, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
	}

	@Override
	public void updatePrintouts() {
		currentServoAnglesTextView.setDouble(m_climber.getServoAngle());
	}
}