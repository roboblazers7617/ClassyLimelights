package io.github.roboblazers7617.limelight.targets;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Barcode Target Result extracted from JSON Output.
 */
public class BarcodeTarget {
	/**
	 * Creates a new BarcodeTarget with blank values.
	 */
	public BarcodeTarget() {}

	/**
	 * Barcode family type (e.g. "QR", "DataMatrix", etc.).
	 */
	@JsonProperty("fam")
	public String family;

	/**
	 * The decoded data content of the barcode.
	 */
	@JsonProperty("data")
	public String data;

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
	 * The size of the target as a percentage of the image [0-1].
	 */
	@JsonProperty("ta")
	public double ta;

	/**
	 * Corners array (pixels) [x0, y0, x1, y1, ...].
	 *
	 * @apiNote
	 *          Must be enabled in output tab.
	 */
	@JsonProperty("pts")
	public double[][] corners;

	/**
	 * Gets the {@link #family} of this BarcodeTarget.
	 *
	 * @return
	 *         {@link #family} string of this BarcodeTarget.
	 */
	public String getFamily() {
		return family;
	}
}
