package io.github.roboblazers7617.limelight.targets;

/**
 * Represents a Limelight Raw Fiducial result from Limelight's NetworkTables output.
 */
public class RawFiducialTarget {
	public int id = 0;
	public double txnc = 0;
	public double tync = 0;
	public double ta = 0;
	public double distToCamera = 0;
	public double distToRobot = 0;
	public double ambiguity = 0;

	/**
	 * Creates a new RawFiducialTarget with the given values.
	 *
	 * @param id
	 *            {@link #id} to use.
	 * @param txnc
	 *            {@link #txnc} to use.
	 * @param tync
	 *            {@link #tync} to use.
	 * @param ta
	 *            {@link #ta} to use.
	 * @param distToCamera
	 *            {@link #distToCamera} to use.
	 * @param distToRobot
	 *            {@link #distToCamera} to use.
	 * @param ambiguity
	 *            {@link #ambiguity} to use.
	 */
	public RawFiducialTarget(int id, double txnc, double tync, double ta, double distToCamera, double distToRobot, double ambiguity) {
		this.id = id;
		this.txnc = txnc;
		this.tync = tync;
		this.ta = ta;
		this.distToCamera = distToCamera;
		this.distToRobot = distToRobot;
		this.ambiguity = ambiguity;
	}
}
