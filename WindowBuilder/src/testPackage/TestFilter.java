package testPackage;

import java.util.ArrayList;
import java.util.HashSet;

import robotBasic.Point;

public class TestFilter {
	
	public static void main(String[] args)
	{
      ArrayList<Point> list = new ArrayList<Point>();
      
      Point point1 = new Point(233,700);
      Point point2 = new Point(12,23);
      Point point3 = new Point(233,700);
      list.add(point1);
      list.add(point2);
      list.add(point3);
      
      System.out.println("student1 == student2: " + (point1 == point2));
      System.out.println("student1.equals(student2): " + (point1.equals(point2)));
      System.out.println("student2.equals(student3): " + (point1.equals(point3)));
      
      HashSet<Point> hashSet = new HashSet<Point>(list);

      ArrayList<Point> arrayList2 = new ArrayList<Point>(hashSet);

      for(int i=0;i<arrayList2.size();i++)
      {
    	  System.out.println(arrayList2.get(i).getX() + "\t" + arrayList2.get(i).getY());
    	  System.out.println(arrayList2.size());
      }

      
    }
	

}
