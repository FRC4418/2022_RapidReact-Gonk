package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;


public class RunFeederWithTrigger extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederWithTrigger(Intake intake) {
		m_intake = intake;

		addRequirements(intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		if (RobotContainer.driverJoystickControls.getReverseFeederAxis() <= 0.1) {
			m_intake.setFeederMotorPercent(RobotContainer.driverJoystickControls.getFeederAxis());
		} else {
			m_intake.setFeederMotorPercent(-RobotContainer.driverJoystickControls.getReverseFeederAxis());
		}
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
