package robotBasic;


import lejos.remote.ev3.RemoteRequestSampleProvider;

import java.io.Serializable;
import java.text.DecimalFormat;

import lejos.remote.ev3.RemoteRequestEV3;


public class Ultrasonic_Sensor implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8428749882690515167L;
	RemoteRequestSampleProvider sensor;
	boolean sensorConnect = false;
	double distance;
	
    public Ultrasonic_Sensor(RemoteRequestEV3 brick, String portName) throws Exception
    {
	
	sensor = (RemoteRequestSampleProvider) brick.createSampleProvider(portName,"lejos.hardware.sensor.EV3UltrasonicSensor","Distance");
	
	}

     public double distance(){
    
    	 float[] sample = new float[1];
    	 sensor.fetchSample(sample, 0); 
    	 
    	if(sample[0] == Float.POSITIVE_INFINITY || sample[0] == Float.NEGATIVE_INFINITY)
    	{
    		sample[0] = (float) 2.499;
    	}
    	
    	DecimalFormat df = new DecimalFormat("#.###");
    	double result =  Double.parseDouble(df.format(sample[0]));
    	distance = result;
    	 return distance;
     }
     
  

	public void usConnect()
     {
    	 sensorConnect = true;
     }
     public void close()
     {
    	 sensorConnect = false;
    	 try {
 			sensor.close();
 		} catch (Exception e) {
 			System.err.println(e.getMessage());
 		}
    	 
     }
} 