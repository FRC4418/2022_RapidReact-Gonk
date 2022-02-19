package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Public resources

	
	public static RobotContainer robotContainer;


	// ----------------------------------------------------------
	// Private resources
	

	private Command defaultAutoCommand;


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

		robotContainer.drivetrain
			.brakeMotors()
			
			// the robot should not be moving while the IMU is calibrating
			.calibrateIMU()
			.resetIMU();

		robotContainer.vision.startCameraStreams();

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
		defaultAutoCommand = robotContainer.defaultAutoCommand();
		defaultAutoCommand.schedule();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void autonomousExit() {
		defaultAutoCommand.cancel();
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