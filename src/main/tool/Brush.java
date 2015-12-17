package main.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Brush extends Tool
{
	private JSpinner thicknessSpinner = new JSpinner(thicknessModel);
	private int lastX = -100, lastY = -100;
	private boolean controlDown;
	
	public Brush()
	{
		property.add(new JLabel("Thickness:"));
		property.add(thicknessSpinner);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		boolean before = lastX == -100 || !e.isControlDown();
		if (before)
		{
			lastX = e.getX();
			lastY = e.getY();
		}
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		if (!before)
		{
			lastX = e.getX();
			lastY = e.getY();
		}
	}
	
	public void mouseDrag(DrawEvent e)
	{
		if (!e.isControlDown())
		{
			Graphics2D g = e.getGraphics();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
			lastX = e.getX();
			lastY = e.getY();
		} else
		{
			Graphics2D g = e.getTempG();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
		}
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public void keyDown(DrawEvent e)
	{
		Graphics2D g = e.getTempG();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	public void keyUp(DrawEvent e)
	{
		
	}
	
	public String getName() { return "Brush"; }
}