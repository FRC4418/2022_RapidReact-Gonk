package frc.robot.commands;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ToggleIntake extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final DigitalInput whiskerSensor = new DigitalInput(2);

	// ----------------------------------------------------------
	// Constructor

	public ToggleIntake(Intake intake) {
		it = intake;
		addRequirements(it);
	}

	@Override
	public void initialize() {
		it.setRollerMotorPercent(0.3d);
	}

	@Override
	public void execute() {
		SmartDashboard.putString("hello", "gamer");
	}

	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		// TODO: Figure out adding 1-2 sec (or smth) delay before stopping intake
		return whiskerSensor.get();	// when whisker sensor is tripped, stop running the intake
	}
}
