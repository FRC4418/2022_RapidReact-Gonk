package frc.robot.displays;


import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.displays.diagnosticsdisplays.DiagnosticsDisplay;


public class DisplaysContainer {
    // ----------------------------------------------------------
    // Private resources
    

    private enum DisplayType {
		HUD,
		DIAGNOSTICS
	}

	private DisplaysGrid hudDisplaysGrid;

	private DisplaysGrid diagnosticDisplaysGrid;


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


	private DisplaysGrid getDisplayGridFromType(DisplayType displayType) {
		assert displayType != null;

		DisplaysGrid displaysGrid = null;

		switch (displayType) {
			default:
				DriverStation.reportError("Display type not supported during reserveNextColumnAtRow", true);
				break;
			case HUD:
				displaysGrid = hudDisplaysGrid;
				break;
			case DIAGNOSTICS:
				displaysGrid = diagnosticDisplaysGrid;
				break;
		}
		assert displaysGrid.originExists();
		return displaysGrid;
	}

    public DisplaysContainer reserveNextColumnAtRow(int row, Display display, DisplayType displayType) {
		assert row >= 0;
		assert display != null;
		assert displayType != null;

		// note that 'row' and 'column' are referring to rows and columns in the DISPLAY 2D GRID ARRAY we're using to track relative positions, not rows and columns in Shuffleboard's coordinates
		var displaysGrid = getDisplayGridFromType(displayType);
		
		// if the row we want doesn't exist, make it
		if (row >= displaysGrid.getNumRows()) {
			// ex. we have three rows (indices 0, 1, and 2) but want row index 3. Then we need to add 1 row (3 - 3 + 1 = 1)
			displaysGrid.addRows(row - displaysGrid.getNumRows() + 1);
		}

		int wantedColumn = 0;

		// the rightmost non-null display at the row we want to reserve a space in
		Display rightmostDisplay = null;
		for (int iii = 0; iii < displaysGrid.getNumColumns(); iii++) {
			var displayInRow = displaysGrid.get(row, iii);
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
		for (var displaysRow: displaysGrid) {
			var displayInColumn = displaysRow.get(wantedColumn);
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
		assert column >= 0;
		assert display != null;
		assert displayType != null;

		// note that 'row' and 'column' are referring to rows and columns in the DISPLAY 2D GRID ARRAY we're using to track relative positions, not rows and columns in Shuffleboard's coordinates
		var displaysGrid = getDisplayGridFromType(displayType);

		// if the column we want doesn't exist, make it
		if (column >= displaysGrid.getNumColumns()) {
			// ex. we have three columns (indices 0, 1, and 2) but want column index 3. Then we need to add 1 column (3 - 3 + 1 = 1)
			displaysGrid.addColumns(column - displaysGrid.getNumColumns() + 1);
		}

		
		return this;
	}

}
