package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class IntakeTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake intake;
	private final MotorTestingDisplay motorTestingDisplay;

	// ----------------------------------------------------------
	// Constructor

	public IntakeTesting(Intake intake, HUD hud) {
		this.intake = intake;
		this.motorTestingDisplay = hud.motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: Event listeners for intake testing

	@Override
	public void execute() {
		if (motorTestingDisplay.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (motorTestingDisplay.rollerToggleSwitch.getBoolean(false)) {
				intake.setRollerMotorPercent(motorTestingDisplay.rollerOutputPercentTextView.getDouble(Intake.DEFAULT_ROLLER_INTAKE_OUTPUT_PERCENT));
			} else {
				intake.setRollerMotorPercent(0.d);
			}

			if (motorTestingDisplay.retractorToggleSwitch.getBoolean(false)) {
				intake.setRetractMotorPosition(motorTestingDisplay.retractorPositionTextView.getDouble(Intake.DEFAULT_RETRACTOR_POSITION));
			} else {
				intake.setRetractMotorPosition(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		intake.setRollerMotorPercent(0.d);
		// it.setRetractMotorPosition(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
