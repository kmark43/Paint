package main.tool;

import java.awt.*;
import java.awt.event.*;

/**
* This class is to store information relevent to drawing to the screen
*/

public class DrawEvent
{
	/**
	* Stores the mouse configuration at the point the event is created
	*/
	private MouseEvent mouseEvent;
	
	/**
	* Used to draw directly to the current layer, 
	*/
	private Graphics2D g;
	
	/**
	* Stores the temporary graphics object used to draw objects which will be overwritten later, for example a rescalable rectangle
	*/
	private Graphics2D temp;
	
	/**
	* @param e The MouseEvent used to determine mouse info for drawing
	* @param g The main graphics object which draws to the image
	* @param tempG The temporary graphics object to draw directly to the canvas
	*/
	public DrawEvent(MouseEvent e, Graphics2D g, Graphics2D tempG)
	{
		this.g = g;
		mouseEvent = e;
		temp = tempG;
	}
	
	/**
	* Returns the x coordinate of the mouse
	*/
	public int getX() { return mouseEvent.getX(); }
	
	/**
	* Returns the y coordinate of the mouse
	*/
	public int getY() { return mouseEvent.getY(); }
	
	/**
	* Determines if the control modifyer is being used
	*/
	public boolean isControlDown() { return mouseEvent.isControlDown(); }
	
	/**
	* Determines if the alt modifyer is being used
	*/
	public boolean isAltDown() { return mouseEvent.isAltDown(); }
	
	/**
	* Determines if the shift modifyer is being used
	*/
	public boolean isShiftDown() { return mouseEvent.isShiftDown(); }
	
	/**
	* Returns the MouseEvent to get extra information about the mouse status
	*/
	public MouseEvent getEvent() { return mouseEvent; }
	
	/**
	* Returns the graphics object that draws to the image
	*/
	public Graphics2D getGraphics() { return g; }
	
	/**
	* Returns the graphics object that draws to the panel
	* and is overwritten with each draw call
	*/
	public Graphics2D getTempG() { return temp; }
}