package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Telemetry;


public class ConveyerDemo extends CommandBase {
	private Manipulator ms;
	private Telemetry tt;

	public ConveyerDemo() {
		ms = Robot.manipulator;
		tt = Robot.telemetry;
		addRequirements(ms);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ms.setLowMotor(tt.lowerConveyorMotorPercentTextView.getDouble(ms.DEFAULT_LOWER_MOTOR_PERCENT));
		ms.setHighMotor(tt.higherConveyorMotorPercentTextView.getDouble(ms.DEFAULT_HIGHER_MOTOR_PERCENT));
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ms.setLowMotor(0.d);
		ms.setHighMotor(0.d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
