package main.filter;

import main.tool.DrawEvent;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import main.layer.*;

public class Negative extends Filter
{
	@Override
	public void modifyImage(DrawEvent e)
	{
		BufferedImage img = e.getManager().getCurrentLayer().getImage();
		Area area = e.getArea();
		Rectangle bounds = area.getBounds();
		if (e.getGraphics().getClip() ==  null)
			area.add(new Area(new Rectangle(0, 0, e.getManager().getCurrentLayer().getImage().getWidth(), e.getManager().getCurrentLayer().getImage().getHeight())));
		bounds = area.getBounds();
		for (int x = bounds.x; x < bounds.x + bounds.width; x++)
		{
			for (int y = bounds.y; y < bounds.y + bounds.height; y++)
			{
				if (area.contains(x, y))
				{
					int rgb   = img.getRGB(x, y);
					int alpha = (rgb >> 24);
					int red   = 255 - (rgb >> 16) & 0xff;
					int green = 255 - (rgb >> 8) & 0xff;
					int blue  = 255 - rgb & 0xff;
					img.setRGB(x, y, (alpha << 24) + (red << 16) + (green << 8) + blue);
				}
			}
		}
	}
	
	public String getName() { return "Negative"; }
}