// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Intake;

public class RunFeederWithTrigger extends CommandBase {

  private final Intake m_intake;
  private final JoystickControls m_joystickControls;

  /** Creates a new RunFeederWithTrigger. */
  public RunFeederWithTrigger(Intake intake, JoystickControls joystickControls) {
    this.m_intake = intake;
    this.m_joystickControls = joystickControls;

    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intake.setFeederMotorPercent(m_joystickControls.feederAxis());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
