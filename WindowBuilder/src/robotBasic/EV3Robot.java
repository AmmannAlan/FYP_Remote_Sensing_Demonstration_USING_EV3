package robotBasic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import windowBuilder.common.ParticleFilter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EV3Robot implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5494916205861334571L;
	public BasicRobot ev3 = new BasicRobot();
	public StaticSweep sweep;
	//public StaticSweep sweep = new StaticSweep();
	//
	//public OccupancyGridMapping TempCal = new OccupancyGridMapping();
	//
	//public ArrayList<Double> angleArray = new ArrayList<Double>();
	//public ArrayList<Integer> Ranges = new ArrayList<Integer>();
	public ArrayList<RobotData> pose = new ArrayList<RobotData>();
	
	//Store the Robot Pose and sweep together ---- In different poses, the robot would have one static sweep
	public HashMap<RobotData, StaticSweep> PositionHash = new HashMap<RobotData, StaticSweep>();
	private ArrayList<StaticSweep> sweepList = new ArrayList<StaticSweep>();
	
	private ArrayList<Point> updateFreePointArray = new ArrayList<Point>();
	private ArrayList<Point> updateOccupiedPointArray = new ArrayList<Point>();
	
	//Store all the particles
	private ArrayList<ParticleFilter> particleFilterList = new ArrayList<ParticleFilter>();
	
	//For read the data use only
	private boolean isRead = false;
	
	//Use for the particle filter 
	
	
	public EV3Robot()
	{
		
	}
	
	public EV3Robot(HashMap<RobotData, StaticSweep> newPosition)
	{
		PositionHash = newPosition;
	}
	/*
	public ArrayList<Double> getScanAngle()
	{
		return angleArray;
	}
	
	public ArrayList<Integer> getRanges()
	{
		return Ranges;
	} */
	
	public BasicRobot getEV3()
	{
		return ev3;
	}
	
	/*
	public void CalculatePointsArray() throws Exception
	{
		sweep.staticSweep(getEV3());
		angleArray = sweep.getScanAngles();
		Ranges = sweep.getRanges();
		TempCal.CalculatePointsArray(sweep.getRanges(),sweep.getScanAngles(), new Point(600,405), 0);
	}*/
	
	public void AddNewSweep(StaticSweep newSweep)
	{
		this.sweepList.add(newSweep);
		UpdatePoints();
	}
	
	public void AddNewPose(RobotData robotPose)
	{
		pose.add(robotPose);
	}
	
	public void AddNewPostionHash1(RobotData robotPose, StaticSweep sweep)
	{
		PositionHash.put(robotPose, sweep);
	}
	
	public void AddNewPositionHash2(RobotData robotPose) throws Exception
	{
		sweep = new StaticSweep();
		sweep.staticSweep(ev3);
		PositionHash.put(robotPose, sweep);
	}
	
	public ArrayList<StaticSweep> getSweepList()
	{
		return this.sweepList;
	}
	
	private void UpdatePoints()
	{
	
	      
		for(int i=0;i<sweepList.size();i++)
		{
			for(int j=0;j<sweepList.get(i).getOccupiedPointArray().size();j++)
			{
				this.updateOccupiedPointArray.add(sweepList.get(i).getOccupiedPointArray().get(j));
			}
		}
		
		for(int i=0;i<sweepList.size();i++)
		{
			for(int j=0;j<sweepList.get(i).getFreePointArray().size();j++)
			{
				for(int t=0;t<sweepList.get(i).getFreePointArray().get(j).size();t++)
				{
					this.updateFreePointArray.add(sweepList.get(i).getFreePointArray().get(j).get(t));
				}
			}
		}
		

		
		HashSet<Point> hs_free = new HashSet<Point>(updateFreePointArray);
	    ArrayList<Point> tempFreeList = new ArrayList<Point>(hs_free);
	    updateFreePointArray = tempFreeList;
		
	    
		HashSet<Point> hs_OCC = new HashSet<Point>(updateOccupiedPointArray);
	    ArrayList<Point> tempOccList = new ArrayList<Point>(hs_OCC);
	    updateOccupiedPointArray = tempOccList;

		for(int i=0;i<this.updateFreePointArray.size();i++)
		{
			for(int j=0;j<this.updateOccupiedPointArray.size();j++)
			{
				if(this.updateOccupiedPointArray.get(j).getX() == this.getUpdateFreePointArray().get(i).getX())
				{
					if(this.updateOccupiedPointArray.get(j).getY() == this.getUpdateFreePointArray().get(i).getY())
					{
						this.updateOccupiedPointArray.remove(j);
					}			
				}
			}
		}
		
	}
	
	public ArrayList<Point> getUpdateFreePointArray()
	{
		return this.updateFreePointArray;
	}
	
	public ArrayList<Point> getUpdateOccupiedPointArray()
	{
		return this.updateOccupiedPointArray;
	}
	
	public ArrayList<ParticleFilter> getParticleFilter()
	{
		return this.particleFilterList;
	}
	
	public void AddNewParticleFilter(ParticleFilter filter)
	{
		this.particleFilterList.add(filter);
	}
	
	public void setRead(boolean flag)
	{
		this.isRead = flag;
	}
	
	public boolean getReadFlag()
	{
		return this.isRead;
	}
	
}
