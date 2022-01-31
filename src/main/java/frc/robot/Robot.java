package frc.robot;


// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Public resources

	
	public static RobotContainer robotContainer;


	// ----------------------------------------------------------
	// Private resources
	

	// TODO: Do camera code
	// private UsbCamera m_frontShooterCamera;
	// private UsbCamera m_rightPanelCamera;

	private Command defaultAutonomous;

	private Command intakeTesting;
	private Command manipulatorTesting;


	// ----------------------------------------------------------
	// Constructor


	public Robot() {

	}


	// ----------------------------------------------------------
	// Robot-mode scheduler methods


	// run when robot is started, put initialization code here
	@Override
	public void robotInit() {
		robotContainer = new RobotContainer();

		robotContainer.configureRobotSpecificDrivetrain();

		defaultAutonomous = robotContainer.getDefaultAutonomousCommand();

		intakeTesting = robotContainer.getIntakeTesting();
		manipulatorTesting = robotContainer.getManipulatorTesting();

		// m_frontShooterCamera = CameraServer.startAutomaticCapture(0);
		// m_rightPanelCamera = CameraServer.startAutomaticCapture(1);
	}

	// called every robot packet (good for diagnostics), after mode-specific periodics
	// runs before LiveWindow & SmartDashboard updates
	@Override
	public void robotPeriodic() {
		// runs base periodic functions. Do not delete/comment out
		CommandScheduler.getInstance().run();

		if (intakeTesting != null) {
			intakeTesting.schedule();
		}
		if (manipulatorTesting != null) {
			manipulatorTesting.schedule();
		}
	}


	// ----------------------------------------------------------
	// Disabled-mode scheduler methods


	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler methods


	@Override
	public void autonomousInit() {
		if (defaultAutonomous != null) {
			defaultAutonomous.schedule();
		}
	}

	@Override
	public void autonomousPeriodic() {

	}


	// ----------------------------------------------------------
	// Teleop-phase scheduler methods


	@Override
	public void teleopInit() {
		// stops auto before teleop starts running
		// comment out to continue auto as another command starts
		defaultAutonomous.cancel();
	}

	@Override
	public void teleopPeriodic() {
		robotContainer.teleopPeriodic();
	}


	// ----------------------------------------------------------
	// Test-phase scheduler methods


	@Override
	public void testInit() {
		// Cancels all running commands at the start of test mode.
		CommandScheduler.getInstance().cancelAll();
	}

	@Override
	public void testPeriodic() {

	}
}