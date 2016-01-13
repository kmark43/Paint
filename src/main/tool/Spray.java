package main.tool;

import main.Main;
import main.layer.LayerManager;

import javax.swing.*;
import java.awt.*;

/**
* This tool represents spray paint
*/
public class Spray extends Tool implements Runnable
{
	private JSpinner radiusSpinner    = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
	private JSpinner thicknessSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
	
	private LayerManager layerManager;
	private Color c;
	private Main main;
	
	private int x, y;
	
	private Graphics g;
	
	private boolean spraying = false;
	
	/**
	* Defines the property panel for the spray paint tool
	* @param main Used to repaint the main panel when more points have been painted
	*/
	public Spray(Main main)
	{
		this.main = main;
		property.setLayout(new GridLayout(2, 2));
		property.add(new JLabel("Radius:"));
		property.add(radiusSpinner);
		property.add(new JLabel("Thickness:"));
		property.add(thicknessSpinner);
	}
	
	@Override
	public void run()
	{
		g.setColor(c);
		int radius = (Integer)radiusSpinner.getValue();
		int thickness = (Integer)thicknessSpinner.getValue();
		while (spraying)
		{
			for (int i = 0; i < thickness; i++)
			{
				int xPos = (int)(Math.random() * (2 * radius + 1)) - radius;
				int yRange = (int)Math.sqrt(radius * radius - xPos * xPos);
				int yPos = (int)(Math.random() * (2 * yRange + 1)) - yRange;
				xPos += x;
				yPos += y;
				g.drawLine(xPos, yPos, xPos, yPos);
			}
			main.repaint(x - radius - 1, y - radius - 1, x + radius + 1, y + radius + 1);
			try
			{
				Thread.sleep(10);
			} catch(Exception ex){}
		}
	}
	
	/**
	* Begins the execution thread for drawing
	*/
	public void mouseDown(DrawEvent e)
	{
		layerManager = e.getManager();
		spraying = true;
		g = e.getGraphics();
		c = e.getGraphics().getColor();
		x = e.getX();
		y = e.getY();
		new Thread(this).start();
	}
	
	/**
	* Updates the x and y coords and color accordingly
	*/
	public void mouseDrag(DrawEvent e)
	{
		c = e.getGraphics().getColor();
		x = e.getX();
		y = e.getY();
	}
	
	/**
	* Updates the x and y coords and color accordingly and stops spraying
	*/
	public void mouseUp  (DrawEvent e)
	{
		c = e.getGraphics().getColor();
		x = e.getX();
		y = e.getY();
		spraying = false;
	}
	
	public String getName() { return "Spray Paint"; }
	public int getShortcut() { return 'S'; }
}