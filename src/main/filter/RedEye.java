package main.filter;

import main.tool.DrawEvent;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import main.layer.*;

/**
* Attempts to remove the red eye effect from the selection area
*/
public class RedEye extends Filter
{
	@Override
	public void modifyImage(DrawEvent e)
	{
		BufferedImage img = e.getManager().getCurrentLayer().getImage();
		Area area = e.getArea();
		Rectangle bounds = area.getBounds();
		if (e.getGraphics().getClip() ==  null)
			area.add(new Area(new Rectangle(0, 0, img.getWidth(), img.getHeight())));
		bounds = area.getBounds();
		for (int x = bounds.x; x < bounds.x + bounds.width; x++)
		{
			for (int y = bounds.y; y < bounds.y + bounds.height; y++)
			{
				if (area.contains(x, y))
				{
					int rgb   = img.getRGB(x, y);
					int alpha = (rgb >> 24);
					int red   = (rgb >> 16) & 0xff;
					int green = (rgb >> 8) & 0xff;
					int blue  = rgb & 0xff;
					
					if (red > green * 1.1 && red > blue * 1.1)
					{
						red = (green + blue) / 3;
						img.setRGB(x, y, (alpha << 24) + (red << 16) + (green << 8) + blue);
					}
				}
			}
		}
	}
	
	public String getName() { return "Red Eye Remover"; }
	public int getShortcut() { return 'R'; }
}