package frc.robot.joystickcontrols.singlejoystickcontrols.arcade;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.joystickcontrols.IO.X3D;
import frc.robot.joystickcontrols.singlejoystickcontrols.SingleJoystickControls;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class DriverX3DArcadeControls extends SingleJoystickControls {
    // ----------------------------------------------------------
    // Joystick helpers

    @Override
    protected double deadband() {
        return X3D.JOYSTICK_DEADBAND;
    }

    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public double getArcadeDriveForwardAxis() {
        return m_primaryJoystick.getRawAxis(X3D.PITCH_AXIS);
    }

    @Override
    public double getArcadeDriveTurnAxis() {
        return m_primaryJoystick.getRawAxis(X3D.ROLL_AXIS);
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
    protected JoystickButton reverseDrivetrainButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_6_ID);
    }

    @Override
    protected JoystickButton driveStraightJoystickButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.GRIP_BUTTON_ID);
    }

    @Override
    protected POVButton driveStraightPOVButton(Joystick joystick) {
        return null;
    }

    // ----------------------------------------------------------
    // Intake axes

    @Override
    public double getReverseFeederAxis() {
        return 0.d;
    }

    @Override
    public double getFeederAxis() {
        return 0.d;
    }

    // ----------------------------------------------------------
    // Intake buttons

    @Override
    protected JoystickButton runFeederDisposalButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_11_ID);
    }

    @Override
    protected JoystickButton runFeederButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_12_ID);
    }

    @Override
    protected JoystickButton toggleFeederButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_4_ID);
    }

    // ----------------------------------------------------------
    // Manipulator buttons

    @Override
    protected JoystickButton runIndexerButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_3_ID);
    }

    @Override
    protected JoystickButton runLauncherButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.TRIGGER_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public DriverX3DArcadeControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, drivetrain, intake, manipulator);
        
        m_primaryJoystick.setXChannel(X3D.ROLL_AXIS);
        m_primaryJoystick.setYChannel(X3D.PITCH_AXIS);
    }
}
