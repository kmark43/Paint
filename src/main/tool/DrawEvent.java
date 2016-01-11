package main.tool;

import main.layer.LayerManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

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
	
	private Area clippingRegion = new Area();
	
	public DrawEvent(LayerManager manager)
	{
		this.manager = manager;
		invzoom = 1;
	}
	
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
	
	public void init(MouseEvent e)
	{
		mouseEvent = e;
		keyEvent = null;
		x = e.getX();
		y = e.getY();
		clearTemp();
	}
	
	public void init(KeyEvent e1, int x, int y)
	{
		keyEvent = e1;
		mouseEvent = null;
		this.x = x;
		this.y = y;
		this.manager = manager;
		clearTemp();
	}
	
	public void setClip(Area clip)
	{
		clippingRegion = clip;
		g.setClip(clip);
		temp.setClip(clip);
	}
	
	public void clearTemp()
	{
		temp.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		temp.setColor(new Color(0, 0, 0, 0));
		temp.fillRect(0, 0, manager.getTemp().getWidth(), manager.getTemp().getHeight());
		temp.setColor(g.getColor());
		temp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	
	public void setTempG() { temp = (Graphics2D)manager.getTemp().getGraphics(); }
	public void setZoom(float zoom) { this.invzoom = 1 / zoom; }
	
	public void setGraphics(Graphics2D gr)
	{
		if (g != null)
		{
			gr.setClip(g.getClip());
			gr.setColor(g.getColor());
			gr.setStroke(g.getStroke());
			gr.setComposite(g.getComposite());
			g.dispose();
		}
		g = gr;
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
	
	public Area getArea() { return clippingRegion; }
	
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
	
	public void setStroke(Stroke stroke)
	{
		g.setStroke(stroke);
		temp.setStroke(stroke);
	}
}