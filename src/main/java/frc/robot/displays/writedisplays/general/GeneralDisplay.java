package frc.robot.displays.writedisplays.general;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.WriteDisplay;


public abstract class GeneralDisplay extends WriteDisplay {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("General");

    public GeneralDisplay(int width, int height) {
        super(width, height);
    }
}