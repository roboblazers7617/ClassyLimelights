package io.github.roboblazers7617.limelight.targets.neural;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Neural Classifier Pipeline Result extracted from JSON Output
 */
public class ClassifierTarget {
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

	@JsonProperty("zone")
	public double zone;

	@JsonProperty("tx")
	public double tx;

	@JsonProperty("txp")
	public double tx_pixels;

	@JsonProperty("ty")
	public double ty;

	@JsonProperty("typ")
	public double ty_pixels;

	/**
	 * Creates a new ClassifierTarget with blank values.
	 */
	public ClassifierTarget() {}
}
