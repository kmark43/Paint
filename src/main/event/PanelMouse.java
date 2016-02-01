package main.event;

import main.DrawPanel;
import main.tool.*;
import main.layer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelMouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private DrawPanel drawPanel;
	private DrawEvent drawEvent;
	private LayerManager layerManager;
	private JScrollPane scroll;
	
	public PanelMouse(DrawPanel drawPanel, DrawEvent e, LayerManager layerManager, JScrollPane scroll)
	{
		this.drawPanel = drawPanel;
		drawEvent = e;
		this.layerManager = layerManager;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (drawPanel.isPhraseActive())
		{
			switch(e.getButton())
			{
				case 1:
					System.out.println("Accept");
					drawPanel.clearPhrase();
					break;
				case 3:
					System.out.println("Cancel");
					drawPanel.clearPhrase();
					break;
			}
			drawPanel.setCurrent(null);
			return;
		}
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible()) return;
		drawEvent.setTempG();
		switch(e.getButton())
		{
			case 1:
				drawPanel.setCurrent(drawPanel.getForeColor());
				break;
			case 3:
				drawPanel.setCurrent(drawPanel.getBackColor());
				break;
			default:
				drawPanel.setCurrent(null);
				return;
		}
		if (drawEvent.getGraphics() != null)
			drawEvent.getGraphics().dispose();
		Graphics2D g = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
		drawEvent.setGraphics(g);
		drawEvent.init(e);
		drawEvent.setColor(drawPanel.getCurrent());
		drawPanel.getCurrentTool().mouseDown(drawEvent);
		drawPanel.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point mouse = drawPanel.getPos();
		mouse.x = e.getX();
		mouse.y = e.getY();
		drawPanel.setPos(mouse);
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || drawPanel.getCurrent() == null) return;
		drawEvent.init(e);
		drawPanel.getCurrentTool().mouseDrag(drawEvent);
		drawPanel.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || drawPanel.getCurrent() == null) return;
		drawEvent.init(e);
		drawPanel.getCurrentTool().mouseUp(drawEvent);
		drawPanel.repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point mouse = drawPanel.getPos();
		int dx = e.getX() - mouse.x;
		int dy = e.getY() - mouse.y;
		mouse.x = e.getX();
		mouse.y = e.getY();
		drawPanel.setPos(mouse);
		if (drawPanel.isPhraseActive())
		{
			
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int dr = e.getWheelRotation();	// 1 down, -1 up
		if (e.isControlDown())
		{
			float zoom = drawPanel.getZoom() * (float)Math.pow(.99, dr);
			// setZoom(zoom);
			drawPanel.setZoom(zoom, e.getX(), e.getY());
		}
		else
			scroll.dispatchEvent(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
}