package main.tool;

import main.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class RectangleSelect extends Tool
{
	private DrawPanel main;
	private JSpinner spinnerX      = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
	private JSpinner spinnerY      = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
	private JSpinner spinnerWidth  = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
	private JSpinner spinnerHeight = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
	
	private int lastX, lastY;
	
	private Rectangle rect = new Rectangle();
	
	public RectangleSelect(DrawPanel main)
	{
		this.main = main;
		property.setLayout(new GridLayout(4, 1));
		addRow(property, new JLabel("X:"), spinnerX);
		addRow(property, new JLabel("Y:"), spinnerY);
		addRow(property, new JLabel("Width:"), spinnerWidth);
		addRow(property, new JLabel("Height:"), spinnerHeight);
	}
	
	private void addRow(JPanel pane, JComponent... comps)
	{
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (JComponent c : comps)
			temp.add(c);
		pane.add(temp);
	}
	
	public void mouseDown(DrawEvent e)
	{
		lastX = e.getX();
		lastY = e.getY();
		constructRect(e);
		Graphics g = e.getTempG();
		g.setClip(null);
		drawSelection(g);
	}
	
	public void drawSelection(Graphics g)
	{
		int i = 0;
		for (i = rect.x; i < rect.x + rect.width - 5; i += 10)
		{
			g.drawLine(i, rect.y, i + 5, rect.y);
			g.drawLine(i, rect.y + rect.height, i + 5, rect.y + rect.height);
		}
		if (i < rect.x + rect.width)
		{
			g.drawLine(i, rect.y, rect.x + rect.width, rect.y);
			g.drawLine(i, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height);
		}
		for (i = rect.y; i < rect.y + rect.height - 5; i += 10)
		{
			g.drawLine(rect.x, i, rect.x, i + 5);
			g.drawLine(rect.x + rect.width, i, rect.x + rect.width, i + 5);
		}
		if (i < rect.y + rect.height)
		{
			g.drawLine(rect.x, i, rect.x, rect.y + rect.height);
			g.drawLine(rect.x + rect.width, i, rect.x + rect.width, rect.y + rect.height);
		}
	}
	
	public void mouseDrag(DrawEvent e)
	{
		constructRect(e);
		Graphics2D g = e.getTempG();
		// Graphics2D g = (Graphics2D)e.getManager().getTemp().getGraphics();
		drawSelection(g);
		// g.setColor(Color.BLACK);
	}
	
	public void mouseUp(DrawEvent e)
	{
		e.getArea().reset();
		e.getArea().add(new Area(rect));
		e.setClip(e.getArea());
	}
	
	public void drawSelection()
	{
		
	}
	
	public void constructRect(DrawEvent e)
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
		rect.x = x1;
		rect.y = y1;
		rect.width = x2 - x1;
		rect.height = y2 - y1;
	}
	
	public String getName() { return "Rectangle Select"; }
}