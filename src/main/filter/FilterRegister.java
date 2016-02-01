package main.filter;

import main.DrawPanel;

/**
* This class is to register all filters to the program, it should be used for any new filtersadded
*/
public class FilterRegister
{
	/**
	* This method registers the filters
	* @parm main The main class
	*/
	public static void registerFilters(DrawPanel main)
	{
		main.registerFilter(new Grayscale());
	}
}