package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class IntakeDemo extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final MotorTestingDisplay mt;

	// ----------------------------------------------------------
	// Constructor

	public IntakeDemo(Intake intake, HUD hud) {
		it = intake;
		mt = hud.motorTestingDisplay;
		addRequirements(it);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		if (mt.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (mt.intakeRollerToggleSwitch.getBoolean(false)) {
				it.setRollerMotorPercent(mt.intakeRollerOutputPercentTextView.getDouble(Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				it.setRollerMotorPercent(0.d);
			}

			if (mt.intakeRetractorToggleSwitch.getBoolean(false)) {
				it.setRetractMotorPosition(mt.intakeRetractorPositionTextView.getDouble(Intake.RETRACT_MOTOR_DEFAULT_POSITION));
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
