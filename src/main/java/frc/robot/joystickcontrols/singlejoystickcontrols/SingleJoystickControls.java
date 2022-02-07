package frc.robot.joystickcontrols.singlejoystickcontrols;


import edu.wpi.first.wpilibj.Joystick;

import frc.robot.commands.drivetrain.DriveStraight;
import frc.robot.commands.drivetrain.ReverseDrivetrain;
import frc.robot.commands.intake.RunFeeder;
import frc.robot.commands.intake.RunReverseFeeder;
import frc.robot.commands.intake.ToggleFeeder;
import frc.robot.commands.manipulator.RunIndexer;
import frc.robot.commands.manipulator.RunLauncher;
import frc.robot.joystickcontrols.JoystickControls;
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
        return m_primaryJoystick.getMagnitude() > Math.sqrt(DEADBAND * 2.d);
    }

    // ----------------------------------------------------------
    // Constructor

    public SingleJoystickControls(Joystick primaryJoystick, Drivetrain drivetrain, Intake intake, Manipulator manipulator) {
        m_primaryJoystick = primaryJoystick;

        // ----------------------------------------------------------
        // Drivetrain

        reverseDrivetrainButton = reverseDrivetrainButton(primaryJoystick);
        if (reverseDrivetrainButton != null) reverseDrivetrainButton.whenHeld(new ReverseDrivetrain(drivetrain));
        driveStraightPOVButton = driveStraightPOVButton(primaryJoystick);
        if (driveStraightPOVButton != null) driveStraightPOVButton.whenHeld(new DriveStraight(drivetrain));
        driveStraightJoystickButton = driveStraightJoystickButton(primaryJoystick);
        if (driveStraightJoystickButton != null) driveStraightJoystickButton.whenHeld(new DriveStraight(drivetrain));

        // ----------------------------------------------------------
        // Intake

        runFeederDisposalButton = runFeederDisposalButton(primaryJoystick);
        if (runFeederDisposalButton != null) runFeederDisposalButton.whenHeld(new RunReverseFeeder(intake));
        runFeederIntakebutton = runFeederButton(primaryJoystick);
        if (runFeederIntakebutton != null) runFeederIntakebutton.whenHeld(new RunFeeder(intake));
        toggleFeederButton = toggleFeederButton(primaryJoystick);
        if (toggleFeederButton != null) toggleFeederButton.toggleWhenPressed(new ToggleFeeder(intake));

        // ----------------------------------------------------------
        // Manipulator

        runIndexerButton = runIndexerButton(primaryJoystick);
        if (runIndexerButton != null) runIndexerButton.whenHeld(new RunIndexer(manipulator));
        runLauncherButton = runLauncherButton(primaryJoystick);
        if (runLauncherButton != null) runLauncherButton.whenHeld(new RunLauncher(manipulator));
    }
}
