package main.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Erase extends Tool
{
	private JSpinner thicknessSpinner = new JSpinner(thicknessModel);
	private int lastX = -100, lastY = -100;
	private boolean controlDown;
	
	public Erase()
	{
		property.add(new JLabel("Thickness:"));
		property.add(thicknessSpinner);
	}
	
	public void mouseDown(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.setColor(new Color(0, 0, 0, 0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lastX = e.getX();
		lastY = e.getY();
		g.drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	public void mouseDrag(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.setColor(new Color(0, 0, 0, 0));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		g.setStroke(new BasicStroke((Integer)thicknessSpinner.getValue()));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.setColor(new Color(0, 0, 0, 0));
		g.drawLine(lastX, lastY, e.getX(), e.getY());
		lastX = e.getX();
		lastY = e.getY();
	}
	
	public String getName() { return "Eraser"; }
	public int getShortcut() { return 'E'; }
}