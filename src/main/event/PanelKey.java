package main.event;

import main.GUIManager;
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
		if (e.isControlDown())
		{
			int index = e.getKeyChar() - '1';
			if (index < Math.min(9, manager.getDrawPane().getLayerManager().getLayerCount()) && index >= 0)
				manager.getDrawPane().getLayerManager().setSelected(index);
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
		if (manager.getDrawPane().getLayerManager().getCurrentLayer() == null || !manager.getDrawPane().getLayerManager().getCurrentLayer().isVisible() || manager.getDrawPane().getCurrent() == null) return;
		drawEvent.init(e, manager.getDrawPane().getPos());
		manager.getDrawPane().getCurrentTool().keyDown(drawEvent);
		manager.getDrawPane().repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (manager.getDrawPane().getLayerManager().getCurrentLayer() == null || !manager.getDrawPane().getLayerManager().getCurrentLayer().isVisible() || manager.getDrawPane().getCurrent() == null) return;
		drawEvent.init(e, manager.getDrawPane().getPos());
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