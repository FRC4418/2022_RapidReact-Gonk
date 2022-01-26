package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Telemetry;


public class ManipulatorDemo extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator ms;
	private final Telemetry tt;

	// ----------------------------------------------------------
	// Constructor

	public ManipulatorDemo(Manipulator manipulator, Telemetry telemetry) {
		ms = manipulator;
		tt = telemetry;
		addRequirements(ms, tt);
	}

	// ----------------------------------------------------------
	// Scheduler actions

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		if (tt.lowerConveyorMotorToggleSwitch.getBoolean(false)) {
			ms.setLowMotorPercent(tt.lowerConveyorMotorPercentTextView.getDouble(Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT));
		} else {
			ms.setLowMotorPercent(0.d);
		}

		if (tt.higherConveyorMotorToggleSwitch.getBoolean(false)) {
			ms.setHighMotorPercent(tt.higherConveyorMotorPercentTextView.getDouble(Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT));
		} else {
			ms.setHighMotorPercent(0.d);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ms.setLowMotorPercent(0.d);
		ms.setHighMotorPercent(0.d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
