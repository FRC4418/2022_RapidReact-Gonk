package frc.robot.commands;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ToggleIntake extends CommandBase {
	// ----------------------------------------------------------
	// Constants

	private final double INTAKE_ROLLER_OUTPUT_PERCENT = 0.3d;
	private final double INTAKE_DELAY_TIME = 2.d;
	
	// ----------------------------------------------------------
	// Resources

	private final Intake it;

	private final DigitalInput whiskerSensor = new DigitalInput(2);

	// delay to keep the intake running for a few seconds, even after we trip the whisker sensor
	private final Timer postWhiskerSensorDelayTimer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public ToggleIntake(Intake intake) {
		it = intake;
		// addRequirements(it);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		it.setRollerMotorPercent(INTAKE_ROLLER_OUTPUT_PERCENT);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		if (whiskerSensor.get() == true) {
			postWhiskerSensorDelayTimer.start();
		}

		if (postWhiskerSensorDelayTimer.hasElapsed(INTAKE_DELAY_TIME)) {
			postWhiskerSensorDelayTimer.stop();
			postWhiskerSensorDelayTimer.reset();
			return true;
		}

		return false;
	}
}
