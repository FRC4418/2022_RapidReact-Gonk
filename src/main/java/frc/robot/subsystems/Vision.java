package frc.robot.subsystems;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Vision extends SubsystemBase {
	// ----------------------------------------------------------
	// Public static resources


	public static UsbCamera frontCenterCamera;
	public static UsbCamera backCenterCamera;


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
	// Camera-streaming methods


	public Vision initCameras() {
		String frontCenterCameraName = "Front-Center";
		frontCenterCamera = CameraServer.startAutomaticCapture(frontCenterCameraName, 0);
		frontCenterCamera.setResolution(640, 480);

		String backCenterCameraName = "Back-Center";
		backCenterCamera = CameraServer.startAutomaticCapture(backCenterCameraName, 1);
		backCenterCamera.setResolution(640, 480);

		// add more UsbCameras to this list as needed
		cameras.addAll(Arrays.asList(
			frontCenterCamera,
			backCenterCamera
		));

		// add more camera String names to this list as needed
		cameraNames.addAll(Arrays.asList(
			frontCenterCameraName,
			backCenterCameraName
		));
		return this;
	}

	public Vision startCameraStreams() {
		new Thread(() -> {
			CvSink[] cvSinks = Arrays.stream(cameras.toArray()).map(
				camera -> CameraServer.getVideo((VideoSource) camera)).toArray(CvSink[]::new);

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
