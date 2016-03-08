package main.dialog;

import main.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* This class for user creation of a new project
*/
public class NewImageDialog extends JPanel implements FocusListener
{
	final static long serialVersionUID = 19287497124L;
	
	private JTextField txtName = new JTextField(6);
	private JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 999999, 1));
	private JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 999999, 1));
	private JRadioButton btnFillBackground = new JRadioButton("Fill with background color", true);
	private JRadioButton btnFillForeground = new JRadioButton("Fill with foreground color");
	private JRadioButton btnFillTransparent = new JRadioButton("Fill with transparency");
	
	/**
	* Initializes the graphics and settings of the dialog
	* @param main the main class
	* @param frame the frame to draw in relation to
	*/
	public NewImageDialog()
	{
		JPanel mainPane = new JPanel(new GridLayout(6, 1));
		
		((JSpinner.DefaultEditor)widthSpinner.getEditor()).getTextField().addFocusListener(this);
		((JSpinner.DefaultEditor)heightSpinner.getEditor()).getTextField().addFocusListener(this);
		
		addRow(mainPane, new JLabel("Name:"),   txtName);
		addRow(mainPane, new JLabel("Width:"),  widthSpinner);
		addRow(mainPane, new JLabel("Height:"), heightSpinner);
		addRow(mainPane, btnFillBackground);
		addRow(mainPane, btnFillForeground);
		addRow(mainPane, btnFillTransparent);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(btnFillBackground);
		bg.add(btnFillForeground);
		bg.add(btnFillTransparent);
		
		add(mainPane);
	}
	
	private void addRow(JPanel main, Component... components)
	{
		JPanel temp = new JPanel();
		for (Component c : components)
			temp.add(c);
		main.add(temp);
	}
	
	@Override
	public void focusGained(final FocusEvent e)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JTextField source = (JTextField)e.getSource();
				source.selectAll();
			}
		});
	}
	
	@Override
	public void focusLost(FocusEvent e){}
	
	/**
	* Gets the name of the new current draw panel
	* @return the name  of the current draw panel
	*/
	public String getName() { return txtName.getText(); }
	
	/**
	* Gets the image width
	* @return image width
	*/
	public int getImageWidth() { return (Integer)widthSpinner.getValue(); }
	
	/**
	* Gets the image height
	* @return image height
	*/
	public int getImageHeight() { return (Integer)heightSpinner.getValue(); }
	
	/**
	* Gets the new image fill mode
	* @return the fill mode for new images
	*/
	public FillType getFillType() { return btnFillBackground.isSelected() ? FillType.BACKGROUND : btnFillForeground.isSelected() ? FillType.FOREGROUND : FillType.TRANSPARENT; }
}