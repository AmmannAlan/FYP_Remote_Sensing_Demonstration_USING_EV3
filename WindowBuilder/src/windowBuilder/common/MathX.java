package windowBuilder.common;

public class MathX {

	 public static double distance(double x1, double y1, double x2, double y2) { 
	        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	    }
	    
	    public static double Gaussian(double mu, double sigma, double x) {       
	        return Math.exp(-(Math.pow(mu - x, 2)) / Math.pow(sigma, 2) / 2.0) / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2));
	    }
}
