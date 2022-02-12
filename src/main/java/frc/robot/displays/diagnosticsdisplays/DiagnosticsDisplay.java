package frc.robot.displays.diagnosticsdisplays;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class DiagnosticsDisplay extends Display {
    protected static final ShuffleboardTab diagnosticsTab = Shuffleboard.getTab("Diagnostics");

    public DiagnosticsDisplay(int column, int row, int width, int height) {
        super(column, row, width, height);
    }

    public DiagnosticsDisplay removeEntryListeners() {
        for (var entry: entries) {
            entry.removeListener(entry.getHandle());
        }
        return this;
    }
}