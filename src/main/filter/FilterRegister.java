package main.filter;

import main.Main;

/**
* This class is to register all filters to the program, it should be used for any new filtersadded
*/
public class FilterRegister
{
	/**
	* This method registers the filters
	* @parm main The main class
	*/
	public static void registerFilters(Main main)
	{
		main.registerFilter(new Grayscale());
	}
}