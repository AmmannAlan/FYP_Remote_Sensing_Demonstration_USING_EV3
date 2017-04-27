package windowBuilder.common;

import java.util.ArrayList;

public class Covariance {
     
	public Covariance()
	{
		
	}
    
	public double Variance(ArrayList<Double> list)
	{
		 double sum = 0;
		 for(int i=0;i<list.size();i++)
		 {
			 sum += list.get(i);
		 }
		 double average = sum / list.size();
		 
		 double temp = 0;
		 for(int i=0; i<list.size(); i++)
		 {
			 temp += Math.pow(average - list.size(), 2);
		 }
		 double variance = temp / list.size();
		 return variance;
	}
	
	public double intVariance(ArrayList<Integer> list)
	{
		double sum = 0;
		 for(int i=0;i<list.size();i++)
		 {
			 sum += list.get(i);
		 }
		 double average = sum / list.size();
		 
		 double temp = 0;
		 for(int i=0; i<list.size(); i++)
		 {
			 temp += Math.pow(average - list.size(), 2);
		 }
		 double variance = temp / list.size();
		 return variance;
	}

	public double CovarianceCalculation(ArrayList<Double> list1, ArrayList<Double> list2)
	{
		double covariance = 0;
		double temp1 = 0;
		double temp2 = 0;
		double tempSum = 0;
		
		for(int i=0;i<list1.size();i++)
		{
			temp1 += list1.get(i);
		}
		double average1 = temp1 / list1.size();
		
		for(int i=0;i<list2.size();i++)
		{
			temp2 += list2.get(i);
		}
		double average2 = temp2 / list2.size();
		
		
		for(int i=0;i<list2.size();i++)
		{
			tempSum += (list1.get(i) - average1)*(list2.get(i) - average2);
		}
		
		covariance = tempSum / list1.size();
		
		
		
		return covariance;
	}
	
	public double Coefficient(ArrayList<Double> list1, ArrayList<Double> list2)
	{
		double coefficient = 0;
		double variance1 = Math.sqrt(Variance(list1));
		double variance2 = Math.sqrt(Variance(list2));
		coefficient = CovarianceCalculation(list1,list2) / (variance1 * variance2);
		return coefficient;
	}
}
