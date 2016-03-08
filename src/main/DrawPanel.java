package main;

import main.GUIManager;
import main.dialog.*;
import main.tool.*;
import main.filter.*;
import main.layer.*;
import main.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
* The panel that manages the current image for manipulation and viewing
*/
public class DrawPanel extends JPanel
{
	final static long serialVersionUID = 214897289174L;
	
	private LayerManager layerManager = new LayerManager(this);
	
	private Color foreColor = Color.BLACK, backColor = Color.WHITE;
	
	private String filePath = "";
	
	private JPanel container;
	private JScrollPane scroll;
	
	private float zoom = 1;
	
	private Color current;
	
	private Point pos;
	
	private GUIManager gui;
	
	private DrawEvent drawEvent;
	private Shape clip;
	
	/**
	* Constructs the default draw panel
	* @param gui The GUIManager for reference
	* @param drawEvent The draw event for tool management
	*/
	public DrawPanel(GUIManager gui, DrawEvent drawEvent)
	{
		this.gui = gui;
		this.drawEvent = drawEvent;
		container = new JPanel(null);
		container.add(this);
		scroll = new JScrollPane(container);
		scroll.setPreferredSize(new Dimension(800, 600));
	}
	
	/**
	* Constructs a draw panel with an image from a file
	* @param gui The GUIManager for reference
	* @param drawEvent The draw event for tool management
	* @param file The file to open
	*/
	public DrawPanel(GUIManager gui, DrawEvent drawEvent, File file)
	{
		this(gui, drawEvent);
		ImageLoader.open(file, this, layerManager);
	}
	
	/**
	* Constructs a blank draw panel
	* @param gui The GUIManager for reference
	* @param drawEvent The draw event for tool management
	* @param width The width of the image
	* @param height The height of the image
	* @param type The filling rule for this instance
	*/
	public DrawPanel(GUIManager gui, DrawEvent drawEvent, int width, int height, FillType type)
	{
		this(gui, drawEvent);
		loadNew(width, height, type);
	}
	
	/**
	* Loads a blank image
	* @param width The width of the new image
	* @param height The height of the new image
	* @param type The mode for filling the image
	*/
	public void loadNew(int width, int height, FillType type) { ImageLoader.loadNew(width, height, type, this, layerManager); }
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		container.setPreferredSize(new Dimension(width, height));
		layerManager.setTemp(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		if (drawEvent.getTempG() != null)
			drawEvent.getTempG().dispose();
		drawEvent.setTempG(layerManager);
		scroll.revalidate();
	}
	
	/**
	* Sets the zoom of the image
	* @param zoom The percentage zoom
	*/
	public void setZoom(float zoom)
	{
		if (layerManager.getLayer(0).getImage() != null)
		{
			setSize((int)(layerManager.getLayer(0).getImage().getWidth() * zoom), (int)(layerManager.getLayer(0).getImage().getHeight() * zoom));
			this.zoom = zoom;
		}
	}
	
	/**
	* Sets the zoom of the image around a focal point
	* @param zoom The percentage zoom
	* @param focusX The x focus point
	* @param focusY The y focus point
	*/
	public void setZoom(float zoom, int focusX, int focusY)
	{
		if (layerManager.getLayer(0).getImage() != null)
		{
			float dzoom = zoom - this.zoom + 1;
			setSize((int)(layerManager.getLayer(0).getImage().getWidth() * zoom), (int)(layerManager.getLayer(0).getImage().getHeight() * zoom));
			Rectangle view = scroll.getViewport().getViewRect();
			view.x = (int)(view.x * dzoom);
			view.y = (int)(view.y * dzoom);
			scroll.getViewport().scrollRectToVisible(view);
			this.zoom = zoom;
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.gray);
		for (int i = 0; i < getWidth(); i += 10)
			for (int j = i % 20; j < getHeight(); j += 20)
				g.fillRect(i, j, 10, 10);
		g.setColor(Color.lightGray);
		for (int i = 0; i < getWidth(); i += 10)
			for (int j = 10 - (i % 20); j < getHeight(); j += 20)
				g.fillRect(i, j, 10, 10);
		
		layerManager.draw(g, zoom);
		
		if (drawEvent.getGraphics() != null && drawEvent.getGraphics().getClip() != null)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.scale(zoom, zoom);
			g2.setColor(Color.blue);
			Shape outline = drawEvent.getGraphics().getClip();
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
			g2.draw(outline);
		}
	}
	
	/**
	* Gives the zoom of the panel and image
	* @return the zoom for the panel
	*/
	public float getZoom() 					{ return zoom; }
	
	/**
	* Gives the current tool being used
	* @return current tool
	*/
	public Tool getCurrentTool() 			{ return gui.getCurrentTool(); }
	
	/**
	* Sets the current color for graphics
	* @param current The color to assign to graphics
	*/
	public void setCurrent(Color current) 	{ this.current = current; }
	
	/**
	* Retrieves the current graphics color
	* @return graphics color
	*/
	public Color getCurrent() 				{ return current; }
	
	/**
	* Assigns the forecolor for draw panel reference
	* @param c The color to assign to forecolor
	*/
	public void setForeColor(Color c) 		{ foreColor = c; }
	
	/**
	* Assigns the backcolor for draw panel reference
	* @param c The color to assign to backcolor
	*/
	public void setBackColor(Color c) 		{ backColor = c; }
	
	/**
	* Retrieves the forecolor
	* @return the forecolor
	*/
	public Color getForeColor() 			{ return foreColor; }
	
	/**
	* Retrieves the backcolor
	* @return the backcolor
	*/
	public Color getBackColor() 			{ return backColor; }
	
	/**
	* Retrieves the drawing event
	* @return draw event
	*/
	public DrawEvent getDrawEvent() 		{ return drawEvent; }
	
	public LayerManager getLayerManager() 	{ return layerManager; }

	public Point getPos()       { return pos; }
	public void setPos(Point p) { pos = p; }
	
	/**
	* Retrieves the scroll pane wrapper
	* @return the scroll pane
	*/
	public JScrollPane getScroll() 			{ return scroll; }
	
	/**
	* Returns the boundaries of parents
	* @return The boundaries of the parent wrappers
	*/
	public Rectangle getMaxBounds() 		{ return gui.getDrawPanels().getBounds(); }
	
	/**
	* Gets the expected file path
	* @return the expected image file path
	*/
	public String getPath() 				{ return filePath; }
	
	/**
	* Sets the expected file path
	* @param path The path to create
	*/
	public void setPath(String path) 		{ this.filePath = path; }
	
	/**
	* Clears the file path
	*/
	public void clearPath() 				{ this.filePath = ""; }
}