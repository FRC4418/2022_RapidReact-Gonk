package frc.robot.commands.drivetrain;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.lights.SetAllLightsToGreen;
import frc.robot.commands.lights.SetAllLightsToRed;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Lights;


public class ReverseDrivetrain extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Drivetrain m_drivetrain;
	private final Lights m_lights;

	// ----------------------------------------------------------
	// Constructor

	public ReverseDrivetrain(Drivetrain drivetrain, Lights lights) {
		m_drivetrain = drivetrain;
		m_lights = lights;
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {
		m_drivetrain.reverseDrivetrain();
		
		if (m_drivetrain.isReversed()) {
			(new SetAllLightsToRed(m_lights)).schedule();
		} else {
			(new SetAllLightsToGreen(m_lights)).schedule();
		}
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}
