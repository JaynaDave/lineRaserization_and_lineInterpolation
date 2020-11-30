//****************************************************************************
// SketchBase.  
//****************************************************************************
// Jayna Dave
// drawLine draws a straight line between two points wither with flat fill or color interpolation
// drawTriangle creates a triangle between three vertices with flat fill based in the color of the first point or with smooth fill when 's' key is pressed
// colorValues is a helper method for color interpolation

//Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	public static ColorType colorValues(Point2D p1, Point2D p2, Point2D current) {
		Point2D colorPoint = new Point2D();
		double p1_distance = Math.sqrt(Math.pow(p1.y - current.y, 2) + Math.pow(p1.x - current.x,2));
		double p2_distance = Math.sqrt(Math.pow(p2.y - current.y, 2) + Math.pow(p2.x - current.x,2));
		double total_distance = Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow(p2.x - p1.x,2));
		
		colorPoint.c.r = (float)(p2.c.r*(p1_distance/total_distance) + p1.c.r*(p2_distance/total_distance));
		colorPoint.c.b = (float)(p2.c.b*(p1_distance/total_distance) + p1.c.b*(p2_distance/total_distance));
		colorPoint.c.g  = (float)(p2.c.g*(p1_distance/total_distance) + p1.c.g*(p2_distance/total_distance));
		
		return colorPoint.c;
	}
	
	
	
	// draw a line segment
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
	{
		// replace the following line with your implementation
		

		
		//determine which is the leftmost pixel the following code will run from line being drawn left to right
		Point2D leftPoint = new Point2D();
		Point2D rightPoint = new Point2D();
		if((p1.x - p2.x) < 0) {
			leftPoint.x = p1.x;
			leftPoint.y = p1.y;
			rightPoint.x = p2.x;
			rightPoint.y = p2.y;
			  
		}else {
			leftPoint.x = p2.x;
			leftPoint.y = p2.y;
			rightPoint.x = p1.x;
			rightPoint.y = p1.y;
			
		}
		//System.out.println("(" + rightPoint.x + ", " + rightPoint.y + ")");
		//System.out.println("(" + leftPoint.x + ", " + leftPoint.y + ")");
		
		//point that will trace the line
		Point2D point = new Point2D();
		point.x = leftPoint.x;
		point.y = leftPoint.y;
		
		
		//undefined slope
		if(rightPoint.x - leftPoint.x == 0) {
			if(rightPoint.y - leftPoint.y >0) {
				point.y = leftPoint.y;
				while(point.y <= rightPoint.y) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.y++;
			}
			}else if(rightPoint.y - leftPoint.y <0){
				point.y= rightPoint.y;
				while(point.y <= leftPoint.y) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.y++;
			}
			}
			
		}else{
			float slope = (float)(rightPoint.y - leftPoint.y)/(rightPoint.x - leftPoint.x);
			//System.out.println(slope);
			if (slope< 1 && slope >=0) {
				int p_k = 2*(rightPoint.y - leftPoint.y) - (rightPoint.x - leftPoint.x);
				while (point.x <= rightPoint.x) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.x++;
					//p_k value that determines if we should round up or down based on where the lines passes through
					if(p_k < 0) {
						p_k = p_k + 2*(rightPoint.y - leftPoint.y);
					}else {
						p_k = p_k + 2*(rightPoint.y - leftPoint.y) - 2*(rightPoint.x - leftPoint.x);
						point.y++;
					}
				}
			
			}else if(slope >= 1) {
				int p_k = 2*(rightPoint.x - leftPoint.x)- (rightPoint.y - leftPoint.y);
				while (point.y <= rightPoint.y) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.y++;
					//p_k value that determines if we should round up or down based on where the lines passes through
					if(p_k < 0) {
						p_k = p_k + 2*(rightPoint.x - leftPoint.x);
					}else {
						p_k = p_k + 2*(rightPoint.x -leftPoint.x) - 2*(rightPoint.y - leftPoint.y);
						point.x++;
					}
				}
				
			}else if(slope <0 && slope >-1) {
				int p_k = 2*(leftPoint.y - rightPoint.y) - (rightPoint.x - leftPoint.x);
				while (point.x <= rightPoint.x) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.x++;
					//p_k value that determines if we should round up or down based on where the lines passes through
					if(p_k < 0) {
						p_k = p_k + 2*(leftPoint.y - rightPoint.y);
					}else {
						p_k = p_k + 2*(leftPoint.y - rightPoint.y) - 2*(rightPoint.x - leftPoint.x);
						point.y--;
					}
				
			}
			}
			else{
				
				int p_k = 2*(rightPoint.x - leftPoint.x) - (leftPoint.y - rightPoint.y);
				while (point.y >= rightPoint.y) {
					drawPoint(buff, new Point2D(point.x, point.y, colorValues(p1, p2, point)));
					point.y--;
					//p_k value that determines if we should round up or down based on where the lines passes through
					if(p_k < 0) {
						p_k = p_k + 2*(rightPoint.x - leftPoint.x);
					}else {
						p_k = p_k + 2*(rightPoint.x - leftPoint.x) - 2*(leftPoint.y - rightPoint.y);
						point.x++;
					}
				
			}
				
			
			}
		}
	}
	
	// draw a triangle
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		
		//System.out.println("(" + p1.x + "," + p1.y + ")");
		//System.out.println("(" + p2.x + "," + p2.y + ")");
		//System.out.println("(" + p3.x + "," + p3.y + ")");
		//System.out.println();

		

	
		//calculate the incenter of the triangle to then determine where the vertices in relation to that point
		double y_coord =((Math.sqrt(Math.pow(p2.x-p3.x, 2) + Math.pow(p2.y - p3.y, 2))*p1.y) + (Math.sqrt(Math.pow(p1.x-p3.x,2) + Math.pow(p1.y - p3.y,2))*p2.y) + (Math.sqrt(Math.pow(p1.x-p2.x,2) + Math.pow(p1.y - p2.y,2))*p3.y))/(Math.sqrt(Math.pow(p2.x-p3.x,2) + Math.pow(p2.y - p3.y,2))+ Math.sqrt(Math.pow(p1.x-p3.x,2) + Math.pow(p1.y - p3.y,2))+ Math.sqrt(Math.pow(p1.x-p2.x,2) + Math.pow(p1.y - p2.y,2)));
	
		
		//determine the distances between the vertices to the incenter y coordinate to determine where the divide the triangle
		double p1_distance = Math.abs(p1.y - y_coord);
		double p2_distance = Math.abs(p2.y - y_coord);
		double p3_distance = Math.abs(p3.y - y_coord);
		
		Point2D top = new Point2D();
		Point2D mid_left = new Point2D();
		Point2D mid_right = new Point2D();
		Point2D bottom = new Point2D();
		
		
		if(p2_distance <= p1_distance && p2_distance <= p3_distance) {
			mid_right.x = p2.x; mid_right.y = p2.y; mid_right.c = p2.c;
			//calculate x coordinate of intersecting line
			float intersect_x;
			if(p1.y - p3.y == 0) {intersect_x = p1.x;}else {intersect_x = ((p2.y - p1.y)*(p1.x-p3.x)/(p1.y - p3.y)) + p1.x;}
			int intersect = (int)(intersect_x);
					
		
			mid_left.y = p2.y; mid_left.x = intersect; mid_left.c = colorValues(p1, p3, mid_left);
			if(p1.y - p3.y > 0) {
				top.y = p1.y; top.x = p1.x; top.c = p1.c;
				bottom.y = p3.y; bottom.x = p3.x; bottom.c = p3.c; 
			}else {
				top.y = p3.y; top.x = p3.x; top.c = p3.c; 
				bottom.y = p1.y; bottom.x = p1.x; bottom.c = p1.c; 
			}
		}else if(p1_distance <= p2_distance && p1_distance <= p3_distance){
			mid_right.x = p1.x; mid_right.y = p1.y; mid_right.c= p1.c;
			//calculate x coordinate of intersecting line
			float intersect_x;
			if(p2.y - p3.y == 0) { intersect_x = p2.x; }else {intersect_x = ((p1.y - p2.y)*(p2.x-p3.x)/(p2.y - p3.y)) + p2.x;}
			int intersect = (int)(intersect_x);
			
			mid_left.y = p1.y; mid_left.x = intersect; mid_left.c = colorValues(p2, p3, mid_left);
			if(p2.y - p3.y > 0) {
				top.y = p2.y; top.x = p2.x; top.c = p2.c; 
				bottom.y = p3.y; bottom.x = p3.x; bottom.c = p3.c;
			}else {
				top.y = p3.y; top.x = p3.x; top.c = p3.c; 
				bottom.y = p2.y; bottom.x = p2.x; bottom.c = p2.c;
			}
			
		}else {
			mid_right.x = p3.x; mid_right.y = p3.y; mid_right.c = p3.c;
			//calculate x coordinate of intersecting line
			float intersect_x;
			if(p2.y - p1.y == 0) { intersect_x = p3.x; }else {intersect_x = ((p3.y - p2.y)*(p2.x-p1.x)/(p2.y - p1.y)) + p2.x;}
			int intersect = (int)(intersect_x);
			
			mid_left.y = p3.y; mid_left.x = intersect; mid_left.c = colorValues(p1, p2, mid_left);
			if(p2.y - p1.y > 0) {
				top.y = p2.y; top.x = p2.x; top.c = p2.c;
				bottom.y = p1.y; bottom.x = p1.x; bottom.c = p1.c; 
			}else {
				top.y = p1.y; top.x = p1.x; top.c = p1.c; 
				bottom.y = p2.y; bottom.x = p2.x; bottom.c = p2.c;
			}
		}
		
		
		//System.out.println("top "+ "(" + top.x + "," + top.y + ")");
		//System.out.println("mid " + "(" + mid_right.x + "," + mid_right.y + ")");
		//System.out.println("mid " + "(" + mid_left.x + "," + mid_left.y + ")");
		//System.out.println("bottom " + "(" + bottom.x + "," + bottom.y + ")");
		//System.out.println();



		
		//top point flat bottom triangle	
		
		//initialize values that will trace the sides of the triangle to fill it in
		Point2D trace1 = new Point2D(top.x, top.y, top.c);
		Point2D trace2 = new Point2D(top.x, top.y, top.c);
	
		
		
		float slope1;//calculate inverses of slopes to determine the next side point to trace to 
		if((top.y - mid_right.y) == 0) {slope1 = 0;}else {slope1 = (float)(top.x - mid_right.x)/(top.y - mid_right.y);}
		float slope2;
		if(top.y - mid_left.y == 0) {slope2 = 0;}else {slope2 = (float)(top.x - mid_left.x)/(top.y - mid_left.y);}
		for(int i = 0; i <=(top.y - mid_right.y); i++) {
			
			//check the do_smooth boolean
			if(do_smooth) {trace1.c = colorValues(top, mid_right, trace1); trace2.c = colorValues(top, mid_left, trace2);}
			else {trace1.c = p1.c; trace2.c = p1.c; }
			
			drawLine(buff, trace1, trace2);
			trace1.y--;
			trace1.x = Math.round((trace1.y - top.y)*slope1 + top.x);
			trace2.y--;
			trace2.x = Math.round((trace2.y - top.y)*slope2 + top.x);
			
			
		}
		
		//flat top, point bottom triangle
		
		//initialize values that will trace the sides of the triangle to fill it in
		Point2D trace3 = new Point2D(bottom.x, bottom.y, bottom.c);
		Point2D trace4 = new Point2D(bottom.x, bottom.y, bottom.c);
		
		
		float slope_left;//calculate inverses of slopes to determine the next side point to trace to 
		if(mid_right.y - bottom.y == 0){slope_left = 0;}else {slope_left = (float)(mid_right.x - bottom.x)/(mid_right.y - bottom.y);}
		float slope_right;
		if(mid_left.y - bottom.y == 0){slope_right = 0;}else {slope_right = (float)(mid_left.x - bottom.x)/(mid_left.y - bottom.y);}
		for(int i = 0; i <=(mid_right.y - bottom.y); i++) {
			
			
			//check do_smooth boolean
			if(do_smooth) {trace3.c = colorValues(bottom, mid_right, trace3); trace4.c = colorValues(bottom, mid_left, trace4);}
			else {trace3.c = p1.c; trace4.c = p1.c; }
			
		
			
			drawLine(buff, trace3, trace4);
			trace3.y++;
			trace3.x = Math.round((trace3.y - bottom.y)*slope_left + bottom.x);
			trace4.y++;
			trace4.x = Math.round((trace4.y - bottom.y)*slope_right + bottom.x);
			
			
		}
		
	}
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawPoint(buff, p3);
	}
}
