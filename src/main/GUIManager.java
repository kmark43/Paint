package main;

import main.tool.*;
import main.filter.*;
import main.dialog.*;
import main.layer.*;
import main.event.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Manages all the GUI components of the project
*/
public class GUIManager implements ActionListener, ChangeListener
{
	/**
	* A list of tools registered in the tool register
	*/
	private ArrayList<Tool> tools = new ArrayList<Tool>();
	
	/**
	* A list of filters registered in the filter register
	*/
	private ArrayList<Filter> filters = new ArrayList<Filter>();
	
	/**
	* An input map to create shortcuts to access tools
	*/
	private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	
	
	/**
	* An input map to create shortcuts to access tools
	*/
	private HashMap<Integer, JMenuItem> keyFilterMap = new HashMap<Integer, JMenuItem>();
	
	/**
	* The frame that holds the user interface for the project
	*/
	private final JFrame frame = new JFrame("Paint");
	
	/**
	* The foreground color button - changeable by the user
	*/
	private JButton btnForecolor = new JButton("");
	
	/**
	* The background color button - changeable by the user
	*/
	private JButton btnBackcolor = new JButton("");
	
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
	private JMenuItem itmUndo = new JMenuItem("Undo");
	private JMenuItem itmRedo = new JMenuItem("Redo");
	
	private JMenu filterMenu = new JMenu("Filters");
	private JMenu toolMenu = new JMenu("Tools");
	
	private JMenu windowMenu = new JMenu("Window");
	
	private JPanel managerContainer = new JPanel();
	
	private JPanel container = new JPanel();
	private JScrollPane scroll = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	/**
	* The tabbed panel storing the drawing panels
	*/
	private JTabbedPane drawPanels = new JTabbedPane();
	
	/**
	* The selected drawing panel
	*/
	private DrawPanel drawPane;
	
	/**
	* The event to pass into mouse events and key events
	*/
	private DrawEvent drawEvent = new DrawEvent(this);
	
	/**
	* Listens for key presses and calls the proper tool's methods
	*/
	private PanelKey keyListener = new PanelKey(this, drawEvent, keyToolMap, keyFilterMap);
	
	/**
	* Listens for mouse presses and calls the proper tool's methods
	*/
	private PanelMouse mouseListener = new PanelMouse(this, keyListener, drawEvent);
	
	/**
	* The tool to be used when the mouse is pressed on a draw panel
	*/
	private Tool currentTool;
	
	public GUIManager()
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void init()
	{
		ToolRegister.registerTools(this);
		FilterRegister.registerFilters(this);
		
		JPanel mainPane = new JPanel(new BorderLayout());
		
		drawPanels.setPreferredSize(new Dimension(800, 600));
		drawPanels.addChangeListener(this);
		drawPanels.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), "removeTab");
		drawPanels.getActionMap().put("removeTab", new AbstractAction()
		{
			final static long serialVersionUID = 12984798714L;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (drawPanels.getSelectedIndex() != -1)
					drawPanels.remove(drawPanels.getSelectedIndex());
			}
		});
		
		JPanel colorPane = new JPanel(new GridLayout(1, 2));
		
		btnForecolor.setBackground(Color.WHITE);
		btnBackcolor.setBackground(Color.BLACK);
		
		btnForecolor.setActionCommand("foreground");
		btnForecolor.addActionListener(this);
		btnBackcolor.setActionCommand("background");
		btnBackcolor.addActionListener(this);
		
		colorPane.add(btnForecolor);
		colorPane.add(btnBackcolor);
		
		JPanel toolPane = new JPanel(new GridLayout(20, 1));
		
		ButtonGroup bg = new ButtonGroup();
		
		for (Tool t : tools)
			addTool(t, toolMenu, toolPane, bg);
		
		for (Filter f : filters)
			addFilter(f, filterMenu);
		
		toolPane.add(colorPane);
		
		fileMenu.add(itmNew);
		fileMenu.add(itmOpen);
		fileMenu.add(itmSave);
		fileMenu.add(itmSaveAs);
		fileMenu.add(new JSeparator());
		fileMenu.add(itmExit);
		
		JPanel eastPane = new JPanel(new BorderLayout());
		eastPane.add(propertyPane, BorderLayout.NORTH);
		eastPane.add(managerContainer, BorderLayout.SOUTH);
		
		fileMenu  .setMnemonic('F');
		itmNew    .setMnemonic('N');
		itmOpen   .setMnemonic('O');
		itmSave   .setMnemonic('S');
		itmSaveAs .setMnemonic('A');
		itmExit   .setMnemonic('E');
		
		itmNew    .addActionListener(this);
		itmOpen   .addActionListener(this);
		itmSave   .addActionListener(this);
		itmSaveAs .addActionListener(this);
		itmExit   .addActionListener(this);
		
		itmNew    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		itmOpen   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		itmSave   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		itmSaveAs .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		
		editMenu.setMnemonic('E');
		editMenu.add(itmUndo);
		itmUndo.setMnemonic('U');
		itmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		itmUndo.addActionListener(this);
		editMenu.add(itmRedo);
		itmRedo.setMnemonic('R');
		itmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
		itmRedo.addActionListener(this);
		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(filterMenu);
		menu.add(toolMenu);
		menu.add(windowMenu);
		
		mainPane.add(toolPane, BorderLayout.WEST);
		mainPane.add(drawPanels, BorderLayout.CENTER);
		mainPane.add(eastPane, BorderLayout.EAST);
		mainPane.add(menu, BorderLayout.NORTH);
		
		frame.add(mainPane);
		
		removeFocus(mainPane);
		bg.getElements().nextElement().doClick();
	}
	
	/**
	* Adds a new tab to drawPanels
	* @param title The title of the DrawPanel
	* @param pane The DrawPanel to add
	*/
	private void addTab(String title, final DrawPanel pane)
	{
		// drawPanels.addTab(title, pane.getScroll());
		JPanel temp = new JPanel();
		JButton btnExit = new JButton("X");
		temp.add(new JLabel(title));
		temp.add(btnExit);
		drawPanels.add(pane.getScroll());
		drawPanels.setTabComponentAt(drawPanels.getTabCount() - 1, temp);
		btnExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				drawPanels.remove(pane.getScroll());
			}
		});
		btnExit.setOpaque(false);
		btnExit.setBorderPainted(false);
		btnExit.setMargin(new Insets(0, 0, 0, 0));
		// btnExit.setBackground(Color.RED);
		btnExit.setBackground(new Color(0, 0, 0, 0));
		btnExit.setForeground(Color.RED);
		btnExit.setFocusable(false);
		temp.setOpaque(false);
		pane.getScroll().addMouseWheelListener(mouseListener);
		pane.addMouseListener(mouseListener);
		pane.addMouseMotionListener(mouseListener);
		pane.addKeyListener(keyListener);
	}
	
	/**
	* Removes the focusable property of all added components
	* @param root The current component to assign properties of children to
	*/
	private void removeFocus(Container root)
	{
		Component comp[] = root.getComponents();
		root.setFocusable(false);
		for (Component c : comp)
			if (c instanceof Container)
				removeFocus((Container)c);
			else
				c.setFocusable(false);
	}
	
	/**
	* Adds a filter to the filter JMenu
	* @param f The filter to add
	* @param filterMenu The menu to add the button to
	*/
	private void addFilter(Filter f, JMenu filterMenu)
	{
		JMenuItem itm = new JMenuItem(f.getName());
		filterMenu.add(itm);
		
		if (f.getShortcut() != 0)
			keyFilterMap.put(f.getShortcut(), itm);
			
		itm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (drawPane == null) return;
				Graphics2D g = (Graphics2D)drawPane.getLayerManager().getCurrentLayer().getImage().getGraphics();
				drawPane.getDrawEvent().setGraphics(g);
				drawPane.getDrawEvent().init(null, drawPane.getPos());
				drawPane.getLayerManager().addHistory();
				f.modifyImage(drawPane.getDrawEvent());
				drawPane.repaint();
			}
		});
	}
	
	/**
	* Adds a tool to the toolMenu and the toolPane
	* @param t The tool
	* @param toolMenu the menu to add the tool to
	* @param toolPane the panel to add the tool to
	* @param bg the ButtonGroup to add the tool to so only one tool is selected at a time
	*/
	private void addTool(Tool t, JMenu toolMenu, JPanel toolPane, ButtonGroup bg)
	{
		JToggleButton btn = new JToggleButton(t.getName());
		JMenuItem itm = new JMenuItem(t.getName());
		toolPane.add(btn);
		btn.setFocusable(false);
		bg.add(btn);
		
		toolMenu.add(itm);
		
		if (t.getShortcut() != 0)
			keyToolMap.put(t.getShortcut(), btn);
		
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
	
	/**
	* Reassigns the current tool
	* @param tool The new tool to set
	*/
	public void setTool(Tool tool)
	{
		currentTool = tool;
		propertyPane.removeAll();
		propertyPane.add(tool.getProperty());
		// frame.pack();
		frame.revalidate();
	}
	
	/**
	* Called by the ToolRegister to add a new tool
	*/
	public void registerTool(Tool tool) { tools.add(tool); }
	
	/**
	* Called by the FilterRegister to add a new filter
	*/
	public void registerFilter(Filter filter) { filters.add(filter); }
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JMenuItem)
		{
			JMenuItem itm = (JMenuItem)e.getSource();
			JMenu parent = (JMenu)((JPopupMenu)itm.getParent()).getInvoker();
			if (parent == fileMenu)
			{
				if (itm == itmNew)
				{
					NewImageDialog dialog = new NewImageDialog();
					int res = JOptionPane.showConfirmDialog(frame, dialog, "New Image", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (res == JOptionPane.YES_OPTION)
					{
						drawPane = new DrawPanel(this, drawEvent, dialog.getImageWidth(), dialog.getImageHeight(), dialog.getFillType());
						addTab(dialog.getName(), drawPane);
					}
				}
				else if (itm == itmOpen)
				{
					// ImageLoader.open(drawPane, drawPane.getLayerManager());
					JFileChooser fc = new JFileChooser();
					fc.showOpenDialog(null);
					File file = fc.getSelectedFile();
					if (file == null)
						return;
					drawPane = new DrawPanel(this, drawEvent, file);
					addTab(file.getName().substring(0, file.getName().lastIndexOf(".")), drawPane);
				}
				else if (itm == itmSave)
				{
					ImageLoader.save(drawPane, drawPane.getLayerManager());
				}
				else if (itm == itmSaveAs)
				{
					ImageLoader.clearFilePath();
					ImageLoader.save(drawPane, drawPane.getLayerManager());
				}
				else if (itm == itmExit)
					System.exit(0);
			} else if (parent == editMenu)
			{
				if (itm == itmUndo)
					drawPane.getLayerManager().undo();
				else if (itm == itmRedo)
					drawPane.getLayerManager().redo();
			}
		}
		else
		{
			JButton source = (JButton)e.getSource();
			if (e.getActionCommand().equals("foreground"))
			{
				Color c = JColorChooser.showDialog(frame, "Pick Color", drawPane.getForeColor());
				if (c != null)
					drawPane.setForeColor(c);
				source.setBackground(c);
			} else
			{
				Color c = JColorChooser.showDialog(frame, "Pick Color", drawPane.getBackColor());
				if (c != null)
					drawPane.setBackColor(c);
				source.setBackground(c);
			}
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (drawPanels.getSelectedComponent() == null) return;
		drawPane = (DrawPanel)((Container)((Container)((Container)drawPanels.getSelectedComponent()).getComponent(0)).getComponent(0)).getComponent(0);
		drawPane.grabFocus();
		managerContainer.removeAll();
		managerContainer.add(drawPane.getLayerManager());
		frame.revalidate();
	}
	
	public DrawPanel getDrawPane() { return drawPane; }
	public JTabbedPane getDrawPanels() { return drawPanels; }
	public LayerManager getLayerManager() { return drawPane.getLayerManager(); }
	public Color getForeColor() { return btnForecolor.getBackground(); }
	public Color getBackColor() { return btnBackcolor.getBackground(); }
	public Tool getCurrentTool() { return currentTool; }
}