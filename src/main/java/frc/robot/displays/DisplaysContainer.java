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

	private ArrayList<ArrayList<Display>> getDisplayGrid(DisplayType displayType) {
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
		return displaysGrid;
	}

    public DisplaysContainer reserveNextColumnAtRow(int row, Display display, DisplayType displayType) {
		// note that 'row' and 'column' are referring to rows and columns in the DISPLAY 2D GRID ARRAY we're using to track relative positions, not rows and columns in Shuffleboard's coordinates
		var displaysGrid = getDisplayGrid(displayType);
		
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
		// getting the rightmost display's absolute Shuffleboard column and width
		int rightmostDisplayColumn = 0;
		int rightmostDisplayWidth = 0;
		if (rightmostDisplay != null) {
			rightmostDisplayColumn = rightmostDisplay.getColumn();
			rightmostDisplayWidth = rightmostDisplay.getWidth();
		}

		// the bottommost non-null display at the column we are about to reserve a space in
		Display bottommostDisplay = null;
		for (var displayRow: displaysGrid) {
			var displayInColumn = displayRow.get(wantedColumn);
			if (displayInColumn != null) {
				bottommostDisplay = displayInColumn;
			}
		}
		// getting the bottommost display's absolute Shuffleboard row and height
		int bottommostDisplayRow = 0;
		int bottommostDisplayHeight = 0;
		if (bottommostDisplay != null) {
			bottommostDisplayRow = bottommostDisplay.getRow();
			bottommostDisplayHeight = bottommostDisplay.getHeight();
		}

		// setting the display's absolute Shuffleboard coordinates and using them to initialize the display at the correct absolute Shuffleboard coordinates given the size and position of the rightmost display in the row and the bottommost display in the column
		display
			.setColumn(rightmostDisplayColumn + rightmostDisplayWidth)
			.setRow(bottommostDisplayRow + bottommostDisplayHeight)
			.initialize();
		return this;
	}

	public DisplaysContainer reserveNextRowAtColumn(int column, Display display, DisplayType displayType) {
		// note that 'row' and 'column' are referring to rows and columns in the DISPLAY 2D GRID ARRAY we're using to track relative positions, not rows and columns in Shuffleboard's coordinates
		var displaysGrid = getDisplayGrid(displayType);

		
		return this;
	}

}
