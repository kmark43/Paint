package main.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RectangleSelect extends Tool
{
	private Main main;
	private JSpinner spinnerX      = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 0));
	private JSpinner spinnerY      = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 0));
	private JSpinner spinnerWidth  = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 0));
	private JSpinner spinnerHeight = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 0));
	
	public RectangleSelect(Main main)
	{
		this.main = main;
		property.setLayout(new GridLayout(4, 1));
		addRow(property, new JLabel("X:"), spinnerX);
		addRow(property, new JLabel("Y:"), spinnerY);
		addRow(property, new JLabel("Width:"), spinnerWidth);
		addRow(property, new JLabel("Height:"), spinnerHeight);
	}
	
	private void addRow(JPanel pane, JComponent... comps)
	{
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		for (JComponent c : comps)
			temp.add(c);
		pane.add(temp);
	}
	
	public void mouseDown(DrawEvent e)
	{
	}
	
	public void mouseDrag(DrawEvent e)
	{
	}
	
	public void mouseUp(DrawEvent e)
	{
	}
	
	public String getName() { return "Rectangle Select"; }
}