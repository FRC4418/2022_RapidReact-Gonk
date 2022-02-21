package frc.robot.displays;


import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.displays.diagnosticsdisplays.DiagnosticsDisplay;


public class DisplaysContainer {
    // ----------------------------------------------------------
    // Private resources
    

    private enum DisplayType {
		HUD,
		DIAGNOSTICS
	}

	private boolean hudOriginExists = false;
	// TODO: P3 Make and use a custom display grid type that's implemented by a 2D ArrayList 
	private ArrayList<ArrayList<Display>> hudDisplaysGrid;

	private boolean diagnosticsOriginExists = false;
	private ArrayList<ArrayList<Display>> diagnosticDisplaysGrid;


    // ----------------------------------------------------------
    // Diagnostic-entry listeners


	public DisplaysContainer addHUDEntryListeners() {
		for (var row: hudDisplaysGrid) {
			for (var display: row) {
				display.addEntryListeners();
			}
		}
		return this;
	}

	public DisplaysContainer addDiagnosticsEntryListeners() {
		for (var row: diagnosticDisplaysGrid) {
			for (var display: row) {
				display.addEntryListeners();
			}
		}
		return this;
	}

	public DisplaysContainer removeDiagnosticsEntryListeners() {
		for (var row: diagnosticDisplaysGrid) {
			for (var display: row) {
				// shhhh...good compiler....gooood
				((DiagnosticsDisplay) display).removeEntryListeners();
			}
		}
		return this;
	}

	
	// ----------------------------------------------------------
    // Relative grid-space reservers


	public void makeOriginWith(Display display, DisplayType displayType) {
		ArrayList<ArrayList<Display>> displaysGrid = null;

		switch (displayType) {
			default:
				DriverStation.reportError("Display type not supported during makeOriginWith", true);
				break;
			case HUD:
				if (hudOriginExists) {
					DriverStation.reportError("HUD display grid origin already exists", true);
				}
				hudDisplaysGrid = new ArrayList<>(new ArrayList<>());
				hudOriginExists = true;
				break;
			case DIAGNOSTICS:
				if (diagnosticsOriginExists) {
					DriverStation.reportError("Diagnostics display grid origin already exists", true);
				}
				diagnosticDisplaysGrid = new ArrayList<>(new ArrayList<>());
				diagnosticsOriginExists = true;

				displaysGrid = diagnosticDisplaysGrid;
				break;
		}

		displaysGrid.get(0).set(0, display);
	}

    public DisplaysContainer reserveNextColumnAtRow(int row, Display display, DisplayType displayType) {
		// note that 'row' and 'column' are referring to rows and columns in the DISPLAY 2D GRID ARRAY we're using to track relative positions, not rows and columns in Shuffleboard's coordinates
		ArrayList<ArrayList<Display>> displaysGrid = null;

		switch (displayType) {
			default:
				DriverStation.reportError("Display type not supported during reserveNextColumnAtRow", true);
				break;
			case HUD:
				assert hudOriginExists;
				displaysGrid = hudDisplaysGrid;
				break;
			case DIAGNOSTICS:
				assert diagnosticsOriginExists;
				displaysGrid = diagnosticDisplaysGrid;
				break;
		}
		
		// if the row we want doesn't exist, make it

		int numRows = displaysGrid.size();
		int numColumns = displaysGrid.get(0).size();
		try {
			displaysGrid.get(row);
		} catch (Exception e) {
			for (int iii = 0; iii <= row - numRows; iii++) {
				displaysGrid.add(new ArrayList<>(numColumns));
			}
		}

		int wantedColumn = 0;

		// the rightmost non-null display at the row we want to reserve a space in
		Display rightmostDisplay = null;
		for (int iii = 0; iii < numColumns; iii++) {
			var displayInRow = displaysGrid.get(row).get(iii);
			if (displayInRow != null) {
				rightmostDisplay = displayInRow;
				wantedColumn = iii;
			}
		}

		// the bottommost non-null display at the column we are about to reserve a space in
		Display bottommostDisplay = null;
		for (var displayRow: displaysGrid) {
			var displayInColumn = displayRow.get(wantedColumn);
			if (displayInColumn != null) {
				bottommostDisplay = displayInColumn;
			}
		}

		// TODO: !!!P1!!! Continue implementation here
		// Use the rightmost and bottommost displays to place the display at the correct grid coordinate and absolute Shuffleboard coordinate
		return this;
	}

	public DisplaysContainer reserveNextRowAtColumn(int column, Display display, DisplayType displayType) {

		return this;
	}

}
