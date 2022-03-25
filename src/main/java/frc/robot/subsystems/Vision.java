package frc.robot.subsystems;


import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
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
		if (Constants.Vision.kUsingFrontCamera) {
			setupFrontCamera();
		}
		if (Constants.Vision.kUsingBackCamera) {
			setupBackCamera();
		}
		if (Constants.Vision.kUsingInnerCamera) {
			setupInnerCamera();
		}
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-setups


	private void setupFrontCamera() {
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

	private void setupBackCamera() {

	}

	private void setupInnerCamera() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public VideoSource getVideoSource(Camera camera) {
		return m_cvSources.get(camera);
	}

	public void toggleCameraStream(Camera camera, boolean enable) {
		if (enable) {
			startCameraPipeline(camera);
			m_threads.get(camera).run();
		} else {
			stopCameraPipeline(camera);
		}
	}


	// ----------------------------------------------------------
	// Camera-image pipelines


	public void startDefaultCameras() {
		if (Constants.Vision.kUsingFrontCamera) {
			startCameraPipeline(Camera.FRONT);
		}

		if (Constants.Vision.kUsingBackCamera) {
			startCameraPipeline(Camera.BACK);
		}

		if (Constants.Vision.kUsingInnerCamera) {
			startCameraPipeline(Camera.INNER);
		}
	}

	public void stopCameraPipeline(Camera camera) {
		m_threads.get(camera).interrupt();
	}

	public void startCameraPipeline(Camera camera) {
		switch (camera) {
			default:
				DriverStation.reportError("Unsupported camera type found in startCameraPipeline(Camera)", true);
				break;
			case FRONT:
				startFrontCameraPipeline();
				break;
			case BACK:
				startBackCameraPipeline();
				break;
			case INNER:
				startInnerCameraPipeline();
				break;
		}
	}

	private void startFrontCameraPipeline() {
		Thread thread = new Thread(() -> {
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

		m_threads.put(Camera.FRONT, thread);

		thread.start();
	}

	private void startBackCameraPipeline() {

	}

	private void startInnerCameraPipeline() {
		Thread thread = new Thread(() -> {
			Mat input = new Mat();
			Mat output = new Mat();

			while (!Thread.interrupted()) {
				var cvSink = m_cvSinks.get(Camera.INNER);
				var cvSource = m_cvSources.get(Camera.INNER);

				// since grabFrame is run in the check for potential error codes, this is also where we grab our input
				if (cvSink.grabFrame(input) == 0) {
					// Send the output the error.
					cvSource.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}

				Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
				m_cvSources.get(Camera.INNER).putFrame(output);
			}
		});

		m_threads.put(Camera.INNER, thread);

		thread.start();
	}
}