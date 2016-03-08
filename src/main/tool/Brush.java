package main.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* Draws at user's mouse
*/
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
			int x = e.getX();
			int y = e.getY();
			if (e.isControlDown())
			{
				if (Math.abs(y - lastY) > Math.abs(x - lastX))
					x = lastX;
				else
					y = lastY;
			}
			g.drawLine(lastX, lastY, x, y);
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
			int x = e.getX();
			int y = e.getY();
			if (e.isControlDown())
			{
				if (Math.abs(y - lastY) > Math.abs(x - lastX))
					x = lastX;
				else
					y = lastY;
			}
			g.drawLine(lastX, lastY, x, y);
		}
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		int x = e.getX();
		int y = e.getY();
		if (e.isControlDown())
		{
			if (Math.abs(y - lastY) > Math.abs(x - lastX))
				x = lastX;
			else
				y = lastY;
		}
		g.drawLine(lastX, lastY, x, y);
		lastX = x;
		lastY = y;
	}
	
	public boolean mouseMove(DrawEvent e)
	{
		if (e.isShiftDown())
		{
			Graphics2D g = e.getTempG();
			g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
			e.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			int x = e.getX();
			int y = e.getY();
			if (e.isControlDown())
			{
				if (Math.abs(y - lastY) > Math.abs(x - lastX))
					x = lastX;
				else
					y = lastY;
			}
			g.drawLine(lastX, lastY, x, y);
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
			int x = e.getX();
			int y = e.getY();
			if (e.getKeyEvent().isControlDown())
			{
				if (Math.abs(y - lastY) > Math.abs(x - lastX))
					x = lastX;
				else
					y = lastY;
			}
			g.drawLine(lastX, lastY, x, y);
			g.setStroke(new BasicStroke(1));
		}
	}
	
	public String getName() { return "Brush"; }
	public int getShortcut() { return 'B'; }
}