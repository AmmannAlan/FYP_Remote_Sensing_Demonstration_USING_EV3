package robotBasic;

import java.io.Serializable;

public class Point implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	
	public Point()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Point(int X, int Y)
	{
		this.x = X;
		this.y = Y;
	}
	
	public Point(Point newPoint)
	{
		this.x = newPoint.getX();
		this.y = newPoint.getY();
	}
	public void setX(int newX)
	{
		this.x = newX;
	}
	
	public void setY(int newY)
	{
		this.y = newY;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public boolean equals(Object obj) 
	{
	    if (obj instanceof Point) {
	        Point another = (Point) obj;
	        if (this.x == another.x && this.y == another.y) {
	                return true;
	        }
	    }
	 
	    return false;
	}
	
	public int hashCode()
	{
		int result = 17;
		result = 31 * result + this.x;
		result = 31 * result + this.y;
		return result;
	}

}
