package frc.robot.displays.motortuning;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.displays.Display;


public abstract class MotorTuningDisplay extends Display {
    protected static final ShuffleboardTab tab = Shuffleboard.getTab("Motor Tuning");

    public MotorTuningDisplay(int width, int height) {
        super(width, height);
    }
}