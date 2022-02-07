package frc.robot.displays.huddisplays;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class HUDDisplay extends Display {
    protected static final ShuffleboardTab hudTab = Shuffleboard.getTab("HUD");

    public HUDDisplay(int column, int row) {
        super(column, row);
    }
}