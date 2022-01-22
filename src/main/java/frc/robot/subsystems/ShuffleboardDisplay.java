// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class ShuffleboardDisplay extends SubsystemBase {
	public static ShuffleboardTab statusDisplayTab;
	
	public NetworkTableEntry tuningModeBooleanBox;
	
	public SendableChooser<AutonomousRoutine> sendableAutoRoutineChooser = new SendableChooser<>();
	
	private boolean inTuningMode;

	/** Creates a new HUDSubsystem. */
	public ShuffleboardDisplay() {
		
	}

	public void initializeDisplay() {
		statusDisplayTab = Shuffleboard.getTab("4418 Status Display");
		
		tuningModeBooleanBox = statusDisplayTab
			.add("Motor Tuning", false)
			.withWidget(BuiltInWidgets.kToggleSwitch)
			.withPosition(0, 0)
			.withSize(2, 1)
			.getEntry();

		sendableAutoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
		sendableAutoRoutineChooser.addOption("Drive Striaght to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
		statusDisplayTab
			.add("Autonomous Routine", sendableAutoRoutineChooser)
			.withWidget(BuiltInWidgets.kComboBoxChooser)
			.withPosition(0, 1)
			.withSize(2, 1);

		
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
		inTuningMode = tuningModeBooleanBox.getBoolean(false);
		SmartDashboard.putBoolean("Motor Tuning Mode Is On", inTuningMode);
	}
}