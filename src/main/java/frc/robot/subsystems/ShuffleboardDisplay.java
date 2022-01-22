// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShuffleboardDisplay extends SubsystemBase {
	public static ShuffleboardTab statusDisplayTab = Shuffleboard.getTab("4418 Status Display");

	public NetworkTableEntry tuningModeBooleanBox = statusDisplayTab
		.add("Motor Tuning Mode", false)
		.withWidget(BuiltInWidgets.kBooleanBox)
		.withPosition(0, 0)
		.withSize(2, 1)
		.getEntry();
	
	private boolean inTuningMode;

	/** Creates a new HUDSubsystem. */
	public ShuffleboardDisplay() {
		
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		inTuningMode = tuningModeBooleanBox.getBoolean(false);
		SmartDashboard.putBoolean("Motor Tuning Mode Is On", inTuningMode);
	}
}