package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

	// delay to keep the intake running for a few seconds, even after we trip the whisker sensor
	private final Timer m_postWhiskerSensorDelayTimer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public ToggleFeeder(Intake intake) {
		m_intake = intake;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_intake.setFeederMotorPercent(MOTOR_OUTPUT_PERCENT);
	}

	int counter = 0;

	@Override
	public void execute() {
		if (counter % 20 == 0) {
			SmartDashboard.putNumber("Toggle Feeder command is running", counter);
		}

		counter++;
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.setFeederMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		if (m_intake.whiskerSensorIsActive()) {
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
