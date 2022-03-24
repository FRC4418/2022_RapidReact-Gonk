package frc.robot.subsystems;


import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.DriverStation;
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


	// ----------------------------------------------------------
	// Private resources

	private HashMap<Camera, UsbCamera> m_cameras = new HashMap<>();

	private HashMap<Camera, CvSink> m_cvSinks = new HashMap<>();

	private HashMap<Camera, CvSource> m_cvSources = new HashMap<>();
	
	private HashMap<Camera, Thread> m_threads = new HashMap<>();


	// ----------------------------------------------------------
	// Constructor

	
	public Vision() {
		// ----------------------------------------------------------
		// Front-center camera

		if (Constants.Vision.kEnableFrontCamera) {
			UsbCamera frontCamera = CameraServer.startAutomaticCapture(Constants.Vision.kFrontCameraUSBPort);
			VideoMode frontCameraInputVideoMode = new VideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			frontCamera.setVideoMode(frontCameraInputVideoMode);
			frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			m_cameras.put(Camera.FRONT, frontCamera);

			CvSink frontCvSink = CameraServer.getVideo((VideoSource) frontCamera);
			frontCvSink.setSource(frontCamera);
			m_cvSinks.put(Camera.FRONT, frontCvSink);

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource frontOutputCvSource = new CvSource(Camera.FRONT.getName() + " Input Stream", frontCameraInputVideoMode);
			m_cvSources.put(Camera.FRONT, frontOutputCvSource);
		}
		
		// ----------------------------------------------------------
		// Back-center camera

		

		// ----------------------------------------------------------
		// Inner camera

		
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-setups


	public void setupFrontCamera() {
		if (Constants.Vision.kEnableFrontCamera) {
			UsbCamera frontCamera = CameraServer.startAutomaticCapture(Constants.Vision.kFrontCameraUSBPort);
			VideoMode inputVideoMode = new VideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			frontCamera.setVideoMode(inputVideoMode);
			frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			m_cameras.put(Camera.FRONT, frontCamera);

			CvSink cvSink = CameraServer.getVideo((VideoSource) frontCamera);
			m_cvSinks.put(Camera.FRONT, cvSink);

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource outputCvSource = CameraServer.putVideo("Front Camera Output", inputVideoMode.width, inputVideoMode.height);
			m_cvSources.put(Camera.FRONT, outputCvSource);
		}
	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public VideoSource getVideoSource(Camera camera) {
		return m_cvSources.get(camera);
	}

	public void toggleCameraStream(Camera camera, boolean enable) {
		// if (enable) {
		// 	m_threads.get(camera).run();
		// } else {
		// 	m_threads.get(camera).sleep(Long.MAX_VALUE);
		// }
	}


	// ----------------------------------------------------------
	// Camera-pipelines


	public void startFrontCameraPipeline() {
		if (Constants.Vision.kEnableFrontCamera) {
			Thread frontCameraThread = new Thread(() -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					var cvSink = m_cvSinks.get(Camera.FRONT);
					var cvSource = m_cvSources.get(Camera.FRONT);

					// since grabFrame is run in the check for potential error codes, this is also where we grab our input
					if (cvSink.grabFrame(input) == 0) {
						// Send the output the error.
						cvSource.notifyError(cvSink.getError());
						// skip the rest of the current iteration
						continue;
					}

					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.FRONT).putFrame(output);
				}
			});
			frontCameraThread.start();
		}
	}
}