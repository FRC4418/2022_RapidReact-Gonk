package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.displays.MotorTestingDisplay;
import frc.robot.subsystems.HUD;


public class ManipulatorTesting extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator ms;
	private final MotorTestingDisplay mt;

	// ----------------------------------------------------------
	// Constructor

	public ManipulatorTesting(Manipulator manipulator, HUD hud) {
		ms = manipulator;
		mt = hud.motorTestingDisplay;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	// TODO: Event listeners for manipulator testing

	@Override
	public void execute() {
		if (mt.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (mt.manipulatorIndexerToggleSwitch.getBoolean(false)) {
				ms.setIndexerToPercent(mt.manipulatorIndexerOutputPercentTextView.getDouble(Manipulator.DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT));
			} else {
				ms.stopIndexer();
			}

			if (mt.manipulatorLauncherToggleSwitch.getBoolean(false)) {
				ms.setLauncherToPercent(mt.manipulatorLauncherOutputPercentTextView.getDouble(Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT));
			} else {
				ms.setLauncherToPercent(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		ms.stopIndexer();
		ms.stopLauncher();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
