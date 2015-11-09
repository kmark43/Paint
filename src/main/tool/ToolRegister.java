package main.tool;

import main.Main;

/**
* This class is to register all tools to the program, it should be used for any new tools added
*/
public class ToolRegister
{
	/**
	* This method registers the tools
	*/
	public static void registerTools(Main main)
	{
		main.registerTool(new Brush());
	}
}