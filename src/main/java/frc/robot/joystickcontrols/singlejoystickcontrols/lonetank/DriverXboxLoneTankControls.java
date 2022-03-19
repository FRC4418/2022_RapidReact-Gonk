package frc.robot.joystickcontrols.singlejoystickcontrols.lonetank;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.joystickcontrols.IO.XboxController;
import frc.robot.joystickcontrols.singlejoystickcontrols.SingleJoystickControls;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class DriverXboxLoneTankControls extends SingleJoystickControls {
    // ----------------------------------------------------------
    // Joystick helpers

    @Override
    protected double deadband() {
        return XboxController.JOYSTICK_DEADBAND;
    }

    // ----------------------------------------------------------
    // Drivetrain axes

    @Override
	public double getCurvatureForwardAxis() {
		return 0.;
	}

	@Override
	public double getCurvatureRotationAxis() {
		return 0.;
	}


    @Override
    public double getArcadeForwardAxis() {
        return 0.;
    }

    @Override
    public double getArcadeTurnAxis() {
        return 0.;
    }

    
    @Override
    public double getTankLeftAxis() {
        return -m_primaryJoystick.getRawAxis(XboxController.LEFT_Y_AXIS);
    }

    @Override
    public double getTankRightAxis() {
        return -m_primaryJoystick.getRawAxis(XboxController.RIGHT_Y_AXIS);
    }

    // ----------------------------------------------------------
    // Drivetrain buttons

    @Override
    protected JoystickButton reverseDrivetrainButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.X_BUTTON_ID);
    }

    @Override
    protected JoystickButton driveStraightButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.VIEW_BUTTON_ID);
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
    protected JoystickButton toggleFeederButton(Joystick joystick) {
        return new JoystickButton(joystick, XboxController.A_BUTTON_ID);
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
	protected POVButton extendClimberButton(Joystick joystick) {
		return new POVButton(joystick, XboxController.ANGLE_UP_POV);
	}

	@Override
	protected POVButton lowerClimberButton(Joystick joystick) {
		return new POVButton(joystick, XboxController.ANGLE_DOWN_POV);
	}

    // ----------------------------------------------------------
    // Constructor

    public DriverXboxLoneTankControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator, Climber climber) {
        super(primaryJoystick, drivetrain, intake, manipulator, climber);

        m_primaryJoystick.setXChannel(XboxController.LEFT_Y_AXIS);
        m_primaryJoystick.setYChannel(XboxController.RIGHT_Y_AXIS);
    }
}