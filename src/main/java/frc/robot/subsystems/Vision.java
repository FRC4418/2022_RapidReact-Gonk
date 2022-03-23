package frc.robot.subsystems;


import java.util.HashMap;

import org.opencv.core.Mat;

import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Public resources


	public enum Camera {
		FRONT("Front Cam"),
		BACK("Back Cam"),
		INNER("Inner Cam");

		private String name;

		Camera(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	// maps camera name to camera object
	public HashMap<Camera, UsbCamera> cameras = new HashMap<>();
	// maps camera name to CV Sink
	public HashMap<Camera, MjpegServer> outputStreams = new HashMap<>();


	// ----------------------------------------------------------
	// Private resources

	private HashMap<Camera, CvSink> m_cvSinks = new HashMap<>();

	private HashMap<Camera, CvSource> m_cvSources = new HashMap<>();
	
	private HashMap<Camera, VisionThread> m_threads = new HashMap<>();


	// ----------------------------------------------------------
	// Constructor

	
	public Vision() {
		// for 2022 Rapid React, only TCP/UDP ports 1180-1190 are allowed for camera data from the roboRIO to dashboard when camera is connected to the roboRIO via USB (section R704 of the game manual)

		// ----------------------------------------------------------
		// Front-center camera

		if (Constants.Vision.kEnableFrontCamera) {
			UsbCamera frontCamera = new UsbCamera(Camera.FRONT.getName(), 0);
			frontCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			// frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Camera.FRONT, frontCamera);

			try (CvSink frontCenterCvSink = new CvSink(Camera.FRONT.getName() + " CvSink")) {
				frontCenterCvSink.setSource(frontCamera);
				m_cvSinks.put(Camera.FRONT, frontCenterCvSink);
			} catch (Exception e) {
				DriverStation.reportError("Could not create front camera's CV Sink", true);
				assert 1 == 0;
			}

			// Use CvSink.grabFrame(Mat) and CvSource.putFrame(Mat) to process the vision pipeline

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource frontOutputStream = new CvSource(Camera.FRONT.getName() + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
			m_cvSources.put(Camera.FRONT, frontOutputStream);
			try (MjpegServer frontCameraServer = new MjpegServer(Camera.FRONT.getName() + " Mjpeg Server", 1181)) {
				frontCameraServer.setSource(frontOutputStream);
				outputStreams.put(Camera.FRONT, frontCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create front camera's MJPEG server", true);
				assert 1 == 0;
			}
		}
		
		// ----------------------------------------------------------
		// Back-center camera

		// if (Constants.Vision.kEnableBackCenterCamera) {
		// 	m_backCenterCamera = new UsbCamera(Constants.Vision.kBackCenterCameraName, 1);
		// 	m_backCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	backCenterCameraServer = new MjpegServer(Constants.Vision.kBackCenterCameraName, 1187);
		// 	backCenterCameraServer.setSource(m_backCenterCamera);
		// 	m_backCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// 	cameras.put(Constants.Vision.kBackCenterCameraName, m_backCenterCamera);
		// }

		// ----------------------------------------------------------
		// Inner camera

		// if (Constants.Vision.kEnableInnerCamera) {
		// 	m_innerCamera = new UsbCamera(Constants.Vision.kInnerCameraName, 0);
		// 	m_innerCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	innerCameraServer = new MjpegServer(Constants.Vision.kInnerCameraName, 1185);
		// 	innerCameraServer.setSource(m_innerCamera);
		// 	m_innerCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// 	cameras.put(Constants.Vision.kInnerCameraName, m_innerCamera);
		// }
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public VideoSource getVideoSource(Camera camera) {
		var thing = outputStreams.get(camera);
		if (thing == null) {
			SmartDashboard.putString("Thing", "thing exists");
		}

		try {
			return outputStreams.get(camera).getSource();
		} catch (Exception e) {

		}
		return cameras.get(camera);
	}

	public void toggleCameraStream(Camera camera, boolean enable) {
		if (enable) {
			outputStreams.get(camera).setSource(m_cvSources.get(camera));
		} else {
			outputStreams.get(camera).setSource(null);
		}
	}


	// ----------------------------------------------------------
	// Camera-pipelines


	private void startVisionThreads() {
		var frontCameraThread = new VisionThread(cameras.get(Camera.FRONT), null, pipeline -> {
			Mat input = new Mat();
			m_cvSinks.get(Camera.FRONT).grabFrame(input);
			
			m_cvSources.get(Camera.FRONT).putFrame(input);
		});
	}
}