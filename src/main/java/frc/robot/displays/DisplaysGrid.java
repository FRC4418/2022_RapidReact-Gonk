package frc.robot.displays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class DisplaysGrid implements Iterable<ArrayList<Display>> {
    private int rows = 4;
    private int columns = 4;

    // this is to avoid having to add more rows or columns for a while
    private List<ArrayList<Display>> grid = new ArrayList<ArrayList<Display>>();

    // maps each grid column/row index to an absolute Shuffleboard column/row (ex. grid column/row 1 corresponds to Shuffleboard column/row 6 because a display(s) in grid column/row 0 has width/height of 5 Shuffleboard columns/rows)
    private List<Integer> absoluteColumns = Arrays.asList(0, 1, 2, 3);
    private List<Integer> absoluteRows = Arrays.asList(0, 1, 2, 3);

    public DisplaysGrid() {
        for (int iii = 0; iii < rows; iii++) {
            grid.add(new ArrayList<>(Collections.nCopies(columns, null)));
        }
    }

    @Override
    public Iterator<ArrayList<Display>> iterator() {
        return grid.iterator();
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumColumns() {
        return columns;
    }

    public boolean originDisplayIsReserved() {
        return grid.get(0).get(0) != null;
    }

    public Display get(int row, int column) {
        return grid.get(row).get(column);
    }

    public DisplaysGrid set(int row, int column, Display display) {
        grid.get(row).set(column, display);
        return this;
    }

    // adds numRows amount of rows to grid
    public DisplaysGrid addRows(int numRows) {
        assert numRows > 0;
        for (int iii = 0; iii < numRows; iii++) {
            grid.add(new ArrayList<>(columns));
            absoluteRows.add(absoluteRows.get(absoluteRows.size() - 1) + 1);
		}
        rows += numRows;
        return this;
    }

    // adds numColumns amount of columns to grid
    public DisplaysGrid addColumns(int numColumns) {
        assert numColumns > 0;
        for (var displaysRow: grid) {
            for (int iii = 0; iii < numColumns; iii++) {
                displaysRow.add(null);
            }
            absoluteColumns.add(absoluteColumns.get(absoluteColumns.size() - 1) + 1);
        }
        columns += numColumns;
        return this;
    }

    public DisplaysGrid makeOriginWith(Display display) {
        assert display != null;
        assert !originDisplayIsReserved();

        set(0, 0, display);
        display
            .setColumn(0)
            .setRow(0);
        return this;
    }

    private int maxWidthOfColumn(int column) {
        int maxWidth = 0;
        for (var displaysRow: grid) {
            var display = displaysRow.get(column);
            if (display == null) {
                continue;
            }
            int displayWidth = display.getWidth();
            if (displayWidth > maxWidth) {
                maxWidth = displayWidth;
            }
        }
        return maxWidth;
    }

    private DisplaysGrid calculateAbsoluteColumns() {
        int previousColumnsWidthsSum = 0;
        for (int column = 1; column < columns; column++) {
            previousColumnsWidthsSum += maxWidthOfColumn(column - 1);
            absoluteColumns.set(column, previousColumnsWidthsSum);
        }
        return this;
    }

    private int maxHeightOfRow(int row) {
        int maxHeight = 0;
        for (var display: grid.get(row)) {
            if (display == null) {
                continue;
            }
            int height = display.getHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

    private DisplaysGrid calculateAbsoluteRows() {
        int previousRowsHeightsSum = 0;
        for (int row = 1; row < rows; row++) {
            previousRowsHeightsSum += maxHeightOfRow(row - 1);
            absoluteRows.set(row, previousRowsHeightsSum);
        }
        return this;
    }

    public DisplaysGrid reserveNextColumnAtRow(int row, Display display) {
        assert row >= 0;
		assert display != null;
        assert originDisplayIsReserved();
        
        // if the row we want doesn't exist, make it
		if (row >= rows) {
			// ex. we have three rows (indices 0, 1, and 2) but want row index 3. Then we need to add 1 row (3 - 3 + 1 = 1)
			addRows(row - rows + 1);
		}

        Display rightmostDisplay = null;
        // the grid column (NOT the Shuffleboard column [AKA absolute-Shuffleboard column]) of the rightmost display in the row we want
        int rightmostDisplayColumn = 0;
		for (int column = 0; column < columns; column++) {
			var displayInRow = get(row, column);
			if (displayInRow != null) {
                rightmostDisplay = displayInRow;
				rightmostDisplayColumn = column;
			}
		}

        int reservedColumn = rightmostDisplayColumn + 1;

        set(row, reservedColumn, display);

        // oh hell no I am not explaining what row and column "pegs" are without a whiteboard and like two hours of your time

        int absoluteColumn = 0;
        if (rightmostDisplay != null) {
            absoluteColumn = rightmostDisplay.getColumn() + rightmostDisplay.getWidth();
        }
        int potentialNumRowPegs = display.getHeight() - rightmostDisplay.getHeight();
        if (potentialNumRowPegs > 0 && reservedColumn >= 1) {
            for (int column = 0; column < reservedColumn; column++) {
                // called 'rowIndex' to avoid name collision with 'row' parameter
                for (int rowIndex = 1; rowIndex <= potentialNumRowPegs; rowIndex++) {
                    try {
                        var absoluteColumnPegger = get(rowIndex, column);
                        int reservedColumnAfterPegging = absoluteColumnPegger.getColumn() + absoluteColumnPegger.getWidth();
                        absoluteColumn = Math.max(absoluteColumn, reservedColumnAfterPegging);
                    } catch (Exception e) {}
                }
            }
        }

        Display bottommostDisplay = null;
        // the grid row (NOT the Shuffleboard row [AKA absolute-Shuffleboard row]) of the bottommost display in the column we're reserving
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			var displayInRow = get(rowIndex, reservedColumn);
			if (displayInRow != null) {
                bottommostDisplay = displayInRow;
			}
		}

        int absoluteRow = 0;
        if (bottommostDisplay != null) {
            absoluteRow = bottommostDisplay.getRow() + bottommostDisplay.getHeight();
        }
        int potentialNumColumnPegs = display.getWidth();
        if (potentialNumColumnPegs > 0 && row >= 1) {
            for (int rowIndex = 0; rowIndex < row; rowIndex++) {
                for (int column = 1; column <= potentialNumColumnPegs; column++) {
                    try {
                        var rowPegger = get(rowIndex, column);
                        int reservedRowAfterPegging = rowPegger.getRow() + rowPegger.getHeight();
                        absoluteRow = Math.max(absoluteRow, reservedRowAfterPegging);
                    } catch (Exception e) {}
                }
            }
        }

        display
            .setColumn(absoluteColumn)
            .setRow(absoluteRow);
        return this;
    }

    public DisplaysGrid reserveNextRowAtColumn(int column, Display display) {
        assert column >= 0;
        assert display != null;
        assert originDisplayIsReserved();

        // if the column we want doesn't exist, make it
		if (column >= columns) {
			// ex. we have three columns (indices 0, 1, and 2) but want column index 3. Then we need to add 1 column (3 - 3 + 1 = 1)
			addRows(column - columns + 1);
		}

        // the grid row (NOT the Shuffleboard row [AKA absolute-Shuffleboard row]) of the bottommost display in the column we want
        int bottommostDisplayRow = 0;
		for (int row = 0; row < rows; row++) {
			var displayInColumn = get(row, column);
			if (displayInColumn != null) {
				bottommostDisplayRow = row;
			}
		}

        set(bottommostDisplayRow + 1, column, display);
        return this;
    }

    public DisplaysGrid show() {
        calculateAbsoluteColumns();
        calculateAbsoluteRows();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                var display = get(row, column);
                if (display == null) {
                    continue;
                }
                display.initialize();
            }
        }
        return this;
    }

    public DisplaysGrid addEntryListeners() {
        for (var row: grid) {
            for (var display: row) {
                if (display == null) {
                    continue;
                }
                display.addEntryListeners();
            }
        }
        return this;
    }
}
