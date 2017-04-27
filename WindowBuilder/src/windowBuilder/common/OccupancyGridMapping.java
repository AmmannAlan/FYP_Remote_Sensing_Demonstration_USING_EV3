package windowBuilder.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import robotBasic.Point;


public class OccupancyGridMapping implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4661461994462753282L;
	public ArrayList<Point> grid_occs = new ArrayList<Point>();
	public ArrayList<ArrayList<Point>> freePoints = new ArrayList<ArrayList<Point>>();
	
	
	//On the following there needs to be the particle filter to calibrate
	/**
	 * 
	 */
	
	/*
	private double lo_free = 1;
	private double lo_occ = 0;
	private double lo_max = 100;
	private double lo_min = -100;
	*/
	//Local Coordinates of occupied points 
	private Point coordinate;
	
	/*
	public void localCoordinates(StaticSweep sweep, Point coordinate, double angle)
	{
		size = sweep.getRanges().size();

		for(int i=0; i<size; i++)
		{
			int xLength = (int) ((int) sweep.getRanges().get(i) * Math.sin(sweep.getScanAngles().get(i) + angle));
			int yLength = (int) ((int) sweep.getRanges().get(i) * Math.cos(sweep.getScanAngles().get(i) + angle));
			grid_occs.add(new Point(coordinate.getX() + xLength, coordinate.getY() - yLength));
		}
		
	} */
	
	/*
	public void freePointsArray(Point coordinate)
	{
		
		for(int i =0; i < grid_occs.size(); i++)
		{
			BresenhamLine TempFreePoints = new BresenhamLine();
			TempFreePoints.BresenhamCalculation(coordinate.getX(), coordinate.getY(), grid_occs.get(i).getX(), grid_occs.get(i).getY());
			freePoints.add(TempFreePoints.getPointArray());
			
		}
	} */
	
	public void CalculatePointsArray(ArrayList<Integer> Ranges, ArrayList<Double> angleArray, Point coordinate, double angle)
	{
		this.coordinate = coordinate;
		ArrayList<Point> temp_grid_occs = new ArrayList<Point>();
		//int size = Ranges.size();

		//Set new ranges
		ArrayList<Integer> filteredRanges = new ArrayList<Integer>();
		
		filteredRanges = RawRangeFilter(Ranges, angleArray);
		
		//Test the particle filter here
		
		
		for(int i=0; i<filteredRanges.size(); i++)
		{
			int xLength = (int) ((int) filteredRanges.get(i) * Math.sin(angleArray.get(i) + angle));
			int yLength = (int) ((int) filteredRanges.get(i) * Math.cos(angleArray.get(i) + angle));
			temp_grid_occs.add(new Point(coordinate.getX() + xLength, coordinate.getY() - yLength));
		}
		
		for(int i =0; i < temp_grid_occs.size(); i++)
		{
			BresenhamLine TempFreePoints = new BresenhamLine();
			TempFreePoints.BresenhamCalculation(coordinate.getX(), coordinate.getY(), temp_grid_occs.get(i).getX(), temp_grid_occs.get(i).getY());
			freePoints.add(TempFreePoints.getPointArray());
			
		}
		
		for(int i=0;i<freePoints.size();i++)
		{
		   int m = freePoints.get(i).size() - 1;
		   grid_occs.add(new Point(freePoints.get(i).get(m).getX(),freePoints.get(i).get(m).getY()));
		   freePoints.get(i).remove(m);
				
		}
		
		
        upDatePointsArray();
        RemoveSameElements();
		
		
	}
	
	private void upDatePointsArray()
	{
		//BresenhamLine AdditionalOccupiedPoints = new BresenhamLine();
		
		ArrayList<Point> additional = new ArrayList<Point>();
		for(int i=0;i<grid_occs.size()-1;i++)
		{
			BresenhamLine TempOccupiedPoints = new BresenhamLine();
			int x0 = grid_occs.get(i).getX();
			int y0 = grid_occs.get(i).getY();
			int x1 = grid_occs.get(i+1).getX();
			int y1 = grid_occs.get(i+1).getY();
			TempOccupiedPoints.BresenhamCalculation(x0,y0,x1,y1);
			for(int m=1;m<TempOccupiedPoints.getPointArray().size()-1;m++)
			{
				grid_occs.add(TempOccupiedPoints.getPointArray().get(m));
				additional.add(TempOccupiedPoints.getPointArray().get(m));
				
			}
		}
		
		for(int i =0; i < additional.size(); i++)
		{
			int x0 = this.coordinate.getX();
			int y0 = this.coordinate.getY();
			int x1 = additional.get(i).getX();
			int y1 = additional.get(i).getY();
			BresenhamLine TempFreePoints = new BresenhamLine();
			
			TempFreePoints.BresenhamCalculation(x0, y0, x1, y1);
			ArrayList<Point> TempArray = new ArrayList<Point>();
			TempArray = TempFreePoints.getPointArray();
            int m = TempArray.size() - 1;
            TempArray.remove(m);
			
			freePoints.add(TempArray);
			
		}
		
	}
	
	private void RemoveSameElements()
	{ 
		   ArrayList<ArrayList<Point>> temp = new ArrayList<ArrayList<Point>>();
		   HashSet<Point> hashSet = new HashSet<Point>(grid_occs);
		   ArrayList<Point> arrayList2 = new ArrayList<Point>(hashSet);
		   grid_occs = arrayList2;
		   
		   for(int i=0;i<freePoints.size();i++)
		   {
			   HashSet<Point> hs = new HashSet<Point>(freePoints.get(i));
			   ArrayList<Point> tempList = new ArrayList<Point>(hs);
			   temp.add(tempList);
		   }
		   
		   freePoints = temp;

		
	}
	
	private ArrayList<Integer> RawRangeFilter(ArrayList<Integer> ranges,  ArrayList<Double> angleArray)
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
			right1.add(ranges.get(i));
			left1.add(ranges.get(i+30));
			
		}
		
		for(int i=7;i<23;i++)
		{
			right2.add(ranges.get(i));
			left2.add(ranges.get(i+30));
		}
		for(int i=0;i<7;i++)
		{
			middle.add(ranges.get(i));
			middle.add(ranges.get(i+30));
		}
		
		int minRight1Value = getMinValue(right1);
		int minLeft1Value = getMinValue(left1);
		//for the middle part, try to use the median value
		//int minMiddleValue = getMinValue(middle);
		int medianMiddleValue = getMedianValue(middle);
		
		double varianceRight = covariance.intVariance(right2);
		double varianceLeft = covariance.intVariance(left2);
		
		int minRight2Value;
		int minLeft2Value;
		if(varianceRight > 40 || varianceLeft > 40)
		{
			minRight2Value = (int) medianMiddleValue/2;
			minLeft2Value = (int) medianMiddleValue/2;
			
		}else
		{
			minRight2Value = getMinValue(right2);
			minLeft2Value = getMinValue(left2);
		}
		
		for(int i=0;i<7;i++)
		{
			int tempMiddleValue = (int) (medianMiddleValue / Math.cos(angleArray.get(i)));
			values.add(tempMiddleValue);
			
		}
		
		for(int i=7;i<23;i++)
		{
		    values.add(minRight2Value);
		}
		
		for(int i=6;i>-1;i--)
		{
			int tempValueRight =  (int) (minRight1Value / Math.cos(angleArray.get(i)));
			values.add(tempValueRight);
		}
		
		//Enlarge this part Add 30-37 points
		for(int i=0;i<7;i++) //30-37
		{
			int tempValueRight = (int) (minRight1Value / Math.cos(angleArray.get(i)));
			values.add(tempValueRight);
		}
		
		//Left side
		for(int i=0;i<7;i++)
		{
			int tempMiddleValue = (int) (medianMiddleValue / Math.cos(angleArray.get(i)));
			values.add(tempMiddleValue);
		}
		
		for(int i=7;i<23;i++)
		{
			values.add(minLeft2Value);
		}
		
		for(int i=6;i>-1;i--)
		{
			int tempValueLeft =  (int) (minLeft1Value / Math.cos(angleArray.get(i)));
			values.add(tempValueLeft);
		}
		
		//Enlarge this part Add 30-37 points more
		for(int i=0;i<7;i++) //30-37
		{
			int tempValueLeft = (int) (minLeft1Value / Math.cos(angleArray.get(i)));
			values.add(tempValueLeft);
		}
      
		
		return values;
	}
	
	public ArrayList<Point> getOccupiedPoints()
	{
		return grid_occs;
	}
	
	
	public ArrayList<ArrayList<Point>> getFreePoints()
	{
		return freePoints;
	}
	
	private int getMinValue(ArrayList<Integer> tempList)
	{
		int MinValue = 0;
		
		Collections.sort(tempList);
		
	    MinValue = tempList.get(0);
		return MinValue;
	}
	
	private int getMedianValue(ArrayList<Integer> tempList)
	{
		int MedianValue;
		int size = tempList.size();
		Collections.sort(tempList);
		if(size % 2 == 0 )
		{
			MedianValue = (tempList.get(size/2) + tempList.get(size/2 -1 ))/2;
		}else
		{
			MedianValue = tempList.get((size-1)/2);
		}
		
		return MedianValue;
	}

	
	
}
