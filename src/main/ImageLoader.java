package main;

import main.layer.*;
import main.dialog.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.*;
import java.io.*;

/**
* Provides IO functions to load and save images
*/
public class ImageLoader
{	
	/**
	* Loads a blank image
	*/
	public static void loadNew(int width, int height, FillType fillType, DrawPanel panel, LayerManager layerManager)
	{
		panel.setSize(width, height);
		switch (fillType)
		{
			case BACKGROUND:
				layerManager.addLayer(new Layer("Background", width, height, panel.getBackColor()));
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(1);
				break;
			case FOREGROUND:
				layerManager.addLayer(new Layer("Background", width, height, panel.getForeColor()));
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(1);
				break;
			case TRANSPARENT:
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(0);
				break;
		}
		layerManager.setTemp(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		panel.grabFocus();
	}
	
	/**
	* Loads an image from the filesystem
	*/
	public static void open(DrawPanel panel, LayerManager layerManager)
	{
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File file = fc.getSelectedFile();
		if (file == null)
			return;
		open(file, panel, layerManager);
	}
	
	/**
	* Loads an image from the filesystem
	* @param file The file to load
	*/
	public static void open(File file, DrawPanel panel, LayerManager layerManager)
	{
		try
		{
			Image originalImage = ImageIO.read(file);
			BufferedImage img = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			g.drawImage(originalImage, 0, 0, null);
			g.dispose();
			if (layerManager.getCurrentLayer() == null)
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				// double width = (double)panel.getScroll().getWidth() / img.getWidth();
				// double height = (double)panel.getScroll().getHeight() / img.getHeight();
				double width = (double)panel.getMaxBounds().width / img.getWidth();
				double height = (double)panel.getMaxBounds().height / img.getHeight();
				if (width < 1 && height < 1)
					panel.setZoom((float)Math.max(width * .9, height * .9));
				else
					panel.setSize(img.getWidth(), img.getHeight());
			} else
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				int width = Math.max(img.getWidth(), panel.getWidth());
				int height = Math.max(img.getHeight(), panel.getHeight());
				panel.setSize(width, height);
			}
			
			// filePath = file.getPath();
			panel.setPath(file.getPath());
			
			layerManager.setTemp(new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB));
		} catch(IOException ex)
		{
			System.err.println("Error loading " + file.getPath());
			ex.printStackTrace();
		}
		panel.grabFocus();
	}
	
	/**
	* Saves an image to the filesystem
	*/
	public static void save(DrawPanel panel, LayerManager layerManager)
	{
		// if (filePath.equals(""))
		if (panel.getPath().equals(""))
		{
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(null);
			File file = fc.getSelectedFile();
			if (file == null)
				return;
			save(file, panel, layerManager);
		} else
			save(new File(panel.getPath()), panel, layerManager);
	}
	
	/**
	* Saves an image to the filesystem
	*/
	public static void save(File file, DrawPanel panel, LayerManager layerManager)
	{
		try
		{
			BufferedImage img = new BufferedImage(layerManager.getCurrentLayer().getImage().getWidth(), layerManager.getCurrentLayer().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)img.getGraphics();
			layerManager.draw(g, 1);
			ImageIO.write(img, "PNG", file);
			panel.setPath(file.getPath());
		} catch(IOException ex)
		{
			System.err.println("Error saving " + file.getPath());
			ex.printStackTrace();
		}
	}
}