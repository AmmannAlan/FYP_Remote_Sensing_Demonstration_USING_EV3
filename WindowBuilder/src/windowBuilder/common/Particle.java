package windowBuilder.common;

import java.io.Serializable;
import java.util.Random;
import robotBasic.Point;


public class Particle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1945967525462662138L;
	public double forwardNoise;
	public double turnNoise;
	public double senseNoise;
	public int xLength = 1200;
	public int yLength = 900;
	public double probability = 0;
	public double x,y,orientation;
		
	public Point[] landmarks;
	Random random;

	
	public Particle(int width, int height, Point[] landmarks)
	{
		this.landmarks = landmarks;
		this.xLength = height;
		this.yLength = width;
		random = new Random();
		x = randomValue(width, xLength);
		y = randomValue(height, yLength);
		orientation = random.nextDouble()*2f*(Math.PI);
		forwardNoise = 0f;
		turnNoise = 0f;
		senseNoise = 0f;
		//this.landmarks = landmarks;
	}
	
	public void set(double x, double y, double orientation, double prob)
	{
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.probability = prob;
	}
	
	public void setNoise(double fNoise, double tNoise, double sNoise)
	{
		this.forwardNoise = fNoise;
		this.turnNoise = tNoise;
		this.senseNoise = sNoise;
	}
	
	public double[] sense()
	{
		double[] ret = new double[landmarks.length];
		
		for(int i=0;i<landmarks.length;i++)
		{
			double dist = MathX.distance(x, y, landmarks[i].getX(), landmarks[i].getY());
			ret[i] = dist + random.nextGaussian()*senseNoise;
		}

		return ret;
	}
	

		
	public void move(double turn, double forward)
	{
		orientation = orientation + turn + random.nextGaussian()*turnNoise;
		orientation = circle(orientation,Math.PI);
		
		double dist = forward + random.nextGaussian()*forwardNoise;
		x += Math.cos(orientation)*dist;
		y += Math.sin(orientation)*dist;
		x = circle(x,xLength);
		y = circle(y,yLength);
	}
	
	public double measurementProb(int[] measurement)
	{
		double prob = 1.0;
		for(int i=0;i<measurement.length;i++)
		{
			double distance = MathX.distance(x, y, landmarks[i].getX(), landmarks[i].getY());
			prob *= Gaussian(distance,senseNoise,measurement[i]);
		}
		
		probability = prob;
		return prob;
	}
	
	private double circle(double num, double length)
	{
		while(num > length -1 )
		{
			num -= length;
		}
		while(num < 0)
		{
			num += length;
		}
		return num;
	}
	
	public double randomValue(int value, int anotherValue)
	{
		random = new Random();
		double x =0;
		x = random.nextDouble()*value;
		if(x > anotherValue)
		{
			random = new Random();
			x = random.nextDouble()*value;
		}
		return x;
	}
	
	public double getXcoordiante()
	{
		return this.x;
	}
	
	public double getYcoordiante()
	{
		return this.y;
	}
	
	public double getProb()
	{
		return this.probability;
	}
	
	private double Gaussian(double mu, double sigma, double x)
	{
		return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
	}
}
