package frc.robot.commands;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ToggleFeeder extends CommandBase {
	// ----------------------------------------------------------
	// Constants

	private final double MOTOR_OUTPUT_PERCENT = 0.3d;
	private final double DELAY_TIME = 2.d;
	
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;

	private final DigitalInput m_whiskerSensor = new DigitalInput(2);

	// delay to keep the intake running for a few seconds, even after we trip the whisker sensor
	private final Timer m_postWhiskerSensorDelayTimer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public ToggleFeeder(Intake intake) {
		this.m_intake = intake;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_intake.setFeederMotorPercent(MOTOR_OUTPUT_PERCENT);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		m_intake.setFeederMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		if (m_whiskerSensor.get() == true) {
			m_postWhiskerSensorDelayTimer.start();
		}

		if (m_postWhiskerSensorDelayTimer.hasElapsed(DELAY_TIME)) {
			m_postWhiskerSensorDelayTimer.stop();
			m_postWhiskerSensorDelayTimer.reset();
			return true;
		}

		return false;
	}
}
