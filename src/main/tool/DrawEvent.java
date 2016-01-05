package main.tool;

import main.layer.LayerManager;

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
	* Stores the key configuration at the event creation point
	*/
	private KeyEvent keyEvent;
	
	/**
	* Used to draw directly to the current layer, 
	*/
	private Graphics2D g;
	
	/**
	* Stores the temporary graphics object used to draw objects which will be overwritten later, for example a rescalable rectangle
	*/
	private Graphics2D temp;
	
	/**
	* Stores the amount to scale the image and points drawn to image by
	*/
	private float invzoom;
	
	private int x;
	
	private int y;
	
	private LayerManager manager;
	
	/**
	* @param e The MouseEvent used to determine mouse info for drawing
	* @param g The main graphics object which draws to the image
	* @param tempG The temporary graphics object to draw directly to the canvas
	* @param zoom The zoom ratio of the image
	*/
	public DrawEvent(MouseEvent e, Graphics2D g, Graphics2D tempG, float zoom, LayerManager manager)
	{
		this.g = g;
		mouseEvent = e;
		temp = tempG;
		x = e.getX();
		y = e.getY();
		this.invzoom =  1 / zoom;
		this.manager = manager;
	}
	
	/**
	* @param e The MouseEvent used to determine mouse info for drawing
	* @param g The main graphics object which draws to the image
	* @param tempG The temporary graphics object to draw directly to the canvas
	* @param zoom The zoom ratio of the image
	*/
	public DrawEvent(KeyEvent e1, Graphics2D g, Graphics2D tempG, float zoom, int x, int y, LayerManager manager)
	{
		this.g = g;
		keyEvent = e1;
		temp = tempG;
		this.invzoom =  1 / zoom;
		this.x = x;
		this.y = y;
		this.manager = manager;
	}
	
	/**
	* Returns the x coordinate of the mouse
	*/
	public int getX() { return (int)(x * invzoom); }
	
	/**
	* Returns the y coordinate of the mouse
	*/
	public int getY() { return (int)(y * invzoom); }
	
	public int invZoom(int val) { return (int)(val * invzoom); }
	
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
	public MouseEvent getMouseEvent() { return mouseEvent; }
	
	/**
	* Returns the KeyEvent to get the key pressed and alternate information
	*/
	public KeyEvent getKeyEvent() { return keyEvent; }
	
	/**
	* Returns the graphics object that draws to the image
	*/
	public Graphics2D getGraphics() { return g; }
	
	/**
	* Returns the graphics object that draws to the panel
	* and is overwritten with each draw call
	*/
	public Graphics2D getTempG() { return temp; }
	
	public LayerManager getManager() { return manager; }
	
	/**
	* The dispose method to dispose of the graphics objects
	*/
	public void dispose()
	{
		g.dispose();
		temp.dispose();
	}
	
	/**
	* The set color method to set the color of each graphics object
	*/
	public void setColor(Color c)
	{
		g.setColor(c);
		temp.setColor(c);
	}
}