package main.tool;

import java.awt.*;
import javax.swing.*;

public class Rect extends Tool
{
	private int lastX, lastY;
	private RectType currentDrawer = RectType.OUTLINE;
	
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
		currentDrawer = chkFilled.isSelected() ? RectType.FILLED : RectType.OUTLINE;
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
		switch (currentDrawer)
		{
			case OUTLINE:
				g.drawRect(x1, y1, x2 - x1, y2 - y1);
				break;
			case FILLED:
				g.fillRect(x1, y1, x2 - x1, y2 - y1);
				break;
		}
	}
	
	private enum RectType { OUTLINE, FILLED }
	
	public String getName() { return "Rectangle"; }
	public int getShortcut() { return 'R'; }
}