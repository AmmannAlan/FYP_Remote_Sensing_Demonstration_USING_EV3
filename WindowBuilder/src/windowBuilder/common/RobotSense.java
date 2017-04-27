package windowBuilder.common;

import robotBasic.Point;

public class RobotSense {
	
	public int[] sense;
	
	
	public RobotSense (Point[] landmarks, Point coordinate)
	{
		sense = new int[landmarks.length];
		
		for(int i=0;i<landmarks.length;i++)
		{
			int distance = (int) Math.sqrt(Math.pow(landmarks[i].getX()-coordinate.getX(), 2) + Math.pow(landmarks[i].getY()-coordinate.getY(), 2));
		    sense[i] = distance;
		}
	}
	
	public int[] getSense()
	{
		return sense;
	}
	

}
