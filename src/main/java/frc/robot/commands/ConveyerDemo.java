// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;

public class ConveyerDemo extends CommandBase {

  private Manipulator ms;
  /** Creates a new ConveyerDemo. */
  public ConveyerDemo() {
    // Use addRequirements() here to declare subsystem dependencies
    ms = RobotContainer.manipulator;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ms.setLowConveyerMotor(0.6);
    ms.setHighConveyerMotor(0.6);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ms.setLowConveyerMotor(0.0);
    ms.setHighConveyerMotor(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
