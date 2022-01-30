package frc.robot.commands;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;


public class ToggleIntake extends CommandBase {
	// ----------------------------------------------------------
	// Constants

	private final double INTAKE_DELAY_TIME = 2.d;
	
	// ----------------------------------------------------------
	// Resources

	private final Intake it;

	private final DigitalInput whiskerSensor = new DigitalInput(2);

	// delay to keep the intake running for a few seconds, even after we trip the whisker sensor
	private final Timer postWhiskerSensorDelayTimer = new Timer();

	// ----------------------------------------------------------
	// Constructor

	public ToggleIntake(Intake intake) {
		it = intake;
		addRequirements(it);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		it.setRollerMotorPercent(0.3d);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		SmartDashboard.putString("hello", "gamer");
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		if (whiskerSensor.get() == true) {
			postWhiskerSensorDelayTimer.start();
		}

		if (postWhiskerSensorDelayTimer.hasElapsed(INTAKE_DELAY_TIME)) {
			postWhiskerSensorDelayTimer.stop();
			postWhiskerSensorDelayTimer.reset();
			return true;
		}

		return false;
	}
}
