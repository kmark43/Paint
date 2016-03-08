package main.event;

import main.GUIManager;
import main.DrawPanel;
import main.tool.*;
import main.layer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* Manages mouse listeners for drawing panel
*/
public class PanelMouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private GUIManager manager;
	private DrawEvent drawEvent;
	private JScrollPane scroll;
	
	private PanelKey keyListener;
	
	/**
	* Constructs a mouse listener
	* @param manager The manager to reference the GUI
	* @param panelKey The key panel
	* @param e The draw event to reference when an event happens
	*/
	public PanelMouse(GUIManager manager, PanelKey panelKey, DrawEvent e)
	{
		this.manager = manager;
		this.keyListener = panelKey;
		drawEvent = e;
		this.scroll = scroll;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		DrawPanel pane = manager.getDrawPane();
		LayerManager layerManager = pane.getLayerManager();
		Layer currentLayer = layerManager.getCurrentLayer();
		if (currentLayer == null || !currentLayer.isVisible()) return;
		layerManager.addHistory();
		drawEvent.setZoom(pane.getZoom());
		drawEvent.setTempG(layerManager);
		switch(e.getButton())
		{
			case 1:
				pane.setCurrent(pane.getForeColor());
				break;
			case 3:
				pane.setCurrent(pane.getBackColor());
				break;
			default:
				pane.setCurrent(null);
				return;
		}
		if (drawEvent.getGraphics() != null)
			drawEvent.getGraphics().dispose();
		Graphics2D g = currentLayer.getImage().createGraphics();
		drawEvent.setGraphics(g);
		drawEvent.init(e);
		drawEvent.setStroke(new BasicStroke(1, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
		drawEvent.setColor(pane.getCurrent());
		manager.getCurrentTool().mouseDown(drawEvent);
		pane.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		DrawPanel pane = manager.getDrawPane();
		LayerManager layerManager = pane.getLayerManager();
		Layer currentLayer = layerManager.getCurrentLayer();
		Point mouse = pane.getPos();
		mouse.x = e.getX();
		mouse.y = e.getY();
		pane.setPos(mouse);
		if (currentLayer == null || !currentLayer.isVisible() || pane.getCurrent() == null) return;
		drawEvent.init(e);
		manager.getCurrentTool().mouseDrag(drawEvent);
		pane.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		DrawPanel pane = manager.getDrawPane();
		Layer currentLayer = pane.getLayerManager().getCurrentLayer();
		if (currentLayer == null || !currentLayer.isVisible() || pane.getCurrent() == null) return;
		drawEvent.init(e);
		manager.getCurrentTool().mouseUp(drawEvent);
		pane.repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		DrawPanel pane = manager.getDrawPane();
		Layer currentLayer = pane.getLayerManager().getCurrentLayer();
		Point mouse = manager.getDrawPane().getPos();
		int dx = e.getX() - mouse.x;
		int dy = e.getY() - mouse.y;
		mouse.x = e.getX();
		mouse.y = e.getY();
		manager.getDrawPane().setPos(mouse);
		if (keyListener.isPhraseActive())
		{
			
		}
		Graphics2D g = currentLayer.getImage().createGraphics();
		drawEvent.setGraphics(g);
		drawEvent.init(e);
		drawEvent.setStroke(new BasicStroke(1, BasicStroke.JOIN_ROUND, BasicStroke.CAP_ROUND));
		drawEvent.setColor(pane.getCurrent());
		if (manager.getCurrentTool().mouseMove(drawEvent))
			pane.repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int dr = e.getWheelRotation();	// 1 down, -1 up
		if (e.isControlDown())
		{
			float zoom = manager.getDrawPane().getZoom() * (float)Math.pow(.99, dr);
			manager.getDrawPane().setZoom(zoom, e.getX(), e.getY());
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}