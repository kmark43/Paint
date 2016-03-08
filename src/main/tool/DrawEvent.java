package main.tool;

import main.GUIManager;
import main.layer.LayerManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
* This class is to store information relevent to drawing to the screen
*/

public class DrawEvent
{
	private MouseEvent mouseEvent;
	private KeyEvent keyEvent;
	private Graphics2D g;
	private Graphics2D temp;
	private float invzoom;
	private int x;
	private int y;
	private Color foreColor, backColor;
	private GUIManager manager;
	private Area clippingRegion = new Area();
	
	public DrawEvent(GUIManager manager)
	{
		this.manager = manager;
		invzoom = 1;
	}
	
	/**
	* @param e The MouseEvent used to determine mouse info for drawing
	* @param g The main graphics object which draws to the image
	* @param tempG The temporary graphics object to draw directly to the canvas
	* @param zoom The zoom ratio of the image
	* @param manager The graphics manager
	*/
	public DrawEvent(MouseEvent e, Graphics2D g, Graphics2D tempG, float zoom, GUIManager manager)
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
	* @param e1 The MouseEvent used to determine mouse info for drawing
	* @param g The main graphics object which draws to the image
	* @param tempG The temporary graphics object to draw directly to the canvas
	* @param zoom The zoom ratio of the image
	* @param x The initial x coordinates
	* @param y The initial y coordinates
	* @param manager The graphics manager
	*/
	public DrawEvent(KeyEvent e1, Graphics2D g, Graphics2D tempG, float zoom, int x, int y, GUIManager manager)
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
	* Updates variables for a MouseEvent
	* @param e The MouseEvent
	*/
	public void init(MouseEvent e)
	{
		mouseEvent = e;
		keyEvent = null;
		x = e.getX();
		y = e.getY();
		clearTemp();
	}
	
	/**
	* Updates variables for a KeyEvent
	* @param e1 The KeyEvent
	* @param p The point the mouse is currently at
	*/
	public void init(KeyEvent e1, Point p)
	{
		keyEvent = e1;
		mouseEvent = null;
		this.x = p.x;
		this.y = p.y;
		clearTemp();
	}
	
	/**
	* Assigns the clip to both graphics objects
	* @param clip The clipping region
	*/
	public void setClip(Area clip)
	{
		clippingRegion = clip;
		g.setClip(clip);
		temp.setClip(clip);
	}
	
	
	/**
	* Removes the clip from both graphics objects
	*/
	public void clearClip()
	{
		clippingRegion.reset();
		g.setClip(null);
		temp.setClip(null);
	}
	
	/**
	* Clears the temporary image
	* Use for each draw call
	*/
	public void clearTemp()
	{
		temp.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		temp.setColor(new Color(0, 0, 0, 0));
		temp.fillRect(0, 0, getManager().getTemp().getWidth(), getManager().getTemp().getHeight());
		temp.setColor(g.getColor());
		temp.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}
	
	/**
	* Resets the temporary graphics object
	* @param manager The layer manager to access temp from
	*/
	public void setTempG(LayerManager manager) { temp = (Graphics2D)manager.getTemp().getGraphics(); }
	
	/**
	* Updates zoom for x and y positions
	* @param zoom The zoom of the draw panel
	*/
	public void setZoom(float zoom) { this.invzoom = 1 / zoom; }
	
	/**
	* Sets the graphics object and copies previous graphics object properties
	* @param gr The new graphics object being passed in
	*/
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
	
	private int invZoom(int val) { return (int)(val * invzoom); }
	
	/**
	* @return The x position of the mouse relative to image
	*/
	public int getX() { return (int)(x * invzoom); }
	
	/**
	* @return The y position of the mouse relative to image
	*/
	public int getY() { return (int)(y * invzoom); }
	
	/**
	* @return control key state
	*/
	public boolean isControlDown() { return mouseEvent.isControlDown(); }
	
	/**
	* @return alt key state
	*/
	public boolean isAltDown() { return mouseEvent.isAltDown(); }
	
	/**
	* @return shift key state
	*/
	public boolean isShiftDown() { return mouseEvent.isShiftDown(); }
	
	/**
	* @return The MouseEvent information
	*/
	public MouseEvent getMouseEvent() { return mouseEvent; }
	
	/**
	* @return The KeyEvent information
	*/
	public KeyEvent getKeyEvent() { return keyEvent; }
	
	/**
	* @return The graphics object that draws to the image
	*/
	public Graphics2D getGraphics() { return g; }
	
	/**
	* @return The temporary graphics object
	*/
	public Graphics2D getTempG() { return temp; }
	
	/**
	* @return The layer manager for the current draw panel
	*/
	public LayerManager getManager() { return manager.getLayerManager(); }
	
	// public void setManager(LayerManager layerManager) { manager = layerManager; }
	
	/**
	* @return The clipping region expressed as an area
	*/
	public Area getArea() { return clippingRegion; }
	
	/**
	* Disposes both graphics objects
	*/
	public void dispose()
	{
		g.dispose();
		temp.dispose();
	}
	
	/**
	* Sets the color of both graphics objects
	* @param c The color the graphics object should be
	*/
	public void setColor(Color c)
	{
		g.setColor(c);
		temp.setColor(c);
	}
	
	/**
	* Sets the stroke of both graphics objects
	* @param stroke The new stroke of the graphics object
	*/
	public void setStroke(Stroke stroke)
	{
		g.setStroke(stroke);
		temp.setStroke(stroke);
	}
	
	/**
	* @return the fore color of the GUIManager
	*/
	public Color getForeColor() { return manager.getDrawPane().getForeColor(); }
	
	/**
	* @return the background color of the GUIManager
	*/
	public Color getBackColor() { return manager.getDrawPane().getBackColor(); }
}