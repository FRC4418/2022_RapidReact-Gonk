package frc.robot.commands.intake;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.joystickcontrols.JoystickControls;
import frc.robot.subsystems.Intake;


public class RunFeederWithTrigger extends CommandBase {
	// ----------------------------------------------------------
	// Resource

	private final Intake m_intake;
	private final JoystickControls m_driverJoystickControls;
	private final JoystickControls m_spotterJoystickControls;

	// ----------------------------------------------------------
	// Constructor

	public RunFeederWithTrigger(Intake intake, JoystickControls driverJoystickControls, JoystickControls spotterJoystickControls) {
		m_intake = intake;
		m_driverJoystickControls = driverJoystickControls;
		m_spotterJoystickControls = spotterJoystickControls;

		addRequirements(intake);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {}

	@Override
	public void execute() {
		double driverFeederAxis = m_driverJoystickControls.getFeederAxis();
		double driverReverseFeederAxis = m_driverJoystickControls.getReverseFeederAxis();
		
		double spotterFeederAxis = m_spotterJoystickControls.getFeederAxis();
		double spotterReverseFeederAxis = m_spotterJoystickControls.getReverseFeederAxis();

		double output;

		// driver's triggers take priority over the spotter's triggers
		if (driverFeederAxis == 0.d && driverReverseFeederAxis == 0.d) {
			if (spotterFeederAxis == 0.d) {
				m_intake.setFeederMotorPercent(-spotterReverseFeederAxis);
				output = -spotterReverseFeederAxis;
			} else {
				m_intake.setFeederMotorPercent(spotterFeederAxis);
				output = spotterFeederAxis;
			}
		} else {
			// feeder axis (meaning that feeder is spinning to take IN a ball) takes priority over reverse feeder axis
			if (driverFeederAxis == 0.d) {
				m_intake.setFeederMotorPercent(-driverReverseFeederAxis);
				output = -driverReverseFeederAxis;
			} else {
				m_intake.setFeederMotorPercent(driverFeederAxis);
				output = driverFeederAxis;
			}
		}

		SmartDashboard.putNumber("Feeder Output", output);
	}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
