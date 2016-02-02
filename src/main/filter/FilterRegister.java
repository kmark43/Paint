package main.filter;

import main.GUIManager;

/**
* This class is to register all filters to the program, it should be used for any new filtersadded
*/
public class FilterRegister
{
	/**
	* This method registers the filters
	* @parm main The main class
	*/
	public static void registerFilters(GUIManager main)
	{
		main.registerFilter(new Grayscale());
	}
}