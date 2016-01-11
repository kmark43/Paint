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
		main.registerTool(new Erase());
		main.registerTool(new Spray(main));
		main.registerTool(new Fill(main));
		main.registerTool(new RectangleSelect(main));
		main.registerTool(new FreeSelect(main));
		main.registerTool(new Rect());
		main.registerTool(new Oval());
		main.registerTool(new Circle());
	}
}