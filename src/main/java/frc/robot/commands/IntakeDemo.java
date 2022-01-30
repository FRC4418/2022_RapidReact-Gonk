package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.HUD;


public class IntakeDemo extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final HUD hud;

	// ----------------------------------------------------------
	// Constructor

	public IntakeDemo(Intake intake, HUD hud) {
		it = intake;
		this.hud = hud;
		addRequirements(it);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		if (hud.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (hud.rollerIntakeMotorToggleSwitch.getBoolean(false)) {
				it.setRollerMotorPercent(hud.rollerIntakeMotorPercentTextView.getDouble(Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				it.setRollerMotorPercent(0.d);
			}

			if (hud.retractIntakeMotorToggleSwitch.getBoolean(false)) {
				it.setRetractMotorPosition(hud.retractIntakeMotorPositionTextView.getDouble(Intake.RETRACT_MOTOR_DEFAULT_POSITION));
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
