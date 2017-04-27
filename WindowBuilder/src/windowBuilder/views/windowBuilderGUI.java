package windowBuilder.views;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import robotBasic.EV3Robot;
import robotBasic.Point;
import robotBasic.PositionDataStore;
import robotBasic.StaticSweep;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData; 
import windowBuilder.common.*;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;


public class windowBuilderGUI {
	
	//Create a new Robot
	public EV3Robot ev3 = new EV3Robot();
	//
	public StaticSweep sweep; 
	//
	protected Shell shell;
	private Button ConnectButton;
	public int width = 5;
	public int height = 5;
	private Text angle_text;
	private Text distance_text;
	private Text xcoordinate_text;
	private Text ycoordinate_text;
	private Text pose_angle_text;
	
	//Display the Rotation Angle
	public int rotationAngle = 0;
	public int Xcoordinate = 600; //Need Change Here
	public int Ycoordinate = 405; //Need Change Here
	public double distance = 0; //This value will be displayed in the text
	public int poseAngle = 0;
	public int[] poseAngleArray;
	public int particle_number = 10000;
	public boolean connected = false;
	public boolean readFlag = false;
	//Particle Filter
	ParticleFilter filter;
    //public Point[] landmark = new Point[]{new Point(595,405),new Point(600,405),new Point(605,405),new Point(595,400),new Point(600,400),new Point(605,400),new Point(595,395),new Point(600,395),new Point(605,395),new Point(597,403)};
	public Point[] landmark = new Point[10];
	public ArrayList<Integer> angleList = new ArrayList<Integer>();

    public RobotSense sense;
    public ArrayList<Point> poseList = new ArrayList<Point>();
    
    //Test the New Draw Function
    public ArrayList<StaticSweep> sweepList = new ArrayList<StaticSweep>();
    public ArrayList<Point> updateOccupiedPointArray = new ArrayList<Point>();
    public ArrayList<Point> updateFreePointArray = new ArrayList<Point>();
    public PositionDataStore dataStore = new PositionDataStore();
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			windowBuilderGUI window = new windowBuilderGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();

		createContents();
		//
		
		//
		shell.open();
		shell.layout();
	
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 *  Set the Particles,etc //Test Part
	 */
	
	  private void PointSetUp()
	  {
		   
			for(int i=0;i<sweepList.size();i++)
			{
				for(int j=0;j<sweepList.get(i).getOccupiedPointArray().size();j++)
				{
					this.updateOccupiedPointArray.add(sweepList.get(i).getOccupiedPointArray().get(j));
				}
			}
			
			for(int i=0;i<sweepList.size();i++)
			{
				for(int j=0;j<sweepList.get(i).getFreePointArray().size();j++)
				{
					for(int t=0;t<sweepList.get(i).getFreePointArray().get(j).size();t++)
					{
						this.updateFreePointArray.add(sweepList.get(i).getFreePointArray().get(j).get(t));
					}
				}
			}
			

			
			HashSet<Point> hs_free = new HashSet<Point>(updateFreePointArray);
		    ArrayList<Point> tempFreeList = new ArrayList<Point>(hs_free);
		    updateFreePointArray = tempFreeList;
			
		    
			HashSet<Point> hs_OCC = new HashSet<Point>(updateOccupiedPointArray);
		    ArrayList<Point> tempOccList = new ArrayList<Point>(hs_OCC);
		    updateOccupiedPointArray = tempOccList;

			for(int i=0;i<this.updateFreePointArray.size();i++)
			{
				for(int j=0;j<this.updateOccupiedPointArray.size();j++)
				{
					if(this.updateOccupiedPointArray.get(j).getX() == updateFreePointArray.get(i).getX())
					{
						if(this.updateOccupiedPointArray.get(j).getY() == updateFreePointArray.get(i).getY())
						{
							this.updateOccupiedPointArray.remove(j);
						}			
					}
				}
			}
		  
		  dataStore.setPoint(updateFreePointArray, updateOccupiedPointArray);
		  
	  } 
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.keyCode == SWT.ARROW_UP)
				{
					RobotMoveForward();
				}
			}
		});
		shell.setImage(SWTResourceManager.getImage(windowBuilderGUI.class, "/windowBuilder/resources/icon.png"));
		shell.setSize(1500, 1020);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		
		
		//
		angleList.add(0);
        
		//Set the landmark
		/*
		if(sweepList.size() == 1)
       {
		  for(int i=0;i<landmark.length;i++)
		  {
		    landmark[i] = sweepList.get(0).getFreePointList().get(i);
		  }
       }*/
		
		//This area is GRID MAPPING using Canvas
		Canvas MapCanvas = new Canvas(shell, SWT.NONE);
		FormData fd_MapCanvas = new FormData();
		fd_MapCanvas.bottom = new FormAttachment(0, 925);
		fd_MapCanvas.right = new FormAttachment(0, 1450);
		fd_MapCanvas.top = new FormAttachment(0, 25);
		fd_MapCanvas.left = new FormAttachment(0, 250);
		MapCanvas.setLayoutData(fd_MapCanvas);
		
		//MapCanvas.addPaintListener(new StaticSweepPaintListener());
		MapCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				
			    //The size of the canvas is 1200*900, 900 represent 4.5 meters while 1200 represents 6m
				//The vertical direction has 45 grids and the horizontal direction has 60 grids
				//which means one grid is 0.1 meters one pixel length is 0.1/20 = 0.005
				//The maximum scan range is 2.5 meters
			    arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
			    
			    /*
			    for(int j=0;j<80;j++)
			    {
			     for(int i=0;i<60;i++)
			     {
			    	arg0.gc.drawLine(0, i*15, 1200, i*15);
			    	arg0.gc.drawLine(j*15, 0, j*15, 900);
			     }
			    }*/
			    

			    
			    arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
				arg0.gc.drawLine(0, 405, 1200 ,405);
			    arg0.gc.drawLine(600, 0, 600, 900);
			    
			    
			    //Only Works When the robot is connected to the server
			    
			 //Static Robot Sweeping
			 
			    /*
			    //Draw all the particles
			    arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
			    for (Particle p : filter.particles) 
			    {
		           arg0.gc.drawRectangle((int) p.x, (int) p.y, 1, 1);
		        }
			    
			    //Draw the average particle
			    Particle p = filter.getAverageParticle();
			    arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
			    arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
			    arg0.gc.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
			    arg0.gc.fillOval((int) p.x - 5, (int) p.y - 5, 10, 10);
			    
			    //Draw the average particle 
                p = filter.getBestParticle();
                arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
			    arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
			    arg0.gc.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
			    arg0.gc.fillOval((int) p.x - 5, (int) p.y - 5, 10, 10);
			    */
			}
			
		});
	    
		MapCanvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		MapCanvas.addPaintListener(new StaticSweepPaintListener());
		MapCanvas.addPaintListener(new LoadMapPaintListener());
		//MapCanvas.addPaintListener(new TestPaint());
		
		//This button is used to control the robot to move forward
		Button UpButton = new Button(shell, SWT.NONE);
		FormData fd_UpButton = new FormData();
		fd_UpButton.bottom = new FormAttachment(0, 186);
		fd_UpButton.right = new FormAttachment(0, 140);
		fd_UpButton.top = new FormAttachment(0, 136);
		fd_UpButton.left = new FormAttachment(0, 90);
		UpButton.setLayoutData(fd_UpButton);
		UpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				RobotMoveForward();
				
				
				if (ev3.getEV3().isConnected() == false)
				{

					JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
					
				}else if(ev3.getEV3().isConnected() == true && sweepList.size() == 0)
				{
					 MessageBox failure = new MessageBox(shell, SWT.ERROR|SWT.ICON_INFORMATION);
			    	 failure.setText("Unable to Set Particle Filter");
			    	 failure.setMessage("No map is constructed. Unable to Localize");
			    	 failure.open();
				}else 
				{
					//ParticleFilter tempFilter = filter;
					ParticleFilterResample();
					ParticleFilter tempFilter = filter;
					//ParticleFilter tempFilter = new ParticleFilter();
					ev3.AddNewParticleFilter(tempFilter);
					dataStore.addNewParticleFilter(tempFilter);
				}
	
				  
			} 
		});
		UpButton.setImage(SWTResourceManager.getImage(windowBuilderGUI.class, "/windowBuilder/resources/up.png"));
		
	    //This button controls the robot to turn left
		Button LeftButton = new Button(shell, SWT.NONE);
		FormData fd_LeftButton = new FormData();
		fd_LeftButton.bottom = new FormAttachment(0, 256);
		fd_LeftButton.right = new FormAttachment(0, 70);
		fd_LeftButton.top = new FormAttachment(0, 206);
		fd_LeftButton.left = new FormAttachment(0, 20);
		LeftButton.setLayoutData(fd_LeftButton);
		LeftButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(ev3.getEV3().isConnected() == false)
	            {
	           	 JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
	            }
			 
			 try{
				 ev3.getEV3().left();
				 Thread.sleep(1000);
				 ev3.getEV3().pilotStop();
				 
				 //Change the Pose Angle
				 poseAngle = poseAngle - 45;
				 if(poseAngle == -360)
				 {
					 poseAngle = 0;
				 }
				 System.out.println(poseAngle);
				 pose_angle_text.setText(String.valueOf(poseAngle));
				 
			 }catch(Exception e1)
			 {
				 JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
	 			 e1.printStackTrace();
			 }
			 
			 
			}
		});
		LeftButton.setImage(SWTResourceManager.getImage(windowBuilderGUI.class, "/windowBuilder/resources/left.png"));
		
		//This button controls the robot to turn right
		Button RightButton = new Button(shell, SWT.NONE);
		FormData fd_RightButton = new FormData();
		fd_RightButton.bottom = new FormAttachment(0, 256);
		fd_RightButton.right = new FormAttachment(0, 208);
		fd_RightButton.top = new FormAttachment(0, 206);
		fd_RightButton.left = new FormAttachment(0, 158);
		RightButton.setLayoutData(fd_RightButton);
		RightButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if(ev3.getEV3().isConnected() == false)
	            {
	           	 JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
	            }
			 
			 try{
				 ev3.getEV3().right();
				 Thread.sleep(1000);
				 ev3.getEV3().pilotStop();
				 
				 //Change the Pose Angle
				 poseAngle = poseAngle + 45;
				 if(poseAngle == 360)
				 {
					 poseAngle = 0;
				 }
				 System.out.println(poseAngle);
				 pose_angle_text.setText(String.valueOf(poseAngle));
				 
			 }catch(Exception e1)
			 {
				 JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
	 			 e1.printStackTrace();
			 }
			 //poseAngle = poseAngle + 45;
			 //System.out.println(poseAngle);
			 //pose_angle_text.setText(String.valueOf(poseAngle));
			}
		});
		RightButton.setImage(SWTResourceManager.getImage(windowBuilderGUI.class, "/windowBuilder/resources/right.png"));
		
		//This button is used to control the robot move backward 
		Button BackButton = new Button(shell, SWT.NONE);
		FormData fd_BackButton = new FormData();
		fd_BackButton.bottom = new FormAttachment(0, 256);
		fd_BackButton.right = new FormAttachment(0, 140);
		fd_BackButton.top = new FormAttachment(0, 206);
		fd_BackButton.left = new FormAttachment(0, 90);
		BackButton.setLayoutData(fd_BackButton);
		
		BackButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if (ev3.getEV3().isConnected() == false)
				{

					JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (ev3.getEV3().isConnected() == true)
				{
				try{
			 		  ev3.getEV3().backward();
			 		  Thread.sleep(1000);
			 		  ev3.getEV3().pilotStop();
			 		  
			 		 //for the particle 
			 		 angleList.add(poseAngle);
			 		 
			 		 Xcoordinate = (int) (Xcoordinate - 18*Math.sin(poseAngle*Math.PI/180));
			 		 Ycoordinate = (int) (Ycoordinate + 18*Math.cos(poseAngle*Math.PI/180));
			 		 poseList.add(new Point(Xcoordinate, Ycoordinate));
			 		 
			 		 xcoordinate_text.setText(String.valueOf(Xcoordinate));
			 		 ycoordinate_text.setText(String.valueOf(Ycoordinate));
			 		  
			 	}catch(Exception e1){
			 				
			 			JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
			 			e1.printStackTrace();
			 			}
				}
				

				if (ev3.getEV3().isConnected() == false)
				{

					JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
					
				}else if(ev3.getEV3().isConnected() == true && sweepList.size() == 0)
				{
					 MessageBox failure = new MessageBox(shell, SWT.ERROR|SWT.ICON_INFORMATION);
			    	 failure.setText("Unable to Set Particle Filter");
			    	 failure.setMessage("No map is constructed. Unable to Localize");
			    	 failure.open();
				}else 
				{
					ParticleFilterResample();
					ParticleFilter tempFilter = filter;
					ev3.AddNewParticleFilter(tempFilter);
					dataStore.addNewParticleFilter(tempFilter);
				}
				
			
				
			}
		});
		BackButton.setImage(SWTResourceManager.getImage(windowBuilderGUI.class, "/windowBuilder/resources/down.png"));
		
		//Connect Button is Here
		ConnectButton = new Button(shell, SWT.PUSH);
		/*
		ConnectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		}); */
		FormData fd_ConnectButton = new FormData();
		fd_ConnectButton.left = new FormAttachment(0, 20);
		fd_ConnectButton.top = new FormAttachment(0, 60);
		ConnectButton.setLayoutData(fd_ConnectButton);
		ConnectButton.setFont(SWTResourceManager.getFont("Times New Roman", 12, SWT.NORMAL));
		ConnectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
			     
			     try{
			    	 ev3.getEV3().connect();
			    	 MessageBox success = new MessageBox(shell, SWT.OK|SWT.ICON_INFORMATION);
			    	 success.setText("Connection Succeded!");
			    	 success.setMessage("Connection to the Robot is successful.");
			    	 success.open();
			    	 connected = true;
			    	 
			     }catch(Exception e1)
			     {
			    	 MessageBox failure = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
			    	 failure.setText("Connection Failed!");
			    	 failure.setMessage("Connection to the Robot is unsuccessful.");
			    	 failure.open();
			    	 e1.printStackTrace();
			    	 connected = false;
			     }
			}
		});
		ConnectButton.setText("Connect");
		
		//Disconnect the robot from the computer
		Button disconnectButton = new Button(shell, SWT.NONE);
		fd_ConnectButton.right = new FormAttachment(disconnectButton, -25);
		FormData fd_disconnectButton = new FormData();
		fd_disconnectButton.right = new FormAttachment(MapCanvas, -26);
		fd_disconnectButton.left = new FormAttachment(0, 130);
		fd_disconnectButton.bottom = new FormAttachment(UpButton, -45);
		fd_disconnectButton.top = new FormAttachment(ConnectButton, 1, SWT.TOP);
		disconnectButton.setLayoutData(fd_disconnectButton);
		disconnectButton.setFont(SWTResourceManager.getFont("Times New Roman", 11, SWT.NORMAL));
		disconnectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				
				try{
					ev3.getEV3().usClose();
					ev3.getEV3().motorClose();
					ev3.getEV3().disConnect();
					connected = false;
				    JOptionPane.showMessageDialog(null, "Disconnect Successfully", "Robot Message", JOptionPane.DEFAULT_OPTION);
				}catch(Exception e1)
				{
				  JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Message", JOptionPane.DEFAULT_OPTION);
			 	  e1.printStackTrace();
			 	  connected = false;
				}
			}
		});
		disconnectButton.setText("Disconnect");
		
		//Scan the Surroundings and Demonstrated the Map
		Button ScanButton = new Button(shell, SWT.NONE);
		/*
		ScanButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});*/
		FormData fd_ScanButton = new FormData();
		fd_ScanButton.left = new FormAttachment(0, 20);
		fd_ScanButton.right = new FormAttachment(0, 105);
		fd_ScanButton.top = new FormAttachment(0, 303);
		ScanButton.setLayoutData(fd_ScanButton);
		ScanButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("button_start");
				  if(ev3.getEV3().isConnected() == false)
				  {
					  MessageBox Message = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
					  Message.setText("ERROR INFO");
					  Message.setMessage("No Robot is Connected");
					  Message.open();
				  }else if(ev3.getEV3().isConnected() == true)
				  {
					  try {
						  
						
						  sweep = new StaticSweep(new Point(Xcoordinate,Ycoordinate), poseAngle);
						  sweep.staticSweep(ev3.getEV3());
						  ev3.AddNewSweep(sweep);
						  dataStore.addNewSweep(sweep);
						  sweepList.add(sweep);
						  //
						  PointSetUp();
						  
				
						  for(int i=0;i<landmark.length;i++)
						  {
							  landmark[i] = sweepList.get(0).getFreePointList().get(i);
						  }
						  
						  if(sweepList.size() == 1)
						  {
						    filter = new ParticleFilter(particle_number, 1200, 900, landmark);
							filter.setNoise(5f, 5f, 5f);
							
							int x = sweepList.get(0).getRobotPose().getX();
							int y = sweepList.get(0).getRobotPose().getY();
							int[] measurement = new int[10];
							Random random = new Random();
							for(int i =0;i<measurement.length;i++)
							{
								double dist = MathX.distance(x, y, landmark[i].getX(), landmark[i].getY());
								measurement[i] = (int) (dist + (float)random.nextGaussian() * 5f);
							}
							
							filter.resample(measurement);
						    ev3.AddNewParticleFilter(filter);
							dataStore.addNewParticleFilter(filter);
						  }
						  //
						 // sense = new RobotSense(landmarks, new Point(Xcoordinate, Ycoordinate));
						  //

						 // ev3.AddNewParticleFilter(filter);
						 // dataStore.addNewParticleFilter(filter);
						//ev3.CalculatePointsArray();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						MessageBox Message = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
						Message.setText("ERROR INFO");
						Message.setMessage("Connection Error");
						Message.open();
						e.printStackTrace();
					}
				  }
				  
				  //ev3.getSweep().setFinish(true);
				  
				  System.out.println("button_end");
			}
		});
		ScanButton.setFont(SWTResourceManager.getFont("Times New Roman", 12, SWT.NORMAL));
		ScanButton.setText("Scan");
		
		
		
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		//On the following is the File Menu
		MenuItem FileMenu = new MenuItem(menu, SWT.CASCADE);
		FileMenu.setText("Image Menu");
		
		Menu FileSubMenu = new Menu(FileMenu);
		FileMenu.setMenu(FileSubMenu);
		MenuItem SaveImageMenu = new MenuItem(FileSubMenu, SWT.NONE);
		
		
		SaveImageMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {			
				Image drawable = new Image(arg0.display, MapCanvas.getBounds());
				GC gc = new GC(drawable);
				MapCanvas.print(gc);
				ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] {drawable.getImageData()};	
				
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save Canvas to Image");   
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image (.png)","png"));
				File fileToSave = null;
				int userSelection = fileChooser.showSaveDialog(parentFrame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    fileToSave = fileChooser.getSelectedFile();
				    String filePath = fileToSave.getAbsolutePath() + ".png";
				    loader.save(filePath, SWT.IMAGE_PNG);
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			    }
				drawable.dispose();
				gc.dispose();
			}
		}); 
		
		SaveImageMenu.setText("Save Image");
		
		MenuItem ReadImageMenu = new MenuItem(FileSubMenu, SWT.NONE);
		ReadImageMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Read Map Image");   
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Map Image(.png)","png"));
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Map Image(.jpg)","jpg"));
				
		        int userSelection = fileChooser.showOpenDialog(parentFrame);
		        
		        if (userSelection == JFileChooser.APPROVE_OPTION) {
		        	
		        	File selectedFile = fileChooser.getSelectedFile();
		        	Image image = new Image(arg0.display,selectedFile.getAbsolutePath());
		            MapCanvas.addPaintListener(new PaintListener(){
		            	
		           public void paintControl(PaintEvent e) {
		            	       
		        	e.gc.drawImage(image, 0, 0);
		            }
		            });

		        	//System.out.println(selectedFile.getName());
		        	//On the following that the full path will be obtained
		        	System.out.println(selectedFile.getAbsolutePath());
		        	
		        	               
		        }
			}
		});
		ReadImageMenu.setText("Read Image");
		
		
		
		//On the Following is the Map Menu
		MenuItem MapMenu = new MenuItem(menu, SWT.CASCADE);
		MapMenu.setText("Map SubMenu");
		
		Menu MapSubMenu = new Menu(MapMenu);
		MapMenu.setMenu(MapSubMenu);
		
		MenuItem SaveMapData = new MenuItem(MapSubMenu, SWT.NONE);
		SaveMapData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			 
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save Map Data To Binary File");   
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Data (.dat)","dat"));
				File fileToSave = null;
				int userSelection = fileChooser.showSaveDialog(parentFrame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    fileToSave = fileChooser.getSelectedFile();
				    String filePath = fileToSave.getAbsolutePath() + ".dat";
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				    
				    //Save the EV3 Robot Data to binary file
					try {  
				        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filePath));  
				        os.writeObject(dataStore);//
	 
				        os.close();  
				    } catch (FileNotFoundException e) {  
				        e.printStackTrace();
				        
				        MessageBox SaveMapError = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
				    	SaveMapError.setText("Save Map Error!");
				    	SaveMapError.setMessage("Saving the Map is Unsucessful. File Not Found.");
				    	SaveMapError.open();
				    	
				    } catch (IOException e1) {  
				        e1.printStackTrace();  
				        
				        MessageBox SaveMapError = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
				    	SaveMapError.setText("Save Map Error!");
				    	SaveMapError.setMessage("Saving the Map is Unsucessful. IO error.");
				    	SaveMapError.open();
				    }   
				   
			    }
				
			}
		});
		SaveMapData.setText("Save Map Data");
		
		MenuItem ReadMap = new MenuItem(MapSubMenu, SWT.NONE);
		ReadMap.addSelectionListener(new SelectionAdapter() {
			@Override
			//This button is used to read the MAP data from the binary file
			public void widgetSelected(SelectionEvent arg0) {
				
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Read Map Data");   
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Map Data(.dat)","dat"));
				
		        int userSelection = fileChooser.showOpenDialog(parentFrame);
		        
		        if (userSelection == JFileChooser.APPROVE_OPTION) 
		        {
		            File selectedFile = fileChooser.getSelectedFile();

		            System.out.println(selectedFile.getName());
		            
		            try {  
		                ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile().getAbsolutePath())); 
		                System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
		                
		                PositionDataStore new_store = (PositionDataStore) is.readObject();//
		                dataStore = new_store;
		                is.close();  
		                dataStore.setReadFlag(true);
		                
		            } catch (FileNotFoundException e) {  
		                e.printStackTrace();  
		                MessageBox ReadMapError = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
			    		ReadMapError.setText("Read Map Error!");
			    		ReadMapError.setMessage("Read The Map is Unsucessful. File Not Found.");
			    		ReadMapError.open();
			    		
		            } catch (IOException e) {  
		                e.printStackTrace();  
		                MessageBox ReadMapError = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
			    		ReadMapError.setText("Read Map Error!");
			    		ReadMapError.setMessage("Read The Map is Unsucessful. IO Error.");
			    		ReadMapError.open();
			    		
		            } catch (ClassNotFoundException e) {  
		                e.printStackTrace();  
		                MessageBox ReadMapError = new MessageBox(shell, SWT.ERROR|SWT.ICON_ERROR);
			    		ReadMapError.setText("Read Map Error!");
			    		ReadMapError.setMessage("Read The Map is Unsucessful. Class Does Not Match.");
			    		ReadMapError.open();
			    		
		            }  
		         
		        }
		        
		        
			}
		});
		ReadMap.setText("Read Map Data");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		fd_ScanButton.bottom = new FormAttachment(lblNewLabel, -27);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 360);
		fd_lblNewLabel.left = new FormAttachment(0, 20);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setFont(SWTResourceManager.getFont("Times New Roman", 11, SWT.NORMAL));
		lblNewLabel.setText("Current Data");
		
		Button DrawButton = new Button(shell, SWT.NONE);
		DrawButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				MapCanvas.redraw();
			}
		});
		DrawButton.setFont(SWTResourceManager.getFont("Times New Roman", 11, SWT.NORMAL));
		/*
		DrawButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MapCanvas.redraw();
			
			}
		}); */
		
		FormData fd_DrawButton = new FormData();
		fd_DrawButton.right = new FormAttachment(RightButton, 0, SWT.RIGHT);
		fd_DrawButton.top = new FormAttachment(RightButton, 48);
		fd_DrawButton.left = new FormAttachment(ScanButton, 25);
		DrawButton.setLayoutData(fd_DrawButton);
		DrawButton.setText("Draw");
		
		angle_text = new Text(shell, SWT.BORDER);
		FormData fd_angle_text = new FormData();
		fd_angle_text.right = new FormAttachment(LeftButton, 0, SWT.RIGHT);
		fd_angle_text.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		angle_text.setLayoutData(fd_angle_text);
		angle_text.setText(String.valueOf(rotationAngle));
		
		Label angle_label = new Label(shell, SWT.NONE);
		fd_angle_text.top = new FormAttachment(angle_label, 12);
		angle_label.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.NORMAL));
		FormData fd_angle_label = new FormData();
		fd_angle_label.bottom = new FormAttachment(100, -523);
		fd_angle_label.right = new FormAttachment(LeftButton, 40);
		fd_angle_label.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		angle_label.setLayoutData(fd_angle_label);
		angle_label.setText("Angle(degree)");
		
		Label distance_label = new Label(shell, SWT.NONE);
		distance_label.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.NORMAL));
		FormData fd_distance_label = new FormData();
		fd_distance_label.top = new FormAttachment(DrawButton, 72);
		fd_distance_label.left = new FormAttachment(angle_label, 70);
		distance_label.setLayoutData(fd_distance_label);
		distance_label.setText("Distance(m)");
		
		distance_text = new Text(shell, SWT.BORDER);
		FormData fd_distance_text = new FormData();
		fd_distance_text.right = new FormAttachment(MapCanvas, -70);
		fd_distance_text.left = new FormAttachment(angle_text, 60);
		fd_distance_text.top = new FormAttachment(distance_label, 12);
		distance_text.setLayoutData(fd_distance_text);
		//text_1.setTextLimit(30);
		distance_text.setSize(70, 50);
		distance_text.setText(String.valueOf(distance));
		
		Label lblNewLabel_3 = new Label(shell, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.NORMAL));
		FormData fd_lblNewLabel_3 = new FormData();
		fd_lblNewLabel_3.left = new FormAttachment(UpButton, -90);
		fd_lblNewLabel_3.right = new FormAttachment(UpButton, 0, SWT.RIGHT);
		lblNewLabel_3.setLayoutData(fd_lblNewLabel_3);
		lblNewLabel_3.setText("Current Pose");
		
		Label lblNewLabel_4 = new Label(shell, SWT.NONE);
		fd_lblNewLabel_3.top = new FormAttachment(lblNewLabel_4, -38, SWT.TOP);
		fd_lblNewLabel_3.bottom = new FormAttachment(lblNewLabel_4, -6);
		lblNewLabel_4.setFont(SWTResourceManager.getFont("Times New Roman", 9, SWT.NORMAL));
		FormData fd_lblNewLabel_4 = new FormData();
		fd_lblNewLabel_4.top = new FormAttachment(0, 573);
		fd_lblNewLabel_4.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		lblNewLabel_4.setLayoutData(fd_lblNewLabel_4);
		lblNewLabel_4.setText("X Coodinadtae");
		
		Label lblNewLabel_5 = new Label(shell, SWT.NONE);
		lblNewLabel_5.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.NORMAL));
		FormData fd_lblNewLabel_5 = new FormData();
		fd_lblNewLabel_5.top = new FormAttachment(lblNewLabel_4, 19);
		fd_lblNewLabel_5.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		lblNewLabel_5.setLayoutData(fd_lblNewLabel_5);
		lblNewLabel_5.setText("Y Coordinate");
		
		Label lblNewLabel_6 = new Label(shell, SWT.NONE);
		lblNewLabel_6.setFont(SWTResourceManager.getFont("Times New Roman", 9, SWT.NORMAL));
		FormData fd_lblNewLabel_6 = new FormData();
		fd_lblNewLabel_6.top = new FormAttachment(lblNewLabel_5, 20);
		fd_lblNewLabel_6.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		lblNewLabel_6.setLayoutData(fd_lblNewLabel_6);
		lblNewLabel_6.setText("Rotation Angle");
		
		xcoordinate_text = new Text(shell, SWT.BORDER);
		FormData fd_xcoordinate_text = new FormData();
		fd_xcoordinate_text.right = new FormAttachment(MapCanvas, -70);
		fd_xcoordinate_text.left = new FormAttachment(lblNewLabel_4, 23);
		fd_xcoordinate_text.top = new FormAttachment(lblNewLabel_3, 6);
		xcoordinate_text.setLayoutData(fd_xcoordinate_text);
		xcoordinate_text.setText(String.valueOf(Xcoordinate));
		
		ycoordinate_text = new Text(shell, SWT.BORDER);
		FormData fd_ycoordinate_text = new FormData();
		fd_ycoordinate_text.right = new FormAttachment(MapCanvas, -70);
		fd_ycoordinate_text.left = new FormAttachment(lblNewLabel_5, 20);
		fd_ycoordinate_text.top = new FormAttachment(lblNewLabel_5, -3, SWT.TOP);
		ycoordinate_text.setLayoutData(fd_ycoordinate_text);
		ycoordinate_text.setText(String.valueOf(Ycoordinate));
		
		pose_angle_text = new Text(shell, SWT.BORDER);
		FormData fd_pose_angle_text = new FormData();
		fd_pose_angle_text.right = new FormAttachment(xcoordinate_text, 0, SWT.RIGHT);
		fd_pose_angle_text.left = new FormAttachment(lblNewLabel_6, 23);
		fd_pose_angle_text.bottom = new FormAttachment(lblNewLabel_6, 0, SWT.BOTTOM);
		pose_angle_text.setLayoutData(fd_pose_angle_text);
		pose_angle_text.setText(String.valueOf(poseAngle));
		
		Button resetButton = new Button(shell, SWT.NONE);
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				readFlag = true;
				MapCanvas.redraw();
			}
		});
		
		resetButton.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.NORMAL));
		FormData fd_resetButton = new FormData();
		fd_resetButton.right = new FormAttachment(ScanButton, 0, SWT.RIGHT);
		fd_resetButton.top = new FormAttachment(lblNewLabel_6, 25);
		fd_resetButton.left = new FormAttachment(LeftButton, 0, SWT.LEFT);
		resetButton.setLayoutData(fd_resetButton);
		resetButton.setText("Load Map");
		
		
		
				
		
	}
	
	private class StaticSweepPaintListener implements PaintListener{
		

		@Override
		public void paintControl(PaintEvent arg0) {
			// TODO Auto-generated method stub
			
			
			if(connected == true)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
				arg0.gc.drawOval(595,400,10,10);
				arg0.gc.fillOval(595,400,10,10);
			    
              
				
				
				
				//Draw Free Points
				/*
				for(int i=0;i<ev3.getUpdateFreePointArray().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.drawRectangle(ev3.getUpdateFreePointArray().get(i).getX(), ev3.getUpdateFreePointArray().get(i).getY(), 1, 1);
					arg0.gc.fillRectangle(ev3.getUpdateFreePointArray().get(i).getX(), ev3.getUpdateFreePointArray().get(i).getY(), 1, 1);
				}*/
				/**
				 *  On the following there would be one new test Method
				 */
                for(int i=0;i<updateFreePointArray.size();i++)
                {
                	arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.drawRectangle(updateFreePointArray.get(i).getX(),updateFreePointArray.get(i).getY(),1,1);
					arg0.gc.fillRectangle(updateFreePointArray.get(i).getX(),updateFreePointArray.get(i).getY(),1,1);
                }
				
				//Draw Occupied Points
				/*
				for(int i=0;i<ev3.getUpdateOccupiedPointArray().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.drawRectangle(ev3.getUpdateOccupiedPointArray().get(i).getX(), ev3.getUpdateOccupiedPointArray().get(i).getY(), 1, 1);
					arg0.gc.fillRectangle(ev3.getUpdateOccupiedPointArray().get(i).getX(), ev3.getUpdateOccupiedPointArray().get(i).getY(), 1, 1);
				}*/
                /**
                 *  On the following a new Method will be tested
                 * 
                 */
                for(int i=0;i<updateOccupiedPointArray.size();i++)
                {
                	arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.drawRectangle(updateOccupiedPointArray.get(i).getX(), updateOccupiedPointArray.get(i).getY(), 1, 1);
					arg0.gc.fillRectangle(updateOccupiedPointArray.get(i).getX(), updateOccupiedPointArray.get(i).getY(), 1, 1);
                }

				
				//Draw the Position
				for(int i=0;i<ev3.getSweepList().size();i++)
				{
					int tempX = ev3.getSweepList().get(i).getRobotPose().getX();
					int tempY = ev3.getSweepList().get(i).getRobotPose().getY();
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
					//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
					arg0.gc.drawOval(tempX-5,tempY-5,10,10);
					arg0.gc.fillOval(tempX-5,tempY-5,10,10);
				}
               
				for(int i=0;i<ev3.getParticleFilter().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
				    Particle p = ev3.getParticleFilter().get(i).getBestParticle();
				    arg0.gc.drawRectangle((int) p.x -5, (int) p.y -5, 10, 10);
				    arg0.gc.fillRectangle((int) p.x -5, (int) p.y -5, 10, 10);
				}
				
				//Draw the average particle
				//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				for(int i=0;i<ev3.getParticleFilter().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				    Particle p = ev3.getParticleFilter().get(i).getAverageParticle();
				    arg0.gc.drawRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
				    arg0.gc.fillRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
				}
			}
			
			/*
			if(readFlag == true)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
				arg0.gc.drawOval(595,400,10,10);
				arg0.gc.fillOval(595,400,10,10);
				
				//Draw Free Points
			
				for(int i=0;i<dataStore.getFreePoint().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
					arg0.gc.drawRectangle(dataStore.getFreePoint().get(i).getX(), dataStore.getFreePoint().get(i).getY(), 1, 1);
					arg0.gc.fillRectangle(dataStore.getFreePoint().get(i).getX(), dataStore.getFreePoint().get(i).getY(), 1, 1);
				}
				
				//Draw Occupied Points

				for(int i=0;i<dataStore.getOccupiedPoint().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
					arg0.gc.drawRectangle(dataStore.getOccupiedPoint().get(i).getX(), dataStore.getOccupiedPoint().get(i).getY(), 1, 1);
					arg0.gc.fillRectangle(dataStore.getOccupiedPoint().get(i).getX(), dataStore.getOccupiedPoint().get(i).getY(), 1, 1);
				}
				
				for(int i=0;i<dataStore.getSweepList().size();i++)
				{
					int tempX = dataStore.getSweepList().get(i).getRobotPose().getX();
					int tempY = dataStore.getSweepList().get(i).getRobotPose().getY();
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
					//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
					arg0.gc.drawOval(tempX-5,tempY-5,10,10);
					//arg0.gc.fillOval(tempX-5,tempY-5,10,10);
				}
				
				//Draw the best particle 
				//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_GREEN));
				//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_GREEN));
				for(int i=0;i<dataStore.getFilterList().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
				    Particle p = dataStore.getFilterList().get(i).getBestParticle();
				    arg0.gc.drawRectangle((int) p.x -5, (int) p.y -5, 10, 10);
				    arg0.gc.fillRectangle((int) p.x -5, (int) p.y -5, 10, 10);
				}
				
				//Draw the average particle
				//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				for(int i=0;i<dataStore.getFilterList().size();i++)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				    Particle p = dataStore.getFilterList().get(i).getAverageParticle();
				    arg0.gc.drawRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
				    arg0.gc.fillRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
				}
			}  */
			
			/*
			arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
			arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
			arg0.gc.drawRectangle(600,405,20,20);
			arg0.gc.fillRectangle(600,405,20,20);
			*/
			
			
			//Draw all the particles 
			//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
			//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
			
			/*
			for(int i=0;i<ev3.getParticleFilter().size();i++)
			{
				for(Particle p : ev3.getParticleFilter().get(i).particles)
				{
					arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
					arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
					arg0.gc.drawRectangle((int) p.x, (int) p.y, 1, 1);
				}
			}*/
			
			
			
		}
	
	}
	
	public void RobotMoveForward()
	{
		
		if (ev3.getEV3().isConnected() == false)
		{

			JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (ev3.getEV3().isConnected() == true)
		{
		try{
	 		  ev3.getEV3().forward();
	 		  Thread.sleep(1000);
	 		  ev3.getEV3().pilotStop();
	 		  
	 		  //add new angle for the particle filter
	 		  angleList.add(poseAngle);
	 		  
	 		  
	 		  Xcoordinate = (int) (Xcoordinate + 18*Math.sin(poseAngle*Math.PI/180));
	 		  Ycoordinate = (int) (Ycoordinate - 18*Math.cos(poseAngle*Math.PI/180));
	 		  poseList.add(new Point(Xcoordinate, Ycoordinate));
	 		  xcoordinate_text.setText(String.valueOf(Xcoordinate));
	 		  ycoordinate_text.setText(String.valueOf(Ycoordinate));
	 		  
	 		  
	 	}catch(Exception e1){
	 				
	 			JOptionPane.showMessageDialog(null, "No EV3 Connected","Connection Error", JOptionPane.ERROR_MESSAGE);
	 			e1.printStackTrace();
	 			}
		}
	}
	

	
	private void ParticleFilterResample()
	{
		  int[] measurement = new int[landmark.length];
		  Random random = new Random();
 
    
		for(int i =0;i<measurement.length;i++)
		{
			double dist = MathX.distance(Xcoordinate, Ycoordinate, landmark[i].getX(), landmark[i].getY());
		    measurement[i] = (int) (dist + (float)random.nextGaussian() * 5f);
		}
    	
    	  
    	  double forward = Math.sqrt(Math.pow(poseList.get(poseList.size()-1).getX() -600, 2) + Math.pow(poseList.get(poseList.size()-1).getY() - 405, 2));
	      double turn = 0;
	      if(angleList.size() == 1)
	      {
	    	  turn = 0;
	      }else if(angleList.get(angleList.size()-1) - angleList.get(angleList.size()-2) == 315)
	      {
	    	  turn = -45;
	      }else if(angleList.get(angleList.size()-1) - angleList.get(angleList.size()-2) == -315)
	      {
	    	  turn = 45;
	      }else
	      {
	    	  turn = angleList.get(angleList.size()-1) - angleList.get(angleList.size()-2);
	      }
	 
	    		  
    	  filter.move(turn, forward);
	      filter.resample(measurement);
	}
	
	private class LoadMapPaintListener implements PaintListener
	{
		@Override
		public void paintControl(PaintEvent arg0) {
		if(readFlag == true)
		{
			arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_RED));
			arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_RED));
			arg0.gc.drawOval(595,400,10,10);
			arg0.gc.fillOval(595,400,10,10);
			
			//Draw Free Points
		
			for(int i=0;i<dataStore.getFreePoint().size();i++)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_WHITE));
				arg0.gc.drawRectangle(dataStore.getFreePoint().get(i).getX(), dataStore.getFreePoint().get(i).getY(), 1, 1);
				arg0.gc.fillRectangle(dataStore.getFreePoint().get(i).getX(), dataStore.getFreePoint().get(i).getY(), 1, 1);
			}
			
			//Draw Occupied Points

			for(int i=0;i<dataStore.getOccupiedPoint().size();i++)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLACK));
				arg0.gc.drawRectangle(dataStore.getOccupiedPoint().get(i).getX(), dataStore.getOccupiedPoint().get(i).getY(), 1, 1);
				arg0.gc.fillRectangle(dataStore.getOccupiedPoint().get(i).getX(), dataStore.getOccupiedPoint().get(i).getY(), 1, 1);
			}
			
			for(int i=0;i<dataStore.getSweepList().size();i++)
			{
				int tempX = dataStore.getSweepList().get(i).getRobotPose().getX();
				int tempY = dataStore.getSweepList().get(i).getRobotPose().getY();
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_BLUE));
				arg0.gc.drawOval(tempX-5,tempY-5,10,10);
				//arg0.gc.fillOval(tempX-5,tempY-5,10,10);
			}
			
			//Draw the best particle 
			//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_GREEN));
			//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_GREEN));
			for(int i=0;i<dataStore.getFilterList().size();i++)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_YELLOW));
			    Particle p = dataStore.getFilterList().get(i).getBestParticle();
			    arg0.gc.drawRectangle((int) p.x -5, (int) p.y -5, 10, 10);
			    arg0.gc.fillRectangle((int) p.x -5, (int) p.y -5, 10, 10);
			}
			
			//Draw the average particle
			//arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
			//arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
			for(int i=0;i<dataStore.getFilterList().size();i++)
			{
				arg0.gc.setForeground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
				arg0.gc.setBackground(arg0.display.getSystemColor(SWT.COLOR_CYAN));
			    Particle p = dataStore.getFilterList().get(i).getAverageParticle();
			    arg0.gc.drawRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
			    arg0.gc.fillRectangle((int) p.x -5, (int) p.y - 5, 10, 10);
			}
		} 
	}
	}
}
