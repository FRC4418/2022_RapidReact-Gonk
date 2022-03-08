package frc.robot.displays.generaldisplays;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.RobotContainer;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.RobotContainer.Pilot;


public class JoysticksDisplay extends GeneralDisplay {
    public SendableChooser<JoystickMode> driverJoystickModeChooser = new SendableChooser<>();
    private NetworkTableEntry driverSwapLeftAndRightJoysticksToggleSwitch;

    public SendableChooser<JoystickMode> spotterJoystickModeChooser = new SendableChooser<>();
    private NetworkTableEntry spotterSwapLeftAndRightJoysticksToggleSwitch;

    public JoysticksDisplay(int width, int height) {
        super(width, height);
    }

    @Override
    protected GeneralDisplay createEntriesArray() {
        entries = new ArrayList<>(Arrays.asList(
            driverSwapLeftAndRightJoysticksToggleSwitch,

            spotterSwapLeftAndRightJoysticksToggleSwitch
        ));
        return this;
    }

    @Override
    protected GeneralDisplay createDisplayAt(int column, int row) {
        { var layout = tab
			.getLayout("Joysticks", BuiltInLayouts.kGrid)
			.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"))
			.withPosition(column, row)
			.withSize(width, height);

            { var driverLayout = layout
                .getLayout("Driver", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

                driverJoystickModeChooser.setDefaultOption("Arcade", RobotContainer.defaultDriverJoystickMode);
                driverJoystickModeChooser.addOption("Lone Tank", JoystickMode.LONE_TANK);
                driverJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                driverLayout
                    .addPersistent("Mode", driverJoystickModeChooser)
                    .withWidget(BuiltInWidgets.kComboBoxChooser);
                driverSwapLeftAndRightJoysticksToggleSwitch = driverLayout
                    .add("Swap Left & Right", false)
                    .withWidget(BuiltInWidgets.kToggleButton)
                    .getEntry();
            }

            { var spotterLayout = layout
                .getLayout("Spotter", BuiltInLayouts.kGrid)
                .withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

                spotterJoystickModeChooser.setDefaultOption("Lone Tank", JoystickMode.LONE_TANK);
                spotterJoystickModeChooser.addOption("Arcade", RobotContainer.defaultSpotterJoystickMode);
                spotterJoystickModeChooser.addOption("Dual Tank", JoystickMode.DUAL_TANK);
                spotterLayout
                    .addPersistent("Mode", spotterJoystickModeChooser)
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
    public GeneralDisplay addEntryListeners() {        
        {   // Driver
            driverSwapLeftAndRightJoysticksToggleSwitch.addListener(event -> {
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