package frc.robot;


// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.TeleopInput;
// import frc.robot.subsystems.Sensory;
import frc.robot.subsystems.Telemetry;
import frc.robot.commands.ManipulatorDemo;
import frc.robot.commands.IntakeDemo;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	// ----------------------------------------------------------
	// Subsystem dependencies

	public final static TeleopInput teleopInput = new TeleopInput();

	public final static Drivetrain drivetrain = new Drivetrain();
	public final static Intake intake = new Intake();
	public final static Manipulator manipulator = new Manipulator();
	public final static Climber climber = new Climber();

	// public static Sensory sensory = new Sensory();
	public final static Autonomous autonomous = new Autonomous();
	public final static Telemetry telemetry = new Telemetry();


	// ----------------------------------------------------------
	// Resources


	// TODO: Code cool camera stuff
	// private UsbCamera m_frontShooterCamera;
	// private UsbCamera m_rightPanelCamera;

	public static Command m_autonomousCommand;

	// TODO: PERSISTENT CONFIG - enable robot's tuning tools or don't
	public static boolean enableTuningTools = true;

	private ManipulatorDemo manipulatorDemo = new ManipulatorDemo();
	private IntakeDemo intakeDemo = new IntakeDemo();


	// ----------------------------------------------------------
	// Constructor


	public Robot() {

	}


	// ----------------------------------------------------------
	// Robot-mode scheduler actions


	// run when robot is started, put initialization code here
	@Override
	public void robotInit() {
		teleopInput.configureButtonBindings();
		
		// autonomous, drive straight and backwards for 30 inches
		m_autonomousCommand = new AutoDriveStraightForDistance(60.0d, DriveStraightDirection.BACKWARDS);

		manipulatorDemo = new ManipulatorDemo();
		intakeDemo = new IntakeDemo();

		// m_frontShooterCamera = CameraServer.startAutomaticCapture(0);
		// m_rightPanelCamera = CameraServer.startAutomaticCapture(1);

		telemetry.initializeTelemetry();
		if (enableTuningTools) {
			telemetry.initializeTuningTools();
		}
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
		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.schedule();
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
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		if (telemetry.tuningModeToggleSwitch.getBoolean(false)) {
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