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
import edu.wpi.first.vision.VisionThread;
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
			UsbCamera frontCamera = new UsbCamera(Camera.FRONT.getName(), Constants.Vision.kFrontCameraUSBPort);
			frontCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Camera.FRONT, frontCamera);

			try (CvSink frontCvSink = CameraServer.getVideo((VideoSource) frontCamera)) {
				frontCvSink.setSource(frontCamera);
				m_cvSinks.put(Camera.FRONT, frontCvSink);
			} catch (Exception e) {
				DriverStation.reportError("Could not create front camera's CV Sink", true);
				assert 1 == 0;
			}

			// "output streams" in this context mean the image-manipulated camera feed
			// CvSource frontOutputStream = new CvSource(Camera.FRONT.getName() + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
			// m_cvSources.put(Camera.FRONT, frontOutputStream);
			try (MjpegServer frontCameraServer = new MjpegServer(Camera.FRONT.getName() + " Mjpeg Server", 1181)) {
				frontCameraServer.setSource(frontCamera);
				outputStreams.put(Camera.FRONT, frontCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create front camera's MJPEG server", true);
				assert 1 == 0;
			}
		}
		
		// ----------------------------------------------------------
		// Back-center camera

		if (Constants.Vision.kEnableBackCamera) {
			UsbCamera backCamera = new UsbCamera(Camera.BACK.getName(), Constants.Vision.kBackCameraUSBPort);
			backCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
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
			CvSource backOutputStream = new CvSource(Camera.BACK.getName() + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
			m_cvSources.put(Camera.BACK, backOutputStream);
			try (MjpegServer backCameraServer = new MjpegServer(Camera.BACK.getName() + " Mjpeg Server", 1182)) {
				backCameraServer.setSource(backOutputStream);
				outputStreams.put(Camera.BACK, backCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create back camera's MJPEG server", true);
				assert 1 == 0;
			}
		}

		// ----------------------------------------------------------
		// Inner camera

		if (Constants.Vision.kEnableInnerCamera) {
			UsbCamera innerCamera = new UsbCamera(Camera.INNER.getName(), Constants.Vision.kInnerCameraUSBPort);
			innerCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
			innerCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
			cameras.put(Camera.INNER, innerCamera);

			try (CvSink innerCvSink = CameraServer.getVideo((VideoSource) innerCamera)) {
				m_cvSinks.put(Camera.INNER, innerCvSink);
			} catch (Exception e) {
				DriverStation.reportError("Could not create inner camera's CV Sink", true);
				assert 1 == 0;
			}

			// "output streams" in this context mean the image-manipulated camera feed
			// CvSource innerOutputStream = new CvSource(Camera.INNER.getName() + " Input Stream", PixelFormat.kMJPEG, 320, 240, 15);
			// m_cvSources.put(Camera.INNER, innerOutputStream);
			try (MjpegServer innerCameraServer = new MjpegServer(Camera.INNER.getName() + " Mjpeg Server", 1183)) {
				innerCameraServer.setSource(innerCamera);
				outputStreams.put(Camera.INNER, innerCameraServer);
			} catch (Exception e) {
				DriverStation.reportError("Could not create inner camera's MJPEG server", true);
				assert 1 == 0;
			}
		}

		// ----------------------------------------------------------
		// Thread stuff

		startVisionThreads();
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {

	}


	// ----------------------------------------------------------
	// Camera-streaming methods


	public VideoSource getVideoSource(Camera camera) {
		return cameras.get(camera);
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


	private void startVisionThreads() {
		if (Constants.Vision.kEnableFrontCamera) {
			VisionThread frontCameraThread = new VisionThread(cameras.get(Camera.FRONT), null, pipeline -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.FRONT).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.FRONT).putFrame(input);
				}
			});
			frontCameraThread.start();
		}

		if (Constants.Vision.kEnableBackCamera) {
			VisionThread backCameraThread = new VisionThread(cameras.get(Camera.BACK), null, pipeline -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.BACK).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.BACK).putFrame(input);
				}
			});
			backCameraThread.start();
		}

		if (Constants.Vision.kEnableInnerCamera) {
			VisionThread innerCameraThread = new VisionThread(cameras.get(Camera.INNER), null, pipeline -> {
				Mat input = new Mat();
				Mat output = new Mat();
	
				while (!Thread.interrupted()) {
					m_cvSinks.get(Camera.INNER).grabFrame(input);
					Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);
					m_cvSources.get(Camera.INNER).putFrame(input);
				}
			});
			innerCameraThread.start();
		}
	}
}