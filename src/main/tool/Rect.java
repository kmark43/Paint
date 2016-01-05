package main.tool;

import java.awt.*;
import javax.swing.*;

public class Rect extends Tool
{
	private int lastX, lastY;
	private FillDrawer fillDrawer = new FillDrawer();
	private OutlineDrawer outlineDrawer = new OutlineDrawer();
	private RectDrawer currentDrawer = outlineDrawer;
	
	private JCheckBox chkFilled = new JCheckBox("Filled");
	
	public Rect()
	{
		property.add(chkFilled);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		lastX = e.getX();
		lastY = e.getY();
		currentDrawer = chkFilled.isSelected() ? fillDrawer : outlineDrawer;
		drawRect(g, e);
	}
	
	public void mouseDrag(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		drawRect(g, e);
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		drawRect(g, e);
	}
	
	private void drawRect(Graphics g, DrawEvent e)
	{
		int x1 = lastX;
		int y1 = lastY;
		int x2 = e.getX();
		int y2 = e.getY();
		if (x2 < x1)
		{
			x1 = e.getX();
			x2 = lastX;
		}
		if (y2 < y1)
		{
			y1 = e.getY();
			y2 = lastY;
		}
		currentDrawer.drawRect(g, x1, y1, x2 - x1, y2 - y1);
	}
	
	private interface RectDrawer { public void drawRect(Graphics g, int x, int y, int width, int height); }
	private class FillDrawer implements RectDrawer
	{
		public void drawRect(Graphics g, int x, int y, int width, int height)
		{
			g.fillRect(x, y, width, height);
		}
	}
	private class OutlineDrawer implements RectDrawer
	{
		public void drawRect(Graphics g, int x, int y, int width, int height)
		{
			g.drawRect(x, y, width, height);
		}
	}
	
	public String getName() { return "Rectangle"; }
	public int getShortcut() { return 'R'; }
}