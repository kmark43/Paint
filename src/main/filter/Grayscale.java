package main.filter;

import java.awt.image.*;
import main.layer.*;

public class Grayscale extends Filter
{
	@Override
	public void modifyImage(LayerManager layerManager)
	{
		BufferedImage img = layerManager.getCurrentLayer().getImage();
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				int rgb   = img.getRGB(i, j);
				int red   = (rgb >> 16) & 0xff;
				int green = (rgb >> 8) & 0xff;
				int blue  = rgb & 0xff;
				int ave = (red + green + blue) / 3;
				img.setRGB(i, j, (ave << 16) + (ave << 8) + ave);
			}
		}
	}
	
	public String getName() { return "Grayscale"; }
}