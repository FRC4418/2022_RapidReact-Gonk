package frc.robot.displays.diagnosticsdisplays;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public abstract class DiagnosticsDisplay {
    protected final ShuffleboardTab diagnosticsTab = Shuffleboard.getTab("Diagnostics");

    protected int column;
    protected int row;

    protected ArrayList<NetworkTableEntry> entries = new ArrayList<>();

    public DiagnosticsDisplay(int column, int row) {
        this.column = column;
        this.row = row;
        createDisplayAt(column, row);
        initializeEntriesArray();
    }

    protected abstract DiagnosticsDisplay initializeEntriesArray();

    protected abstract DiagnosticsDisplay createDisplayAt(int column, int row);

    public abstract DiagnosticsDisplay addEntryListeners();

    public DiagnosticsDisplay removeEntryListeners() {
        for (var entry: entries) {
            entry.removeListener(entry.getHandle());
        }
        return this;
    }
}