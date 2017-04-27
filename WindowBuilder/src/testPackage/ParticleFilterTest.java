package testPackage;

import java.util.Random;

public class ParticleFilterTest {
	
	

	public static void main(String args[])
	{
		Random random = new Random();
		
		System.out.println("X" + "\t" +"Y");
		for(int i=0;i<30;i++)
		{
		 double x = random.nextDouble() * 300;
	     double y = random.nextDouble() * 400;
	     System.out.println(x + "\t" + y);
		}
	}
			
}
