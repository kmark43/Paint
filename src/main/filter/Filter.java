package main.filter;

import main.layer.*;

/**
* This class difines the methods required for making a filter
*/
public abstract class Filter
{
	/**
	* Runs the current filter on the image
	* @param layerManager The component to access each layer when filtering
	*/
	public abstract void modifyImage(LayerManager layerManager);
	
	/**
	* @return The name of the filter
	*/
	public abstract String getName();
}