package main.filter;

import main.GUIManager;
import main.tool.DrawEvent;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import main.layer.*;

public class Multiplier extends Filter implements Runnable, ChangeListener
{
	private GUIManager manager;
	private JPanel multiplierDialog = new JPanel(new GridLayout(2, 1));
	private JSlider multiplierSlider = new JSlider(0, 510, 255);
	private JLabel lblMultiplier = new JLabel("0");
	private JCheckBox chkPreview = new JCheckBox("Preview", true);
	
	private BufferedImage orig;
	private DrawEvent drawEvent;
	
	public Multiplier(GUIManager manager)
	{
		this.manager = manager;
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		temp.add(new JLabel("Multiplier"));
		temp.add(multiplierSlider);
		temp.add(lblMultiplier);
		multiplierSlider.addChangeListener(this);
		multiplierDialog.add(temp);
		temp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temp.add(chkPreview);
		chkPreview.addChangeListener(this);
		multiplierDialog.add(temp);
	}
	
	@Override
	public void run()
	{
		multiplierSlider.setValue(255);
		lblMultiplier.setText("1");
		int res = JOptionPane.showConfirmDialog(null, multiplierDialog, "Multiplier", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
		lblMultiplier.setText("" + ((float)multiplierSlider.getValue() / 255));
		if (chkPreview.isSelected())
			filter();
	}
	
	public void filter()
	{
		BufferedImage img = drawEvent.getManager().getCurrentLayer().getImage();
		Area area = drawEvent.getArea();
		Rectangle bounds = area.getBounds();
		if (drawEvent.getGraphics().getClip() ==  null)
			area.add(new Area(new Rectangle(0, 0, drawEvent.getManager().getCurrentLayer().getImage().getWidth(), drawEvent.getManager().getCurrentLayer().getImage().getHeight())));
		bounds = area.getBounds();
		float dm = (float)multiplierSlider.getValue() / 255;
		for (int x = bounds.x; x < bounds.x + bounds.width; x++)
		{
			for (int y = bounds.y; y < bounds.y + bounds.height; y++)
			{
				if (area.contains(x, y))
				{
					int rgb   = orig.getRGB(x, y);
					int alpha = (rgb >> 24);
					int red   = clamp((int)(dm * ((rgb >> 16) & 0xff)));
					int green = clamp((int)(dm * ((rgb >> 8) & 0xff)));
					int blue  = clamp((int)(dm * (rgb & 0xff)));
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
	
	public String getName() { return "Multiplier"; }
}