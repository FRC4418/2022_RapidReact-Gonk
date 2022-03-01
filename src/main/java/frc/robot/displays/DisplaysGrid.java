package frc.robot.displays;


import java.util.ArrayList;
import java.util.Arrays;
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
            var newRow = new ArrayList<Display>();
            for (int jjj = 0; jjj < columns; jjj++) {
                newRow.add(null);
            }
            grid.add(newRow);
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
                        absoluteColumn = Math.max(absoluteColumn, absoluteColumnPegger.getColumn() + absoluteColumnPegger.getWidth());
                    } catch (Exception e) {}
                }
            }
        }

        Display bottommostDisplay = null;
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
                        absoluteRow = Math.max(absoluteRow, rowPegger.getRow() + rowPegger.getHeight());
                    } catch (Exception e) {}
                }
            }
        }

        set(row, reservedColumn, display);

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

        Display bottommostDisplay = null;
        // the grid row (NOT the Shuffleboard row [AKA absolute-Shuffleboard row]) of the bottommost display in the column we're reserving
        int bottommostDisplayRow = 0;
		for (int row = 0; row < rows; row++) {
			var displayInRow = get(row, column);
			if (displayInRow != null) {
                bottommostDisplay = displayInRow;
                bottommostDisplayRow = row;
			}
		}

        int reservedRow = bottommostDisplayRow + 1;

        int absoluteRow = 0;
        if (bottommostDisplay != null) {
            absoluteRow = bottommostDisplay.getRow() + bottommostDisplay.getHeight();
        }
        int potentialNumColumnPegs = display.getWidth() - bottommostDisplay.getWidth();
        if (potentialNumColumnPegs > 0 && reservedRow >= 1) {
            for (int row = 0; row < reservedRow; row++) {
                for (int columnIndex = 1; columnIndex <= potentialNumColumnPegs; columnIndex++) {
                    try {
                        var absoluteRowPegger = get(row, columnIndex);
                        absoluteRow = Math.max(absoluteRow, absoluteRowPegger.getRow() + absoluteRowPegger.getHeight());
                    } catch (Exception e) {}
                }
            }
        }

        Display rightmostDisplay = null;
		for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
			var displayInRow = get(reservedRow, columnIndex);
			if (displayInRow != null) {
                rightmostDisplay = displayInRow;
			}
		}

        int absoluteColumn = 0;
        if (rightmostDisplay != null) {
            absoluteColumn = rightmostDisplay.getColumn() + rightmostDisplay.getWidth();
        }
        int potentialNumRowPegs = display.getHeight();
        if (potentialNumRowPegs > 0 && column >= 1) {
            for (int columnIndex = 0; columnIndex < column; columnIndex++) {
                for (int row = 1; row <= potentialNumRowPegs; row++) {
                    try {
                        var absoluteColumnPegger = get(row, columnIndex);
                        absoluteColumn = Math.max(absoluteColumn, absoluteColumnPegger.getColumn() + absoluteColumnPegger.getWidth());
                    } catch (Exception e) {}
                }
            }
        }

        set(reservedRow, column, display);

        display
            .setColumn(absoluteColumn)
            .setRow(absoluteRow);
        return this;
    }

    public DisplaysGrid initialize() {
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