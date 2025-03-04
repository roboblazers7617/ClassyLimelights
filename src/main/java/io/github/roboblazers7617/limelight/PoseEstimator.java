package io.github.roboblazers7617.limelight;

import java.util.Arrays;

import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.TimestampedDoubleArray;
import io.github.roboblazers7617.limelight.targets.RawFiducialTarget;

/**
 * Class to get pose estimate data from a {@link Limelight}.
 */
public class PoseEstimator {
	/**
	 * The NetworkTable for the {@link Limelight}.
	 */
	private final NetworkTable networkTable;
	/**
	 * The pose estimator to use.
	 */
	private final PoseEstimators poseEstimator;

	/**
	 * Subscriber for the pose data from the Limelight.
	 */
	private final DoubleArraySubscriber poseSubscriber;
	/**
	 * Entry for the raw fiducial data from the Limelight.
	 */
	private final NetworkTableEntry rawFiducialsEntry;

	/**
	 * Creates a new PoseEstimator.
	 *
	 * @param limelight
	 *            The {@link Limelight} to get data from.
	 * @param poseEstimator
	 *            The {@link PoseEstimators pose estimator} to use.
	 */
	protected PoseEstimator(Limelight limelight, PoseEstimators poseEstimator) {
		networkTable = limelight.getNetworkTable();
		this.poseEstimator = poseEstimator;

		poseSubscriber = networkTable.getDoubleArrayTopic(poseEstimator.getEntry()).subscribe(new double[0]);
		rawFiducialsEntry = networkTable.getEntry("rawfiducials");
	}

	/**
	 * Gets the pose estimates for this PoseEstimator.
	 *
	 * @return
	 *         Array of pose estimates from this PoseEstimator.
	 */
	public PoseEstimate[] getBotPoseEstimates() {
		return Arrays.stream(poseSubscriber.readQueue())
				.map((poseEstimateArray) -> {
					return generatePoseEstimateFromNT(poseEstimateArray);
				})
				.toArray(PoseEstimate[]::new);
	}

	/**
	 * Parses a pose estimate from NetworkTables data.
	 *
	 * @param poseTimestamped
	 *            The raw pose data from NetworkTables.
	 * @return
	 *         The resulting PoseEstimate.
	 */
	private PoseEstimate generatePoseEstimateFromNT(TimestampedDoubleArray poseTimestamped) {
		double[] poseArray = poseTimestamped.value;
		long timestamp = poseTimestamped.timestamp;

		if (poseArray.length == 0) {
			// Handle the case where no data is available
			return null; // or some default PoseEstimate
		}

		var pose = JsonUtilities.toPose3D(poseArray);
		double latency = JsonUtilities.extractArrayEntry(poseArray, 6);
		int tagCount = (int) JsonUtilities.extractArrayEntry(poseArray, 7);
		double tagSpan = JsonUtilities.extractArrayEntry(poseArray, 8);
		double tagDist = JsonUtilities.extractArrayEntry(poseArray, 9);
		double tagArea = JsonUtilities.extractArrayEntry(poseArray, 10);

		// Convert server timestamp from microseconds to seconds and adjust for latency
		double adjustedTimestamp = (timestamp / 1000000.0) - (latency / 1000.0);

		RawFiducialTarget[] rawFiducials = new RawFiducialTarget[tagCount];
		int valsPerFiducial = 7;
		int expectedTotalVals = 11 + valsPerFiducial * tagCount;

		if (poseArray.length != expectedTotalVals) {
			// Don't populate fiducials
		} else {
			for (int i = 0; i < tagCount; i++) {
				int baseIndex = 11 + (i * valsPerFiducial);
				int id = (int) poseArray[baseIndex];
				double txnc = poseArray[baseIndex + 1];
				double tync = poseArray[baseIndex + 2];
				double ta = poseArray[baseIndex + 3];
				double distToCamera = poseArray[baseIndex + 4];
				double distToRobot = poseArray[baseIndex + 5];
				double ambiguity = poseArray[baseIndex + 6];
				rawFiducials[i] = new RawFiducialTarget(id, txnc, tync, ta, distToCamera, distToRobot, ambiguity);
			}
		}

		return new PoseEstimate(pose, adjustedTimestamp, latency, tagCount, tagSpan, tagDist, tagArea, rawFiducials, poseEstimator.isMegaTag2());
	}

	/**
	 * Gets the latest raw fiducial/AprilTag detection results from NetworkTables.
	 *
	 * @return
	 *         Array of RawFiducialTarget objects containing detection details.
	 */
	public RawFiducialTarget[] getRawFiducialTargets() {
		var rawFiducialArray = rawFiducialsEntry.getDoubleArray(new double[0]);
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
	 * Returns whether or not the given pose estimate is valid.
	 *
	 * @param pose
	 *            Pose estimate to check.
	 * @return
	 *         Is this pose estimate valid?
	 */
	public Boolean validPoseEstimate(PoseEstimate pose) {
		return pose != null && pose.rawFiducials != null && pose.rawFiducials.length != 0;
	}

	/**
	 * The different pose estimators available to query.
	 */
	public enum PoseEstimators {
		/**
		 * (Not Recommended) The robot's pose in the WPILib Red Alliance Coordinate System.
		 */
		RED("botpose_wpired", false),
		/**
		 * (Not Recommended) The robot's pose in the WPILib Red Alliance Coordinate System with MegaTag2.
		 */
		RED_MEGATAG2("botpose_orb_wpired", true),
		/**
		 * (Recommended) The robot's 3D pose in the WPILib Blue Alliance Coordinate System.
		 */
		BLUE("botpose_wpiblue", false),
		/**
		 * (Recommended) The robot's 3D pose in the WPILib Blue Alliance Coordinate System with MegaTag2.
		 */
		BLUE_MEGATAG2("botpose_orb_wpiblue", true);

		/**
		 * {@link Limelight} botpose entry name.
		 */
		private final String entry;
		/**
		 * Is this a MegaTag2 pose estimator?
		 */
		private final boolean isMegaTag2;

		/**
		 * Creates an enum value with the given entry names and MegaTag2 state.
		 *
		 * @param entry
		 *            Bot Pose entry name for {@link Limelight}
		 * @param megatag2
		 *            Is this a MegaTag2 pose estimator?
		 */
		PoseEstimators(String entry, boolean isMegaTag2) {
			this.entry = entry;
			this.isMegaTag2 = isMegaTag2;
		}

		/**
		 * Gets the {@link #entry} for this pose estimator.
		 *
		 * @return
		 *         {@link #entry} for this pose estimator.
		 */
		public String getEntry() {
			return entry;
		}

		/**
		 * Gets the {@link #isMegaTag2} state for this pose estimator.
		 *
		 * @return
		 *         {@link #isMegaTag2} state for this pose estimator.
		 */
		public Boolean isMegaTag2() {
			return isMegaTag2;
		}
	}
}
