// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.Constants.Manipulator.ConveyorShooter;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Telemetry;


public class ConveyerDemo extends CommandBase {
	private Manipulator ms;
	private Telemetry tt;

	public ConveyerDemo() {
		// Use addRequirements() here to declare subsystem dependencies
		ms = RobotContainer.manipulator;
		tt = RobotContainer.telemetry;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ms.setLowConveyerMotor(tt.lowerConveyorMotorPercentTextView.getDouble(ConveyorShooter.DEFAULT_LOWER_MOTOR_PERCENT));
		ms.setHighConveyerMotor(tt.higherConveyorMotorPercentTextView.getDouble(ConveyorShooter.DEFAULT_HIGHER_MOTOR_PERCENT));
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ms.setLowConveyerMotor(0.0d);
		ms.setHighConveyerMotor(0.0d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
