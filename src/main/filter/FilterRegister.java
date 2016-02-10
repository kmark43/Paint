package main.filter;

import main.GUIManager;

/**
* This class is to register all filters to the program, it should be used for any new filters added
*/
public class FilterRegister
{
	/**
	* This method registers the filters
	* @parm main The GUI manager
	*/
	public static void registerFilters(GUIManager main)
	{
		main.registerFilter(new Grayscale());
		main.registerFilter(new Negative());
		main.registerFilter(new Brightness(main.getDrawPane()));
		main.registerFilter(new RedEye());
	}
}