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
	private final static String fadeColorChangeTypes[] = new String[]{"BG to FG", "FG to BG"};
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
		int x = e.getX();
		int y = e.getY();
		if (e.isShiftDown())
		{
			if (Math.abs(y - lastY) > Math.abs(x - lastX))
				x = lastX;
			else
				y = lastY;
		}
		e.getTempG().drawLine(lastX, lastY, x, y);
	}
	
	@Override
	public void mouseUp(DrawEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (e.isShiftDown())
		{
			if (Math.abs(y - lastY) > Math.abs(x - lastX))
				x = lastX;
			else
				y = lastY;
		}
		
		Graphics2D g = e.getGraphics();
		
		switch(mode)
		{
			case COLOR:
				drawColor(e, x, y);
				break;
			case TRANSPARENCY:
				drawTransparency(e, x, y, e.getManager().getCurrentLayer().getImage());
				break;
			case MULTIPLIER:
				drawMultiplier(e, x, y, e.getManager().getCurrentLayer().getImage());
				break;
		}
	}
	
	private void drawColor(DrawEvent e, int x, int y)
	{
		Color startColor = e.getForeColor();
		Color endColor = e.getBackColor();
		
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(x, y);
		
		int alpha = (Integer)ocpacitySpinner.getValue();
		
		Graphics2D g = e.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		int startY = Math.min(lastY, Math.min(e.getArea().getBounds().y, y));
		int endY = Math.max(lastY, Math.max(e.getArea().getBounds().y + e.getArea().getBounds().height, y));
		
		int dx = Math.abs(x - lastX);
		int dy = Math.abs(y - lastY);
		
		
		if (dy == 0)
		{
			int x1 = lastX;
			int x2 = x;
			if (x1 > x2)
			{
				Color c1 = e.getForeColor();
				Color c2 = e.getBackColor();
				float r1 = c1.getRed();
				float g1 = c1.getGreen();
				float b1 = c1.getBlue();
				float r2 = c2.getRed();
				float g2 = c2.getGreen();
				float b2 = c2.getBlue();
				float dr = (r2 - r1) / (x1 - x2);
				float dg = (g2 - g1) / (x1 - x2);
				float db = (b2 - b1) / (x1 - x2);
				for (int i = x2; i <= x1; i++, r1 += dr, g1 += dg, b1 += db)
				{
					g.setColor(new Color((int)r1, (int)g1, (int)b1, alpha));
					g.drawLine(i, y, i, y);
				}
			}
			else
			{
				Color c1 = e.getBackColor();
				Color c2 = e.getForeColor();
				float r1 = c1.getRed();
				float g1 = c1.getGreen();
				float b1 = c1.getBlue();
				float r2 = c2.getRed();
				float g2 = c2.getGreen();
				float b2 = c2.getBlue();
				float dr = (r2 - r1) / (x2 - x1);
				float dg = (g2 - g1) / (x2 - x1);
				float db = (b2 - b1) / (x2 - x1);
				for (int i = x1; i <= x2; i++, r1 += dr, g1 += dg, b1 += db)
				{
					g.setColor(new Color((int)r1, (int)g1, (int)b1, alpha));
					g.drawLine(i, y, i, y);
				}
			}
		}
		else if (dx == 0)
		{
			int y1 = lastY;
			int y2 = y;
			if (y1 > y2)
			{
				Color c1 = e.getForeColor();
				Color c2 = e.getBackColor();
				float r1 = c1.getRed();
				float g1 = c1.getGreen();
				float b1 = c1.getBlue();
				float r2 = c2.getRed();
				float g2 = c2.getGreen();
				float b2 = c2.getBlue();
				float dr = (r2 - r1) / (y1 - y2);
				float dg = (g2 - g1) / (y1 - y2);
				float db = (b2 - b1) / (y1 - y2);
				for (int i = y2; i <= y1; i++, r1 += dr, g1 += dg, b1 += db)
				{
					g.setColor(new Color((int)r1, (int)g1, (int)b1, alpha));
					g.drawLine(x, i, x, i);
				}
			}
			else
			{
				Color c1 = e.getBackColor();
				Color c2 = e.getForeColor();
				float r1 = c1.getRed();
				float g1 = c1.getGreen();
				float b1 = c1.getBlue();
				float r2 = c2.getRed();
				float g2 = c2.getGreen();
				float b2 = c2.getBlue();
				float dr = (r2 - r1) / (y2 - y1);
				float dg = (g2 - g1) / (y2 - y1);
				float db = (b2 - b1) / (y2 - y1);
				for (int i = y1; i <= y2; i++, r1 += dr, g1 += dg, b1 += db)
				{
					g.setColor(new Color((int)r1, (int)g1, (int)b1, alpha));
					g.drawLine(x, i, x, i);
				}
			}
		}
		else if (dx > dy)
		{
			int minX = lastX;
			int minY = lastY;
			int maxX = x;
			int maxY = y;
			if (minX > maxX)
			{
				minX = x;
				minY = y;
				maxX = lastX;
				maxY = lastY;
			}
			
			int startX = Math.min(lastX, Math.min(e.getArea().getBounds().x, x));
			int endX = Math.max(lastX, Math.max(e.getArea().getBounds().x + e.getArea().getBounds().width, x));
			
			float dydx = (float)(maxY - minY) / (maxX - minX + 1);
			float recipSlope = -1 / dydx;
			
			Color c1 = e.getForeColor();
			Color c2 = e.getBackColor();
			
			float red1   = c1.getRed();
			float green1 = c1.getGreen();
			float blue1  = c1.getBlue();
			
			float red2   = c2.getRed();
			float green2 = c2.getGreen();
			float blue2  = c2.getBlue();
			
			float dRed   = (red2 - red1)     / (x - lastX + 1);
			float dGreen = (green2 - green1) / (x - lastX + 1);
			float dBlue  = (blue2 - blue1)   / (x - lastX + 1);
			
			int keyPixelCount = 1 << 4;	//To prevent rounding errors when accumulating based off of slope
			float yValue = minY;
			
			float redValue   = red1;
			float greenValue = green1;
			float blueValue  = blue1;
			
			for (int i = startX; i <= maxX; i++, yValue += dydx, redValue += dRed, greenValue += dGreen, blueValue += dBlue)
			{
				int y1 = minY;
				int x1 = (int)((y1 - yValue + recipSlope * i) / recipSlope);
				int x2 = minX;
				int y2 = (int)(recipSlope * (x2 - i) + yValue);
				g.setColor(new Color(clamp(redValue), clamp(greenValue), clamp(blueValue), alpha));
				g.drawLine(x1, y1, x2, y2);
			}
		}
		else
		{
			
		}
	}
	
	private int clamp(float value)
	{
		return (int)Math.min(255, Math.max(value, 0));
	}
	
	private void drawTransparency(DrawEvent e, int x, int y, BufferedImage img)
	{
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(e.getX(), e.getY());
		int alpha = (Integer)ocpacitySpinner.getValue();
	}
	
	private void drawMultiplier(DrawEvent e, int x, int y, BufferedImage img)
	{
		Point startPoint = new Point(lastX, lastY);
		Point endPoint = new Point(e.getX(), e.getY());
	}
	
	public String getName() { return "Gradient"; }
	public int getShortcut() { return 'G'; }
}