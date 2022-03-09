package frc.robot.displays;


public abstract class Display {
    protected boolean initialized = false;

    protected int column;
    protected int row;

    protected int width;
    protected int height;

    public Display(int width, int height) {
        assert width > 0;
        assert height > 0;
        this.width = width;
        this.height = height;
    }

    public Display setColumn(int column) {
        assert !initialized;
        this.column = column;
        return this;
    }

    public int getColumn() {
        return column;
    }

    public Display setRow(int row) {
        assert !initialized;
        this.row = row;
        return this;
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
        
        initialized = true;
        return this;
    }

    protected abstract Display createDisplayAt(int column, int row);
}