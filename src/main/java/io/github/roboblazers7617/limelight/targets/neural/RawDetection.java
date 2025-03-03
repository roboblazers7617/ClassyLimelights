package io.github.roboblazers7617.limelight.targets.neural;

/**
 * Represents a Limelight Raw Neural Detector result from Limelight's NetworkTables output.
 */
public class RawDetection {
	public int classId = 0;
	public double txnc = 0;
	public double tync = 0;
	public double ta = 0;
	public double corner0_X = 0;
	public double corner0_Y = 0;
	public double corner1_X = 0;
	public double corner1_Y = 0;
	public double corner2_X = 0;
	public double corner2_Y = 0;
	public double corner3_X = 0;
	public double corner3_Y = 0;

	/**
	 * Creates a new RawDetection with the given values.
	 *
	 * @param classId
	 *            {@link #classId} to use.
	 * @param txnc
	 *            {@link #txnc} to use.
	 * @param tync
	 *            {@link #tync} to use.
	 * @param ta
	 *            {@link #ta} to use.
	 * @param corner0_X
	 *            {@link #corner0_X} to use.
	 * @param corner0_Y
	 *            {@link #corner0_Y} to use.
	 * @param corner1_X
	 *            {@link #corner1_X} to use.
	 * @param corner1_Y
	 *            {@link #corner1_Y} to use.
	 * @param corner2_X
	 *            {@link #corner2_X} to use.
	 * @param corner2_Y
	 *            {@link #corner2_Y} to use.
	 * @param corner3_X
	 *            {@link #corner3_X} to use.
	 * @param corner3_Y
	 *            {@link #corner3_Y} to use.
	 */
	public RawDetection(int classId, double txnc, double tync, double ta, double corner0_X, double corner0_Y, double corner1_X, double corner1_Y, double corner2_X, double corner2_Y, double corner3_X, double corner3_Y) {
		this.classId = classId;
		this.txnc = txnc;
		this.tync = tync;
		this.ta = ta;
		this.corner0_X = corner0_X;
		this.corner0_Y = corner0_Y;
		this.corner1_X = corner1_X;
		this.corner1_Y = corner1_Y;
		this.corner2_X = corner2_X;
		this.corner2_Y = corner2_Y;
		this.corner3_X = corner3_X;
		this.corner3_Y = corner3_Y;
	}
}
