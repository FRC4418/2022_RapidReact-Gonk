package frc.robot.joystickcontrols.arcade;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.joystickcontrols.SingleJoystickControls;
import frc.robot.joystickcontrols.IO.X3D;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class X3DArcadeControls extends SingleJoystickControls {
    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
    public double getArcadeDriveForwardAxis() {
        return m_primaryJoystick.getRawAxis(X3D.PITCH_AXIS);
    }

    @Override
    public double getArcadeDriveAngleAxis() {
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
    public JoystickButton driveStraightJoystickButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.GRIP_BUTTON_ID);
    }

    @Override
    public POVButton driveStraightPOVButton(Joystick joystick) {
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
    public JoystickButton runFeederDisposalButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_11_ID);
    }

    @Override
    public JoystickButton runFeederButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_12_ID);
    }

    @Override
    public JoystickButton toggleFeederButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_4_ID);
    }

    // ----------------------------------------------------------
    // Manipulator buttons

    @Override
    public JoystickButton runIndexerButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.BUTTON_3_ID);
    }

    @Override
    public JoystickButton runLauncherButton(Joystick joystick) {
        return new JoystickButton(joystick, X3D.TRIGGER_BUTTON_ID);
    }

    // ----------------------------------------------------------
    // Constructor

    public X3DArcadeControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        super(primaryJoystick, drivetrain, intake, manipulator);
    }
}
