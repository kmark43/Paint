package main.event;

import main.GUIManager;
import main.DrawPanel;
import main.layer.*;
import main.tool.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
* Manages key listeners for drawing panel
*/
public class PanelKey implements KeyListener
{
	private GUIManager manager;
	private DrawEvent drawEvent;
	private StringBuilder currentPhrase = new StringBuilder();
	private char initialChar = ' ';
	private boolean phraseActive = false;
	
	private boolean keysDown[] = new boolean[256];
	
	private HashMap<Integer, JToggleButton> keyToolMap   = new HashMap<Integer, JToggleButton>();
	private HashMap<Integer, JMenuItem> keyFilterMap = new HashMap<Integer, JMenuItem>();
	
	public PanelKey(GUIManager manager, DrawEvent e, HashMap<Integer, JToggleButton> keyToolMap, HashMap<Integer, JMenuItem> keyFilterMap)
	{
		this.manager = manager;
		drawEvent = e;
		this.keyToolMap = keyToolMap;
		this.keyFilterMap = keyFilterMap;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (keysDown[e.getKeyCode()]) return;
		try
		{
			keysDown[e.getKeyCode()] = true;
		} catch(Exception ex){}
		DrawPanel pane = manager.getDrawPane();
		LayerManager layerManager = pane.getLayerManager();
		Layer currentLayer = layerManager.getCurrentLayer();
		if (e.isControlDown())
		{
			int index = e.getKeyChar() - '1';
			if (index < Math.min(9, layerManager.getLayerCount()) && index >= 0)
				layerManager.setSelected(index);
		} else if (!e.isAltDown() && !e.isShiftDown())
		{
			if (!phraseActive)
			{
				switch (e.getKeyCode())
				{
					case 'T':
						break;
					case 'F':
						break;
					default:
						return;
				}
				currentPhrase.append(e.getKeyChar());
				initialChar = (char)e.getKeyCode();
				phraseActive = true;
			}
			else
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_ENTER:
						phraseActive = false;
						currentPhrase.setLength(0);
						break;
					case KeyEvent.VK_ESCAPE:
						phraseActive = false;
						currentPhrase.setLength(0);
						break;
					default:
						switch (initialChar)
						{
							case 'T':
								JToggleButton btn = keyToolMap.get(e.getKeyCode());
								if (btn != null)
									btn.doClick();
								phraseActive = false;
								currentPhrase.setLength(0);
								break;
							case 'F':
								JMenuItem itm = keyFilterMap.get(e.getKeyCode());
								if (itm != null)
									itm.doClick();
								phraseActive = false;
								currentPhrase.setLength(0);
								break;
						}
						break;
				}
			}
		}
		if (currentLayer == null || !currentLayer.isVisible() || pane.getCurrent() == null) return;
		drawEvent.init(e, pane.getPos());
		pane.getCurrentTool().keyDown(drawEvent);
		pane.repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		try
		{
			keysDown[e.getKeyCode()] = false;
		} catch(Exception ex){}
		DrawPanel pane = manager.getDrawPane();
		Layer currentLayer = pane.getLayerManager().getCurrentLayer();
		if (currentLayer == null || !currentLayer.isVisible() || pane.getCurrent() == null) return;
		drawEvent.init(e, pane.getPos());
		manager.getDrawPane().getCurrentTool().keyUp(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e){}
	
	/**
	* Determines specified key state
	* @param index The key being checked
	* @return the state of that key
	*/
	public boolean isKeyDown(int index) { return keysDown[index]; }
	
	/**
	* Returns the initial character used for the key phrase
	* @return The initial character
	*/
	public char getInitialChar() { return initialChar; }
	
	/**
	* Sets the initial character used for key phrases
	* @param c the new initial character
	*/
	public void setInitialChar(char c) { initialChar = c; }
	
	/**
	* Determines if a key phrase is active
	* @return phrase status
	*/
	public boolean isPhraseActive() { return phraseActive; }
	
	/**
	* Clears the key phrase to resume normal functionality
	*/
	public void clearPhrase()
	{
		currentPhrase.setLength(0);
		initialChar = ' ';
		phraseActive = false;
	}
}