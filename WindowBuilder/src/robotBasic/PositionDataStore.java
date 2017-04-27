package robotBasic;

import java.io.Serializable;
import java.util.ArrayList;

import windowBuilder.common.ParticleFilter;

public class PositionDataStore implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3150917063759221743L;
	public ArrayList<Point> freePointArray = new ArrayList<Point>();
	public ArrayList<Point> occupiedPointArray = new ArrayList<Point>();
	public ArrayList<ParticleFilter> filterArray = new ArrayList<ParticleFilter>();
	private ArrayList<StaticSweep> sweepList = new ArrayList<StaticSweep>();
	
	private boolean readFlag = false;

	public PositionDataStore()
	{
		
	}
	
	public PositionDataStore(ArrayList<Point> free , ArrayList<Point> occ, ArrayList<ParticleFilter> filterList)
	{
		freePointArray = free;
		occupiedPointArray = occ;
		filterArray = filterList;
	}
	
	
	public void setPoint(ArrayList<Point> free , ArrayList<Point> occ)
	{
		freePointArray = free;
		occupiedPointArray = occ;
		
	}
	
	public void setFilterArray(ArrayList<ParticleFilter> filterList)
	{
		filterArray = filterList;
	}
	
	public void addNewParticleFilter(ParticleFilter filter)
	{
		filterArray.add(filter);
	}
	
	public void setReadFlag(boolean flag)
	{
		readFlag = flag;
	}
	
	public boolean getReadFlag()
	{
		return this.readFlag;
	}
	
	public ArrayList<Point> getFreePoint()
	{
		return freePointArray;
	}
	
	public ArrayList<Point> getOccupiedPoint()
	{
		return occupiedPointArray;
	}
	
	public ArrayList<ParticleFilter> getFilterList()
	{
		return filterArray;
	}
	
	public ArrayList<StaticSweep> getSweepList()
	{
		return sweepList;
	}
	
	public void addNewSweep(StaticSweep sweep)
	{
		sweepList.add(sweep);
	}
}
