package main.tool;

import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.*;

/**
* Creates a linear gradient over an image
*/
//TODO: Add support for exponential gradients
public class Gradient extends Tool
{
	//Pointers
	private final static int COLOR        = 0;
	private final static int MULTIPLIER   = 1;
	
	private final static int FGTOBG = 0;
	private final static int BGTOFG = 1;
	
	//Dialog options
	private final static String gradientTypes[] = new String[]{"Color", "Multiplier"};
	private final static String fadeColorChangeTypes[] = new String[]{"BG to FG", "FG to BG"};
	
	private int mode = COLOR;
	private Color startColor, endColor;
	
	private JComboBox<String> selectionTypeBox = new JComboBox<String>(gradientTypes);
	private JComboBox<String> fadeColorTypeBox = new JComboBox<String>(fadeColorChangeTypes);
	
	private int lastX, lastY;
	
	public Gradient()
	{
		property.setLayout(new GridLayout(2, 1));
		addRow(property, new JLabel("Type:"), selectionTypeBox);
		addRow(property, new JLabel("Fade:"), fadeColorTypeBox);
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
			case MULTIPLIER:
				drawMultiplier(e, x, y, e.getManager().getCurrentLayer().getImage());
				break;
		}
	}
	
	private void drawColor(DrawEvent e, int x, int y)
	{
		Graphics2D g = e.getGraphics();
		
		int startY = Math.min(lastY, Math.min(e.getArea().getBounds().y, y));
		int endY = Math.max(lastY, Math.max(e.getArea().getBounds().y + e.getArea().getBounds().height, y));
		
		int absdx = Math.abs(x - lastX);
		int absdy = Math.abs(y - lastY);
		int x1 = lastX;
		int x2 = e.getX();
		int y1 = lastY;
		int y2 = e.getY();
		
		int widthMultiplier = 1;
		int heightMultiplier = 1;
		
		if (x1 > x2)
			widthMultiplier = -1;
		
		if (y1 > y2)
			heightMultiplier = -1;
		
		Rectangle rect = new Rectangle(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
		
		Color c1 = e.getBackColor();
		Color c2 = e.getForeColor();
		
		if (fadeColorTypeBox.getSelectedIndex() == 1)
		{
			c1 = e.getForeColor();
			c2 = e.getBackColor();
		}
		
		float red1   = c1.getRed();
		float green1 = c1.getGreen();
		float blue1  = c1.getBlue();
		float red2   = c2.getRed();
		float green2 = c2.getGreen();
		float blue2  = c2.getBlue();
		
		int wh = rect.width + rect.height;
		for (int i = 0; i <= rect.width; i++)
		{
			for (int j = 0; j <= rect.height; j++)
			{
				int ij = i + j;
				int red   = clamp((int)((red1 * (wh - ij) + red2 * ij) / wh));
				int green = clamp((int)((green1 * (wh - ij) + green2 * ij) / wh));
				int blue  = clamp((int)((blue1 * (wh - ij) + blue2 * ij) / wh));
				g.setColor(new Color(red, green, blue));
				g.drawLine(i * widthMultiplier + rect.x, j * heightMultiplier + rect.y, i * widthMultiplier + rect.x, j * heightMultiplier + rect.y);
			}
		}
	}
	
	private void drawMultiplier(DrawEvent e, int x, int y, BufferedImage img)
	{
		Graphics2D g = e.getGraphics();
		
		int startY = Math.min(lastY, Math.min(e.getArea().getBounds().y, y));
		int endY = Math.max(lastY, Math.max(e.getArea().getBounds().y + e.getArea().getBounds().height, y));
		
		int absdx = Math.abs(x - lastX);
		int absdy = Math.abs(y - lastY);
		int x1 = lastX;
		int x2 = e.getX();
		int y1 = lastY;
		int y2 = e.getY();
		
		int widthMultiplier = 1;
		int heightMultiplier = 1;
		
		if (x1 > x2)
			widthMultiplier = -1;
		
		if (y1 > y2)
			heightMultiplier = -1;
		
		Rectangle rect = new Rectangle(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
		
		Color c1 = e.getBackColor();
		Color c2 = e.getForeColor();
		
		float mul = 1f / 255f;
		
		float red1   = c1.getRed() * mul;
		float green1 = c1.getGreen() * mul;
		float blue1  = c1.getBlue() * mul;
		float red2   = c2.getRed() * mul;
		float green2 = c2.getGreen() * mul;
		float blue2  = c2.getBlue() * mul;
		
		Area area = e.getArea();
		if (e.getGraphics().getClip() ==  null)
			area.add(new Area(new Rectangle(0, 0, img.getWidth(), img.getHeight())));
		
		int wh = rect.width + rect.height;
		for (int i = 0; i <= rect.width; i++)
		{
			for (int j = 0; j <= rect.height; j++)
			{
				int xPoint = i * widthMultiplier + rect.x;
				int yPoint = j * heightMultiplier + rect.y;
				if (area.contains(xPoint, yPoint))
				{
					int ij = i + j;
					Color c = new Color(img.getRGB(xPoint, yPoint));
					float red   = c.getRed() * mul * (red1 * (wh - ij) + red2 * ij) / wh;
					float green = c.getGreen() * mul * (green1 * (wh - ij) + green2 * ij) / wh;
					float blue  = c.getBlue() * mul * (blue1 * (wh - ij) + blue2 * ij) / wh;
					
					g.setColor(new Color(red, green, blue));
					g.drawLine(xPoint, yPoint, xPoint, yPoint);
				}
			}
		}
	}
	
	private int clamp(float value) { return (int)Math.min(255, Math.max(value, 0)); }
	
	public String getName() { return "Gradient"; }
	public int getShortcut() { return 'G'; }
}