package frc.robot.IO;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.IO.Constants.XboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class XboxArcadeControls extends JoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public int arcadeDriveForwardAxis() {
        return XboxController.LEFT_Y_AXIS;
    }

    @Override
    public int arcadeDriveAngleAxis() {
        return XboxController.LEFT_X_AXIS;
    }

    @Override
    public int tankDriveLeftAxis() {
        return -1;
    }

    @Override
    public int tankDriveRightAxis() {
        return -1;
    }

    // ----------------------------------------------------------
    // Drivetrain buttons

    @Override
    public POVButton driveStraightPOVButton(Joystick joystick) {
        return new POVButton(joystick, XboxController.ANGLE_UP_POV);
    }

    @Override
    public JoystickButton driveStraightJoystickButton(Joystick joystick) {
        return null;
    }

    // ----------------------------------------------------------
    // Intake axes

    @Override
    public int rollerDisposalAxis() {
        return XboxController.LEFT_TRIGGER_AXIS;
    }

    @Override
    public int rollerIntakeAxis() {
        return XboxController.RIGHT_TRIGGER_AXIS;
    }

    // ----------------------------------------------------------
    // Intake buttons

    @Override
    public JoystickButton runRollerDisposalButton(Joystick joystick) {
        return null;
    }

    @Override
    public JoystickButton runRollerIntakebutton(Joystick joystick) {
        return null;
    }

    @Override
    public JoystickButton toggleIntakeButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.A_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Manipulator buttons

    @Override
    public JoystickButton runIndexerButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.B_BUTTON_ID);
    }

    @Override
    public JoystickButton runLauncherButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.RIGHT_BUMPER_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public XboxArcadeControls(Joystick primaryJoystick, Joystick secondaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, secondaryJoystick, drivetrain, intake, manipulator);
    }
}
