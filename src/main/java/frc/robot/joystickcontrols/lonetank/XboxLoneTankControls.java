package frc.robot.joystickcontrols.lonetank;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.joystickcontrols.SingleJoystickControls;
import frc.robot.joystickcontrols.IO.XboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class XboxLoneTankControls extends SingleJoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public int arcadeDriveForwardAxis() {
        return -1;
    }

    @Override
    public int arcadeDriveAngleAxis() {
        return -1;
    }

    @Override
    public int tankDriveLeftAxis() {
        return XboxController.LEFT_Y_AXIS;
    }

    @Override
    public int tankDriveRightAxis() {
        return XboxController.RIGHT_Y_AXIS;
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
    public int reverseFeederAxis() {
        return XboxController.LEFT_TRIGGER_AXIS;
    }

    @Override
    public int feederAxis() {
        return XboxController.RIGHT_TRIGGER_AXIS;
    }

    // ----------------------------------------------------------
    // Intake buttons

    @Override
    public JoystickButton runFeederDisposalButton(Joystick joystick) {
        return null;
    }

    @Override
    public JoystickButton runFeederButton(Joystick joystick) {
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

    public XboxLoneTankControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, drivetrain, intake, manipulator);
    }
}
