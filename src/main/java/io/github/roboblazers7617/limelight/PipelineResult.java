package io.github.roboblazers7617.limelight;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import io.github.roboblazers7617.limelight.targets.BarcodeTarget;
import io.github.roboblazers7617.limelight.targets.FiducialTarget;
import io.github.roboblazers7617.limelight.targets.RetroreflectiveTarget;
import io.github.roboblazers7617.limelight.targets.neural.ClassifierTarget;
import io.github.roboblazers7617.limelight.targets.neural.DetectorTarget;

/**
 * Limelight PipelineResult object, parsed from a Limelight's JSON results output.
 */
public class PipelineResult {
	/**
	 * Error output from the JSON parser.
	 */
	public String error;

	/**
	 * The current Pipeline's index.
	 */
	@JsonProperty("pID")
	public double pipelineID;

	/**
	 * The targeting latency (milliseconds consumed by tracking loop this frame).
	 */
	@JsonProperty("tl")
	public double latency_pipeline;

	/**
	 * The capture latency (milliseconds between the end of the exposure of the middle row to the
	 * beginning of the tracking loop).
	 */
	@JsonProperty("cl")
	public double latency_capture;

	/**
	 * The latency introduced by parsing the JSON file.
	 */
	public double latency_jsonParse;

	/**
	 * Timestamp in milliseconds from Limelight boot.
	 */
	@JsonProperty("ts")
	public double timestamp_LIMELIGHT_publish;

	@JsonProperty("ts_rio")
	public double timestamp_RIOFPGA_capture;

	/**
	 * Are there valid targets in this result?
	 */
	@JsonProperty("v")
	@JsonFormat(shape = Shape.NUMBER)
	public boolean valid;

	/**
	 * Botpose (MegaTag).
	 *
	 * @apiNote
	 *          Array format is [x, y, z, roll, pitch, yaw (meters, degrees)]
	 */
	@JsonProperty("botpose")
	public double[] botpose;

	/**
	 * Botpose (MegaTag, WPI Red driverstation).
	 *
	 * @apiNote
	 *          Array format is [x, y, z, roll, pitch, yaw (meters, degrees)]
	 */
	@JsonProperty("botpose_wpired")
	public double[] botpose_wpired;

	/**
	 * Botpose (MegaTag, WPI Blue driverstation).
	 *
	 * @apiNote
	 *          Array format is [x, y, z, roll, pitch, yaw (meters, degrees)]
	 */
	@JsonProperty("botpose_wpiblue")
	public double[] botpose_wpiblue;

	/**
	 * The number of tags used to compute the botpose.
	 */
	@JsonProperty("botpose_tagcount")
	public double botpose_tagcount;

	/**
	 * The maximum distance between tags used to compute the botpose in meters.
	 */
	@JsonProperty("botpose_span")
	public double botpose_span;

	/**
	 * The average distance between tags used to compute the botpose in meters.
	 */
	@JsonProperty("botpose_avgdist")
	public double botpose_avgdist;

	/**
	 * The average area of the tags used to compute the botpose.
	 */
	@JsonProperty("botpose_avgarea")
	public double botpose_avgarea;

	@JsonProperty("t6c_rs")
	public double[] camerapose_robotspace;

	/**
	 * Gets the {@link #botpose} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose} of this PipelineResult, parsed into a {@link Pose3d}.
	 */
	public Pose3d getBotPose3d() {
		return JsonUtilities.toPose3D(botpose);
	}

	/**
	 * Gets the {@link #botpose_wpired} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose_wpired} of this PipelineResult, parsed into a {@link Pose3d}.
	 */
	public Pose3d getBotPose3d_wpiRed() {
		return JsonUtilities.toPose3D(botpose_wpired);
	}

	/**
	 * Gets the {@link #botpose_wpiblue} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose_wpiblue} of this PipelineResult, parsed into a {@link Pose3d}.
	 */
	public Pose3d getBotPose3d_wpiBlue() {
		return JsonUtilities.toPose3D(botpose_wpiblue);
	}

	/**
	 * Gets the {@link #botpose} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose} of this PipelineResult, parsed into a {@link Pose2d}.
	 */
	public Pose2d getBotPose2d() {
		return JsonUtilities.toPose2D(botpose);
	}

	/**
	 * Gets the {@link #botpose_wpired} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose_wpired} of this PipelineResult, parsed into a {@link Pose2d}.
	 */
	public Pose2d getBotPose2d_wpiRed() {
		return JsonUtilities.toPose2D(botpose_wpired);
	}

	/**
	 * Gets the {@link #botpose_wpiblue} of this PipelineResult.
	 *
	 * @return
	 *         The {@link #botpose_wpiblue} of this PipelineResult, parsed into a {@link Pose2d}.
	 */
	public Pose2d getBotPose2d_wpiBlue() {
		return JsonUtilities.toPose2D(botpose_wpiblue);
	}

	/**
	 * Retroreflective pipeline outputs.
	 */
	@JsonProperty("Retro")
	public RetroreflectiveTarget[] targets_Retro;

	/**
	 * Fiducial target pipeline outputs.
	 */
	@JsonProperty("Fiducial")
	public FiducialTarget[] targets_Fiducials;

	/**
	 * Classifier pipeline outputs.
	 */
	@JsonProperty("Classifier")
	public ClassifierTarget[] targets_Classifier;

	/**
	 * Neural Detector pipeline outputs.
	 */
	@JsonProperty("Detector")
	public DetectorTarget[] targets_Detector;

	/**
	 * Barcode pipeline outputs.
	 */
	@JsonProperty("Barcode")
	public BarcodeTarget[] targets_Barcode;

	/**
	 * Creates a new PipelineResult with blank values.
	 */
	public PipelineResult() {
		botpose = new double[6];
		botpose_wpired = new double[6];
		botpose_wpiblue = new double[6];
		camerapose_robotspace = new double[6];
		targets_Retro = new RetroreflectiveTarget[0];
		targets_Fiducials = new FiducialTarget[0];
		targets_Classifier = new ClassifierTarget[0];
		targets_Detector = new DetectorTarget[0];
		targets_Barcode = new BarcodeTarget[0];
	}
}
