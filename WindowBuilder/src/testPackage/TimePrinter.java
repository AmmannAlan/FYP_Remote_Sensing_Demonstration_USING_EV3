package testPackage;

import java.util.Date;  

public class TimePrinter implements Runnable {  
	  
	    int pauseTime;  
	    String name;  
	    boolean isStop = false;
	    
	   
	  
	    public TimePrinter(int x, String n) {  
	        pauseTime = x;  
	        name = n;  
	       
	    }  
	    
	 
	  
	    /** 
	     * @param args 
	     *            当使用 runnable 接口时，您不能直接创建所需类的对象并运行它；必须从 Thread 类的一个实例内部运行它 
	     * @throws InterruptedException 
	     *  
	     */  
	    
		public static void main(String[] args) throws InterruptedException {  
	        // TODO Auto-generated method stub  
	        // TimePrinter tp1 = new TimePrinter(1000, "Fast Guy"); 继承Thread  
	        // tp1.start();  
	        // TimePrinter tp2 = new TimePrinter(3000, "Slow Guy");  
	        // tp2.start();  
	        Thread t1 = new Thread(new TimePrinter(1000, "Fast Guy"));  
	        t1.start(); 
	    
	        Thread t2 = new Thread(new TimePrinter(2000, "Slow Guy"));  
	        t2.start();  
	        
	        Thread.sleep(10000);
	        t1.interrupt();
	        
	       
	        if(t1.isInterrupted() == true)
	        {
	          Thread.sleep(20);
	          t2.interrupt();  
	          
	        }
	        
	        //System.out.println("-----Thread 2 is over ----");
	       
	    }  
	  
	    @SuppressWarnings("unused")
		private void start() {  
	        this.run();  
	    }  
	  
	    @Override  
	    public void run() {  
	    	
	    	try{
	    	while(!Thread.interrupted())
	    	{
	    	
	    		System.out.println(name + ":" + new Date(System.currentTimeMillis()));
	    		
	    	    Thread.sleep(pauseTime);
	    	}
	    	
	    	}catch (InterruptedException e)
	    	{
	    		
	    		System.out.println("This Thread is stopped");
	    	}
	      /*  while (true) {  
	            try {  
	                System.out.println(name + ":"  
	                        + new Date(System.currentTimeMillis()));  
	                Thread.sleep(pauseTime);  
	            } catch (Exception e) {  
	                System.out.println(e);  
	            }  */
	    	System.out.println("This Thread interrupted!");  

	    	
	            
	        }  
	  
	 }  
	  
	