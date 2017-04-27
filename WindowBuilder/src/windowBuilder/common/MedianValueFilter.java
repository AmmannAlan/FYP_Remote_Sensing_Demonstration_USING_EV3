package windowBuilder.common;

import java.util.ArrayList;
import java.util.Collections;

public class MedianValueFilter {
	
	
	
	public MedianValueFilter()
	{
		
	}
	
	public ArrayList<Double> getFilteredList(ArrayList<Double> list)
	{
	
        ArrayList<Double> filterList = new ArrayList<Double>();
        


        for(int i = -1; i<list.size() - 1 ; i++)
        {
        	ArrayList<Double> tempList = new ArrayList<Double>();
        	
        	if(i == -1)
        	{
             tempList.add(list.get(i+1));
       	     tempList.add(list.get(i+1));
       	     tempList.add(list.get(i+2));
       	     filterList.add(getMedianValue(tempList));
        	}else if(i == list.size() - 2)
        	{
        		tempList.add(list.get(i));
       	        tempList.add(list.get(i+1));
       	        tempList.add(list.get(i+1));
       	        filterList.add(getMedianValue(tempList));
        	}else
        	{
        		tempList.add(list.get(i));
        		tempList.add(list.get(i+1));
        		tempList.add(list.get(i+2));
        		filterList.add(getMedianValue(tempList));
        	}
        }
        
        
        return filterList;
		
        
		
	}
	
	public double getMedianValue(ArrayList<Double> dataList)
	{
		Collections.sort(dataList);
		double MedianValue = dataList.get((dataList.size()-1)/2);
		return MedianValue;
		
	}
	
	
}
