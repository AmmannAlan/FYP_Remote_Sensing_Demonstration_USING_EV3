package robotBasic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import windowBuilder.common.Covariance;
import windowBuilder.common.KalmanFilter;
import windowBuilder.common.MedianValueFilter;
import windowBuilder.common.OccupancyGridMapping;

public class StaticSweep implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2829048347104579575L;
	//public BasicRobot ev3 = new BasicRobot();
	//Store the angles
	public ArrayList<Double> angleArray = new ArrayList<Double>();
	//Store the distance of the ul-trasonic sensor distance
	public ArrayList<Integer> Ranges = new ArrayList<Integer>();
    private boolean isFinished = false;
    
    //Robot Pose
    private Point coordiante;
    private int angle;
    private RobotData pose;
    
    private ArrayList<ArrayList<Point>> freePointArray = new ArrayList<ArrayList<Point>>();
    private ArrayList<Point> OccupiedPointArray = new ArrayList<Point>();
    
    //Filtered Range
    private ArrayList<Integer> FilteredRange = new ArrayList<Integer>();
    private ArrayList<Point> freePointsList = new ArrayList<Point>();
  
    public StaticSweep()
    {
    	
    }
    
    public StaticSweep(Point newCoordinate,int angle)
    {
    	this.coordiante = newCoordinate;
    	this.angle = angle;
    	this.pose = new RobotData(this.coordiante.getX(), this.coordiante.getY(),this.angle);
    }
    
    
	public void staticSweep(BasicRobot ev3) throws Exception
	{
		double sensor_noise = 0;
		double process_noise = 0.132; //Need change Here 
		Covariance variance = new Covariance();
		ArrayList<Double> temp_range = new ArrayList<Double>();
		
		int initialPosition = ev3.InitialPosition();
		ev3.setSensorMotorSpeed(10);
	    ev3.SensorMotorRight();
	    
	    while(ev3.getSensorMotor().isMoving() == true)
	       {
	    	   //ev3.SensorRotate(90);
	    	   temp_range.add(ev3.ultrasonicDistance());
	    	   System.out.println(ev3.ultrasonicDistance());
	    	   //Thread.sleep(10);
	    	   if(temp_range.size() == 30)
	    	   {
	    	   ev3.SensorMotorStop();
	    	   }
	       }
	    
		for(int i=0;i<37;i++)
		{

			angleArray.add(i*3*Math.PI/180);
			
		}
		ev3.setSensorMotorSpeed(200);
		ev3.ToCertainPos(initialPosition);
        
		ev3.setSensorMotorSpeed(10);
	    ev3.SensorMotorLeft();
	    
	    while(ev3.getSensorMotor().isMoving() == true)
	       {
	    	   //ev3.SensorRotate(90);
	    	   temp_range.add(ev3.ultrasonicDistance());
	    	   System.out.println(ev3.ultrasonicDistance());
	    	   //Thread.sleep(10);
	    	   if(temp_range.size() == 60)
	    	   {
	    	   ev3.SensorMotorStop();
	    	   }
	       }
		
		for(int i=0;i<37;i++)
		{
			
			angleArray.add(-i*3*Math.PI/180);

		}
		ev3.setSensorMotorSpeed(200);
		ev3.ToCertainPos(initialPosition);
		sensor_noise = variance.Variance(temp_range);
		
		//Apply Median Value Filter firstly, then apply the Kalman Filter
		MedianValueFilter mvf = new MedianValueFilter();
		temp_range = mvf.getFilteredList(temp_range);
		KalmanFilter kf = new KalmanFilter(process_noise,sensor_noise,2000,0);
		
		for(int i =0;i<temp_range.size();i++)
		{
			Ranges.add((int) (200*kf.getFilteredValue(temp_range.get(i))));
		}
		
		PointArrayCalculation();
	}
		
	private void PointArrayCalculation()
	{
		OccupancyGridMapping TempCal = new OccupancyGridMapping();
		TempCal.CalculatePointsArray(Ranges, angleArray, this.coordiante, this.angle);
		this.freePointArray = TempCal.getFreePoints();
		this.OccupiedPointArray = TempCal.getOccupiedPoints();
		storeFreePoints();
	}
	
	public ArrayList<ArrayList<Point>> getFreePointArray()
	{
		return this.freePointArray;
	}
	
	public ArrayList<Point> getOccupiedPointArray()
	{
		return this.OccupiedPointArray;
	}
	
    public ArrayList<Integer> getRanges()
    {
    	return this.Ranges;
    }
    
    public ArrayList<Double> getScanAngles()
    {
    	return angleArray;
    }
	
	public void setFinish(boolean flag)
	{
		this.isFinished = flag;
	}
	
	public boolean returnFinish()
	{
	   return this.isFinished;	
	}
	
	public RobotData getRobotPose()
	{
		return this.pose;
	}
    
	public int[] getMeasurementInNumber()
	{
		int[] measurement = new int[60];
		for(int i=0;i<measurement.length;i++)
		{
			measurement[i] = this.Ranges.get(i);
		}
		return measurement;
	}
	
	//Get the raw range filter
	public ArrayList<Integer> getFilteredRange()
	{
		this.FilteredRange = RawRangeFilter();
		return this.FilteredRange;
	}
	
	//Put all the points in one list
	private void storeFreePoints()
	{
		ArrayList<Point> tempFreeList = new ArrayList<Point>();
		
		for(int i=0;i<freePointArray.size();i++)
		{
			for(int m=0;m<freePointArray.get(i).size();m++)
			{
				tempFreeList.add(freePointArray.get(i).get(m));
			}
		}
		
		HashSet<Point> hs_free = new HashSet<Point>(tempFreeList);
	    ArrayList<Point> temp = new ArrayList<Point>(hs_free);
	    this.freePointsList = temp;
		
		
	}
	
	public ArrayList<Point> getFreePointList()
	{
		return this.freePointsList;
	}
	
	
	//On the following is the raw range filter
	
	private ArrayList<Integer> RawRangeFilter()
	{
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		Covariance covariance = new Covariance();
		//Divided the range into several parts 
		ArrayList<Integer> right1 = new ArrayList<Integer>();
		ArrayList<Integer> right2 = new ArrayList<Integer>();
		ArrayList<Integer> middle = new ArrayList<Integer>();
		ArrayList<Integer> left1 = new ArrayList<Integer>();
		ArrayList<Integer> left2 = new ArrayList<Integer>();
		
		for(int i=23;i<30;i++)
		{
			right1.add(Ranges.get(i));
			left1.add(Ranges.get(i+30));
			
		}
		for(int i=7;i<23;i++)
		{
			right2.add(Ranges.get(i));
			left2.add(Ranges.get(i+30));
		}
		for(int i=0;i<7;i++)
		{
			middle.add(i);
			middle.add(i+30);
		}
		
		int minRight1Value = getMinValue(right1);
		int minLeft1Value = getMinValue(left1);
		int minMiddleValue = getMinValue(middle);
		
		double varianceRight = covariance.intVariance(right2);
		double varianceLeft = covariance.intVariance(left2);
		
		int minRight2Value;
		int minLeft2Value;
		if(varianceRight > 0.2 || varianceLeft > 0.2)
		{
			minRight2Value = (int) minMiddleValue/2;
			minLeft2Value = (int) minMiddleValue/2;
			
		}else
		{
			minRight2Value = getMinValue(right2);
			minLeft2Value = getMinValue(left2);
		}
		
        for(int i=0;i<7;i++)
        {
        	int tempValueRight =  (int) (minRight1Value / Math.cos(angleArray.get(i)));
        	int tempValueLeft = (int) (minLeft1Value / Math.cos(angleArray.get(i)));
        	int tempMiddleValue = (int) (minMiddleValue / Math.cos(angleArray.get(i)));
        	values.set(29-i, tempValueRight);
        	values.set(59-i, tempValueLeft);
        	values.set(i, tempMiddleValue);
        	values.set(i+30, tempMiddleValue);
        
        }
        
        for(int i=7;i<23;i++)
        {
        	values.set(i, minRight2Value);
        	values.set(i+30, minLeft2Value);
        }
		
		return values;
	}
	
	private int getMinValue(ArrayList<Integer> tempList)
	{
		int MinValue = 0;
		
		Collections.sort(tempList);
		
	    MinValue = tempList.get(0);
		return MinValue;
	}
	
}
