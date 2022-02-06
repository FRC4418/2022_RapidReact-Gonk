package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Intake;


public class RunFeederWithTrigger extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;
	private final JoystickControls m_joystickControls;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederWithTrigger(Intake intake, JoystickControls joystickControls) {
		m_intake = intake;
		m_joystickControls = joystickControls;

		addRequirements(intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		if (m_joystickControls.getReverseFeederAxis() <= 0.1) {
			m_intake.setFeederMotorPercent(m_joystickControls.getFeederAxis());
		} else {
			m_intake.setFeederMotorPercent(-m_joystickControls.getReverseFeederAxis());
		}
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
