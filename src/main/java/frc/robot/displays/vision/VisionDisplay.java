package frc.robot.displays.vision;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class VisionDisplay extends Display {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Vision");

    public VisionDisplay(int width, int height) {
        super(width, height);
    }
}