package testPackage;
import java.util.ArrayList;
import robotBasic.EV3Robot;
import robotBasic.Point;
import robotBasic.StaticSweep;
import windowBuilder.common.*;

public class Test {
	
	
	
	public static double kf_calculation(ArrayList<Double> real, ArrayList<Double> distance, int index)
	{
		Covariance co = new Covariance();
		KalmanFilter kf = new KalmanFilter(0.0012, 32, 2000, 0);
		ArrayList<Double> fv = new ArrayList<Double>();
		
		for(int i=0;i<real.size();i++)
		{
			 fv.add(kf.getFilteredValue(distance.get(i)));
		}
		
		double coefficient = co.Coefficient(distance, fv);
		return coefficient;
		
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		EV3Robot ev3 = new EV3Robot();
		StaticSweep sweep = new StaticSweep(new Point(0,0), 0);
		
		ev3.getEV3().connect();
		sweep.staticSweep(ev3.getEV3());
		ev3.AddNewSweep(sweep);
		for(int i=0;i<ev3.getSweepList().size();i++)
		{
			for(int m=0;m<ev3.getSweepList().get(i).getOccupiedPointArray().size();m++)
			{
				int x =  ev3.getSweepList().get(i).getOccupiedPointArray().get(m).getX();
				int y =  ev3.getSweepList().get(i).getOccupiedPointArray().get(m).getY();
				
				System.out.println(x + "\t" + y);
			}
		}
		System.out.println(ev3.getSweepList().size());
		for(int i=0;i<ev3.getSweepList().size();i++)
		{
			System.out.println(ev3.getSweepList().get(i).getOccupiedPointArray().size());
		}
		

		ev3.getEV3().disConnect();
		

		
		
		/*
		ArrayList<Double> distance = new ArrayList<Double>();
        BasicRobot ev3 = new BasicRobot();
        
        
       ev3.connect();
       int iniPos = ev3.InitialPosition();
       ev3.setSensorMotorSpeed(10);
       ev3.SensorMotorRight();
      // Thread.sleep(3000);
       
       //ev3.SensorMotorStop();
       while(ev3.getSensorMotor().isMoving() == true)
       {
    	   //ev3.SensorRotate(90);
    	   distance.add(ev3.ultrasonicDistance());
    	   System.out.println(ev3.ultrasonicDistance());
    	   //Thread.sleep(10);
    	   if(distance.size() == 30)
    	   {
    	   ev3.SensorMotorStop();
    	   }
       }
       
       ev3.SensorRotate(iniPos);
       ev3.disConnect();
        */
	
	   
	
	}
  
     

}

