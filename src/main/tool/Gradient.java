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
		addRow(property, new JLabel("Ocpacity:"), ocpacitySpinner);
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