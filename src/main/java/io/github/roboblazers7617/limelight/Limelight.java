package io.github.roboblazers7617.limelight;

import static edu.wpi.first.units.Units.Degrees;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import io.github.roboblazers7617.limelight.PoseEstimator.PoseEstimators;

/**
 * An object that represents a physical Limelight.
 */
public class Limelight {
	/**
	 * The hostname of the Limelight.
	 */
	public final String name;
	/**
	 * The {@link NetworkTable} used by this Limelight.
	 */
	public final NetworkTable networkTable;
	/**
	 * The {@link LimelightSettings} of this Limelight.
	 */
	public final LimelightSettings settings;
	/**
	 * The {@link PipelineDataCollator} used by this Limelight.
	 */
	public final PipelineDataCollator dataCollator;
	/**
	 * The NetworkTables entry for the robot orientation input.
	 */
	private final DoubleArrayEntry robotOrientationEntry;

	/**
	 * Creates a new Limelight.
	 *
	 * @param name
	 *            The hostname of the Limelight.
	 */
	public Limelight(String name) {
		this.name = JsonUtilities.sanitizeName(name);
		networkTable = NetworkTableInstance.getDefault().getTable(this.name);

		settings = new LimelightSettings(this);
		dataCollator = new PipelineDataCollator(this);

		robotOrientationEntry = networkTable.getDoubleArrayTopic("robot_orientation_set").getEntry(new double[0]);
	}

	/**
	 * Gets the {@link #networkTable} for this Limelight.
	 *
	 * @return
	 *         The {@link #networkTable} for this Limelight.
	 */
	public NetworkTable getNetworkTable() {
		return networkTable;
	}

	/**
	 * Gets the {@link #settings} for this Limelight.
	 *
	 * @return
	 *         The {@link #settings} for this Limelight.
	 */
	public LimelightSettings getSettings() {
		return settings;
	}

	/**
	 * Gets the {@link #dataCollator} for this Limelight.
	 *
	 * @return
	 *         The {@link #dataCollator} for this Limelight.
	 */
	public PipelineDataCollator getDataCollator() {
		return dataCollator;
	}

	/**
	 * Sets the pipeline's robot orientation input with the given rotation.
	 *
	 * @param rotation
	 *            The robot rotation to set.
	 */
	public void setRobotOrientation(Rotation3d rotation) {
		robotOrientationEntry.set(new double[] { rotation.getMeasureZ().in(Degrees), 0.0, rotation.getMeasureY().in(Degrees), 0, rotation.getMeasureX().in(Degrees), 0 });
		networkTable.getInstance().flush();
	}

	/**
	 * Creates a new {@link PoseEstimator} for this Limelight.
	 *
	 * @param estimator
	 *            The {@link PoseEstimators pose estimator} to use.
	 * @return
	 *         The created {@link PoseEstimator}.
	 */
	public PoseEstimator makePoseEstimator(PoseEstimators estimator) {
		return new PoseEstimator(this, estimator);
	}

	/**
	 * Gets a URL for the given request to this Limelight.
	 *
	 * @param request
	 *            The request string to use.
	 * @return
	 *         The URL for the request, or null if invalid.
	 */
	public URL getLimelightURLString(String request) {
		String urlString = "http://" + name + ".local:5807/" + request;
		URL url;
		try {
			url = new URL(urlString);
			return url;
		} catch (MalformedURLException e) {
			System.err.println("bad LL URL");
		}
		return null;
	}

	/**
	 * Asynchronously takes a snapshot.
	 *
	 * @param snapshotName
	 *            The name to give to the snapshot.
	 * @see #snapshotSynchronous(String)
	 */
	public void snapshot(String snapshotName) {
		CompletableFuture.supplyAsync(() -> {
			return snapshotSynchronous(snapshotName);
		});
	}

	/**
	 * Synchronously takes a snapshot.
	 *
	 * @param snapshotName
	 *            The name to give to the snapshot.
	 * @return
	 *         Was the snapshot successful?
	 */
	private boolean snapshotSynchronous(String snapshotName) {
		URL url = getLimelightURLString("capturesnapshot");
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			if (snapshotName != null && snapshotName != "") {
				connection.setRequestProperty("snapname", snapshotName);
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				return true;
			} else {
				System.err.println("Bad LL Request");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
}
