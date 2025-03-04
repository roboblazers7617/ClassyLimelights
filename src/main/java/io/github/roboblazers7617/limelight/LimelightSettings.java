package io.github.roboblazers7617.limelight;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.List;

/**
 * Settings class to apply configurable options to the {@link Limelight}.
 * <p>
 * These settings are sent from the roboRIO back to the Limelight to affect the LL.
 * <p>
 * One or more chains of ".withXXXX" methods can change the LL settings. The action of each ".withXXXX" method is
 * essentially immediate, however, some slight delay is possible and the {@link #save()} method will immediately save any
 * settings that had not yet been saved.
 * <p>
 * Initially, at constructor time, settings are fetched from the LL, however, there is no provision to programatically
 * access those values - they are dead, useless.
 */
public class LimelightSettings {
	/**
	 * {@link NetworkTable} for the {@link Limelight}.
	 */
	private NetworkTable limelightTable;
	/**
	 * {@link Limelight} to fetch data for.
	 */
	private Limelight limelight;
	/**
	 * LED Mode entry for the Limelight.
	 *
	 * @see LEDMode#value
	 */
	private NetworkTableEntry ledMode;
	/**
	 * PipelineIndex entry for the Limelight.
	 */
	private NetworkTableEntry pipelineIndex;
	/**
	 * Priority TagID entry for the Limelight.
	 */
	private NetworkTableEntry priorityTagID;
	/**
	 * Stream mode.
	 *
	 * @see StreamMode#value
	 */
	private NetworkTableEntry streamMode;
	/**
	 * Crop window entry for the camera. The crop window in the UI must be completely open.
	 *
	 * @apiNote
	 *          DoubleArray [cropXMin,cropXMax,cropYMin,cropYMax] values between -1 and 1.
	 */
	private DoubleArrayEntry cropWindow;
	/**
	 * Imu Mode entry for the Limelight 4.
	 *
	 * @see ImuMode#value
	 */
	private NetworkTableEntry imuMode;
	/**
	 * Imu assist alpha/strengh entry while in the Imu assist modes using a complementary filter.
	 *
	 * @apiNote
	 *          Default is 0.001.
	 */
	private NetworkTableEntry imuAssistAlpha;
	/**
	 * Entry for the number of frames to skip between processing the contents of a frame.
	 */
	private NetworkTableEntry processFrameFrequency;
	/**
	 * Entry that sets 3d offset point for easy 3d targeting Sets the 3D point-of-interest offset for the current fiducial pipeline.
	 * <p>
	 * https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-3d#point-of-interest-tracking
	 *
	 * @apiNote
	 *          DoubleArray [offsetX(meters), offsetY(meters), offsetZ(meters)].
	 */
	private DoubleArrayEntry fiducial3DOffset;
	/**
	 * DoubleArray entry of valid AprilTag ID's to track.
	 */
	private DoubleArrayEntry fiducialIDFiltersOverride;
	/**
	 * Entry for the downscaling factor for AprilTag detection. Increasing downscale can improve performance at the cost of potentially reduced detection range.
	 *
	 * @see DownscalingOverride#value
	 */
	private NetworkTableEntry downscale;
	/**
	 * Camera pose entry relative to the robot.
	 *
	 * @apiNote
	 *          DoubleArray [forward(meters), side(meters), up(meters), roll(degrees), pitch(degrees), yaw(degrees)]
	 */
	private DoubleArrayEntry cameraToRobot;

	/**
	 * Create a {@link LimelightSettings} object with all configurable features of a {@link Limelight}.
	 *
	 * @param camera
	 *            {@link Limelight} to use.
	 */
	public LimelightSettings(Limelight camera) {
		limelight = camera;
		limelightTable = limelight.getNetworkTable();
		ledMode = limelightTable.getEntry("ledMode");

		pipelineIndex = limelightTable.getEntry("pipeline");
		priorityTagID = limelightTable.getEntry("priorityid");
		streamMode = limelightTable.getEntry("stream");
		cropWindow = limelightTable.getDoubleArrayTopic("crop").getEntry(new double[0]);
		imuMode = limelightTable.getEntry("imumode_set");
		imuAssistAlpha = limelightTable.getEntry("imuassistalpha_set");
		processFrameFrequency = limelightTable.getEntry("throttle_set");
		downscale = limelightTable.getEntry("fiducial_downscale_set");
		fiducial3DOffset = limelightTable.getDoubleArrayTopic("fiducial_offset_set").getEntry(new double[0]);
		cameraToRobot = limelightTable.getDoubleArrayTopic("camerapose_robotspace_set").getEntry(new double[0]);
		fiducialIDFiltersOverride = limelightTable.getDoubleArrayTopic("fiducial_id_filters_set").getEntry(new double[0]);
	}

	/**
	 * Set the {@link #ledMode LED mode}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param mode
	 *            LED mode to set.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withLimelightLEDMode(LEDMode mode) {
		ledMode.setNumber(mode.getValue());
		return this;
	}

	/**
	 * Set the {@link #pipelineIndex current pipeline index}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param index
	 *            Pipeline index to use.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withPipelineIndex(int index) {
		pipelineIndex.setNumber(index);
		return this;
	}

	/**
	 * Set the {@link #priorityTagID Priority Tag ID}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param aprilTagId
	 *            AprilTag ID to set as a priority.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withPriorityTagId(int aprilTagId) {
		priorityTagID.setNumber(aprilTagId);
		return this;
	}

	/**
	 * Set the {@link #streamMode Stream mode}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param mode
	 *            Stream mode to use.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withStreamMode(StreamMode mode) {
		streamMode.setNumber(mode.getValue());
		return this;
	}

	/**
	 * Sets the crop window for the camera. The crop window in the UI must be completely open.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param minX
	 *            Minimum X value (-1 to 1)
	 * @param maxX
	 *            Maximum X value (-1 to 1)
	 * @param minY
	 *            Minimum Y value (-1 to 1)
	 * @param maxY
	 *            Maximum Y value (-1 to 1)
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withCropWindow(double minX, double maxX, double minY, double maxY) {
		cropWindow.set(new double[] { minX, maxX, minY, maxY });
		return this;
	}

	/**
	 * Set the {@link #imuMode IMU Mode}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param mode
	 *            {@link ImuMode} to use.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withImuMode(ImuMode mode) {
		imuMode.setNumber(mode.getValue());
		return this;
	}

	/**
	 * Set the {@link #imuAssistAlpha IMU's alpha value} for its complementary filter while in one of the {@link ImuMode}'s assist modes.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param alpha
	 *            Alpha value to set for the complementary filter. Default of 0.001.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withImuAssistAlpha(double alpha) {
		imuAssistAlpha.setDouble(alpha);
		return this;
	}

	/**
	 * Sets the {@link #processFrameFrequency number of frames to skip} between processing the contents of a frame.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param skippedFrames
	 *            Number of frames to skip in between processing camera data.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withProcessedFrameFrequency(int skippedFrames) {
		processFrameFrequency.setNumber(skippedFrames);
		return this;
	}

	/**
	 * Sets the {@link #downscale downscaling factor} for AprilTag detection. Increasing downscale can improve performance at the cost of
	 * potentially reduced detection range.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param downscalingOverride
	 *            Downscale factor.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withFiducialDownscalingOverride(DownscalingOverride downscalingOverride) {
		downscale.setDouble(downscalingOverride.getValue());
		return this;
	}

	/**
	 * Set the {@link #fiducial3DOffset offset} from the AprilTag that is of interest. More information here. <a
	 * href="https://docs.limelightvision.io/docs/docs-limelight/pipeline-apriltag/apriltag-3d#point-of-interest-tracking">Docs
	 * page</a>
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param offset
	 *            {@link Translation3d} offset.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withAprilTagOffset(Translation3d offset) {
		fiducial3DOffset.set(JsonUtilities.translation3dToArray(offset));
		return this;
	}

	/**
	 * Set the {@link #fiducialIDFiltersOverride AprilTagID filter/override} of which to track.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param idFilter
	 *            Array of AprilTag ID's to track
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withArilTagIdFilter(List<Double> idFilter) {
		fiducialIDFiltersOverride.set(idFilter.stream().mapToDouble(Double::doubleValue).toArray());
		return this;
	}

	/**
	 * Set the {@link #cameraToRobot camera position offset}.
	 *
	 * @apiNote
	 *          This method changes the Limelight - normally immediately.
	 * @param offset
	 *            {@link Pose3d} of the {@link Limelight} with the {@link edu.wpi.first.math.geometry.Rotation3d} set in Meters.
	 * @return
	 *         This object for method chaining.
	 */
	public LimelightSettings withCameraOffset(Pose3d offset) {
		cameraToRobot.set(JsonUtilities.pose3dToArray(offset));
		return this;
	}

	/**
	 * Push any pending changes to the {@link NetworkTable} instance immediately.
	 *
	 * @apiNote
	 *          This method changes the Limelight immediately.
	 * @apiNote
	 *          Most setting changes are done essentially immediately and this method
	 *          isn't needed but does no harm to assure changes.
	 */
	public void save() {
		NetworkTableInstance.getDefault().flush();
	}

	/**
	 * LED Mode for the {@link Limelight}.
	 */
	public enum LEDMode {
		/**
		 * Pipeline mode.
		 */
		PipelineControl(0),
		/**
		 * Force the LEDs off.
		 */
		ForceOff(1),
		/**
		 * Force the LEDs to blink.
		 */
		ForceBlink(2),
		/**
		 * Force the LEDs on.
		 */
		ForceOn(3);

		/**
		 * The NetworkTables value for this entry.
		 */
		public final int value;

		/**
		 * Creates a new LEDMode entry.
		 *
		 * @param value
		 *            The {@link #value} to assign.
		 */
		LEDMode(int value) {
			this.value = value;
		}

		/**
		 * Gets the {@link #value} of this entry.
		 *
		 * @return
		 *         {@link #value} of this entry.
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * Stream mode for the {@link Limelight}
	 */
	public enum StreamMode {
		/**
		 * Side by side.
		 */
		Standard(0),
		/**
		 * Picture in picture, with secondary in corner.
		 */
		PictureInPictureMain(1),
		/**
		 * Picture in picture, with main in corner.
		 */
		PictureInPictureSecondary(2);

		/**
		 * The NetworkTables value for this entry.
		 */
		public final int value;

		/**
		 * Creates a new StreamMode entry.
		 *
		 * @param value
		 *            The {@link #value} to assign.
		 */
		StreamMode(int value) {
			this.value = value;
		}

		/**
		 * Gets the {@link #value} of this entry.
		 *
		 * @return
		 *         {@link #value} of this entry.
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * Downscaling Override Enum for {@link Limelight}
	 */
	public enum DownscalingOverride {
		/**
		 * Pipeline downscaling.
		 */
		Pipeline(0),
		/**
		 * No downscaling.
		 */
		NoDownscale(1),
		/**
		 * Half downscaling.
		 */
		HalfDownscale(2),
		/**
		 * Double downscaling.
		 */
		DoubleDownscale(3),
		/**
		 * Triple downscaling.
		 */
		TripleDownscale(4),
		/**
		 * Quadruple downscaling.
		 */
		QuadrupleDownscale(5);

		/**
		 * The NetworkTables value for this entry.
		 */
		public final int value;

		/**
		 * Creates a new DownscalingOverride entry.
		 *
		 * @param value
		 *            The {@link #value} to assign.
		 */
		DownscalingOverride(int value) {
			this.value = value;
		}

		/**
		 * Gets the {@link #value} of this entry.
		 *
		 * @return
		 *         {@link #value} of this entry.
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * IMU Mode Enum for the {@link Limelight}
	 */
	public enum ImuMode {
		/**
		 * Use external IMU yaw submitted via {@link Limelight#setRobotOrientation(edu.wpi.first.math.geometry.Rotation3d)} for MT2 localization. The internal IMU is ignored entirely.
		 */
		ExternalImu(0),
		/**
		 * Use external IMU yaw submitted via {@link Limelight#setRobotOrientation(edu.wpi.first.math.geometry.Rotation3d)} for MT2 localization. The internal IMU is synced with the external IMU.
		 */
		SyncInternalImu(1),
		/**
		 * Use internal IMU for MT2 localization. Ignores external IMU updates from {@link Limelight#setRobotOrientation(edu.wpi.first.math.geometry.Rotation3d)}.
		 */
		InternalImu(2),
		/**
		 * Use internal IMU for MT2 localization while using correcting it with estimated yaws from MT1. Ignores external IMU updates from {@link Limelight#setRobotOrientation(edu.wpi.first.math.geometry.Rotation3d)}.
		 */
		MT1AssistInternalImu(3),
		/**
		 * Use internal IMU for MT2 localization while correcting it with external IMU updates from {@link Limelight#setRobotOrientation(edu.wpi.first.math.geometry.Rotation3d)}.
		 */
		ExternalAssistInternalIMU(4);

		/**
		 * The NetworkTables value for this entry.
		 */
		public final int value;

		/**
		 * Creates a new ImuMode entry.
		 *
		 * @param value
		 *            The {@link #value} to assign.
		 */
		ImuMode(int value) {
			this.value = value;
		}

		/**
		 * Gets the {@link #value} of this entry.
		 *
		 * @return
		 *         {@link #value} of this entry.
		 */
		public int getValue() {
			return value;
		}
	}
}
