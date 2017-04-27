package windowBuilder.common;

import java.util.ArrayList;

import robotBasic.Point;

public class BresenhamLine {
	
	public ArrayList<Point> PointArray = new ArrayList<Point>();

	public void BresenhamCalculation(int x0, int y0, int x1, int y1)
	{
		//Modify the Coordinates
		/*
		x0 = ModifyValue(x0);
		y0 = ModifyValue(y0);
		x1 = ModifyValue(x1);
		y1 = ModifyValue(y1); */
				
		int d = 0;
		int dy = Math.abs(y1 - y0);
		int dx = Math.abs(x1 - x0);
		
		int dy2 = (dy << 1); //Initial Value:1
		int dx2 = (dx << 1); //Initial Value:1
		
		int ix = x0 < x1 ? 1 : -1; //Initial Value:1
		int iy = y0 < y1 ? 1 : -1; //Initial Value:1
		
		if(dy <= dx)
		{
			for (;;) 
			{
                PointArray.add(new Point(x0,y0));
                if (x0 == x1)
                    break;
                x0 += ix;
                d += dy2;
                if (d > dx) 
                {
                    y0 += iy;
                    d -= dx2;
                }
            }
		}else {
         for (;;) 
         {
    
            PointArray.add(new Point(x0,y0));
            if (y0 == y1)
               break;
            y0 += iy;
            d += dx2;
            if (d > dy) 
            {
               x0 += ix;
               d -= dy2;
            }
          }
        }
		
	}
	
	public int ModifyValue(int number)
	{
		int value = 0;
		if(number % 5 < 3)
		{
			value = number - number%5;
		}else if(number%5 > 2)
		{
			value = number + 5 - number%5; 
		}
		
		return value;
		
	}
	
	public ArrayList<Point> getPointArray()
	{
		
		
		return PointArray;
	}
	
	public void displayArray()
	{
		System.out.println("The Coordinate");
		for (int i=0;i<PointArray.size();i++)
		{
			System.out.println(PointArray.get(i).getX() + "\t" + PointArray.get(i).getY());
		}
	}
	
	//On the following is one test method of the class
	
	public static void main(String arg0[])
	{
		BresenhamLine bre = new BresenhamLine();
		bre.BresenhamCalculation(12, 13, 14, 15);
		bre.displayArray();
		System.out.println(bre.getPointArray().size());
	} 
}
