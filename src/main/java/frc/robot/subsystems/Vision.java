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
// import edu.wpi.first.cscore.VideoMode.PixelFormat;
// import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Public static resources


	// private UsbCamera frontCenterCamera;
	private String frontCenterCameraName;
	public static MjpegServer frontCenterCameraServer;

	// private UsbCamera backCenterCamera;
	private String backCenterCameraName;
	public static MjpegServer backCenterCameraServer;


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

		// frontCenterCameraName = "Front-Center";
		// if (Constants.Vision.kEnableFrontCenterCamera) {
		// 	frontCenterCamera = new UsbCamera(frontCenterCameraName, 0);
		// 	frontCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	frontCenterCameraServer = new MjpegServer(frontCenterCameraName, 1185);
		// 	frontCenterCameraServer.setSource(frontCenterCamera);
		// 	frontCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// }
		// cameras.put(frontCenterCameraName, frontCenterCamera);
		
		// // ----------------------------------------------------------
		// // Back-center camera

		// backCenterCameraName = "Back-Center";
		// if (Constants.Vision.kEnableBackCenterCamera) {
		// 	backCenterCamera = new UsbCamera(backCenterCameraName, 1);
		// 	backCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		// 	backCenterCameraServer = new MjpegServer(backCenterCameraName, 1187);
		// 	backCenterCameraServer.setSource(backCenterCamera);
		// 	backCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		// }
		// cameras.put(backCenterCameraName, backCenterCamera);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public void enableFrontCenterCameraStream(boolean enable) {
		enableCameraStreamFor(frontCenterCameraName, enable);
		Constants.Vision.kEnableFrontCenterCamera = enable;
	}

	public void enableBackCenterCameraStream(boolean enable) {
		enableCameraStreamFor(backCenterCameraName, enable);
		Constants.Vision.kEnableBackCenterCamera = enable;
	}

	public void startDefaultCameraStreams() {
		CameraServer.startAutomaticCapture(0);
		CameraServer.startAutomaticCapture(1);
	}

	private void enableCameraStreamFor(String cameraName, boolean enable) {
		if (enable) {
			startStreamForCamera(cameraName);
		} else {
			stopStreamForCamera(cameraName);
		}
	}

	private void startStreamForCamera(String cameraName) {
		var camera = cameras.get(cameraName);
		assert camera != null;

		var thread = new Thread(() -> {
			CvSink cvSink = CameraServer.getVideo((VideoSource) camera);
			// MJPEG stream name is the same as the camera name
			CvSource outputStream = CameraServer.putVideo((String) cameraName, 640, 480);

			Mat source = new Mat();
			Mat output = new Mat();
			outputMats.put(cameraName, output);

			while (!Thread.interrupted()) {
				cvSink.grabFrame(source);
				Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
				outputStream.putFrame(output);
			}
		});
		thread.start();

		streamingThreads.put(cameraName, thread);
	}

	private void stopStreamForCamera(String cameraName) {
		var camera = cameras.get(cameraName);
		assert camera != null;

		streamingThreads.get(cameraName).interrupt();
		CameraServer.removeServer(cameraName);
	}
}
