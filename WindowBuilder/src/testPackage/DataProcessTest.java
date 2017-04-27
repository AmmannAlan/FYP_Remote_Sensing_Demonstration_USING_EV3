package testPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import robotBasic.PositionDataStore;
import robotBasic.StaticSweep;
import windowBuilder.common.ParticleFilter;

public class DataProcessTest
{
	
	
	
   public static void main(String[] args)
   {
	   
		PositionDataStore dataStore = new PositionDataStore();
		ArrayList<ParticleFilter> filterArray = new ArrayList<ParticleFilter>();
		ArrayList<StaticSweep> sweepList = new ArrayList<StaticSweep>();
		
	   
	 try{
		ObjectInputStream is = new ObjectInputStream(new FileInputStream("bench_use.dat")); 
		PositionDataStore newStore = (PositionDataStore) is.readObject();
		dataStore = newStore;
		is.close();
		
	}catch (Exception e)
	{
		e.printStackTrace();
	}
	
	 filterArray = dataStore.getFilterList();
	 sweepList = dataStore.getSweepList();
	 
	 System.out.println(filterArray.size());
	 
     System.out.println("----The Pose Point Info-----");
     
     System.out.println("X" + "\t" + "Y");
     for(int i=0;i<sweepList.size();i++)
     {
    	 System.out.println(sweepList.get(i).getRobotPose().getX() + "\t" + sweepList.get(i).getRobotPose().getY());
     }
	 
	 
	 System.out.println("--------Program Finished----------");
	 
   }
     
   
}


