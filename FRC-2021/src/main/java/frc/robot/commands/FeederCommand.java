// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;


public class FeederCommand extends CommandBase {
	public FeederCommand() {
		
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		RobotContainer.manipulatorsubsystem.setShoulderFireMotor(-0.5);
	}

	@Override
	public void end(boolean interrupted) {
		RobotContainer.manipulatorsubsystem.setShoulderFireMotor(0.0);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
