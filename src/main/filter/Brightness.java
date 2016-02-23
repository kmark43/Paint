package main.filter;

import main.GUIManager;
import main.tool.DrawEvent;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import main.layer.*;

public class Brightness extends Filter implements Runnable, ChangeListener
{
	private GUIManager manager;
	private JPanel brightnessDialog = new JPanel(new GridLayout(3, 1));
	private JSlider brightnessSlider = new JSlider(-255, 255);
	private JSlider contrastSlider = new JSlider(-255, 255);
	private JLabel lblBrightness = new JLabel("0");
	private JLabel lblContrast = new JLabel("0");
	private JCheckBox chkPreview = new JCheckBox("Preview", true);
	
	private BufferedImage orig;
	private DrawEvent drawEvent;
	
	public Brightness(GUIManager pane)
	{
		this.manager = pane;
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		temp.add(new JLabel("Brightness"));
		temp.add(brightnessSlider);
		temp.add(lblBrightness);
		brightnessSlider.addChangeListener(this);
		brightnessDialog.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		temp.add(new JLabel("Contrast"));
		temp.add(contrastSlider);
		temp.add(lblContrast);
		contrastSlider.addChangeListener(this);
		brightnessDialog.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.add(chkPreview);
		chkPreview.addChangeListener(this);
		brightnessDialog.add(temp);
	}
	
	@Override
	public void run()
	{
		brightnessSlider.setValue(0);
		contrastSlider.setValue(0);
		lblBrightness.setText("0");
		lblContrast.setText("0");
		int res = JOptionPane.showConfirmDialog(null, brightnessDialog, "Brightness", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION)
			filter();
		else
		{
			BufferedImage img = drawEvent.getManager().getCurrentLayer().getImage();
			Graphics g = img.getGraphics();
			g.drawImage(orig, 0, 0, null);
			g.dispose();
			manager.getDrawPane().repaint();
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		lblBrightness.setText("" + brightnessSlider.getValue());
		lblContrast.setText("" + contrastSlider.getValue());
		if (chkPreview.isSelected())
			filter();
	}
	
	public void filter()
	{
		BufferedImage img = drawEvent.getManager().getCurrentLayer().getImage();
		Area area = drawEvent.getArea();
		Rectangle bounds = area.getBounds();
		if (drawEvent.getGraphics().getClip() ==  null)
			area.add(new Area(new Rectangle(0, 0, img.getWidth(), img.getHeight())));
		bounds = area.getBounds();
		int db = brightnessSlider.getValue();
		int contrast = contrastSlider.getValue();
		double contrastFactor = (double)(259 * (contrast + 255)) / (255 * (259 - contrast));
		for (int x = bounds.x; x < bounds.x + bounds.width; x++)
		{
			for (int y = bounds.y; y < bounds.y + bounds.height; y++)
			{
				if (area.contains(x, y))
				{
					int rgb   = orig.getRGB(x, y);
					int alpha = (rgb >> 24);
					int red   = clamp(db + ((rgb >> 16) & 0xff));
					int green = clamp(db + ((rgb >> 8) & 0xff));
					int blue  = clamp(db + (rgb & 0xff + db));
					red = clamp((int)(contrastFactor * (red - 128) + 128));
					green = clamp((int)(contrastFactor * (green - 128) + 128));
					blue = clamp((int)(contrastFactor * (blue - 128) + 128));
					img.setRGB(x, y, (alpha << 24) + (red << 16) + (green << 8) + blue);
				}
			}
		}
		manager.getDrawPane().repaint();
	}
	
	private int clamp(int value)
	{
		if (value < 0) return 0;
		else if (value > 255) return 255;
		return value;
	}
	
	public void reset(DrawEvent e)
	{
		BufferedImage img = e.getManager().getCurrentLayer().getImage();
		Graphics g = img.getGraphics();
		g.drawImage(orig, 0, 0, null);
		g.dispose();
	}
	
	@Override
	public void modifyImage(DrawEvent e)
	{
		BufferedImage img = e.getManager().getCurrentLayer().getImage();
		orig = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = orig.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		drawEvent = e;
		new Thread(this).start();
	}
	
	public String getName() { return "Brightness"; }
	public int getShortcut() { return 'B'; }
}