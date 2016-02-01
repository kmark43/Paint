package main.tool;

import main.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class FreeSelect extends Tool
{
	private DrawPanel main;
	
	private int lastX, lastY;
	private Polygon polygon = new Polygon();
	
	public FreeSelect(DrawPanel main)
	{
		this.main = main;
	}
	
	public void drawClip(Graphics2D g)
	{
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}
	
	public void mouseDown(DrawEvent e)
	{
		if (e.getMouseEvent().getClickCount() == 1)
		{
			lastX = e.getX();
			lastY = e.getY();
			polygon.addPoint(e.getX(), e.getY());
			Graphics2D g = e.getTempG();
			g.setClip(null);
			drawClip(g);
		}
		else
		{
			Area area = e.getArea();
			area.reset();
			area.add(new Area(polygon));
			e.setClip(area);
			polygon.reset();
		}
	}
	
	public void mouseDrag(DrawEvent e)
	{
		polygon.addPoint(e.getX(), e.getY());
		Graphics2D g = e.getTempG();
		drawClip(g);
	}
	
	public void mouseUp(DrawEvent e)
	{
		drawClip(e.getTempG());
	}
	
	public String getName() { return "Free Select"; }
}