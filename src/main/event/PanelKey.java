package main.event;

import main.GUIManager;
import main.DrawPanel;
import main.layer.*;
import main.tool.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class PanelKey implements KeyListener
{
	private GUIManager manager;
	private DrawEvent drawEvent;
	private StringBuilder currentPhrase = new StringBuilder();
	private char initialChar = ' ';
	private boolean phraseActive = false;
	
	private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	
	public PanelKey(GUIManager manager, DrawEvent e, HashMap<Integer, JToggleButton> keyToolMap)
	{
		this.manager = manager;
		drawEvent = e;
		this.keyToolMap = keyToolMap;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
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
						System.out.println("Accept");
						currentPhrase.setLength(0);
						break;
					case KeyEvent.VK_ESCAPE:
						phraseActive = false;
						System.out.println("Cancel");
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
		
		DrawPanel pane = manager.getDrawPane();
		Layer currentLayer = pane.getLayerManager().getCurrentLayer();
		if (currentLayer == null || !currentLayer.isVisible() || pane.getCurrent() == null) return;
		drawEvent.init(e, pane.getPos());
		manager.getDrawPane().getCurrentTool().keyUp(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e){}
	
	public char getInitialChar() { return initialChar; }
	public void setInitialChar(char c) { initialChar = c; }
	public boolean isPhraseActive() { return phraseActive; }
	
	public void clearPhrase()
	{
		currentPhrase.setLength(0);
		initialChar = ' ';
		phraseActive = false;
	}
}