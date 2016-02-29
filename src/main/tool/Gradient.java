package main.tool;

import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Gradient extends Tool
{
	private final static int COLOR        = 0;
	private final static int TRANSPARENCY = 1;
	private final static int MULTIPLIER   = 2;
	
	private final static int FGTOBG = 0;
	private final static int BGTOFG = 1;
	
	private final static String gradientTypes[] = new String[]{"Color", "Transparency", "Multiplier"};
	private final static String fadeColorChangeTypes[] = new String[]{"FG to BG", "BG to FG"};
	private final static String fadeTransparencyChangeTypes[] = new String[]{"FG to Transparent", "Transparent to FG"};
	
	private int mode = COLOR;
	private Color startColor, endColor;
	
	private JComboBox<String> selectionTypeBox = new JComboBox<String>(gradientTypes);
	private JSpinner ocpacitySpinner = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
	
	private BufferedImage modifierImage;
	private int lastX, lastY;
	
	public Gradient()
	{
		property.setLayout(new GridLayout(2, 1));
		addRow(property, new JLabel("Type:"), selectionTypeBox);
		addRow(property, new JLabel("Opacity:"), ocpacitySpinner);
	}
	
	private void addRow(JPanel mainPane, Component... comps)
	{
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (Component c : comps)
			temp.add(c);
		mainPane.add(temp);
	}
	
	@Override
	public void mouseDown(DrawEvent e)
	{
		mode = selectionTypeBox.getSelectedIndex();
		lastX = e.getX();
		lastY = e.getY();
		e.getTempG().setStroke(new BasicStroke(3));
		e.getTempG().drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	@Override
	public void mouseDrag(DrawEvent e)
	{
		e.getTempG().drawLine(lastX, lastY, e.getX(), e.getY());
	}
	
	@Override
	public void mouseUp(DrawEvent e)
	{
		Graphics2D g = e.getGraphics();
		
		switch(mode)
		{
			case COLOR:
				drawColor(e);
				break;
			case TRANSPARENCY:
				drawTransparency(e, e.getManager().getCurrentLayer().getImage());
				break;
			case MULTIPLIER:
				drawMultiplier(e, e.getManager().getCurrentLayer().getImage());
				break;
		}
	}
	
	private void drawColor(DrawEvent e)
	{
		Color startColor = e.getForeColor();
		Color endColor = e.getBackColor();
		
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(e.getX(), e.getY());
		
		int alpha = (Integer)ocpacitySpinner.getValue();
		
		Graphics2D g = e.getGraphics();
		
		int startY = Math.min(lastY, Math.min(e.getArea().getBounds().y, e.getY()));
		int endY = Math.max(lastY, Math.max(e.getArea().getBounds().y + e.getArea().getBounds().height, e.getY()));
		
		int dx = Math.abs(e.getX() - lastX);
		int dy = Math.abs(e.getY() - lastY);
		
		if (dx > dy && dx != 0)
		{
			int minX = lastX;
			int minY = lastY;
			int maxX = e.getX();
			int maxY = e.getY();
			if (minX > maxX)
			{
				minX = e.getX();
				minY = e.getY();
				maxX = lastX;
				maxY = lastY;
			}
			
			int startX = Math.min(lastX, Math.min(e.getArea().getBounds().x, e.getX()));
			int endX = Math.max(lastX, Math.max(e.getArea().getBounds().x + e.getArea().getBounds().width, e.getX()));
			
			float dydx = (float)(maxY - minY) / (maxX - minX);
			float recipSlope = -1 / dydx;
			
			Color c1 = e.getForeColor();
			Color c2 = e.getBackColor();
			
			float red1   = c1.getRed();
			float green1 = c1.getGreen();
			float blue1  = c1.getBlue();
			
			float red2   = c2.getRed();
			float green2 = c2.getGreen();
			float blue2  = c2.getBlue();
			
			float dRed   = (red2 - red1)     / (e.getX() - lastX);
			float dGreen = (green2 - green1) / (e.getX() - lastX);
			float dBlue  = (blue2 - blue1)   / (e.getX() - lastX);
			
			int keyPixelCount = 1 << 4;	//To prevent rounding errors when accumulating based off of slope
			
			for (int i = startX; i <= maxX; i += keyPixelCount)
			{
				float yValue = dydx * (i - minX) + minY;
				int lastValue = i + keyPixelCount;
				float redValue   = dRed   * (i - lastX) + red1;
				float greenValue = dGreen * (i - lastX) + green1;
				float blueValue  = dBlue  * (i - lastX) + blue1;
				if (i + keyPixelCount > maxX)
					lastValue = maxX;
				for (int j = i; j < lastValue; j++, yValue += dydx, redValue += dRed, greenValue += dGreen, blueValue += dBlue)
				{
					int y1 = minY;
					int x1 = (int)((y1 - yValue + recipSlope * j) / recipSlope);
					int x2 = minX;
					int y2 = (int)(recipSlope * (x2 - j) + yValue);
					g.setColor(new Color((int)redValue, (int)greenValue, (int)blueValue, alpha));
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
		else if (dx != 0)
		{
			
		}
		else
		{
			Color c1 = e.getForeColor();
			Color c2 = e.getBackColor();
			Color c3 = new Color((c1.getRed() + c2.getRed()) / 2, (c1.getGreen() + c2.getGreen()) / 2, (c1.getBlue() + c2.getBlue()) / 2);
			g.setColor(c3);
			g.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
		}
	}
	
	private void drawTransparency(DrawEvent e, BufferedImage img)
	{
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(e.getX(), e.getY());
		int alpha = (Integer)ocpacitySpinner.getValue();
	}
	
	private void drawMultiplier(DrawEvent e, BufferedImage img)
	{
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(e.getX(), e.getY());
	}
	
	public String getName() { return "Gradient"; }
	public int getShortcut() { return 'G'; }
}