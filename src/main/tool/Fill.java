package main.tool;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.Queue;
import java.util.LinkedList;

public class Fill extends Tool
{
	private JSpinner toleranceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
	
	public Fill()
	{
		property.add(new JLabel("Tolerance:"));
		property.add(toleranceSpinner);
	}
	
	public void mouseDown(DrawEvent e)
	{
		BufferedImage img = e.getManager().getCurrentLayer().getImage();
		if (img != null)
		{
			int x = e.getX();
			int y = e.getY();
			Point p = new Point(x, y);
			Queue<Point> q = new LinkedList<Point>();
			Color targetColor = new Color(img.getRGB(p.x, p.y), true);
			Color changeColor = e.getGraphics().getColor();
			int tolerance = (Integer)toleranceSpinner.getValue();
			boolean painted[][] = new boolean[img.getHeight()][img.getWidth()];
			q.add(p);
			Shape clipArea = e.getGraphics().getClip();
			if (clipArea == null)
				clipArea = new Rectangle(0, 0, e.getManager().getCurrentLayer().getImage().getWidth(), e.getManager().getCurrentLayer().getImage().getHeight());
			while (!q.isEmpty())
			{
				p = q.remove();
				if (clipArea.contains(p.x, p.y))
				{
					Color current = new Color(img.getRGB(p.x, p.y), true);
					if (!painted[p.y][p.x] && targetColor.getAlpha() == current.getAlpha() && Math.abs(current.getRed() - targetColor.getRed()) <= tolerance && Math.abs(current.getGreen() - targetColor.getGreen()) <= tolerance && Math.abs(current.getBlue() - targetColor.getBlue()) <= tolerance)
					{
						painted[p.y][p.x] = true;
						img.setRGB(p.x, p.y, changeColor.getRGB());
						q.add(new Point(p.x + 1, p.y));
						q.add(new Point(p.x - 1, p.y));
						q.add(new Point(p.x, p.y + 1));
						q.add(new Point(p.x, p.y - 1));
					}
				}
			}
		}
	}
	
	public void mouseDrag(DrawEvent e){}
	public void mouseUp(DrawEvent e){}
	
	public String getName() { return "Fill"; }
	public int getShortcut() { return 'F'; }
}