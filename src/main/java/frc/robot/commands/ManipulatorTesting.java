package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class ManipulatorTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator manipulator;
	private final MotorTestingDisplay motorTestingDisplay;

	// ----------------------------------------------------------
	// Constructor

	public ManipulatorTesting(Manipulator manipulator, HUD hud) {
		this.manipulator = manipulator;
		this.motorTestingDisplay = hud.motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: Event listeners for manipulator testing

	@Override
	public void execute() {
		if (motorTestingDisplay.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (motorTestingDisplay.indexerToggleSwitch.getBoolean(false)) {
				manipulator.setIndexerToPercent(motorTestingDisplay.indexerOutputPercentTextView.getDouble(Manipulator.DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT));
			} else {
				manipulator.stopIndexer();
			}

			if (motorTestingDisplay.launcherToggleSwitch.getBoolean(false)) {
				manipulator.setLauncherToPercent(motorTestingDisplay.launcherOutputPercentTextView.getDouble(Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT));
			} else {
				manipulator.setLauncherToPercent(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		manipulator.stopIndexer();
		manipulator.stopLauncher();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
