package main.tool;

import main.GUIManager;

/**
* This class is to register all tools to the program, it should be used for any new tools added
*/
public class ToolRegister
{
	/**
	* This method registers the tools
	*/
	public static void registerTools(GUIManager main)
	{
		main.registerTool(new Brush());
		main.registerTool(new Erase());
		main.registerTool(new Spray(main.getDrawPane()));
		main.registerTool(new Fill(main.getDrawPane()));
		main.registerTool(new RectangleSelect(main.getDrawPane()));
		main.registerTool(new FreeSelect(main.getDrawPane()));
		main.registerTool(new Rect());
		main.registerTool(new Oval());
		main.registerTool(new Circle());
	}
}