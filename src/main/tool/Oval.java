package main.tool;

import java.awt.*;
import javax.swing.*;

public class Oval extends Tool
{
	private int lastX, lastY;
	private OvalType currentDrawer = OvalType.OUTLINE;
	
	private JCheckBox chkFilled = new JCheckBox("Filled");
	
	public Oval()
	{
		property.add(chkFilled);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		lastX = e.getX();
		lastY = e.getY();
		currentDrawer = chkFilled.isSelected() ? OvalType.FILLED : OvalType.OUTLINE;
		drawOval(g, e);
	}
	
	public void mouseDrag(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		drawOval(g, e);
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		drawOval(g, e);
	}
	
	private void drawOval(Graphics g, DrawEvent e)
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
				g.drawOval(x1, y1, x2 - x1, y2 - y1);
				break;
			case FILLED:
				g.fillOval(x1, y1, x2 - x1, y2 - y1);
				break;
		}
	}
	
	private enum OvalType { OUTLINE, FILLED }
	
	public String getName() { return "Oval"; }
	public int getShortcut() { return 'O'; }
}