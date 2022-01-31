package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class IntakeTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final MotorTestingDisplay mt;

	// ----------------------------------------------------------
	// Constructor

	public IntakeTesting(Intake intake, HUD hud) {
		it = intake;
		mt = hud.motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: Event listeners for intake testing

	@Override
	public void execute() {
		if (mt.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (mt.rollerToggleSwitch.getBoolean(false)) {
				it.setRollerMotorPercent(mt.rollerOutputPercentTextView.getDouble(Intake.DEFAULT_ROLLER_OUTPUT_PERCENT));
			} else {
				it.setRollerMotorPercent(0.d);
			}

			if (mt.retractorToggleSwitch.getBoolean(false)) {
				it.setRetractMotorPosition(mt.retractorPositionTextView.getDouble(Intake.DEFAULT_RETRACTOR_POSITION));
			} else {
				it.setRetractMotorPosition(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
		// it.setRetractMotorPosition(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
