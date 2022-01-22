// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Autonomous extends SubsystemBase {
	// TODO: Figure out what autonomous strategy options we should have
	public enum AutonomousRoutine {
		DRIVE_STRAIGHT_BACKWARDS,
		DRIVE_STRAIGHT_TO_LOW_HUB
	}

	public Autonomous() {
		
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}
