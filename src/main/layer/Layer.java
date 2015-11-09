package main.layer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
* This class is to represent each layer of the image
*/
public class Layer
{
	/**
	* The name of the current layer
	*/
	private String name;
	
	/**
	* Determines layer visibility
	*/
	private boolean visible = true;
	
	/**
	* Stores the image for the layer
	*/
	private BufferedImage img;
	
	/**
	* Creates a new, blank layer
	* @param name The name of the layer
	* @param width The width of the new layer
	* @param startColor The color to fill the layer with
	*/
	public Layer(String name, int width, int height, Color startColor)
	{
		this.name = name;
		int rgb = startColor.getRGB();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				img.setRGB(i, j, rgb);
	}
	
	/**
	* Create a layer from a file
	* @param name The layer name
	* @param file The file where the image is stored
	*/
	public Layer(String name, File file)
	{
		try
		{
			img = ImageIO.read(file);
			this.name = name;
		} catch(Exception ex)
		{
			System.err.println("Error opening \"" + file.getPath() + "\"");
			ex.printStackTrace();
		}
	}
	
	/**
	* Create a layer from a file. The layer name will be the name of the file
	* @param The file where the image is stored
	*/
	public Layer(File file) { this(file.getName(), file); }
	
	/**
	* Create a layer from a file
	* @param name The layer name
	* @param path The path to the file where the image is stored
	*/
	public Layer(String name, String path) { this(name, new File(path)); }
	
	/**
	* Create a layer from a file. The layer name will be the name of the file
	* @param path The path to the file
	*/
	public Layer(String path) { this(new File(path)); }
	
	/**
	* Creates a layer from another layer as a copy
	* @param otherLayer The layer to duplicate
	*/
	public Layer(Layer otherLayer) { name = otherLayer.name + " - copy"; }
	
	/**
	* Sets the visibility of the current layer
	* @return weather the layer is visible
	*/
	public void setVisible(boolean v) { visible = v; }
	
	/**
	* Returns the image the layer stores
	* @return Returns the image stored by the layer
	*/
	public BufferedImage getImage() { return img; }
	
	/**
	* Returns the name of the layer
	* @return Returns the name of the layer
	*/
	public String getName() { return name; }
	
	public void setName(String n) { name = n; }
	
	public boolean isVisible() { return visible; }
	
	public String toString() { return "main.layer.Layer[name = " + name + ", visible = " + visible + ", img = " + img + "]"; }
}