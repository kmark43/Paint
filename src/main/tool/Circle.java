package main.tool;

import javax.swing.*;
import java.awt.*;

public class Circle extends Tool
{
	private int lastX, lastY;
	private FillDrawer fillDrawer = new FillDrawer();
	private OutlineDrawer outlineDrawer = new OutlineDrawer();
	private OvalDrawer currentDrawer = outlineDrawer;
	
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
		currentDrawer = chkFilled.isSelected() ? fillDrawer : outlineDrawer;
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
		currentDrawer.drawOval(g, x1 - radius, y1 - radius, radius * 2, radius * 2);;
	}
	
	private interface OvalDrawer { public void drawOval(Graphics g, int x, int y, int width, int height); }
	private class FillDrawer implements OvalDrawer
	{
		public void drawOval(Graphics g, int x, int y, int width, int height)
		{
			g.fillOval(x, y, width, height);
		}
	}
	private class OutlineDrawer implements OvalDrawer
	{
		public void drawOval(Graphics g, int x, int y, int width, int height)
		{
			g.drawOval(x, y, width, height);
		}
	}
	
	public String getName() { return "Circle"; }
	public int getShortcut() { return 'C'; }
}