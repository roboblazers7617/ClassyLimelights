package io.github.roboblazers7617.limelight.targets.neural;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Neural Detector Pipeline Result extracted from JSON Output
 */
public class DetectorTarget {
	/**
	 * Human-readable class name string.
	 */
	@JsonProperty("class")
	public String className;

	/**
	 * Class ID integer.
	 */
	@JsonProperty("classID")
	public double classID;

	/**
	 * Confidence of the predicition.
	 */
	@JsonProperty("conf")
	public double confidence;

	/**
	 * The size of the target as a percentage of the image [0-1].
	 */
	@JsonProperty("ta")
	public double ta;

	/**
	 * The X-coordinate of the center of the target in degrees relative to crosshair. Positive-right,
	 * center-zero.
	 */
	@JsonProperty("tx")
	public double tx;

	/**
	 * The Y-coordinate of the center of the target in degrees relative to crosshair. Positive-down,
	 * center-zero.
	 */
	@JsonProperty("ty")
	public double ty;

	/**
	 * The X-coordinate of the center of the target in pixels relative to crosshair. Positive-right,
	 * center-zero.
	 */
	@JsonProperty("txp")
	public double tx_pixels;

	/**
	 * The Y-coordinate of the center of the target in pixels relative to crosshair. Positive-down,
	 * center-zero.
	 */
	@JsonProperty("typ")
	public double ty_pixels;

	/**
	 * The X-coordinate of the center of the target in degrees relative to principal pixel.
	 * Positive-right, center-zero.
	 */
	@JsonProperty("tx_nocross")
	public double tx_nocrosshair;

	/**
	 * The Y-coordinate of the center of the target in degrees relative to principal pixel.
	 * Positive-down, center-zero.
	 */
	@JsonProperty("ty_nocross")
	public double ty_nocrosshair;

	/**
	 * Creates a new DetectorTarget with blank values.
	 */
	public DetectorTarget() {}
}
