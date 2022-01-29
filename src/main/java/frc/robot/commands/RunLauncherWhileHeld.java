// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Manipulator;



public class RunLauncherWhileHeld extends CommandBase {
	/** Creates a new RunLuancher. */
	//TODO: Add requirements

	private final double motorOutputPercent = 0.7d;
	private final Manipulator ms;

	public RunLauncherWhileHeld(Manipulator manipulator) {	
		ms = manipulator; 
	}	

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}	

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ms.setHighMotorPercent(motorOutputPercent);
		ms.setLowMotorPercent(motorOutputPercent);
	}	

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ms.setLowMotorPercent(0.d);
		ms.setHighMotorPercent(0.d);
	}	

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
	  return false;
	}
}
