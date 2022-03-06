package frc.robot.joystickcontrols.singlejoystickcontrols;


import edu.wpi.first.wpilibj.Joystick;

import frc.robot.commands.climber.ToggleClimberPins;
import frc.robot.commands.drivetrain.DriveStraight;
import frc.robot.commands.drivetrain.ReverseDrivetrain;
import frc.robot.commands.drivetrain.DriveStraight.DriveStraightDirection;
import frc.robot.commands.intake.ExtendIntakeArmWhileHeld;
import frc.robot.commands.intake.RunFeederAndIndexer;
import frc.robot.commands.intake.RunReverseFeeder;
import frc.robot.commands.intake.ToggleIndexBall;
import frc.robot.commands.manipulator.RunIndexer;
import frc.robot.commands.manipulator.RunLauncher;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public abstract class SingleJoystickControls extends JoystickControls {
    // ----------------------------------------------------------
    // Joysticks
    
    protected final Joystick m_primaryJoystick;

    // ----------------------------------------------------------
    // Joystick helpers

    @Override
    public boolean isActivelyDriving() {
        return m_primaryJoystick.getMagnitude() > Math.sqrt(DEADBAND * 2.);
    }

    @Override
    public int getPrimaryJoystickPort() {
        return m_primaryJoystick.getPort();
    }

    @Override
    public int getSecondaryJoystickPort() {
        // it's weird, but this obviously erroneous implementation is needed by the driver to override, since the base JoystickControls class is the common type used for our polymorphic code
        return -1;
    }

    // ----------------------------------------------------------
    // Constructor

    public SingleJoystickControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator, Climber climber) {
        m_primaryJoystick = primaryJoystick;

        // ----------------------------------------------------------
        // Drivetrain

        reverseDrivetrainButton = reverseDrivetrainButton(primaryJoystick);
        if (reverseDrivetrainButton != null) {
            reverseDrivetrainButton.whenPressed(new ReverseDrivetrain(drivetrain));

            reverseDrivetrainButton.whenReleased(new ReverseDrivetrain(drivetrain));
        }
        driveStraightPOVButton = driveStraightPOVButton(primaryJoystick);
        if (driveStraightPOVButton != null) driveStraightPOVButton.whenHeld(new DriveStraight(drivetrain, DriveStraightDirection.FORWARDS));
        driveStraightJoystickButton = driveStraightJoystickButton(primaryJoystick);
        if (driveStraightJoystickButton != null) driveStraightJoystickButton.whenHeld(new DriveStraight(drivetrain, DriveStraightDirection.FORWARDS));

        // ----------------------------------------------------------
        // Intake
 
        runFeederDisposalButton = runReverseFeederButton(primaryJoystick);
        if (runFeederDisposalButton != null) runFeederDisposalButton.whenHeld(new RunReverseFeeder(intake));
        runFeederIntakebutton = runFeederButton(primaryJoystick);
        if (runFeederIntakebutton != null) runFeederIntakebutton.whenHeld(new RunFeederAndIndexer(intake, manipulator));
        toggleFeederButton = toggleFeederButton(primaryJoystick);
        if (toggleFeederButton != null) toggleFeederButton.toggleWhenPressed(new ToggleIndexBall(intake, manipulator));
        extendIntakeArmButton = extendIntakeArmButton(primaryJoystick);
        if (extendIntakeArmButton != null) extendIntakeArmButton.whenHeld(new ExtendIntakeArmWhileHeld(intake));

        // ----------------------------------------------------------
        // Manipulator

        runIndexerButton = runIndexerButton(primaryJoystick);
        if (runIndexerButton != null) runIndexerButton.whenHeld(new RunIndexer(manipulator));
        runLauncherButton = runLauncherButton(primaryJoystick);
        if (runLauncherButton != null) runLauncherButton.whenHeld(new RunLauncher(manipulator));

        // ----------------------------------------------------------
        // Climber

        toggleClimberPinsButton = toggleClimberPinsButton(primaryJoystick);
        if (toggleClimberPinsButton != null) toggleClimberPinsButton.whenPressed(new ToggleClimberPins(climber));
    }
}
