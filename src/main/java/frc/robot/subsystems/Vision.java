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


	private UsbCamera m_frontCenterCamera;
	private String m_frontCenterCameraName;
	public static MjpegServer frontCenterCameraServer;

	private UsbCamera m_backCenterCamera;
	private String m_backCenterCameraName;
	public static MjpegServer backCenterCameraServer;

	private UsbCamera m_innerCamera;
	private String m_innerCameraName;
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

		m_frontCenterCameraName = "Front-Center";
		if (Constants.Vision.kEnableFrontCenterCamera) {
			m_frontCenterCamera = new UsbCamera(m_frontCenterCameraName, 0);
			m_frontCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			frontCenterCameraServer = new MjpegServer(m_frontCenterCameraName, 1185);
			frontCenterCameraServer.setSource(m_frontCenterCamera);
			m_frontCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		}
		cameras.put(m_frontCenterCameraName, m_frontCenterCamera);
		
		// ----------------------------------------------------------
		// Back-center camera

		// backCenterCameraName = "Back-Center";
		// if (Constants.Vision.kEnableBackCenterCamera) {
		// 	backCenterCamera = new UsbCamera(backCenterCameraName, 1);
		// 	backCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	backCenterCameraServer = new MjpegServer(backCenterCameraName, 1187);
		// 	backCenterCameraServer.setSource(backCenterCamera);
		// 	backCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// }
		// cameras.put(backCenterCameraName, backCenterCamera);

		// ----------------------------------------------------------
		// Inner camera

		// m_innerCameraName = "Inner";
		// if (Constants.Vision.kEnableInnerCamera) {
		// 	m_innerCamera = new UsbCamera(m_innerCameraName, 0);
		// 	m_innerCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	innerCameraServer = new MjpegServer(m_innerCameraName, 1185);
		// 	innerCameraServer.setSource(m_innerCamera);
		// 	m_innerCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// }
		// cameras.put(m_innerCameraName, m_innerCamera);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	
}
