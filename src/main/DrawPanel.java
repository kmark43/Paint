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

public class DrawPanel extends JPanel
{
	final static long serialVersionUID = 214897289174L;
	
	private LayerManager layerManager = new LayerManager(this);
	
	// private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	
	private Color foreColor = Color.BLACK, backColor = Color.WHITE;
	
	private String filePath = "";
	
	private JPanel container;
	private JScrollPane scroll;
	
	private float zoom = 1;
	
	// private Tool currentTool;
	private Color current;
	private Point pos = new Point();
	
	private GUIManager gui;
	
	private DrawEvent drawEvent;
	private Shape clip;
	
	// private PanelKey keyListener;
	// private PanelMouse mouseListener;
	
	public DrawPanel(GUIManager gui, DrawEvent drawEvent)//JScrollPane scroll, JPanel container)
	{
		this.gui = gui;
		this.drawEvent = drawEvent;
		// this.scroll = scroll;
		// this.container = container;
		container = new JPanel(null);
		container.add(this);
		scroll = new JScrollPane(container);
		scroll.setPreferredSize(new Dimension(800, 600));
		
		// keyListener = new PanelKey(this, drawEvent, layerManager, keyToolMap);
		// mouseListener = new PanelMouse(this, keyListener, drawEvent, layerManager, scroll);
		
		// container.addMouseWheelListener(mouseListener);
		// addMouseListener(mouseListener);
		// addMouseMotionListener(mouseListener);
		// addKeyListener(keyListener);
	}
	
	public DrawPanel(GUIManager gui, DrawEvent drawEvent, int width, int height, FillType type)
	{
		this(gui, drawEvent);
		loadNew(width, height, type);
	}
	
	// public void setTool(Tool tool)
	// {
		// currentTool = tool;
	// }
	
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
	
	public void setZoom(float zoom)
	{
		if (layerManager.getLayer(0).getImage() != null)
		{
			setSize((int)(layerManager.getLayer(0).getImage().getWidth() * zoom), (int)(layerManager.getLayer(0).getImage().getHeight() * zoom));
			this.zoom = zoom;
			drawEvent.setZoom(zoom);
		}
	}
	
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
			drawEvent.setZoom(zoom);
		}
	}
	
	public float getZoom() { return zoom; }
	
	public void setPos(Point p) { this.pos = p; }
	public Point getPos() { return pos; }
	
	public Tool getCurrentTool() { return gui.getCurrentTool(); }
	
	public void setCurrent(Color current) { this.current = current; }
	public Color getCurrent() { return current; }
	
	public void setForeColor(Color c) { foreColor = c; }
	public void setBackColor(Color c) { backColor = c; }
	
	public Color getForeColor() { return foreColor; }
	public Color getBackColor() { return backColor; }
	
	// public HashMap<Integer, JToggleButton> getKeyToolMap() { return keyToolMap; }
	
	public DrawEvent getDrawEvent() { return drawEvent; }
	
	public LayerManager getLayerManager() { return layerManager; }
	
	public JScrollPane getScroll() { return scroll; }
	
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
}