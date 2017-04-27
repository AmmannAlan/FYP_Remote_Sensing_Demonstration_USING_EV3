package windowBuilder.common;

import java.io.Serializable;
import java.util.Random;

import robotBasic.Point;

public class ParticleFilter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4103508985285218860L;
	public int particle_number = 0;
	public Particle[] particles;
    Random gen = new Random();
        
    public ParticleFilter(int numParticles, int width, int height, Point[] landmarks)
    {
    	 this.particle_number = numParticles;
    	 particles = new Particle[numParticles];
    	 for (int i = 0; i < numParticles; i++) {
             particles[i] = new Particle(width, height, landmarks);
         }
    	 
    }
    
    public void setNoise(double fNoise, double tNoise, double sNoise)
    {
    	for(int i=0; i<particle_number; i++)
    	{
    		particles[i].setNoise(sNoise, tNoise, sNoise);
    	}
    }
    
    //Move is the distance that the robot moves, turn is the angle [face angle]
    public void move(double turn, double forward)
    {
    	 for (int i = 0; i < particle_number; i++) 
    	 {
             particles[i].move(turn, forward);
         }
    }
    
    public void resample(int[] measurement)
    {
    	Particle[] new_particles = new Particle[particle_number];
    	
    	  for (int i = 0; i < particle_number; i++) 
    	  {
              particles[i].measurementProb(measurement);
          }
    	  
          double B = 0f;
          Particle best = getBestParticle();
          int index = (int) gen.nextDouble() * particle_number;
          
          for (int i = 0; i < particle_number; i++) 
          {
              B += gen.nextDouble() * 2f * best.probability;
              
              while (B > particles[index].probability) 
              {
                  B -= particles[index].probability;
                  index = circle(index + 1, particle_number);
              }
              
              new_particles[i] = new Particle(particles[index].xLength, particles[index].yLength,particles[index].landmarks);
              new_particles[i].set(particles[index].x, particles[index].y, particles[index].orientation, particles[index].probability);
              new_particles[i].setNoise(particles[index].forwardNoise, particles[index].turnNoise, particles[index].senseNoise);
          }

          particles = new_particles;       
    	
    }
    
    public Particle getBestParticle() 
    {
        Particle particle = particles[0];
        for (int i = 0; i < particle_number; i++) {
            if (particles[i].probability > particle.probability) {
                particle = particles[i];
            }
        }
        return particle;
    }
    
    public Particle getAverageParticle()
    {
    	  Particle p = new Particle(particles[0].xLength, particles[0].yLength,particles[0].landmarks);
          float x = 0, y = 0, orient = 0, prob = 0;
          for(int i=0;i<particle_number;i++) {
              x += particles[i].x;
              y += particles[i].y;
              orient += particles[i].orientation;
              prob += particles[i].probability;
          }
          x /= particle_number;
          y /= particle_number;
          orient /= particle_number;
          prob /= particle_number;
          try {
              p.set(x, y, orient, prob);
          } catch (Exception ex) {
        	  
              //Logger.getLogger(ParticleFilter.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          p.setNoise(particles[0].forwardNoise, particles[0].turnNoise, particles[0].senseNoise);
          
          return p;
    }
    
    private int circle(int num, int length) 
    {
        while (num > length - 1) {
            num -= length;
        }
        while (num < 0) {
            num += length;
        }
        return num;
    }
    
}
