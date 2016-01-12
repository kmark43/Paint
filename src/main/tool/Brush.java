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
		if (lastX == -100 || !e.isControlDown())
		{
			Graphics2D g = e.getGraphics();
			e.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			lastX = e.getX();
			lastY = e.getY();
			g.drawLine(lastX, lastY, e.getX(), e.getY());
		}
		else
		{
			Graphics2D g = e.getTempG();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
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
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public void keyDown(DrawEvent e)
	{
		if (e.getKeyEvent().getKeyCode() == KeyEvent.VK_CONTROL)
		{
			Graphics2D g = e.getTempG();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
			g.setStroke(new BasicStroke(1));
		}
	}
	
	public String getName() { return "Brush"; }
	public int getShortcut() { return 'B'; }
}