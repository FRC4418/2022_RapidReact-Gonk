package frc.robot.subsystems;


import java.util.ArrayList;
import java.util.Arrays;

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


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Public static resources


	private UsbCamera frontCenterCamera;
	public static MjpegServer frontCenterCameraServer;

	private UsbCamera backCenterCamera;
	public static MjpegServer backCenterCameraServer;


	// ----------------------------------------------------------
	// Private resources


	private ArrayList<String> cameraNames = new ArrayList<>();
	private ArrayList<UsbCamera> cameras = new ArrayList<>();
	private ArrayList<Mat> outputMats = new ArrayList<>();


	// ----------------------------------------------------------
	// Constructor

	
	public Vision() {
		
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-creation methods


	public Vision createCameras() {
		// for 2022 Rapid React, only TCP/UDP ports 1180-1190 are allowed for camera data from the roboRIO to dashboard when camera is connected to the roboRIO via USB (section R704 of the game manual)

		String frontCenterCameraName = "Front-Center";
		frontCenterCamera = new UsbCamera(frontCenterCameraName, 0);
		frontCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		frontCenterCameraServer = new MjpegServer(frontCenterCameraName, 1185);
		frontCenterCameraServer.setSource(frontCenterCamera);
		
		String backCenterCameraName = "Back-Center";
		backCenterCamera = new UsbCamera(backCenterCameraName, 1);
		backCenterCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
		backCenterCameraServer = new MjpegServer(backCenterCameraName, 1187);
		backCenterCameraServer.setSource(backCenterCamera);

		// add more UsbCameras to this list as needed
		cameras.addAll(Arrays.asList(
			frontCenterCamera,
			backCenterCamera
		));

		for (var camera: cameras) {
			camera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
		}

		// add more camera String names to this list as needed
		cameraNames.addAll(Arrays.asList(
			frontCenterCameraName,
			backCenterCameraName
		));
		return this;
	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public Vision startCameraStreams() {
		assert !cameras.isEmpty();
		new Thread(() -> {
			CvSink[] cvSinks = Arrays.stream(cameras.toArray()).map(
				camera -> CameraServer.getVideo((VideoSource) camera)).toArray(CvSink[]::new);

			// MJPEG server name is same as camera name
			CvSource[] outputStreams = Arrays.stream(cameraNames.toArray()).map(
				cameraName -> CameraServer.putVideo((String) cameraName, 640, 480)).toArray(CvSource[]::new);

			ArrayList<Mat> sources = new ArrayList<>();
			for (int iii = 0; iii < cameras.size(); iii++) {
				sources.add(new Mat());
				outputMats.add(new Mat());
			}

			while(!Thread.interrupted()) {
				for (int iii = 0; iii < cameras.size(); iii++) {
					var source = sources.get(iii);
					var output = outputMats.get(iii);

					cvSinks[iii].grabFrame(source);
					Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
					outputStreams[iii].putFrame(output);
				}
			}
		}).start();
		return this;
	}
}
