package robotBasic;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.remote.ev3.RemoteRequestPilot;
import lejos.remote.ev3.RemoteRequestRegulatedMotor;
import lejos.remote.ev3.RemoteRequestSampleProvider;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.robotics.navigation.Move;


public class BasicRobot implements Serializable{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 788298558950251805L;
	// Test the part 
	  public ArrayList<Double> ranges = new ArrayList<Double>();
	  public double rangeDistance;
	  public RemoteRequestSampleProvider ultrasonicSensor;
	 //
	  public static String address = "10.0.1.1";
      RemoteRequestEV3 brick;
     
      /*---Sensor Motor and Other Motors---*/
      public RemoteRequestRegulatedMotor motorL;
      public RemoteRequestRegulatedMotor motorR;
      public RemoteRequestRegulatedMotor SensorMotor;
      RemoteRequestPilot pilot;
      Move pilotMove;
      public int angle = 0;
      
      int fast_turn = 90;
      int slow_turn = 60;
      
      /*----Ultrasonic Sensor----*/
      Ultrasonic_Sensor us;
      //float distance;
      String usPort = "S" + 1;
      int travel_speed = 5;
     //
      public boolean sensorRotate = false;
      
      //
      boolean has_pilot = false;
      protected boolean disconnected = false;
      public boolean connected = false;
      
	  public boolean closed = false;
	  public boolean straight = false;
	  
      public void connect() throws Exception
      {
    	  try{
    		  brick = new RemoteRequestEV3(address);
    		  this.connected = true;
    	  }catch(Exception e){
    		brick.disConnect();
    		throw e;
            }
    
    	  
    	  try{
    		  us = new Ultrasonic_Sensor(brick, usPort);
    	  }catch(Exception e)
    	  {
    		  brick.disConnect();
    		  throw e;
    	  }
    	  /*-----Sensor Motor-----*/
    	  try{
    		  SensorMotor=(RemoteRequestRegulatedMotor)brick.createRegulatedMotor("D", 'M');
        	  motorR = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("B",'S');
        	  motorL = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("C",'S');    	  
        	  motorR.setSpeed(90);
        	  motorL.setSpeed(90); 
        	  SensorMotor.setSpeed(3);
        	  pilot = (RemoteRequestPilot) brick.createPilot(1, 2, "C", "B");
        	  pilot.setLinearSpeed(1);
    	  }catch(Exception e)
    	  {
    		  brick.disConnect();
    		  throw e;
    	  }
    	  /*
    	  SensorMotor=(RemoteRequestRegulatedMotor)brick.createRegulatedMotor("D", 'M');
    	  motorR = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("B",'L');
    	  motorL = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("C",'L');	  
    	  motorR.setSpeed(200);
    	  motorL.setSpeed(200);
    	  pilot = (RemoteRequestPilot) brick.createPilot(4, 9, "C", "B"); 
    	  */
    	  this.connected = true;
    	  this.disconnected = false;
      }
      
      //Test Function & Method
      //Connect to Other Motors only
      
      public void newConnect() throws Exception
      {
    	  try{
    		  brick = new RemoteRequestEV3(address);
    		  this.connected = true;
    	  }catch(Exception e){
    		brick.disConnect();
    		throw e;
            }
    	  
    	  
    	  try{
        	  motorR = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("B",'S');
        	  motorL = (RemoteRequestRegulatedMotor) brick.createRegulatedMotor("C",'S');    	  
        	  motorR.setSpeed(40);
        	  motorL.setSpeed(40); 
        	  pilot = (RemoteRequestPilot) brick.createPilot(1, 2, "C", "B");
        	  pilot.setLinearSpeed(2);
        	  
    	  }catch(Exception e)
    	  {
    		  brick.disConnect();
    		  throw e;
    	  }
      }
      
      public void newDisconnect()
      {
    	 
    	  this.brick.disConnect();
      }
      
      
      
      
      
      //Test Line Finished here 
      public void distanceTravelled()
      {
    	 
      }
      public void motorClose()
      {
    	  motorR.close();
    	  motorL.close();
    	  SensorMotor.close();
      }
      
      public void disConnect()
      {
    	  this.connected = false;
    	  this.motorR.close();
    	  this.motorL.close();
    	  this.SensorMotor.close();
    	  this.us.close();
    	  this.brick.disConnect();
      }
      
      public void pilotStop()
      {
    	  pilot.stop();
      }
      
      public boolean isConnected()
      {
    	  return connected;
      }
     
    public double ultrasonicDistance() throws Exception
    {
      //DecimalFormat df = new DecimalFormat("#.###");
      //double distance = 100*us.distance();
      double distance = us.distance();
      //distance = Double.parseDouble(df.format(distance));
      return distance;
    }
    
    public void closeRobot()
    {
    	us.close();
    	
    }
    public void usClose()
    {
    	us.close();
    }
    
 
      public RemoteRequestEV3 getBrick() {
  		return brick;
  	  }
      
      public void Hello(){
    	  LCD.drawString("Hello World", 0, 4);
    	  Delay.msDelay(2000);
    	  LCD.clearDisplay();
      }
      
      public void conntectToRobot() throws Exception
      {
    	  connect(); //connect to the robot	  
      }
      
      /*------The motor of the sensor ------*/
      public void scare() {
          int pos = SensorMotor.getTachoCount();
          SensorMotor.setSpeed(60);
          SensorMotor.rotateTo(pos + 90);
          SensorMotor.waitComplete();
          SensorMotor.rotateTo(pos + 40);
          SensorMotor.rotateTo(pos);
          SensorMotor.waitComplete();
          SensorMotor.rotateTo(pos + 40);
          SensorMotor.waitComplete();
          SensorMotor.rotateTo(pos);
          SensorMotor.waitComplete();
          /*------------------------*/
          //SensorMotor.waitComplete();
          Delay.msDelay(10000);
          SensorMotor.rotateTo(pos - 80);
          SensorMotor.waitComplete();
          SensorMotor.rotateTo(pos);
          SensorMotor.waitComplete();
          SensorMotor.rotateTo(pos - 60);
          SensorMotor.rotateTo(pos);
          SensorMotor.waitComplete();
          SensorMotor.close();
      }
      
      public void rotate(int degree)
      {
    	  int pos = SensorMotor.getTachoCount();
          SensorMotor.setSpeed(200);
          SensorMotor.rotateTo(pos + degree);
         // SensorMotor.waitComplete();
      }
      
      public void ToCertainPos(int pos)
      {
        SensorMotor.rotateTo(pos);
      }
      
      public void SensorToRight(int i)
      {
    	  int currentPos = SensorMotor.getTachoCount();
    	  SensorMotor.rotateTo(currentPos + i); 
    	  SensorMotor.waitComplete();
    	  SensorMotor.stop();
      }
      
      public void SensorToLeft(int i)
      {
    	  int currentPos = SensorMotor.getTachoCount();
    	  SensorMotor.rotateTo(currentPos - i); 
    	  SensorMotor.waitComplete();
    	  SensorMotor.stop();
      }
      
      //Just for test.java
      public void SensorRotate(int pos)
      {
    	 SensorMotor.setSpeed(200);
    	  SensorMotor.rotateTo(pos);
    	  //SensorMotor.waitComplete();
          setSensorMotorStatus(false);
    	  SensorMotor.stop();
    	  
      }
      
      public void SensorMotorRight()
      {
    	  SensorMotor.forward();
      }
      
      public void SensorMotorLeft()
      {
    	  SensorMotor.backward();
      }
      
      public void SensorMotorStop()
      {
    	  SensorMotor.stop();
      }
      public void setSensorMotorStatus(boolean flag)
      {
    	  sensorRotate = flag;
      }
      //Loop Outside the program.
      public int InitialPosition()
      {
    	  return SensorMotor.getTachoCount();
      }
      

      
      public double dataRotation(int degree) 
      {
    	  int division = 5;
    	  int fractionTimes = (int) degree/division;
    	  int pos = SensorMotor.getTachoCount();
    	  double distance = 0;
    	  SensorMotor.setSpeed(200);
    	  /*Rotate to 90 degrees and measure the distance*/
    	  /*Direction: Right*/
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  SensorMotor.rotateTo(pos + division);
    		  SensorMotor.waitComplete();
    		  //double distance = us.distance();
    		 // System.out.println(distance);
    		
    		  pos = SensorMotor.getTachoCount();
    	
    	  }
    	  
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  SensorMotor.rotateTo(pos - division);
    		  SensorMotor.waitComplete();
    		  distance = us.distance();
    		 //System.out.println(distance);
    		
    		  pos = SensorMotor.getTachoCount();
    	  }
    	  
    	  /*Rotate to 90 degrees and measure the distance*/
    	  /*Direction: Left*/
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  SensorMotor.rotateTo(pos - division);
    		  SensorMotor.waitComplete();
    		//  double distance = us.distance();
    		// System.out.println(distance);
    		
    		  pos = SensorMotor.getTachoCount();
    	  }
    	  
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  SensorMotor.rotateTo(pos + division);
    		  SensorMotor.waitComplete();
    		  distance = us.distance();
    		  //System.out.println(distance);
    		
    		  pos = SensorMotor.getTachoCount();
    	
    	  }
    	  return distance;
      }
      
      //New Function Implements the Static Sweep Function
      
      /*The first function is to rotate the Sensor*/
      public void StaticSweep()
      {
    	  int division = 5;
    	  int fractionTimes = 18;
    	  int pos = SensorMotor.getTachoCount();
    	  SensorMotor.setSpeed(200);
    	  /*Rotate to 90 degrees and measure the distance*/
    	  /*Direction: Right*/
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  angle = i*5;
    		  SensorMotor.rotateTo(pos + division);
    		  SensorMotor.waitComplete();
    		  pos = SensorMotor.getTachoCount();
    	
    	  }
    	  
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  angle = 90 - i*5;
    		  SensorMotor.rotateTo(pos - division);
    		  SensorMotor.waitComplete(); 		
    		  pos = SensorMotor.getTachoCount();
    	  }
    	  
    	  /*Rotate to 90 degrees and measure the distance*/
    	  /*Direction: Left*/
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  angle = i*5;
    		  SensorMotor.rotateTo(pos - division);
    		  SensorMotor.waitComplete();
    		  pos = SensorMotor.getTachoCount();
    	  }
    	  
    	  for(int i=0;i<fractionTimes;i++)
    	  {
    		  angle = 90 - i*5;
    		  SensorMotor.rotateTo(pos + division);
    		  SensorMotor.waitComplete();
    		  pos = SensorMotor.getTachoCount();
    	
    	  }
    	
      }
      
      public void setSensorMotorSpeed(int i)
      {
    	  SensorMotor.setSpeed(i);
      }
     
      
      public int staticAngle()
      {
    	  return angle;
      }
      //------------------------------------------------------
 
    
      public void closeMotorSensor()
      {
    	  us.close();
    	  SensorMotor.close();
      }
      /*-----------Below is the code of the robot moving-----------*/
      
      public void setTravelSpeed(int travelSpeed)
  	  {
  		travel_speed = travelSpeed;
  	  }
      
      public RemoteRequestRegulatedMotor getSensorMotor()
      {
    	  return SensorMotor;
      }
      
      public void forward() throws InterruptedException
      {
    	  pilot.forward();
    	  
      }
      
      public void short_forward()
      {
    	  pilot.travel(1);
      }
      
      public void backward()
      {
    	  pilot.backward();
      }
      
      public void short_backward()
      {
    	  pilot.travel(-1);
      }
      
      public void stop()
      {
    	  pilot.stop();
      }
      
      public void left()
      {
    	  motorR.setSpeed(fast_turn);
    	  motorL.setSpeed(fast_turn);
    	  motorR.backward();
    	  motorL.forward();
    	  straight = false;
      }
      
      public void very_short_left()
      {
    	  pilot.setAngularSpeed(travel_speed);
    	  pilot.rotate(-30);
    	  straight = false;
      }
      
      public void short_left()
      {
    	  pilot.setAngularSpeed(travel_speed);
    	  pilot.rotate(-30);
    	  straight = false;
      }
      
      public void forward_left()
      {
    	  motorL.setSpeed(slow_turn);
    	  motorL.forward();
    	  motorR.stop();
    	  straight = false;
      }
      
      public void right()
      {
    	  motorR.setSpeed(fast_turn);
    	  motorL.setSpeed(fast_turn);
    	  motorR.forward();
    	  motorL.backward();
    	  straight = false;
      }
      
      public void short_right()
      {
    	  pilot.setAngularSpeed(travel_speed);
    	  pilot.rotate(90);
    	  straight = false;
      }
      
      public void very_short_right()
      {
    	  pilot.setAngularSpeed(fast_turn);
    	  pilot.rotate(30);
    	  straight = false;
      }
      
      public RemoteRequestPilot getPilot()
      {
    	  return pilot;
      }
      
      public void forward_right()
      {
    	  motorR.setSpeed(slow_turn);
    	  motorR.forward();
    	  motorL.stop();
    	  straight = false;  
      }
      
      public Ultrasonic_Sensor getUS()
      {
    	  return us;
      }
      
      public void shutDown()
      {
    	 
      }
      
      public void DisplayRanges()
      {
    	  for(int i =0;i<ranges.size(); i++)
    	  {
    		  System.out.println(ranges.get(i));
    	  }
      }
      
      public boolean isSensorMotorMoving()
      {
    	  return sensorRotate;
      }

      //Test Only
      public void sensorRotateTest() throws Exception
      {
    	  Thread t1 = new Thread(new SensorRotation());
    	  Thread t2 = new Thread(new ReadDistance());
    	  
    	  t1.start();
    	  t2.start();
    	  Thread.sleep(12000);
    	  t1.interrupt();
    	  System.out.println(t1.isInterrupted());
    	  
    	  
    	  if(t1.isInterrupted() == false)
    	  {
    		  //Thread.sleep(20);
    		  t2.interrupt();
    	  }
    	  
    	  
      } 
      
      public int getMotorSpeed()
      {
    	  return motorR.getSpeed();
      }
     

	public class SensorRotation implements Runnable
      {
		
		public SensorRotation()
		{

			 try{
	   		  SensorMotor=(RemoteRequestRegulatedMotor)brick.createRegulatedMotor("D", 'M');
	       	  SensorMotor.setSpeed(40);
	   	  }catch(Exception e)
	   	  {
	   		  
	   		  throw e;
	   	  }
		
		}
		
		public void start()
		{
			this.run();
		}
		    
		    /*
		    public SensorRotation(int newDegree)
		    {
		    	this.degree = newDegree;
		    } */
          
    		public void run() 
    		{
                int inipos = SensorMotor.getTachoCount();
				 SensorMotor.rotateTo(-90);
				 SensorMotor.rotateTo(inipos);
				 SensorMotor.rotateTo(90);
				 SensorMotor.rotateTo(inipos);
				 try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 SensorMotor.close();
				 
				 System.out.println(" --- Sensor Rotation Leaving Normally ---");
    		}
    		
    		
    		    		
    	}
    
	
      public class ReadDistance implements Runnable
      {
    	  
    	  
    	  public void start()
    	  {
    		  this.run();
    	  }
    	  
    	  public ReadDistance() throws Exception
    	  {
    		  try
    		  {
    			 String port = "S" + 1;
    			 ultrasonicSensor = (RemoteRequestSampleProvider) brick.createSampleProvider(port,"lejos.hardware.sensor.EV3UltrasonicSensor","Distance");
    		  }catch(Exception e)
    		  {
    			  throw e;
    		  }
    	  }
    	  
    	  
    	  public void run()
    	  {
    		  try
    		  {
    		  while(!Thread.interrupted())
    		  {
    		   
    			  float[] sample = new float[1];
    		      ultrasonicSensor.fetchSample(sample, 0); 
    		    	 
    		    	if(sample[0] == Float.POSITIVE_INFINITY || sample[0] == Float.NEGATIVE_INFINITY)
    		    	{
    		    		sample[0] = (float) 2.499;
    		    	}
    		    	
    		    	 DecimalFormat df = new DecimalFormat("#.###");
    		    	 double result =  Double.parseDouble(df.format(sample[0]));
    		    	 double distance = result;
    		     ranges.add(distance);
    		     System.out.println(distance);
    		     Thread.sleep(25);
			  
    			  
    		  }
    		  }catch(InterruptedException e)
    		  {
    			  ultrasonicSensor.close();
    			  System.out.println("Read Distance Thread is over");
    		  }
    		  
    		System.out.println("Read Distance Thread is over !!!");
    		 
    	  }
      }

      
}




