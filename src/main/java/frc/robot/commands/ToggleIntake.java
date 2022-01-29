// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ToggleIntake extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final DigitalInput whiskerSensor = new DigitalInput(2);

	// ----------------------------------------------------------
	// Constructor

	public ToggleIntake(Intake intake) {
		it = intake;
		addRequirements(it);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		it.setRollerMotorPercent(0.3d);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {

	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		// TODO: Figure out adding 1-2 sec (or smth) delay before stopping intake
		return whiskerSensor.get();	// when whisker sensor is tripped, stop running the intake
	}
}
