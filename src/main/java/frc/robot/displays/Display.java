package frc.robot.displays;


import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTableEntry;


public abstract class Display {
    protected int column;
    protected int row;

    protected int width;
    protected int height;

    protected ArrayList<NetworkTableEntry> entries = new ArrayList<>();

    public Display(int column, int row, int width, int height) {
        this.column = column;
        this.row = row;

        this.width = width;
        this.height = height;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Display initialize() {
        createDisplayAt(column, row);
        createEntriesArray();
        return this;
    }

    protected abstract Display createEntriesArray();

    protected abstract Display createDisplayAt(int column, int row);

    public abstract Display addEntryListeners();
}