package frc.robot.displays;


import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTableEntry;


public abstract class Display {
    protected int column;
    protected int row;

    protected ArrayList<NetworkTableEntry> entries = new ArrayList<>();

    public Display(int column, int row) {
        this.column = column;
        this.row = row;

        createDisplayAt(column, row);
        initializeEntriesArray();
    }

    protected abstract Display initializeEntriesArray();

    protected abstract Display createDisplayAt(int column, int row);

    public abstract Display addEntryListeners();
}
