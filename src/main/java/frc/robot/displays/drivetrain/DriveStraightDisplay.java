package frc.robot.displays.drivetrain;

import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import frc.robot.displays.Display;
import frc.robot.subsystems.Autonomous;

public class DriveStraightDisplay extends DrivingDisplay {
    private final Autonomous m_autonomous;
    
    private NetworkTableEntry
        driveStraightSpeedPercent;
    
    public DriveStraightDisplay(Autonomous autonomous, int width, int height) {
        super(width, height);

        m_autonomous = autonomous;
    }

    @Override
    protected Display createDisplayAt(int column, int row) {
        { var layout = tab
            .getLayout("Drive-Straight", BuiltInLayouts.kGrid)
            .withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "TOP"))
            .withPosition(column, row)
            .withSize(width, height);

            driveStraightSpeedPercent = layout
                .addPersistent("Speed Percent", Autonomous.getDrivingMaxMotorMPS())
                .withWidget(BuiltInWidgets.kTextView)
                .getEntry();
        }
        return this;
    }

    @Override
    public void addEntryListeners() {
        driveStraightSpeedPercent.addListener(event -> {
            m_autonomous.setDrivingMaxSpeedMPS(event.value.getDouble());
        }, EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }
}