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
	
	/**
	* The spinner which specifies the width of the new image
	*/
	private JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 999999, 1));
	
	/**
	* The spinner which specifies the height of the new image
	*/
	private JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(500, 1, 999999, 1));
	
	// /**
	// * The submit button to create the new image
	// */
	// private JButton btnOk = new JButton("OK");
	
	// /**
	// * The cancel button to prevent the creation of the new image
	// */
	// private JButton btnCancel = new JButton("Cancel");
	
	/**
	* The fill background option used to fill the image with the secondary color in a separate layer
	*/
	private JRadioButton btnFillBackground = new JRadioButton("Fill with background color", true);
	
	/**
	* The fill foreground option used to fill the image with the first color in a separate layer
	*/
	private JRadioButton btnFillForeground = new JRadioButton("Fill with foreground color");
	
	/**
	* The fill transparent which does not create a background layer
	*/
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
		
		addRow(mainPane, new JLabel("Width:"), widthSpinner);
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
	
	public int getImageWidth() { return (Integer)widthSpinner.getValue(); }
	public int getImageHeight() { return (Integer)heightSpinner.getValue(); }
	public FillType getFillType() { return btnFillBackground.isSelected() ? FillType.BACKGROUND : btnFillForeground.isSelected() ? FillType.FOREGROUND : FillType.TRANSPARENT; }
}