package main.filter;

import main.layer.*;
import main.tool.DrawEvent;

/**
* This class defines the methods required for making a filter
*/
public abstract class Filter
{
	/**
	* Runs the current filter on the image
	* @param e The draw event for the current event
	*/
	public abstract void modifyImage(DrawEvent e);
	
	/**
	* @return The name of the filter
	*/
	public abstract String getName();
}