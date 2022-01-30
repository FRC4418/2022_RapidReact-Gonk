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
				ms.setIndexerMotorPercent(mt.manipulatorIndexerOutputPercentTextView.getDouble(Manipulator.INDEXER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				ms.setIndexerMotorPercent(0.d);
			}

			if (mt.manipulatorLauncherToggleSwitch.getBoolean(false)) {
				ms.setLauncherMotorPercent(mt.manipulatorLauncherOutputPercentTextView.getDouble(Manipulator.LAUNCHER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				ms.setLauncherMotorPercent(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		ms.setIndexerMotorPercent(0.d);
		ms.setLauncherMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
