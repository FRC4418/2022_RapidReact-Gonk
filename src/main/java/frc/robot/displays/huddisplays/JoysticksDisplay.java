package frc.robot.displays.huddisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.RobotContainer.Pilot;
import frc.robot.displays.Display;


public class JoysticksDisplay extends HUDDisplay {
    public SendableChooser<JoystickMode> driverJoystickModeChooser = new SendableChooser<>();
    private NetworkTableEntry driverSwapLeftAndRightJoysticksToggleSwitch;

    public SendableChooser<JoystickMode> spotterJoystickModeChooser = new SendableChooser<>();
    private NetworkTableEntry spotterSwapLeftAndRightJoysticksToggleSwitch;

    public JoysticksDisplay(int width, int height) {
        super(width, height);
    }

    @Override
    protected Display createEntriesArray() {
        entries = new ArrayList<>(Arrays.asList(
            driverSwapLeftAndRightJoysticksToggleSwitch,

            spotterSwapLeftAndRightJoysticksToggleSwitch
        ));
        return this;
    }

    @Override
    protected Display createDisplayAt(int column, int row) {
        { var joysticksLayout = hudTab
			.getLayout("Joysticks", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

            { var driverLayout = joysticksLayout
                .getLayout("Driver", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                driverJoystickModeChooser.setDefaultOption("Arcade", RobotContainer.defaultDriverJoystickMode);
                driverJoystickModeChooser.addOption("Lone Tank", JoystickMode.LONE_TANK);
                driverJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                driverLayout
                    .add("Mode", driverJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
                driverSwapLeftAndRightJoysticksToggleSwitch = driverLayout
                    .add("Swap Left & Right", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry();
            }

            { var spotterLayout = joysticksLayout
                .getLayout("Spotter", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"));

                spotterJoystickModeChooser.setDefaultOption("Arcade", RobotContainer.defaultSpotterJoystickMode);
                spotterJoystickModeChooser.addOption("Lone Tank", JoystickMode.LONE_TANK);
                spotterJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                spotterLayout
                    .add("Mode", spotterJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
                spotterSwapLeftAndRightJoysticksToggleSwitch = spotterLayout
                    .add("Swap Left & Right", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry();
            }
        }
        return this;
    }

    int counter = 0;

    @Override
    public Display addEntryListeners() {        
        {   // Driver
            driverSwapLeftAndRightJoysticksToggleSwitch.addListener(event -> {
                SmartDashboard.putNumber("Swapped", counter++);
                RobotContainer.swapJoysticksFor(Pilot.DRIVER);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        }

        {   // Spotter
            spotterSwapLeftAndRightJoysticksToggleSwitch.addListener(event -> {
                RobotContainer.swapJoysticksFor(Pilot.SPOTTER);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        }

        return this;
    }
}