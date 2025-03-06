package io.github.roboblazers7617.limelight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import io.github.roboblazers7617.limelight.targets.RawFiducialTarget;

/**
 * Represents a 3D Pose Estimate.
 */
public class PoseEstimate {
	/**
	 * The estimated robot pose.
	 */
	public Pose3d pose;

	/**
	 * The NetworkTables timestamp in seconds.
	 */
	public double timestampSeconds;

	/**
	 * The latency in milliseconds.
	 */
	public double latency;

	/**
	 * The number of tags used to compute this pose.
	 */
	public int tagCount;

	/**
	 * The maximium distance between the tags used to compute this pose in meters.
	 */
	public double tagSpan;

	/**
	 * The average distance between tags used to compute this pose in meters.
	 */
	public double avgTagDist;

	/**
	 * The average area of the tags used to compute this pose.
	 */
	public double avgTagArea;

	/**
	 * The raw fiducial data used to estimate this pose.
	 */
	public RawFiducialTarget[] rawFiducials;

	/**
	 * Was this pose computed using MegaTag2?
	 */
	public boolean isMegaTag2;

	/**
	 * Instantiates a PoseEstimate object with default values.
	 */
	public PoseEstimate() {
		this.pose = new Pose3d();
		this.timestampSeconds = 0;
		this.latency = 0;
		this.tagCount = 0;
		this.tagSpan = 0;
		this.avgTagDist = 0;
		this.avgTagArea = 0;
		this.rawFiducials = new RawFiducialTarget[] {};
		this.isMegaTag2 = false;
	}

	/**
	 * Creates a new PoseEstimate.
	 *
	 * @param pose
	 *            The {@link #pose} to use.
	 * @param timestampSeconds
	 *            The {@link #timestampSeconds} to use.
	 * @param latency
	 *            The {@link #latency} to use.
	 * @param tagCount
	 *            The {@link #tagCount} to use.
	 * @param tagSpan
	 *            The {@link #tagSpan} to use.
	 * @param avgTagDist
	 *            The {@link #avgTagDist} to use.
	 * @param avgTagArea
	 *            The {@link #avgTagArea} to use.
	 * @param rawFiducials
	 *            The {@link #rawFiducials} to use.
	 * @param isMegaTag2
	 *            {@link #isMegaTag2}?
	 */
	public PoseEstimate(Pose3d pose, double timestampSeconds, double latency, int tagCount, double tagSpan, double avgTagDist, double avgTagArea, RawFiducialTarget[] rawFiducials, boolean isMegaTag2) {
		this.pose = pose;
		this.timestampSeconds = timestampSeconds;
		this.latency = latency;
		this.tagCount = tagCount;
		this.tagSpan = tagSpan;
		this.avgTagDist = avgTagDist;
		this.avgTagArea = avgTagArea;
		this.rawFiducials = rawFiducials;
		this.isMegaTag2 = isMegaTag2;
	}

	/**
	 * Gets the {@link #pose} for this PoseEstimate.
	 *
	 * @return
	 *         The {@link #pose} for this PoseEstimate.
	 */
	public Pose3d getPose3d() {
		return pose;
	}

	/**
	 * Gets the {@link #pose} as a {@link Pose2d}.
	 *
	 * @return
	 *         {@link #pose} as a {@link Pose2d}.
	 * @see Pose3d#toPose2d()
	 */
	public Pose2d getPose2d() {
		return pose.toPose2d();
	}

	/**
	 * Gets the {@link #rawFiducials} for this PoseEstimate.
	 *
	 * @return
	 *         The {@link #rawFiducials} for this PoseEstimate.
	 */
	public RawFiducialTarget[] getDetectedTags() {
		return rawFiducials;
	}

	/**
	 * Gets the {@link #timestampSeconds} for this PoseEstimate.
	 *
	 * @return
	 *         The {@link #timestampSeconds} for this PoseEstimate.
	 */
	public double getTimestampSeconds() {
		return timestampSeconds;
	}

	/**
	 * Gets the {@link #tagCount} for this PoseEstimate.
	 *
	 * @return
	 *         The {@link #tagCount} for this PoseEstimate.
	 */
	public int getTagCount() {
		return tagCount;
	}

	/**
	 * Prints detailed information about this PoseEstimate to standard output. Includes timestamp,
	 * latency, tag count, tag span, average tag distance, average tag area, and detailed information
	 * about each detected fiducial.
	 */
	public void print() {
		System.out.printf("Pose Estimate Information:%n");
		System.out.printf("Timestamp (Seconds): %.3f%n", timestampSeconds);
		System.out.printf("Latency: %.3f ms%n", latency);
		System.out.printf("Tag Count: %d%n", tagCount);
		System.out.printf("Tag Span: %.2f meters%n", tagSpan);
		System.out.printf("Average Tag Distance: %.2f meters%n", avgTagDist);
		System.out.printf("Average Tag Area: %.2f%% of image%n", avgTagArea);
		System.out.printf("Is MegaTag2: %b%n", isMegaTag2);
		System.out.println();

		if (rawFiducials == null || rawFiducials.length == 0) {
			System.out.println("No RawFiducials data available.");
			return;
		}

		System.out.println("Raw Fiducials Details:");
		for (int i = 0; i < rawFiducials.length; i++) {
			RawFiducialTarget fiducial = rawFiducials[i];
			System.out.printf(" Fiducial #%d:%n", i + 1);
			System.out.printf("  ID: %d%n", fiducial.id);
			System.out.printf("  TXNC: %.2f%n", fiducial.txnc);
			System.out.printf("  TYNC: %.2f%n", fiducial.tync);
			System.out.printf("  TA: %.2f%n", fiducial.ta);
			System.out.printf("  Distance to Camera: %.2f meters%n", fiducial.distToCamera);
			System.out.printf("  Distance to Robot: %.2f meters%n", fiducial.distToRobot);
			System.out.printf("  Ambiguity: %.2f%n", fiducial.ambiguity);
			System.out.println();
		}
	}
}
