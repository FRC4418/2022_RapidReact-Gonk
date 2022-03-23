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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Private resources


	// maps camera name to camera object
	private HashMap<String, UsbCamera> cameras = new HashMap<>();
	// maps camera name to CV Sink
	private HashMap<String, CvSink> cvSinks = new HashMap<>(); 


	// ----------------------------------------------------------
	// Constructor

	
	public Vision() {
		// for 2022 Rapid React, only TCP/UDP ports 1180-1190 are allowed for camera data from the roboRIO to dashboard when camera is connected to the roboRIO via USB (section R704 of the game manual)

		// ----------------------------------------------------------
		// Front-center camera

		if (Constants.Vision.kEnableFrontCenterCamera) {
			UsbCamera frontCenterCamera = new UsbCamera(Constants.Vision.kFrontCenterCameraName, 0);
			frontCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			// m_frontCenterCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Constants.Vision.kFrontCenterCameraName, frontCenterCamera);

			CvSink frontCenterCvSink = new CvSink(Constants.Vision.kFrontCenterCameraName + " CvSink");
			frontCenterCvSink.setSource(frontCenterCamera);
			cvSinks.put(Constants.Vision.kFrontCenterCameraName, frontCenterCvSink);

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource frontCenterOutputStream = new CvSource(Constants.Vision.kFrontCenterCameraName + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
			try (MjpegServer frontCenterCameraServer = new MjpegServer(Constants.Vision.kFrontCenterCameraName + " Mjpeg Server", 1181)) {
				frontCenterCameraServer.setSource(frontCenterOutputStream);
			} catch (Exception e) {
				DriverStation.reportError("Could not create front-center camera's MJPEG server", true);
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


	
}
