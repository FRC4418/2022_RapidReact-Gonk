// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class TeleopInput extends SubsystemBase {
	public static class MacroMode {
		private static final double	
			moveClimberInputRate = 1.0,
			spinControlPanelInputRate = 1.0,
			spinWinchInputRate = 1.0,
			arcadeDriveMagnitudeInputRate = 1.0,
			tankDriveMagnitudeInputRate = 1.0;
	}

	public static class MicroMode {
		private static final double
			moveClimberInputRate = 0.3,
			spinControlPanelInputRate = 0.3,
			spinWinchInputRate = 0.3,
			arcadeDriveMagnitudeInputRate = 0.3,
			tankDriveMagnitudeInputRate = 0.3;
	}

	public static double
		moveClimberInputRate,
		spinControlPanelInputRate,
		spinWinchInputRate,
		arcadeDriveMagnitudeInputRate,
		tankDriveMagnitudeInputRate;

	private boolean usingMacroSensitivity = true;

	public TeleopInput() {
		if (usingMacroSensitivity) {
			setToMacroMode();
		} else {
			setToMicroMode();
		}
	}

	public void toggleSensitivityMode() {
		usingMacroSensitivity = !usingMacroSensitivity;
		if (usingMacroSensitivity) {
			setToMacroMode();
		} else {
			setToMicroMode();
		}
	}

	private void setToMacroMode() {
		moveClimberInputRate = MacroMode.moveClimberInputRate;
		spinControlPanelInputRate = MacroMode.spinControlPanelInputRate;
		spinWinchInputRate = MacroMode.spinWinchInputRate;
		arcadeDriveMagnitudeInputRate = MacroMode.arcadeDriveMagnitudeInputRate;
		tankDriveMagnitudeInputRate = MacroMode.tankDriveMagnitudeInputRate;
	}

	private void setToMicroMode() {
		moveClimberInputRate = MicroMode.moveClimberInputRate;
		spinControlPanelInputRate = MicroMode.spinControlPanelInputRate;
		spinWinchInputRate = MicroMode.spinWinchInputRate;
		arcadeDriveMagnitudeInputRate = MicroMode.arcadeDriveMagnitudeInputRate;
		tankDriveMagnitudeInputRate = MicroMode.tankDriveMagnitudeInputRate;
	}

	@Override
	public void periodic() {
		
	}
}
