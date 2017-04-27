package robotBasic;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RobotData implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4944032662178224759L;
	public int coordinateX;
    public int coordinateY;
    public double angle;
    
    public RobotData()
    {
    	this.coordinateX = 0;
    	this.coordinateY = 0;
    	this.angle = 0;
    }
    
    public RobotData(int x, int y, double angle)
    {
    	this.coordinateX = x;
    	this.coordinateY = y;
    	this.angle = angle;
    }
    
    public int getX()
    {
    	return this.coordinateX;
    }
    
    public int getY()
    {
    	return this.coordinateY;
    }
    
    public double getAngle()
    {
    	return this.angle;
    }
    
    @XmlElement
    public void setX(int x)
    {
    	this.coordinateX = x;
    }
    
    @XmlElement
    public void setY(int y)
    {
    	this.coordinateY = y;
    }
    
    @XmlElement
    public void setAngle(int newAngle)
    {
    	this.angle = newAngle;
    }
}
