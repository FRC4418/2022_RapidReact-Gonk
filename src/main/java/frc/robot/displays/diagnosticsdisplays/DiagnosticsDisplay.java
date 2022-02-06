package frc.robot.displays.diagnosticsdisplays;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public abstract class DiagnosticsDisplay {
    protected final ShuffleboardTab diagnosticsTab = Shuffleboard.getTab("Diagnostics");

    protected int column;
    protected int row;

    public DiagnosticsDisplay(int column, int row) {
        this.column = column;
        this.row = row;
        createDisplayAt(column, row);
        addEntryListeners();
    }

    protected abstract DiagnosticsDisplay createDisplayAt(int column, int row);

    public abstract DiagnosticsDisplay addEntryListeners();

    public abstract DiagnosticsDisplay removeEntryListeners();
}