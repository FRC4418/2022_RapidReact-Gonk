package frc.robot.joystickcontrols.singlejoystickcontrols.arcade;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.joystickcontrols.IO.XboxController;
import frc.robot.joystickcontrols.singlejoystickcontrols.SingleJoystickControls;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class SpotterXboxLoneTankControls extends SingleJoystickControls {
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
        return 0.;
    }

    @Override
    public double getArcadeDriveTurnAxis() {
        return 0.;
    }

    
    @Override
    public double getTankDriveLeftAxis() {
        // dumb Xbox controller gives NEGATIVE values when you push the joystick FORWARAD
        return -m_primaryJoystick.getRawAxis(XboxController.LEFT_Y_AXIS);
    }

    @Override
    public double getTankDriveRightAxis() {
        // dumb Xbox controller gives NEGATIVE values when you push the joystick FORWARAD
        return -m_primaryJoystick.getRawAxis(XboxController.RIGHT_Y_AXIS);
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
    protected JoystickButton runReverseFeederButton(Joystick joystick) {
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

    @Override
    protected JoystickButton extendIntakeArmButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.Y_BUTTON_ID);
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
    // Climber buttons

    @Override
    protected JoystickButton extendClimberButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.MENU_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public SpotterXboxLoneTankControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator, Climber climber) {
        super(primaryJoystick, drivetrain, intake, manipulator, climber);
    }
}
