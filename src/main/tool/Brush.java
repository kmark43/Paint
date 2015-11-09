package main.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Brush extends Tool
{
	private JSpinner thicknessSpinner = new JSpinner(thicknessModel);
	private int lastX, lastY;
	
	public Brush()
	{
		property.add(new JLabel("Thickness:"));
		property.add(thicknessSpinner);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		lastX = e.getX();
		lastY = e.getY();
		g.drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	public void mouseDrag(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	public String getName() { return "Brush"; }
}