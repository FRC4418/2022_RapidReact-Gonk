package frc.robot.displays;


import java.util.ArrayList;
import java.util.Iterator;


public class DisplaysGrid implements Iterable<ArrayList<Display>> {
    private int rows = 1;
    private int columns = 10;

    private ArrayList<ArrayList<Display>> grid = new ArrayList<>(new ArrayList<>());

    public DisplaysGrid() {

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

    public boolean originExists() {

        return grid.get(0).get(0) != null;
    }

    public ArrayList<Display> get(int row) {
        return grid.get(row);
    }

    public Display get(int row, int column) {
        return grid.get(row).get(column);
    }

    public DisplaysGrid setRow(int index, ArrayList<Display> newDisplaysRow) {
        grid.set(index, newDisplaysRow);
        return this;
    }

    public DisplaysGrid makeOriginWith(Display display) {
        assert display != null;
        assert !originExists();

        grid.get(0).set(0, display);
        return this;
    }

    // adds numRows amount of rows to grid
    public DisplaysGrid addRows(int numRows) {
        assert numRows > 0;
        for (int iii = 0; iii < numRows; iii++) {
            grid.add(new ArrayList<>(columns));
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
        }
        columns += numColumns;
        return this;
    }
}
