package io.github.roboblazers7617.limelight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.IntegerEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.StringArrayEntry;
import edu.wpi.first.networktables.StringEntry;
import io.github.roboblazers7617.limelight.targets.RawFiducialTarget;
import io.github.roboblazers7617.limelight.targets.neural.RawDetection;

/**
 * Fetches pipeline data from a {@link Limelight}.
 */
public class PipelineDataCollator {
	/**
	 * The NetworkTable for the given Limelight.
	 */
	private final NetworkTable networkTable;
	/**
	 * Object mapper used to parse JSON data from the Limelight.
	 */
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Raw detections entry for the Limelight.
	 */
	private final NetworkTableEntry rawDetectionsEntry;
	/**
	 * Raw fiducials entry for the Limelight.
	 */
	private final NetworkTableEntry rawFiducialsEntry;
	/**
	 * Target valid entry for the Limelight.
	 */
	private final IntegerEntry targetValidEntry;
	/**
	 * Target horizontal offset from target entry for the Limelight.
	 */
	private final DoubleEntry targetXEntry;
	/**
	 * Target vertical offset from target entry for the Limelight.
	 */
	private final DoubleEntry targetYEntry;
	/**
	 * Target horizontal offset from principal pixel entry for the Limelight.
	 */
	private final DoubleEntry targetXNCEntry;
	/**
	 * Target vertical offset from principal pixel entry for the Limelight.
	 */
	private final DoubleEntry targetYNCEntry;
	/**
	 * Target area percentage for the Limelight [0-100].
	 */
	private final DoubleEntry targetAreaEntry;
	/**
	 * Array containing various metrics about the Limelight readings.
	 */
	private final DoubleArrayEntry targetT2DEntry;
	/**
	 * The name of the classifier pipeline's computed class from the Limelight.
	 */
	private final StringEntry targetClassiferClassEntry;
	/**
	 * The name of the detector pipeline's computed class from the Limelight.
	 */
	private final StringEntry targetDetectorClassEntry;
	/**
	 * The pipeline's latency contribution in ms from the Limelight.
	 */
	private final DoubleEntry pipelineLatencyEntry;
	/**
	 * The total capture pipeline's latency in ms from the Limelight.
	 */
	private final DoubleEntry captureLatencyEntry;
	/**
	 * The active pipeline index of the Limelight.
	 */
	private final IntegerEntry pipelineIndexEntry;
	/**
	 * The active pipeline's type of the Limelight.
	 */
	private final StringEntry pipelineTypeEntry;
	/**
	 * Gets a full JSON dump of target results.
	 */
	private final StringEntry jsonEntry;
	/**
	 * The 3D transform of the robot in the coordinate system of the primary in-view AprilTag.
	 */
	private final DoubleArrayEntry botPoseTargetSpaceEntry;
	/**
	 * The 3D transform of the Limelight in the coordinate system of the primary in-view AprilTag.
	 */
	private final DoubleArrayEntry cameraPoseTargetSpaceEntry;
	/**
	 * The 3D transform of the primary in-view AprilTag in the coordinate system of the Limelight.
	 */
	private final DoubleArrayEntry targetPoseCameraSpaceEntry;
	/**
	 * The 3D transform of the primary in-view AprilTag in the coordinate system of the robot.
	 */
	private final DoubleArrayEntry targetPoseRobotSpaceEntry;
	/**
	 * The 3D transform of the Limelight in the coordinate system of the robot.
	 */
	private final DoubleArrayEntry cameraPoseRobotSpaceEntry;
	/**
	 * The MegaTag standard deviations.
	 *
	 * @apiNote
	 *          Array format [MT1x, MT1y, MT1z, MT1roll, MT1pitch, MT1Yaw, MT2x, MT2y, MT2z, MT2roll,
	 *          MT2pitch, MT2yaw]
	 * @apiNote
	 *          This data shouldn't be used for pose estimation because it is calculated based off the
	 *          poses from the last few seconds and, as a result, both lags behind a few seconds and
	 *          is only really accurate when standing still for a few seconds. More information can be
	 *          found in <a href=
	 *          "https://github.com/LimelightVision/limelight-feedback/issues/30#issuecomment-2685398662">this
	 *          issue</a>.
	 */
	private final DoubleArrayEntry standardDeviationsEntry;
	/**
	 * The target color [H, S, V].
	 */
	private final DoubleArrayEntry targetColorEntry;
	/**
	 * The ID of the primary in-view AprilTag detected by the LimeLight.
	 */
	private final IntegerEntry tagIdEntry;
	/**
	 * The class name of primary neural detector result or neural classifier result.
	 */
	private final StringEntry targetClassEntry;
	/**
	 * The raw data from all detected barcodes.
	 */
	private final StringArrayEntry rawBarcodesEntry;
	/**
	 * The hardware metrics from the Limelight.
	 *
	 * @apiNote
	 *          Array format is [FPS, CPU temp, RAM usage, temp].
	 */
	private final DoubleArrayEntry hardwareMetricsEntry;

	/**
	 * Creates a new PipelineDataCollator.
	 *
	 * @param limelight
	 *            The {@link Limelight} to get data from.
	 */
	public PipelineDataCollator(Limelight limelight) {
		networkTable = limelight.getNetworkTable();

		// Configure the ObjectMapper to not fail on unknown properties.
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		rawDetectionsEntry = networkTable.getEntry("rawdetections");
		rawFiducialsEntry = networkTable.getEntry("rawfiducials");
		targetValidEntry = networkTable.getIntegerTopic("tv").getEntry(0);
		targetXEntry = networkTable.getDoubleTopic("tx").getEntry(0);
		targetYEntry = networkTable.getDoubleTopic("ty").getEntry(0);
		targetXNCEntry = networkTable.getDoubleTopic("txnc").getEntry(0);
		targetYNCEntry = networkTable.getDoubleTopic("tync").getEntry(0);
		targetAreaEntry = networkTable.getDoubleTopic("ta").getEntry(0);
		targetT2DEntry = networkTable.getDoubleArrayTopic("t2d").getEntry(new double[0]);
		targetClassiferClassEntry = networkTable.getStringTopic("tcclass").getEntry("");
		targetDetectorClassEntry = networkTable.getStringTopic("tdclass").getEntry("");
		pipelineLatencyEntry = networkTable.getDoubleTopic("tl").getEntry(0);
		captureLatencyEntry = networkTable.getDoubleTopic("cl").getEntry(0);
		pipelineIndexEntry = networkTable.getIntegerTopic("getpipe").getEntry(0);
		pipelineTypeEntry = networkTable.getStringTopic("getpipetype").getEntry("");
		jsonEntry = networkTable.getStringTopic("json").getEntry("");
		botPoseTargetSpaceEntry = networkTable.getDoubleArrayTopic("botpose_targetspace").getEntry(new double[0]);
		cameraPoseTargetSpaceEntry = networkTable.getDoubleArrayTopic("camerapose_targetspace").getEntry(new double[0]);
		targetPoseCameraSpaceEntry = networkTable.getDoubleArrayTopic("targetpose_cameraspace").getEntry(new double[0]);
		targetPoseRobotSpaceEntry = networkTable.getDoubleArrayTopic("targetpose_robotspace").getEntry(new double[0]);
		cameraPoseRobotSpaceEntry = networkTable.getDoubleArrayTopic("camerapose_robotspace").getEntry(new double[0]);
		standardDeviationsEntry = networkTable.getDoubleArrayTopic("stddevs").getEntry(new double[0]);
		targetColorEntry = networkTable.getDoubleArrayTopic("tc").getEntry(new double[0]);
		tagIdEntry = networkTable.getIntegerTopic("tid").getEntry(0);
		targetClassEntry = networkTable.getStringTopic("tclass").getEntry("");
		rawBarcodesEntry = networkTable.getStringArrayTopic("rawbarcodes").getEntry(new String[0]);
		hardwareMetricsEntry = networkTable.getDoubleArrayTopic("hw").getEntry(new double[0]);
	}

	/**
	 * Gets the latest raw neural detector results from NetworkTables name.
	 *
	 * @return
	 *         Array of RawDetection objects containing detection details.
	 */
	public RawDetection[] getRawDetections() {
		double[] rawDetectionArray = rawDetectionsEntry.getDoubleArray(new double[0]);
		int valsPerEntry = 12;
		if (rawDetectionArray.length % valsPerEntry != 0) {
			return new RawDetection[0];
		}

		int numDetections = rawDetectionArray.length / valsPerEntry;
		RawDetection[] rawDetections = new RawDetection[numDetections];

		for (int i = 0; i < numDetections; i++) {
			int baseIndex = i * valsPerEntry; // Starting index for this detection's data
			int classId = (int) JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex);
			double txnc = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 1);
			double tync = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 2);
			double ta = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 3);
			double corner0_X = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 4);
			double corner0_Y = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 5);
			double corner1_X = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 6);
			double corner1_Y = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 7);
			double corner2_X = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 8);
			double corner2_Y = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 9);
			double corner3_X = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 10);
			double corner3_Y = JsonUtilities.extractArrayEntry(rawDetectionArray, baseIndex + 11);

			rawDetections[i] = new RawDetection(classId, txnc, tync, ta, corner0_X, corner0_Y, corner1_X, corner1_Y, corner2_X, corner2_Y, corner3_X, corner3_Y);
		}

		return rawDetections;
	}

	/**
	 * Gets the latest raw fiducial/AprilTag detection results from NetworkTables.
	 *
	 * @return
	 *         Array of RawFiducialTarget objects containing detection details.
	 */
	public RawFiducialTarget[] getRawFiducialTargets() {
		double[] rawFiducialArray = rawFiducialsEntry.getDoubleArray(new double[0]);
		int valsPerEntry = 7;
		if (rawFiducialArray.length % valsPerEntry != 0) {
			return new RawFiducialTarget[0];
		}

		int numFiducials = rawFiducialArray.length / valsPerEntry;
		RawFiducialTarget[] rawFiducials = new RawFiducialTarget[numFiducials];

		for (int i = 0; i < numFiducials; i++) {
			int baseIndex = i * valsPerEntry;
			int id = (int) JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex);
			double txnc = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 1);
			double tync = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 2);
			double ta = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 3);
			double distToCamera = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 4);
			double distToRobot = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 5);
			double ambiguity = JsonUtilities.extractArrayEntry(rawFiducialArray, baseIndex + 6);

			rawFiducials[i] = new RawFiducialTarget(id, txnc, tync, ta, distToCamera, distToRobot, ambiguity);
		}

		return rawFiducials;
	}

	/**
	 * Gets the latest JSON results output and returns a LimelightResults object.
	 *
	 * @param showParseTime
	 *            Print out the parse time to standard output.
	 * @return
	 *         LimelightResults object containing all current target data
	 */
	public PipelineResult getLatestResults(boolean showParseTime) {
		long start = System.nanoTime();
		PipelineResult results = new PipelineResult();
		try {
			results = mapper.readValue(getJSONDump(), PipelineResult.class);
		} catch (JsonProcessingException e) {
			results.error = "lljson error: " + e.getMessage();
		}

		long end = System.nanoTime();
		double millis = (end - start) * .000001;
		results.latency_jsonParse = millis;
		if (showParseTime) {
			System.out.printf("lljson: %.2f\r\n", millis);
		}

		return results;
	}

	/**
	 * Does the Limelight have a valid target?
	 *
	 * @return
	 *         True if a valid target is present, false otherwise.
	 */
	public boolean getTV() {
		return 1.0 == targetValidEntry.get();
	}

	/**
	 * Gets the horizontal offset from the crosshair to the target in degrees.
	 *
	 * @return
	 *         Horizontal offset angle in degrees.
	 */
	public double getTX() {
		return targetXEntry.get();
	}

	/**
	 * Gets the vertical offset from the crosshair to the target in degrees.
	 *
	 * @return
	 *         Vertical offset angle in degrees.
	 */
	public double getTY() {
		return targetYEntry.get();
	}

	/**
	 * Gets the horizontal offset from the principal pixel/point to the target in degrees. This is the
	 * most accurate 2d metric if you are using a calibrated camera and you don't need adjustable
	 * crosshair functionality.
	 *
	 * @return
	 *         Horizontal offset angle in degrees
	 */
	public double getTXNC() {
		return targetXNCEntry.get();
	}

	/**
	 * Gets the vertical offset from the principal pixel/point to the target in degrees. This is the
	 * most accurate 2d metric if you are using a calibrated camera and you don't need adjustable
	 * crosshair functionality.
	 *
	 * @return
	 *         Vertical offset angle in degrees
	 */
	public double getTYNC() {
		return targetYNCEntry.get();
	}

	/**
	 * Gets the target area as a percentage of the image (0-100%).
	 *
	 * @return
	 *         Target area percentage (0-100).
	 */
	public double getTA() {
		return targetAreaEntry.get();
	}

	/**
	 * T2D is an array that contains several targeting metrics.
	 *
	 * @return
	 *         Array containing [targetValid, targetCount, targetLatency, captureLatency, tx, ty, txnc,
	 *         tync, ta, tid, targetClassIndexDetector, targetClassIndexClassifier,
	 *         targetLongSidePixels, targetShortSidePixels, targetHorizontalExtentPixels,
	 *         targetVerticalExtentPixels, targetSkewDegrees]
	 */
	public double[] getT2DArray() {
		return targetT2DEntry.get();
	}

	/**
	 * Gets the number of targets currently detected.
	 *
	 * @return
	 *         Number of detected targets
	 */
	public int getTargetCount() {
		double[] t2d = getT2DArray();
		if (t2d.length == 17) {
			return (int) t2d[1];
		}
		return 0;
	}

	/**
	 * Gets the classifier class index from the currently running neural classifier pipeline.
	 *
	 * @return
	 *         Class index from classifier pipeline.
	 */
	public int getClassifierClassIndex() {
		double[] t2d = getT2DArray();
		if (t2d.length == 17) {
			return (int) t2d[10];
		}
		return 0;
	}

	/**
	 * Gets the detector class index from the primary result of the currently running neural detector
	 * pipeline.
	 *
	 * @return
	 *         Class index from detector pipeline.
	 */
	public int getDetectorClassIndex() {
		double[] t2d = getT2DArray();
		if (t2d.length == 17) {
			return (int) t2d[11];
		}
		return 0;
	}

	/**
	 * Gets the current neural classifier result class name.
	 *
	 * @return
	 *         Class name string from classifier pipeline.
	 */
	public String getClassifierClass() {
		return targetClassiferClassEntry.get();
	}

	/**
	 * Gets the primary neural detector result class name.
	 *
	 * @return
	 *         Class name string from detector pipeline.
	 */
	public String getDetectorClass() {
		return targetDetectorClassEntry.get();
	}

	/**
	 * Gets the pipeline's processing latency contribution.
	 *
	 * @return
	 *         Pipeline latency in milliseconds.
	 */
	public double getLatency_Pipeline() {
		return pipelineLatencyEntry.get();
	}

	/**
	 * Gets the capture latency.
	 *
	 * @return
	 *         Capture latency in milliseconds.
	 */
	public double getLatency_Capture() {
		return captureLatencyEntry.get();
	}

	/**
	 * Gets the active pipeline index.
	 *
	 * @return
	 *         Current pipeline index (0-9).
	 */
	public double getCurrentPipelineIndex() {
		return pipelineIndexEntry.get();
	}

	/**
	 * Gets the current pipeline type.
	 *
	 * @return
	 *         Pipeline type string (e.g. "retro", "apriltag", etc).
	 */
	public String getCurrentPipelineType() {
		return pipelineTypeEntry.get();
	}

	/**
	 * Gets the full JSON results dump.
	 *
	 * @return
	 *         JSON string containing all current results.
	 */
	public String getJSONDump() {
		return jsonEntry.get();
	}

	/**
	 * Gets the robot's 3D pose with respect to the currently tracked target's coordinate system.
	 *
	 * @return
	 *         Pose3d object representing the robot's position and orientation relative to the target.
	 */
	public Pose3d getBotPose3d_TargetSpace() {
		double[] poseArray = botPoseTargetSpaceEntry.get();
		return JsonUtilities.toPose3D(poseArray);
	}

	/**
	 * Gets the camera's 3D pose with respect to the currently tracked target's coordinate system.
	 *
	 * @return
	 *         Pose3d object representing the camera's position and orientation relative to the target.
	 */
	public Pose3d getCameraPose3d_TargetSpace() {
		double[] poseArray = cameraPoseTargetSpaceEntry.get();
		return JsonUtilities.toPose3D(poseArray);
	}

	/**
	 * Gets the target's 3D pose with respect to the camera's coordinate system.
	 *
	 * @return
	 *         Pose3d object representing the target's position and orientation relative to the camera.
	 */
	public Pose3d getTargetPose3d_CameraSpace() {
		double[] poseArray = targetPoseCameraSpaceEntry.get();
		return JsonUtilities.toPose3D(poseArray);
	}

	/**
	 * Gets the target's 3D pose with respect to the robot's coordinate system.
	 *
	 * @return
	 *         Pose3d object representing the target's position and orientation relative to the robot.
	 */
	public Pose3d getTargetPose3d_RobotSpace() {
		double[] poseArray = targetPoseRobotSpaceEntry.get();
		return JsonUtilities.toPose3D(poseArray);
	}

	/**
	 * Gets the camera's 3D pose with respect to the robot's coordinate system.
	 *
	 * @return
	 *         Pose3d object representing the camera's position and orientation relative to the robot.
	 */
	public Pose3d getCameraPose3d_RobotSpace() {
		double[] poseArray = cameraPoseRobotSpaceEntry.get();
		return JsonUtilities.toPose3D(poseArray);
	}

	/**
	 * Gets the standard deviation readings as an array.
	 *
	 * @return
	 *         Array containing standard deviations for vision measurements.
	 * @apiNote
	 *          Array format documented in the {@link #standardDeviationsEntry} Javadoc. Also, some
	 *          details about where (not) to use this data can be found there.
	 */
	public double[] getStandardDeviationsArray() {
		return standardDeviationsEntry.get();
	}

	/**
	 * Gets the color of the target.
	 *
	 * @return
	 *         Color in the format [H, S, V].
	 */
	public double[] getTargetColor() {
		return targetColorEntry.get();
	}

	/**
	 * Gets the ID of the primary AprilTag.
	 *
	 * @return
	 *         AprilTag ID.
	 */
	public double getFiducialID() {
		return tagIdEntry.get();
	}

	/**
	 * Gets the class ID of the primary neural detector/classifier result.
	 *
	 * @return
	 *         Class ID.
	 */
	public String getNeuralClassID() {
		return targetClassEntry.get();
	}

	/**
	 * Gets the raw barcode results.
	 *
	 * @return
	 *         String array of barcode data.
	 */
	public String[] getRawBarcodeData() {
		return rawBarcodesEntry.get();
	}

	/**
	 * Gets the raw hardware metrics array.
	 *
	 * @return
	 *         Double array containing hardware metrics.
	 * @apiNote
	 *          Array format documented under {@link #hardwareMetricsEntry}.
	 */
	public double[] getHardwareMetrics() {
		return hardwareMetricsEntry.get();
	}

	/**
	 * Gets the FPS from the {@link #hardwareMetricsEntry}.
	 *
	 * @return
	 *         Current pipeline FPS.
	 */
	public double getFps() {
		return JsonUtilities.extractArrayEntry(getHardwareMetrics(), 0);
	}

	/**
	 * Gets the CPU temperature from the {@link #hardwareMetricsEntry}.
	 *
	 * @return
	 *         Current CPU temperature.
	 */
	public double getCpuTemperature() {
		return JsonUtilities.extractArrayEntry(getHardwareMetrics(), 1);
	}

	/**
	 * Gets the RAM usage from the {@link #hardwareMetricsEntry}.
	 *
	 * @return
	 *         Current RAM usage.
	 */
	public double getRamUsage() {
		return JsonUtilities.extractArrayEntry(getHardwareMetrics(), 2);
	}

	/**
	 * Gets the temperature from the {@link #hardwareMetricsEntry}.
	 *
	 * @return
	 *         Current temperature.
	 */
	public double getTemperature() {
		return JsonUtilities.extractArrayEntry(getHardwareMetrics(), 3);
	}
}
