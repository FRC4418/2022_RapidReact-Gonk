package frc.robot.IO;


import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.RobotContainer;
import frc.robot.IO.Constants.DeviceType;
import frc.robot.IO.Constants.X3D;
import frc.robot.IO.Constants.XboxController;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.RunRollerDisposal;
import frc.robot.commands.RunRollerIntake;
import frc.robot.commands.ToggleIntake;
import frc.robot.displays.JoysticksDisplay;


// TODO: Different set of driver controls for tank mode
public class DriverControls {
    // ----------------------------------------------------------
    // Constants

    private final int driverReservedPrimaryJoystickPort = 0;
    private final int driverReservedSecondaryJoystickPort = 1;

    // ----------------------------------------------------------
    // Public resources

    // (ex. Xbox uses trigger axes for roller disposal/intake while X3D uses buttons)
    public DeviceType deviceType;

    // ----------------------------------------------------------
    // Private resources

    private RobotContainer rc;

    private JoysticksDisplay jd;

    private JoystickMode joystickMode;

    private Joystick
        // the primay joystick has the subjective majority of controls for critical robot functions
        primaryJoystick,
        secondaryJoystick,

        // kinda wacky, but this is so that the primary joystick can be either the left or right joystick
        leftmostJoystick,
        rightmostJoystick;

    private int
        ARCADE_DRIVE_FORWARD_AXIS,
        ARCADE_DRIVE_ANGLE_AXIS,

        TANK_DRIVE_LEFT_AXIS,
        TANK_DRIVE_RIGHT_AXIS;

    private POVButton
        driveStraightPOVButton;
        
    private JoystickButton
        driveStraightJoystickButton,

        toggleIntakeButton,
        runRollerDisposalButton,
        runRollerIntakebutton,

        runIndexerButton,
        runLauncherButton;

    // ----------------------------------------------------------
    // Constructor and methods

    public DriverControls(RobotContainer robotContainer, JoysticksDisplay joysticksDisplay) {
        rc = robotContainer;
        jd = joysticksDisplay;
        addLeftRightJoystickFlipEventListener();
    }

    private DriverControls addLeftRightJoystickFlipEventListener() {
        jd.driverFlipLeftAndRightJoysticksToggleSwitch.addListener(event -> {            
            int tempLeftJoystickPort = leftmostJoystick.getPort();
            leftmostJoystick = new Joystick(rightmostJoystick.getPort());
            rightmostJoystick = new Joystick(tempLeftJoystickPort);

            // SmartDashboard.putString("New L R ports", leftmostJoystick.getPort() + " " + rightmostJoystick.getPort());
            SmartDashboard.putNumber("Event listener called", counter++);

            configureButtonBindingsFor(leftmostJoystick, rightmostJoystick);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        return this;
    }

    public DriverControls listenForJoystickMode() {
        JoystickMode newJoystickMode = jd.driverJoystickModeChooser.getSelected();
        if (joystickMode != newJoystickMode) {
            joystickMode = newJoystickMode;

            leftmostJoystick = new Joystick(driverReservedPrimaryJoystickPort);
            rightmostJoystick = new Joystick(driverReservedSecondaryJoystickPort);

            configureButtonBindingsFor(leftmostJoystick, rightmostJoystick);
        }
        return this;
    }

    public DriverControls periodicTeleopDrive() {
        switch (joystickMode) {
			case ARCADE:
				rc.drivetrain.arcadeDrive(
					getArcadeDriveForwardAxis(),	// forward
					getArcadeDriveAngleAxis());	// angle
				break;
			case LONE_TANK:
			case DUAL_TANK:
				rc.drivetrain.tankDrive(
					getTankDriveLeftAxis(),		// left
					getTankDriveRightAxis());	// right
		}
        return this;
    }

    public DriverControls periodicTeleopIntakeRoller() {
        double intakeTriggerMagnitude = getRollerIntakeAxis();
		double disposalTriggerMagnitude = getRollerDisposalAxis();
		
		if (intakeTriggerMagnitude > 0.d) {
			rc.intake.setRollerMotorPercent(-intakeTriggerMagnitude);
			rc.manipulator.setIndexerToPercent(intakeTriggerMagnitude);
		} else if (disposalTriggerMagnitude > 0.d) {
			rc.intake.setRollerMotorPercent(disposalTriggerMagnitude);
			rc.manipulator.setIndexerToPercent(disposalTriggerMagnitude);
		} else {
            rc.intake.stopRoller();
			if (!runIndexerButton.get()) {
				rc.manipulator.stopIndexer();
			}
		}
        return this;
    }

    // ----------------------------------------------------------
    // Axes

    // Arcade drive axes

    public double getArcadeDriveForwardAxis() {
        return primaryJoystick.getRawAxis(ARCADE_DRIVE_FORWARD_AXIS);
    }

    public double getArcadeDriveAngleAxis() {
        return primaryJoystick.getRawAxis(ARCADE_DRIVE_ANGLE_AXIS);
    }

    // Tank drive axes

    public double getTankDriveLeftAxis() {
        switch (deviceType) {
            default:
                DriverStation.reportError("Unknown device type when reading tank drive left axis", true);
                return 0.d;
            case XboxController:
                return leftmostJoystick.getRawAxis(TANK_DRIVE_LEFT_AXIS);
            case X3D:
                return -1.d * leftmostJoystick.getRawAxis(TANK_DRIVE_LEFT_AXIS);
        }
    }

    public double getTankDriveRightAxis() {
        switch (deviceType) {
            default:
                DriverStation.reportError("Unknown device type when reading tank drive right axis", true);
                return 0.d;
            case XboxController:
                return rightmostJoystick.getRawAxis(TANK_DRIVE_RIGHT_AXIS);
            case X3D:
                return -1.d * rightmostJoystick.getRawAxis(TANK_DRIVE_RIGHT_AXIS);
        }
    }
    
    // Roller axes

    // spin intake in reverse, spitting out a ball, using the axis' magnitude
    public double getRollerDisposalAxis() {
        return primaryJoystick.getRawAxis(XboxController.LEFT_TRIGGER_AXIS);
    }

    // right trigger on Xbox to spin intake, taking in a ball, using the trigger's magnitude
    public double getRollerIntakeAxis() {
        return primaryJoystick.getRawAxis(XboxController.RIGHT_TRIGGER_AXIS);
    }

    // ----------------------------------------------------------
    // Button bindings

    int counter = 0;

    private DriverControls configureArcadeButtonBindings() {
        switch (primaryJoystick.getName()) {
            default:                        
                DriverStation.reportError("Only Xbox controllers and X3D joysticks are supported for ARCADE mode", true);
                break;
            // if using a single Xbox controller
            case XboxController.USB_DEVICE_NAME:
                // Device set-up

                deviceType = DeviceType.XboxController;

                // Drivetrain

                ARCADE_DRIVE_FORWARD_AXIS = XboxController.LEFT_Y_AXIS;
                ARCADE_DRIVE_ANGLE_AXIS = XboxController.LEFT_X_AXIS;
                TANK_DRIVE_LEFT_AXIS = -1;
                TANK_DRIVE_RIGHT_AXIS = -1;

                driveStraightPOVButton = new POVButton(primaryJoystick, XboxController.ANGLE_UP_POV);
                driveStraightPOVButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
                driveStraightJoystickButton = null;

                // Intake

                runRollerDisposalButton = null;
                runRollerIntakebutton = null;

                toggleIntakeButton = new JoystickButton(primaryJoystick, XboxController.A_BUTTON_ID);
                toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                
                // Manipulator

                runIndexerButton = new JoystickButton(primaryJoystick, XboxController.B_BUTTON_ID);
                runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));

                runLauncherButton = new JoystickButton(primaryJoystick, XboxController.RIGHT_BUMPER_BUTTON_ID);
                runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));
                break;
            // if using a single X3D controller
            case X3D.USB_DEVICE_NAME:
                // Device set-up

                deviceType = DeviceType.X3D;

                // Drivetrain

                ARCADE_DRIVE_FORWARD_AXIS = X3D.PITCH_AXIS;
                ARCADE_DRIVE_ANGLE_AXIS = X3D.ROLL_AXIS;
                TANK_DRIVE_LEFT_AXIS = -1;
                TANK_DRIVE_RIGHT_AXIS = -1;

                driveStraightJoystickButton = new JoystickButton(primaryJoystick, X3D.GRIP_BUTTON_ID);
                driveStraightJoystickButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
                driveStraightPOVButton = null;

                // Intake

                runRollerDisposalButton = new JoystickButton(primaryJoystick, X3D.BUTTON_11_ID);
                runRollerDisposalButton.whenHeld(new RunRollerDisposal(rc.intake));

                runRollerIntakebutton = new JoystickButton(primaryJoystick, X3D.BUTTON_12_ID);
                runRollerIntakebutton.whenHeld(new RunRollerIntake(rc.intake));

                toggleIntakeButton = new JoystickButton(primaryJoystick, X3D.BUTTON_4_ID);
                toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                
                // Manipulator

                runIndexerButton = new JoystickButton(primaryJoystick, X3D.BUTTON_3_ID);
                runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));

                runLauncherButton = new JoystickButton(primaryJoystick, X3D.TRIGGER_BUTTON_ID);
                runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));
                break;                
        }
        return this;
    }

    private DriverControls configureLoneTankButtonBindings() {
        // since we use a single joystick for both the left and right tank drive axes, doesn't matter which joystick ref we get the name from
        switch (primaryJoystick.getName()) {
            default:                        
                DriverStation.reportError("Only Xbox controllers are supported for LONE_TANK mode", true);
                break;
            // if using a single Xbox controller
            case XboxController.USB_DEVICE_NAME:
                // Device set-up

                deviceType = DeviceType.XboxController;

                // Drivetrain

                TANK_DRIVE_LEFT_AXIS = XboxController.LEFT_Y_AXIS;
                TANK_DRIVE_RIGHT_AXIS = XboxController.RIGHT_Y_AXIS;
                ARCADE_DRIVE_FORWARD_AXIS = -1;
                ARCADE_DRIVE_ANGLE_AXIS = -1;

                driveStraightPOVButton = new POVButton(primaryJoystick, XboxController.ANGLE_UP_POV);
                driveStraightPOVButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
                driveStraightJoystickButton = null;

                // Intake

                runRollerDisposalButton = null;
                runRollerDisposalButton = null;

                toggleIntakeButton = new JoystickButton(primaryJoystick, XboxController.A_BUTTON_ID);
                toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                
                // Manipulator

                runIndexerButton = new JoystickButton(primaryJoystick, XboxController.B_BUTTON_ID);
                runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));

                runLauncherButton = new JoystickButton(primaryJoystick, XboxController.RIGHT_BUMPER_BUTTON_ID);
                runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));
                break;
        }
        return this;
    }

    // if controller2 is null, then configure button bindings for the driver's single controller
    private DriverControls configureButtonBindingsFor(Joystick leftmostJoystick, Joystick rightmostJoystick) {
        if (leftmostJoystick == null && rightmostJoystick == null) {
            DriverStation.reportError("Joystick device references for driver controls are null", true);
        // just one joystick is non-null
        } else if (leftmostJoystick == null || rightmostJoystick == null) {
            // fancy conditional operator to get the non-null joystick
            Joystick nonNullJoystick = leftmostJoystick == null ? rightmostJoystick: leftmostJoystick;

            primaryJoystick = nonNullJoystick;
            secondaryJoystick = null;

            switch (joystickMode) {
                default:
                    DriverStation.reportError("Only the ARCADE and LONE_TANK modes are supported for single-joystick driver controls", true);
                    break;
                // arcade mode using one joystick
                case ARCADE:
                    configureArcadeButtonBindings();
                    break;
                // tank mode using one joystick
                case LONE_TANK:
                    configureLoneTankButtonBindings();
                    break;
            }
        // both joysticks are non-null
        } else {
            primaryJoystick = rightmostJoystick;
            secondaryJoystick = leftmostJoystick;

            switch (joystickMode) {
                default:
                    DriverStation.reportError("Only the ARCADE, LONE_TANK, and DUAL_TANK modes are supported for dual-joystick driver controls", true);
                    break;
                // arcade mode that uses just one of the two available joysticks (we use the primary joystick)
                case ARCADE:
                    SmartDashboard.putNumber("L and R non-null and in ARCADE", counter++);

                    configureArcadeButtonBindings();
                    break;
                // lone tank mode that uses just one of the two available joysticks (we use the primary joystick)
                case LONE_TANK:
                    configureLoneTankButtonBindings();
                    break;
                // tank mode using two joysticks
                case DUAL_TANK:
                    // if you don't know why we do .equals() instead of == for string comparison, you're just bad lol
                    if (!(primaryJoystick.getName().equals(secondaryJoystick.getName()))) {
                        DriverStation.reportError("Only same-device-type joysticks (ex. two X3Ds) are supported for dual-joystick TANK mode", true);
                    }

                    // we've established that the left and right joysticks are the same device-type, so we can just arbitrarily choose one of them to know the device-type
                    switch (primaryJoystick.getName()) {
                        default:                            
                            DriverStation.reportError("Only X3D joysticks are supported for dual-joystick DUAL_TANK mode", true);
                            break;
                        case X3D.USB_DEVICE_NAME:
                            // Device set-up

                            deviceType = DeviceType.X3D;

                            // Drivetrain

                            TANK_DRIVE_LEFT_AXIS = X3D.PITCH_AXIS;
                            TANK_DRIVE_RIGHT_AXIS = X3D.PITCH_AXIS;
                            ARCADE_DRIVE_FORWARD_AXIS = -1;
                            ARCADE_DRIVE_ANGLE_AXIS = -1;

                            driveStraightJoystickButton = new JoystickButton(primaryJoystick, X3D.GRIP_BUTTON_ID);
                            driveStraightJoystickButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
                            driveStraightPOVButton = null;

                            // Intake

                            runRollerDisposalButton = new JoystickButton(primaryJoystick, X3D.BUTTON_11_ID);
                            runRollerDisposalButton.whenHeld(new RunRollerDisposal(rc.intake));

                            runRollerIntakebutton = new JoystickButton(primaryJoystick, X3D.BUTTON_12_ID);
                            runRollerIntakebutton.whenHeld(new RunRollerIntake(rc.intake));

                            toggleIntakeButton = new JoystickButton(primaryJoystick, X3D.BUTTON_3_ID);
                            toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                            
                            // Manipulator

                            runIndexerButton = new JoystickButton(primaryJoystick, X3D.BUTTON_3_ID);
                            runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));

                            runLauncherButton = new JoystickButton(primaryJoystick, X3D.TRIGGER_BUTTON_ID);
                            runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));
                            break;
                    }
                    break;
            }
        }
        
        return this;
    }
}