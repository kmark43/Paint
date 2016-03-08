package main.tool;

import javax.swing.*;
import java.awt.*;

/**
* This class defines methods this program needs to implement proper draw functions
*/
public abstract class Tool
{
	protected JPanel property = new JPanel();
	
	protected SpinnerNumberModel thicknessModel = new SpinnerNumberModel(3, 1, 100, 1);
	
	/**
	* This method is called when the mouse is pressed.
	* Tools should implement this method to do the initial draw
	* call.
	* @param e The DrawEvent to provide information relative to drawing
	*/
	public abstract void mouseDown(DrawEvent e);
	
	/**
	* This method is called when the mouse is dragged.
	* Tools should implement this method to draw while the user moves
	* the mouse across the screen
	* @param e The DrawEvent to provide information relative to drawing
	*/
	public abstract void mouseDrag(DrawEvent e);
	
	/**
	* This method is called when the mouse is released.
	* Tools should implement this method to finalize draw functions and to
	* solidify temporary draws.
	* @param e The DrawEvent to provide information relative to drawing
	*/
	public abstract void mouseUp  (DrawEvent e);
	
	/**
	* This method returns the name of the current tool
	* used to display the name when the user selects tools
	*/
	public abstract String getName();
	
	/**
	* @param e The draw event object to pass to the current tool
	* @return whether or not to repaint
	*/
	public boolean mouseMove(DrawEvent e) { return false; }
	
	/**
	* @param e The draw event for tools to use
	*/
	public void keyDown(DrawEvent e){}
	
	/**
	* @param e The draw event for tools to use
	*/
	public void keyUp(DrawEvent e){}
	
	/**
	* This method returns the property panel used for tool properties like
	* line thickness, radius, etc...
	* @return The property panel for this tool
	*/
	public JPanel getProperty() { return property; }
	
	/**
	* Retrieves shortcut key
	* @return KeyCode for tool shortcut
	*/
	public int getShortcut() { return 0; }
}