package main;

import main.dialog.*;
import main.tool.*;
import main.layer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JPanel implements MouseListener, MouseMotionListener
{
	final static long serialVersionUID = 214897289174L;
	
	private ArrayList<Tool> tools = new ArrayList<Tool>();
	private LayerManager layerManager = new LayerManager();
	
	private BufferedImage temp;
	
	private JFrame frame = new JFrame();
	
	private JPanel propertyPane = new JPanel();
	private JPanel statusPane   = new JPanel();
	private JPanel toolPane     = new JPanel();
	private JMenuBar menu       = new JMenuBar();
	
	private JMenu fileMenu      = new JMenu("File");
	private JMenuItem itmNew    = new JMenuItem("New");
	private JMenuItem itmOpen   = new JMenuItem("Open");
	private JMenuItem itmSave   = new JMenuItem("Save");
	private JMenuItem itmSaveAs = new JMenuItem("Save As");
	private JMenuItem itmExit   = new JMenuItem("Exit");
	
	private JMenu editMenu = new JMenu("Edit");
	
	private JMenu filterMenu = new JMenu("Filters");
	
	private JMenu toolMenu = new JMenu("Tools");
	
	private JMenu windowMenu = new JMenu("Window");
	
	private float zoom = 1;
	
	private Tool currentTool;
	private Color foreColor = Color.BLACK, backColor = Color.WHITE;
	
	public Main()
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				grabFocus();
			}
		});
		// new NewImageDialog(this, null);
	}
	
	private void init()
	{
		JPanel mainPane = new JPanel(new BorderLayout());
		
		JPanel container = new JPanel();
		container.setLayout(null);
		container.add(this);
		JScrollPane scroll = new JScrollPane(container);
		scroll.setPreferredSize(new Dimension(800, 600));
		
		JPanel toolPane = new JPanel();
		ToolRegister.registerTools(this);
		ButtonGroup bg = new ButtonGroup();
		
		for (Tool t : tools)
			addTool(t, toolMenu, toolPane, bg);
		bg.getElements().nextElement().doClick();
		
		fileMenu.add(itmNew);
		fileMenu.add(itmOpen);
		fileMenu.add(itmSave);
		fileMenu.add(itmSaveAs);
		fileMenu.add(new JSeparator());
		fileMenu.add(itmExit);
		
		Main main = this;
		
		ActionListener fileMenuListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == itmNew)
				{
					new NewImageDialog(main, null);
				} else if (e.getSource() == itmOpen)
				{
					
				} else if (e.getSource() == itmSave)
				{
					
				} else if (e.getSource() == itmSaveAs)
				{
				
				} else if (e.getSource() == itmExit)
				{
					System.exit(0);
				}
			}
		};
		
		itmNew    .setMnemonic('N');
		itmOpen   .setMnemonic('O');
		itmSave   .setMnemonic('S');
		itmSaveAs .setMnemonic('A');
		itmExit   .setMnemonic('E');
		
		itmNew    .addActionListener(fileMenuListener);
		itmOpen   .addActionListener(fileMenuListener);
		itmSave   .addActionListener(fileMenuListener);
		itmSaveAs .addActionListener(fileMenuListener);
		itmExit   .addActionListener(fileMenuListener);
		
		itmNew .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		itmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(filterMenu);
		menu.add(toolMenu);
		menu.add(windowMenu);
		
		mainPane.add(toolPane, BorderLayout.WEST);
		mainPane.add(scroll, BorderLayout.CENTER);
		mainPane.add(propertyPane, BorderLayout.EAST);
		mainPane.add(menu, BorderLayout.NORTH);
		
		frame.add(mainPane);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	private void addTool(Tool t, JMenu toolMenu, JPanel toolPane, ButtonGroup bg)
	{
		JToggleButton btn = new JToggleButton(t.getName());
		JMenuItem itm = new JMenuItem(t.getName());
		toolPane.add(btn);
		btn.setFocusable(false);
		bg.add(btn);
		
		toolMenu.add(itm);
		
		itm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btn.doClick();
			}
		});
		
		btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setTool(t);
			}
		});
	}
	
	public void setTool(Tool tool)
	{
		currentTool = tool;
		propertyPane.removeAll();
		propertyPane.add(tool.getProperty());
		frame.pack();
		frame.revalidate();
	}
	
	public void registerTool(Tool tool)
	{
		tools.add(tool);
	}
	
	public void loadNew(int width, int height, FillType type)
	{
		setSize(width, height);
		switch (type)
		{
			case BACKGROUND:
				layerManager.addLayer(new Layer("Background", width, height, backColor));
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(1);
				break;
			case FOREGROUND:
				layerManager.addLayer(new Layer("Background", width, height, foreColor));
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(1);
				break;
			case TRANSPARENT:
				layerManager.addLayer(new Layer("Default", width, height, new Color(0, 0, 0, 0)));
				layerManager.setSelected(0);
				break;
		}
		temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public DrawEvent getEvent(MouseEvent e)
	{
		Graphics2D bufG   = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
		Graphics2D tempG  = (Graphics2D)temp.getGraphics();
		tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		tempG.setColor(new Color(0, 0, 0, 0));
		tempG.fillRect(0, 0, temp.getWidth(), temp.getHeight());
		tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		DrawEvent event = new DrawEvent(e, bufG, tempG);
		return event;
	}
	
	Color current;
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null) return;
		DrawEvent event = getEvent(e);
		current = e.getButton() == 3 ? backColor : foreColor;
		event.setColor(current);
		currentTool.mouseDown(event);
		event.dispose();
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null) return;
		DrawEvent event = getEvent(e);
		event.setColor(current);
		currentTool.mouseDrag(event);
		event.dispose();
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null) return;
		DrawEvent event = getEvent(e);
		event.setColor(current);
		currentTool.mouseUp(event);
		event.dispose();
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.gray);
		for (int i = 0; i < getWidth(); i += 10)
			for (int j = i % 20; j < getHeight(); j += 20)
				g.fillRect(i, j, 10, 10);
		g.setColor(Color.lightGray);
		for (int i = 0; i < getWidth(); i += 10)
			for (int j = 10 - (i % 20); j < getHeight(); j += 20)
				g.fillRect(i, j, 10, 10);
		layerManager.draw(g, temp, zoom);
	}
	
	public static void main(String args[])
	{
		new Main();
	}
}