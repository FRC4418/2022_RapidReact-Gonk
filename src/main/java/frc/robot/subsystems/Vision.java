package frc.robot.subsystems;


import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Public static resources


	public static MjpegServer frontCenterCameraServer;

	public static MjpegServer backCenterCameraServer;

	public static MjpegServer innerCameraServer;


	// ----------------------------------------------------------
	// Private resources


	// maps camera name to camera object
	private HashMap<String, UsbCamera> cameras = new HashMap<>();
	// maps camera name to output mat
	private HashMap<String, Mat> outputMats = new HashMap<>();
	// maps camera name to dedicated streaming-thread
	private HashMap<String, Thread> streamingThreads = new HashMap<>();


	// ----------------------------------------------------------
	// Constructor

	
	public Vision() {
		// for 2022 Rapid React, only TCP/UDP ports 1180-1190 are allowed for camera data from the roboRIO to dashboard when camera is connected to the roboRIO via USB (section R704 of the game manual)

		// ----------------------------------------------------------
		// Front-center camera

		if (Constants.Vision.kEnableFrontCenterCamera) {
			UsbCamera m_frontCenterCamera = new UsbCamera(Constants.Vision.kFrontCenterCameraName, 0);
			m_frontCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			frontCenterCameraServer = new MjpegServer(Constants.Vision.kFrontCenterCameraName, 1185);
			frontCenterCameraServer.setSource(m_frontCenterCamera);
			// m_frontCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Constants.Vision.kFrontCenterCameraName, m_frontCenterCamera);

			CvSink cvSink = new CvSink(Constants.Vision.kFrontCenterCameraName + " CvSink");
			cvSink.setSource(m_frontCenterCamera);

			// "input stream" as in what the camera sees, with no image manipulationss
			CvSource inputStream = new CvSource(Constants.Vision.kFrontCenterCameraName + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
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


	
}
