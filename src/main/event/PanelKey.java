package main.event;

import main.DrawPanel;
import main.layer.*;
import main.tool.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class PanelKey implements KeyListener
{
	private DrawPanel drawPanel;
	private DrawEvent drawEvent;
	private LayerManager layerManager;
	private StringBuilder currentPhrase = new StringBuilder();
	private char initialChar = ' ';
	private boolean phraseActive = false;
	
	private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	
	public PanelKey(DrawPanel drawPanel, DrawEvent e, LayerManager layerManager, HashMap<Integer, JToggleButton> keyToolMap)
	{
		this.drawPanel = drawPanel;
		drawEvent = e;
		this.layerManager = layerManager;
		this.keyToolMap = keyToolMap;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
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
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || drawPanel.getCurrent() == null) return;
		drawEvent.init(e, drawPanel.getPos());
		drawPanel.getCurrentTool().keyDown(drawEvent);
		drawPanel.repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || drawPanel.getCurrent() == null) return;
		drawEvent.init(e, drawPanel.getPos());
		drawPanel.getCurrentTool().keyUp(drawEvent);
		drawPanel.repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e){}
}