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

	// maps camera name to camera object
	public HashMap<Camera, UsbCamera> cameras = new HashMap<>();
	// maps camera name to CV Sink
	public HashMap<Camera, MjpegServer> outputMjpegServers = new HashMap<>();


	// ----------------------------------------------------------
	// Private resources

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
			cameras.put(Camera.FRONT, frontCamera);

			CvSink frontCvSink = CameraServer.getVideo((VideoSource) frontCamera);
			frontCvSink.setSource(frontCamera);
			m_cvSinks.put(Camera.FRONT, frontCvSink);

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource frontOutputCvSource = new CvSource(Camera.FRONT.getName() + " Input Stream", frontCameraInputVideoMode);
			m_cvSources.put(Camera.FRONT, frontOutputCvSource);
			
			MjpegServer frontCameraServer = new MjpegServer(Camera.FRONT.getName() + " Mjpeg Server", Constants.Vision.kFrontCameraTCPPort);
			frontCameraServer.setSource(frontCamera);
			outputMjpegServers.put(Camera.FRONT, frontCameraServer);
		}
		
		// ----------------------------------------------------------
		// Back-center camera

		if (Constants.Vision.kEnableBackCamera) {
			UsbCamera backCamera = CameraServer.startAutomaticCapture(Constants.Vision.kBackCameraUSBPort);
			VideoMode backCameraInputVideoMode = new VideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			backCamera.setVideoMode(backCameraInputVideoMode);
			backCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Camera.BACK, backCamera);

			try (CvSink backCvSink = CameraServer.getVideo((VideoSource) backCamera)) {
				backCvSink.setSource(backCamera);
				m_cvSinks.put(Camera.BACK, backCvSink);
			} catch (Exception e) {
				DriverStation.reportError("Could not create back camera's CV Sink", true);
				assert 1 == 0;
			}

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource backOutputCvSource = new CvSource(Camera.BACK.getName() + " Input Stream", backCameraInputVideoMode);
			// CvSource backOutputStream = CameraServer.putVideo(Camera.BACK.getName(), 640, 480);
			m_cvSources.put(Camera.BACK, backOutputCvSource);
			
			try (MjpegServer backCameraServer = new MjpegServer(Camera.BACK.getName() + " Mjpeg Server", Constants.Vision.kBackCameraTCPPort)) {
				backCameraServer.setSource(backCamera);
				outputMjpegServers.put(Camera.BACK, backCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create back camera's MJPEG server", true);
				assert 1 == 0;
			}
		}

		// ----------------------------------------------------------
		// Inner camera

		if (Constants.Vision.kEnableInnerCamera) {
			UsbCamera innerCamera = CameraServer.startAutomaticCapture(Constants.Vision.kInnerCameraUSBPort);
			VideoMode innerCameraInputVideoMode = new VideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			innerCamera.setVideoMode(innerCameraInputVideoMode);
			innerCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Camera.INNER, innerCamera);

			try (CvSink innerCvSink = CameraServer.getVideo((VideoSource) innerCamera)) {
				m_cvSinks.put(Camera.INNER, innerCvSink);
			} catch (Exception e) {
				DriverStation.reportError("Could not create inner camera's CV Sink", true);
				assert 1 == 0;
			}

			// "output streams" in this context mean the image-manipulated camera feed
			CvSource innerOutputCvSource = new CvSource(Camera.INNER.getName() + " Input Stream", innerCameraInputVideoMode);
			// CvSource innerOutputStream = CameraServer.putVideo(Camera.INNER.getName(), 640, 480);
			m_cvSources.put(Camera.INNER, innerOutputCvSource);
			
			try (MjpegServer innerCameraServer = new MjpegServer(Camera.INNER.getName() + " Mjpeg Server", Constants.Vision.kInnerCameraTCPPort)) {
				innerCameraServer.setSource(innerCamera);
				outputMjpegServers.put(Camera.INNER, innerCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create inner camera's MJPEG server", true);
				assert 1 == 0;
			}
		}
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public VideoSource getVideoSource(Camera camera) {
		return m_cvSources.get(camera);
	}

	public void toggleCameraStream(Camera camera, boolean enable) {
		if (enable) {
			m_threads.get(camera).run();
		} else {
			// m_threads.get(camera).sleep(Long.MAX_VALUE);
		}
	}


	// ----------------------------------------------------------
	// Camera-pipelines


	public void startVisionThreads() {
		if (Constants.Vision.kEnableFrontCamera) {
			Thread frontCameraThread = new Thread(() -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.FRONT).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.FRONT).putFrame(output);
				}
			});
			frontCameraThread.start();
		}

		if (Constants.Vision.kEnableBackCamera) {
			Thread backCameraThread = new Thread(() -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.BACK).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.BACK).putFrame(output);
				}
			});
			backCameraThread.start();
		}

		if (Constants.Vision.kEnableInnerCamera) {
			Thread innerCameraThread = new Thread(() -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.INNER).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.INNER).putFrame(output);
				}
			});
			innerCameraThread.start();
		}
	}
}