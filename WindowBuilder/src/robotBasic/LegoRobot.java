package robotBasic;

import java.util.ArrayList;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.hardware.sensor.BaseSensor;

/**
 * An interface for a Lego EV3 Robot to be used in an EASS environment.
 * @author louiseadennis
 *
 */
public interface LegoRobot {
	/**
	 * Get the brick associated with this robot.
	 * @return
	 */
	public RemoteRequestEV3 getBrick();
		
	/**
	 * Attach a sensor to a port.
	 * @param portnumber
	 * @param sensor
	 */
	public void setSensor(int portnumber, BaseSensor sensor);
	
	/**
	 * Get this robot's sensors.
	 * @return
	 */
	public ArrayList<BaseSensor> getSensors();
	
	/**
	 * Add percepts from an EASS environement.
	 * @param env
	 */
	//public void addPercepts(EASSEV3Environment env);

	/**
	 * Close sockets and clean up.
	 */
	public void close();
	
}