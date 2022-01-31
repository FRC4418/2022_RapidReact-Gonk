package frc.robot.IO;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.RobotContainer;
import frc.robot.IO.Joysticks.X3D;
import frc.robot.IO.Joysticks.XboxController;
import frc.robot.RobotContainer.JoystickMode;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.RunIndexer;
import frc.robot.commands.RunLauncher;
import frc.robot.commands.ToggleIntake;
import frc.robot.displays.JoysticksDisplay;


// TODO: Different set of driver controls for tank mode
public class DriverControls {
    // ----------------------------------------------------------
    // Resources

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

    private JoystickButton
        driveStraightButton,
        
        toggleIntakeButton,
        runIndexerButton,
        runLauncherButton;

    // ----------------------------------------------------------
    // Constructor and methods

    public DriverControls(RobotContainer robotContainer, JoysticksDisplay joysticksDisplay) {
        rc = robotContainer;
        jd = joysticksDisplay;
    }

    public DriverControls listenForJoystickMode() {
        JoystickMode newJoystickMode = jd.driverJoystickModeChooser.getSelected();
        if (joystickMode != newJoystickMode) {
            joystickMode = newJoystickMode;

            leftmostJoystick = new Joystick(0);

            switch (joystickMode) {
                case ARCADE:
                case LONE_TANK:
                    rightmostJoystick = null;
                    break;
                case DUAL_TANK:
                    rightmostJoystick = new Joystick(1);
                    break;
            }

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
        return leftmostJoystick.getRawAxis(TANK_DRIVE_LEFT_AXIS);
    }

    public double getTankDriveRightAxis() {
        return rightmostJoystick.getRawAxis(TANK_DRIVE_RIGHT_AXIS);
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

    // if controller2 is null, then configure button bindings for the driver's single controller
    public DriverControls configureButtonBindingsFor(Joystick leftmostJoystick, Joystick rightmostJoystick) {
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
                    switch (primaryJoystick.getName()) {
                        default:                        
                            DriverStation.reportError("Only Xbox controllers and X3D joysticks are supported for single-joystick ARCADE mode", true);
                            break;
                        // if using a single Xbox controller
                        case XboxController.USB_DEVICE_NAME:       
                            // Drivetrain

                            ARCADE_DRIVE_FORWARD_AXIS = XboxController.LEFT_Y_AXIS;
                            ARCADE_DRIVE_ANGLE_AXIS = XboxController.LEFT_X_AXIS;
                            TANK_DRIVE_LEFT_AXIS = -1;
                            TANK_DRIVE_RIGHT_AXIS = -1;
        
                            driveStraightButton = new JoystickButton(primaryJoystick, XboxController.ANGLE_UP_POV);
                            driveStraightButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
        
                            // Intake
        
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
                            // Drivetrain

                            ARCADE_DRIVE_FORWARD_AXIS = X3D.PITCH_AXIS;
                            ARCADE_DRIVE_ANGLE_AXIS = X3D.ROLL_AXIS;
                            TANK_DRIVE_LEFT_AXIS = -1;
                            TANK_DRIVE_RIGHT_AXIS = -1;
        
                            driveStraightButton = new JoystickButton(primaryJoystick, X3D.GRIP_BUTTON_ID);
                            driveStraightButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
        
                            // Intake
        
                            toggleIntakeButton = new JoystickButton(primaryJoystick, X3D.BUTTON_4_ID);
                            toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                            
                            // Manipulator

                            runIndexerButton = new JoystickButton(primaryJoystick, X3D.BUTTON_3_ID);
                            runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));
        
                            runLauncherButton = new JoystickButton(primaryJoystick, X3D.TRIGGER_BUTTON_ID);
                            runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));
                            break;                
                    }
                    break;
                // tank mode using one joystick
                case LONE_TANK:
                    // since we use a single joystick for both the left and right tank drive axes, doesn't matter which joystick ref we get the name from
                    switch (primaryJoystick.getName()) {
                        default:                        
                            DriverStation.reportError("Only Xbox controllers are supported for single-joystick TANK mode", true);
                            break;
                        // if using a single Xbox controller
                        case XboxController.USB_DEVICE_NAME:
                            // Drivetrain

                            TANK_DRIVE_LEFT_AXIS = XboxController.LEFT_Y_AXIS;
                            TANK_DRIVE_RIGHT_AXIS = XboxController.RIGHT_Y_AXIS;
                            ARCADE_DRIVE_FORWARD_AXIS = -1;
                            ARCADE_DRIVE_ANGLE_AXIS = -1;
        
                            driveStraightButton = new JoystickButton(primaryJoystick, XboxController.ANGLE_UP_POV);
                            // TODO: Make command-binding for buttons less repetitive for all joystick quantities and modes
                            driveStraightButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));
        
                            // Intake
        
                            toggleIntakeButton = new JoystickButton(primaryJoystick, XboxController.A_BUTTON_ID);
                            toggleIntakeButton.toggleWhenPressed(new ToggleIntake(rc.intake));
                            
                            // Manipulator

                            runIndexerButton = new JoystickButton(primaryJoystick, XboxController.B_BUTTON_ID);
                            runIndexerButton.whenHeld(new RunIndexer(rc.manipulator));
        
                            runLauncherButton = new JoystickButton(primaryJoystick, XboxController.RIGHT_BUMPER_BUTTON_ID);
                            runLauncherButton.whenHeld(new RunLauncher(rc.manipulator));

                            break;
                    }
                    break;
            }
        // both joysticks are non-null
        } else {
            primaryJoystick = rightmostJoystick;
            secondaryJoystick = leftmostJoystick;

            switch (joystickMode) {
                default:
                    DriverStation.reportError("Only the DUAL_TANK mode is supported for dual-joystick driver controls", true);
                    break;
                // tank mode using two joysticks
                case DUAL_TANK:
                    // if you don't know why we do .equals() instead of == for string comparison, you're just bad lol
                    if (primaryJoystick.getName().equals(secondaryJoystick.getName())) {
                        DriverStation.reportError("Only same-device-type joysticks (ex. two X3Ds) are supported for dual-joystick TANK mode", true);
                    }

                    // we've established that the left and right joysticks are the same device-type, so we can just arbitrarily choose one of them to know the device-type
                    switch (primaryJoystick.getName()) {
                        default:                            
                            DriverStation.reportError("Only X3D joysticks are supported for dual-joystick TANK mode", true);
                            break;
                        case X3D.USB_DEVICE_NAME:
                            // Drivetrain

                            TANK_DRIVE_LEFT_AXIS = X3D.PITCH_AXIS;
                            TANK_DRIVE_RIGHT_AXIS = X3D.PITCH_AXIS;
                            ARCADE_DRIVE_FORWARD_AXIS = -1;
                            ARCADE_DRIVE_ANGLE_AXIS = -1;

                            driveStraightButton = new JoystickButton(primaryJoystick, X3D.GRIP_BUTTON_ID);
                            driveStraightButton.whenHeld(new DriveStraightWhileHeld(rc.drivetrain));

                            // Intake

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