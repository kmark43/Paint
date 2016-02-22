package main.event;

import main.GUIManager;
import main.DrawPanel;
import main.tool.*;
import main.layer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelMouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private GUIManager manager;
	private DrawEvent drawEvent;
	private JScrollPane scroll;
	
	private PanelKey keyListener;
	
	public PanelMouse(GUIManager manager, PanelKey panelKey, DrawEvent e)//, LayerManager manager.getDrawPane().getLayerManager(), JScrollPane scroll)
	{
		this.manager = manager;
		this.keyListener = panelKey;
		drawEvent = e;
		this.scroll = scroll;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (keyListener.isPhraseActive())
		{
			switch(e.getButton())
			{
				case 1:
					System.out.println("Accept");
					keyListener.clearPhrase();
					break;
				case 3:
					System.out.println("Cancel");
					keyListener.clearPhrase();
					break;
			}
			manager.getDrawPane().setCurrent(null);
			return;
		}
		if (manager.getDrawPane().getLayerManager().getCurrentLayer() == null || !manager.getDrawPane().getLayerManager().getCurrentLayer().isVisible()) return;
		manager.getDrawPane().getLayerManager().addHistory();
		drawEvent.setTempG(manager.getDrawPane().getLayerManager());
		drawEvent.setZoom(manager.getDrawPane().getZoom());
		switch(e.getButton())
		{
			case 1:
				manager.getDrawPane().setCurrent(manager.getDrawPane().getForeColor());
				break;
			case 3:
				manager.getDrawPane().setCurrent(manager.getDrawPane().getBackColor());
				break;
			default:
				manager.getDrawPane().setCurrent(null);
				return;
		}
		if (drawEvent.getGraphics() != null)
			drawEvent.getGraphics().dispose();
		Graphics2D g = (Graphics2D)manager.getDrawPane().getLayerManager().getCurrentLayer().getImage().getGraphics();
		drawEvent.setGraphics(g);
		drawEvent.init(e);
		drawEvent.setColor(manager.getDrawPane().getCurrent());
		manager.getCurrentTool().mouseDown(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point mouse = manager.getDrawPane().getPos();
		mouse.x = e.getX();
		mouse.y = e.getY();
		manager.getDrawPane().setPos(mouse);
		if (manager.getDrawPane().getLayerManager().getCurrentLayer() == null || !manager.getDrawPane().getLayerManager().getCurrentLayer().isVisible() || manager.getDrawPane().getCurrent() == null) return;
		drawEvent.init(e);
		manager.getCurrentTool().mouseDrag(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (manager.getDrawPane().getLayerManager().getCurrentLayer() == null || !manager.getDrawPane().getLayerManager().getCurrentLayer().isVisible() || manager.getDrawPane().getCurrent() == null) return;
		drawEvent.init(e);
		manager.getCurrentTool().mouseUp(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point mouse = manager.getDrawPane().getPos();
		int dx = e.getX() - mouse.x;
		int dy = e.getY() - mouse.y;
		mouse.x = e.getX();
		mouse.y = e.getY();
		manager.getDrawPane().setPos(mouse);
		if (keyListener.isPhraseActive())
		{
			
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int dr = e.getWheelRotation();	// 1 down, -1 up
		if (e.isControlDown())
		{
			float zoom = manager.getDrawPane().getZoom() * (float)Math.pow(.99, dr);
			// setZoom(zoom);
			manager.getDrawPane().setZoom(zoom, e.getX(), e.getY());
		}
		// else
			// scroll.dispatchEvent(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}