package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;


public class TurnLocateAndCollectClosestCargo extends CommandBase {
	// TODO: P1 Implement this auto command for turning to, driving towards, and collecting closest cargo

	private final Drivetrain m_drivetrain;
	private final Intake m_intake;
	private final Manipulator m_manipulator;
	private final Vision m_vision;

	public TurnLocateAndCollectClosestCargo(Drivetrain drivetrain, Intake intake, Manipulator manipulator, Vision vision) {
		m_drivetrain = drivetrain;
		m_intake = intake;
		m_manipulator = manipulator;
		m_vision = vision;
	}

	@Override
	public void initialize() {}

	@Override
	public void execute() {}

	@Override
	public void end(boolean interrupted) {}

	@Override
	public boolean isFinished() {
		return false;
	}
}
