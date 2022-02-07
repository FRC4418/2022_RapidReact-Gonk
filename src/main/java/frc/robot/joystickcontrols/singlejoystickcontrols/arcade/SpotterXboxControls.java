package frc.robot.joystickcontrols.singlejoystickcontrols.arcade;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.joystickcontrols.IO.XboxController;
import frc.robot.joystickcontrols.singlejoystickcontrols.SingleJoystickControls;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class SpotterXboxControls extends SingleJoystickControls {
    // ----------------------------------------------------------
    // Joystick helpers

    @Override
    protected double deadband() {
        return XboxController.JOYSTICK_DEADBAND;
    }

    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public double getArcadeDriveForwardAxis() {
        return 0.d;
    }

    @Override
    public double getArcadeDriveTurnAxis() {
        return 0.d;
    }

    
    @Override
    public double getTankDriveLeftAxis() {
        return 0.d;
    }

    @Override
    public double getTankDriveRightAxis() {
        return 0.d;
    }

    // ----------------------------------------------------------
    // Drivetrain buttons

    @Override
    public JoystickButton reverseDrivetrainButton(Joystick joystick) {
        return null;
    }

    @Override
    protected POVButton driveStraightPOVButton(Joystick joystick) {
        return new POVButton(joystick, XboxController.ANGLE_UP_POV);
    }

    @Override
    protected JoystickButton driveStraightJoystickButton(Joystick joystick) {
        return null;
    }

    // ----------------------------------------------------------
    // Intake axes

    @Override
    public double getReverseFeederAxis() {
        return m_primaryJoystick.getRawAxis(XboxController.LEFT_TRIGGER_AXIS);
    }

    @Override
    public double getFeederAxis() {
        return m_primaryJoystick.getRawAxis(XboxController.RIGHT_TRIGGER_AXIS);
    }

    // ----------------------------------------------------------
    // Intake buttons

    @Override
    protected JoystickButton runFeederDisposalButton(Joystick joystick) {
        return null;
    }

    @Override
    protected JoystickButton runFeederButton(Joystick joystick) {
        return null;
    }

    @Override
    public JoystickButton toggleFeederButton(Joystick joystick) {
        return null;
    }

    // ----------------------------------------------------------
    // Manipulator buttons

    @Override
    protected JoystickButton runIndexerButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.B_BUTTON_ID);
    }

    @Override
    protected JoystickButton runLauncherButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.RIGHT_BUMPER_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public SpotterXboxControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, drivetrain, intake, manipulator);
    }
}
