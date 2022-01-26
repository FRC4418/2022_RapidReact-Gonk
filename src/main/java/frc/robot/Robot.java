package frc.robot;


// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Resources

	
	public RobotContainer robotContainer;

	// TODO: Code cool camera stuff
	// private UsbCamera m_frontShooterCamera;
	// private UsbCamera m_rightPanelCamera;

	private Command defaultAutonomous;

	private Command intakeDemo;
	private Command manipulatorDemo;


	// ----------------------------------------------------------
	// Constructor


	public Robot() {

	}


	// ----------------------------------------------------------
	// Robot-mode scheduler actions


	// run when robot is started, put initialization code here
	@Override
	public void robotInit() {
		robotContainer = new RobotContainer();

		// m_frontShooterCamera = CameraServer.startAutomaticCapture(0);
		// m_rightPanelCamera = CameraServer.startAutomaticCapture(1);
	}

	// called every robot packet (good for diagnostics), after mode-specific periodics
	// runs before LiveWindow & SmartDashboard updates
	@Override
	public void robotPeriodic() {
		// runs base periodic functions. Do not delete/comment out
		CommandScheduler.getInstance().run();
	}


	// ----------------------------------------------------------
	// Disabled-mode scheduler actions


	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler actions


	// Runs autonomous command selected by {@link Robot} class
	@Override
	public void autonomousInit() {
		defaultAutonomous = robotContainer.getDefaultAutonomousCommand();

		// schedule the autonomous command (example)
		if (defaultAutonomous != null) {
			defaultAutonomous.schedule();
		}
	}

	@Override
	public void autonomousPeriodic() {

	}


	// ----------------------------------------------------------
	// Teleop-phase scheduler actions


	@Override
	public void teleopInit() {
		// stops auto before teleop starts running
		// comment out to continue auto as another command starts
		if (defaultAutonomous != null) {
			defaultAutonomous.cancel();
		}

		intakeDemo = robotContainer.getIntakeDemo();
		manipulatorDemo = robotContainer.getManipulatorDemo();
	}

	@Override
	public void teleopPeriodic() {
		if (robotContainer.getTuningModeToggleSwitch()) {
			if (!intakeDemo.isScheduled()) {
				intakeDemo.schedule();
			}
			if (!manipulatorDemo.isScheduled()) {
				manipulatorDemo.schedule();
			}
		} else {
			if (!intakeDemo.isScheduled()) {
				intakeDemo.cancel();
			}
			if (!manipulatorDemo.isScheduled()) {
				manipulatorDemo.cancel();
			}
		}
	}


	// ----------------------------------------------------------
	// Test-phase scheduler actions


	@Override
	public void testInit() {
		// Cancels all running commands at the start of test mode.
		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {

	}
}