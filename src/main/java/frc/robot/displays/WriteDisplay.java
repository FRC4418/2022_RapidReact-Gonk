package frc.robot.displays;


public abstract class WriteDisplay extends Display {
    public WriteDisplay(int width, int height) {
        super(width, height);
    }

    public abstract Display addEntryListeners();
}