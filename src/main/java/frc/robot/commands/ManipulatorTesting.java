package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class ManipulatorTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator m_manipulator;
	private final MotorTestingDisplay m_motorTestingDisplay;

	// ----------------------------------------------------------
	// Constructor

	public ManipulatorTesting(Manipulator manipulator, HUD hud) {
		this.m_manipulator = manipulator;
		this.m_motorTestingDisplay = hud.motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: Event listeners for manipulator testing

	@Override
	public void execute() {
		if (m_motorTestingDisplay.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (m_motorTestingDisplay.indexerToggleSwitch.getBoolean(false)) {
				m_manipulator.setIndexerToPercent(m_motorTestingDisplay.indexerOutputPercentTextView.getDouble(Manipulator.DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT));
			} else {
				m_manipulator.stopIndexer();
			}

			if (m_motorTestingDisplay.launcherToggleSwitch.getBoolean(false)) {
				m_manipulator.setLauncherToPercent(m_motorTestingDisplay.launcherOutputPercentTextView.getDouble(Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT));
			} else {
				m_manipulator.setLauncherToPercent(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		m_manipulator.stopIndexer();
		m_manipulator.stopLauncher();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
