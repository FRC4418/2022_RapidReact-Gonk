package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.displays.MotorTestingDisplay;


public class IntakeTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake m_intake;
	private final MotorTestingDisplay m_motorTestingDisplay;

	// ----------------------------------------------------------
	// Constructor

	public IntakeTesting(Intake intake, MotorTestingDisplay motorTestingDisplay) {
		this.m_intake = intake;
		this.m_motorTestingDisplay = motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: P3 Event listeners for intake testing

	@Override
	public void execute() {
		if (m_motorTestingDisplay.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (m_motorTestingDisplay.feederToggleSwitch.getBoolean(false)) {
				m_intake.setFeederMotorPercent(m_motorTestingDisplay.feederOutputPercentTextView.getDouble(Intake.DEFAULT_FEEDER_OUTPUT_PERCENT));
			} else {
				m_intake.setFeederMotorPercent(0.d);
			}

			if (m_motorTestingDisplay.retractorToggleSwitch.getBoolean(false)) {
				m_intake.setRetractMotorPosition(m_motorTestingDisplay.retractorPositionTextView.getDouble(Intake.DEFAULT_RETRACTOR_POSITION));
			} else {
				m_intake.setRetractMotorPosition(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_intake.setFeederMotorPercent(0.d);
		// it.setRetractMotorPosition(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
