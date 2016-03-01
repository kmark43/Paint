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
		if (lastX == -100 || !e.isShiftDown())
		{
			Graphics2D g = e.getGraphics();
			e.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			lastX = e.getX();
			lastY = e.getY();
			g.drawLine(lastX, lastY, e.getX(), e.getY());
		}
		else
		{
			Graphics2D g = e.getTempG();
			e.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
		}
	}
	
	public void mouseDrag(DrawEvent e)
	{
		if (!e.isShiftDown())
		{
			Graphics2D g = e.getGraphics();
			g.drawLine(lastX, lastY, e.getX(), e.getY());
			lastX = e.getX();
			lastY = e.getY();
		} else
		{
			Graphics2D g = e.getTempG();
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
	
	public boolean mouseMove(DrawEvent e)
	{
		if (e.isShiftDown())
		{
			Graphics2D g = e.getTempG();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			e.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawLine(lastX, lastY, e.getX(), e.getY());
			return true;
		}
		return false;
	}
	
	public void keyDown(DrawEvent e)
	{
		if (e.getKeyEvent().getKeyCode() == KeyEvent.VK_SHIFT)
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