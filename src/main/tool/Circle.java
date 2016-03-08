package main.tool;

import javax.swing.*;
import java.awt.*;

/**
* Draws a circle with where the user first presses as the center and current location as part of edge
*/
public class Circle extends Tool
{
	private int lastX, lastY;
	private CircleType currentDrawer = CircleType.OUTLINE;
	
	private JCheckBox chkFilled = new JCheckBox("Filled");
	
	public Circle()
	{
		property.add(chkFilled);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		lastX = e.getX();
		lastY = e.getY();
		currentDrawer = chkFilled.isSelected() ? CircleType.FILLED : CircleType.OUTLINE;
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
		int radius = (int)Math.sqrt((e.getX() - lastX) * (e.getX() - lastX) + (e.getY() - lastY) * (e.getY() - lastY));
		switch (currentDrawer)
		{
			case OUTLINE:
				g.drawOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
				break;
			case FILLED:
				g.fillOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
				break;
		}
	}
	
	private enum CircleType { OUTLINE, FILLED }
	
	public String getName() { return "Circle"; }
	public int getShortcut() { return 'C'; }
}