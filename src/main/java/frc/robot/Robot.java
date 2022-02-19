package frc.robot;


// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Public resources

	
	public static RobotContainer robotContainer;


	// ----------------------------------------------------------
	// Private resources
	

	// TODO: P1 Do camera code
	// private UsbCamera m_frontShooterCamera;
	// private UsbCamera m_rightPanelCamera;


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

		robotContainer.drivetrain.brakeMotors();

		// the robot should not be moving while the IMU is calibrating
		robotContainer.sensory
			.calibrateIMU()
			.resetIMU();

		// m_frontShooterCamera = CameraServer.startAutomaticCapture(0);
		// m_rightPanelCamera = CameraServer.startAutomaticCapture(1);

		if (RobotContainer.enableDiagnostics) {
			robotContainer
				.addDiagnosticsEntryListeners()
				.initializeJoystickValues();
		}
	}

	// called every robot packet (good for diagnostics), after mode-specific periodics
	// runs before LiveWindow & SmartDashboard updates
	@Override
	public void robotPeriodic() {
		// runs base periodic functions. Do not delete/comment out
		CommandScheduler.getInstance().run();

		robotContainer
			.listenForRobotSelection()
			.listenForJoystickModes()
			.listenForJoystickDevices();
		
		if (RobotContainer.enableDiagnostics) {
			// robotContainer
			// 	.printJoystickValues();
		}
	}


	// ----------------------------------------------------------
	// Disabled-mode scheduler methods


	@Override
	public void disabledInit() {
		robotContainer.drivetrain.coastMotors();
	}

	@Override
	public void disabledPeriodic() {

	}

	@Override
	public void disabledExit() {
		robotContainer.drivetrain.brakeMotors();
	}


	// ----------------------------------------------------------
	// Autonomous-phase scheduler methods


	@Override
	public void autonomousInit() {
		robotContainer.defaultAutoCommand().schedule();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void autonomousExit() {
		robotContainer.defaultAutoCommand().cancel();
	}


	// ----------------------------------------------------------
	// Teleop-phase scheduler methods


	@Override
	public void teleopInit() {
		// stops auto before teleop starts running
		// comment out to continue auto as another command starts
		robotContainer.defaultAutoCommand().cancel();
	}

	@Override
	public void teleopPeriodic() {
		
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