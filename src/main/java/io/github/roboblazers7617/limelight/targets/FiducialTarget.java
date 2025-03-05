package io.github.roboblazers7617.limelight.targets;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import io.github.roboblazers7617.limelight.JsonUtilities;

/**
 * Represents an AprilTag/Fiducial Target Result extracted from JSON Output.
 */
public class FiducialTarget {
	/**
	 * The ID of the primary in-view fiducial.
	 */
	@JsonProperty("fID")
	public double fiducialID;

	/**
	 * The family of the primary in-view fiducial.
	 */
	@JsonProperty("fam")
	public String fiducialFamily;

	/**
	 * The 3D transform of the Limelight in the coordinate system of the primary in-view fiducial.
	 */
	@JsonProperty("t6c_ts")
	private double[] cameraPose_TargetSpace;

	/**
	 * The 3D transform of the robot in the coordinate system of the field.
	 */
	@JsonProperty("t6r_fs")
	private double[] robotPose_FieldSpace;

	/**
	 * The 3D transform of the robot in the coordinate system of the primary in-view fiducial.
	 */
	@JsonProperty("t6r_ts")
	private double[] robotPose_TargetSpace;

	/**
	 * The 3D transform of the target in the coordinate system of the Limelight.
	 */
	@JsonProperty("t6t_cs")
	private double[] targetPose_CameraSpace;

	/**
	 * The 3D transform of the target in the coordinate system of the robot.
	 */
	@JsonProperty("t6t_rs")
	private double[] targetPose_RobotSpace;

	/**
	 * Gets the {@link #cameraPose_TargetSpace} for this target.
	 *
	 * @return
	 *         The {@link #cameraPose_TargetSpace} for this target, parsed into a {@link Pose3d}.
	 */
	public Pose3d getCameraPose_TargetSpace() {
		return JsonUtilities.toPose3D(cameraPose_TargetSpace);
	}

	/**
	 * Gets the {@link #robotPose_FieldSpace} for this target.
	 *
	 * @return
	 *         The {@link #robotPose_FieldSpace} for this target, parsed into a {@link Pose3d}.
	 */
	public Pose3d getRobotPose_FieldSpace() {
		return JsonUtilities.toPose3D(robotPose_FieldSpace);
	}

	/**
	 * Gets the {@link #robotPose_TargetSpace} for this target.
	 *
	 * @return
	 *         The {@link #robotPose_TargetSpace} for this target, parsed into a {@link Pose3d}.
	 */
	public Pose3d getRobotPose_TargetSpace() {
		return JsonUtilities.toPose3D(robotPose_TargetSpace);
	}

	/**
	 * Gets the {@link #targetPose_CameraSpace} for this target.
	 *
	 * @return
	 *         The {@link #targetPose_CameraSpace} for this target, parsed into a {@link Pose3d}.
	 */
	public Pose3d getTargetPose_CameraSpace() {
		return JsonUtilities.toPose3D(targetPose_CameraSpace);
	}

	/**
	 * Gets the {@link #targetPose_RobotSpace} for this target.
	 *
	 * @return
	 *         The {@link #targetPose_RobotSpace} for this target, parsed into a {@link Pose3d}.
	 */
	public Pose3d getTargetPose_RobotSpace() {
		return JsonUtilities.toPose3D(targetPose_RobotSpace);
	}

	/**
	 * Gets the {@link #cameraPose_TargetSpace} for this target.
	 *
	 * @return
	 *         The {@link #cameraPose_TargetSpace} for this target, parsed into a {@link Pose2d}.
	 */
	public Pose2d getCameraPose_TargetSpace2D() {
		return JsonUtilities.toPose2D(cameraPose_TargetSpace);
	}

	/**
	 * Gets the {@link #robotPose_FieldSpace} for this target.
	 *
	 * @return
	 *         The {@link #robotPose_FieldSpace} for this target, parsed into a {@link Pose2d}.
	 */
	public Pose2d getRobotPose_FieldSpace2D() {
		return JsonUtilities.toPose2D(robotPose_FieldSpace);
	}

	/**
	 * Gets the {@link #robotPose_TargetSpace} for this target.
	 *
	 * @return
	 *         The {@link #robotPose_TargetSpace} for this target, parsed into a {@link Pose2d}.
	 */
	public Pose2d getRobotPose_TargetSpace2D() {
		return JsonUtilities.toPose2D(robotPose_TargetSpace);
	}

	/**
	 * Gets the {@link #targetPose_CameraSpace} for this target.
	 *
	 * @return
	 *         The {@link #targetPose_CameraSpace} for this target, parsed into a {@link Pose2d}.
	 */
	public Pose2d getTargetPose_CameraSpace2D() {
		return JsonUtilities.toPose2D(targetPose_CameraSpace);
	}

	/**
	 * Gets the {@link #targetPose_RobotSpace} for this target.
	 *
	 * @return
	 *         The {@link #targetPose_RobotSpace} for this target, parsed into a {@link Pose2d}.
	 */
	public Pose2d getTargetPose_RobotSpace2D() {
		return JsonUtilities.toPose2D(targetPose_RobotSpace);
	}

	@JsonProperty("ta")
	public double ta;

	@JsonProperty("tx")
	public double tx;

	@JsonProperty("ty")
	public double ty;

	@JsonProperty("txp")
	public double tx_pixels;

	@JsonProperty("typ")
	public double ty_pixels;

	@JsonProperty("tx_nocross")
	public double tx_nocrosshair;

	@JsonProperty("ty_nocross")
	public double ty_nocrosshair;

	@JsonProperty("ts")
	public double ts;

	/**
	 * Creates a new FiducialTarget with blank values.
	 */
	public FiducialTarget() {
		cameraPose_TargetSpace = new double[6];
		robotPose_FieldSpace = new double[6];
		robotPose_TargetSpace = new double[6];
		targetPose_CameraSpace = new double[6];
		targetPose_RobotSpace = new double[6];
	}
}
