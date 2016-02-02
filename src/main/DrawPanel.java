package main;

import main.dialog.*;
import main.tool.*;
import main.filter.*;
import main.layer.*;
import main.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class DrawPanel extends JPanel //implements ActionListener
{
	final static long serialVersionUID = 214897289174L;
	
	// private ArrayList<Tool> tools = new ArrayList<Tool>();
	// private ArrayList<Filter> filters = new ArrayList<Filter>();
	// private LayerManager layerManager = new LayerManager(this);
	private LayerManager layerManager;
	
	private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	private StringBuilder currentPhrase = new StringBuilder();
	private char initialChar = ' ';
	private boolean phraseActive = false;
	
	private Color foreColor = Color.BLACK, backColor = Color.WHITE;
	
	private String filePath = "";
	
	// private JFrame frame = new JFrame("Paint");
	
	// private JPanel propertyPane = new JPanel();
	// private JPanel statusPane   = new JPanel();
	// private JPanel toolPane     = new JPanel();
	// private JMenuBar menu       = new JMenuBar();
	
	// private JMenu fileMenu      = new JMenu("File");
	// private JMenuItem itmNew    = new JMenuItem("New");
	// private JMenuItem itmOpen   = new JMenuItem("Open");
	// private JMenuItem itmSave   = new JMenuItem("Save");
	// private JMenuItem itmSaveAs = new JMenuItem("Save As");
	// private JMenuItem itmExit   = new JMenuItem("Exit");
	
	// private JMenu editMenu = new JMenu("Edit");
	
	
	// private JMenu filterMenu = new JMenu("Filters");
	// private JMenu toolMenu = new JMenu("Tools");
	
	// private JMenu windowMenu = new JMenu("Window");
	
	// private JPanel container = new JPanel();
	// private JScrollPane scroll = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private JPanel container;
	private JScrollPane scroll;
	
	private float zoom = 1;
	
	
	private Tool currentTool;
	private Color current;
	private Point pos = new Point();
	
	private DrawEvent drawEvent;
	
	// private int mouseX, mouseY;
	
	public DrawPanel(LayerManager layerManager, JScrollPane scroll, JPanel container)
	{
		this.layerManager = layerManager;
		this.scroll = scroll;
		this.container = container;
		
		drawEvent = new DrawEvent(layerManager);
		
		PanelMouse mouseListener = new PanelMouse(this, drawEvent, layerManager, scroll);
		PanelKey keyListener = new PanelKey(this, drawEvent, layerManager, keyToolMap);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addKeyListener(keyListener);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// init();
		// frame.pack();
		// frame.setLocationRelativeTo(null);
		// frame.setVisible(true);
		
		// SwingUtilities.invokeLater(new Runnable()
		// {
			// public void run()
			// {
				// grabFocus();
			// }
		// });
	}
	
	// private void init()
	// {
		// ToolRegister.registerTools(this);
		// FilterRegister.registerFilters(this);
		// PanelMouse mouseListener = new PanelMouse(this, drawEvent, layerManager, scroll);
		// PanelKey keyListener = new PanelKey(this, drawEvent, layerManager, keyToolMap);
		
		// JPanel mainPane = new JPanel(new BorderLayout());
		
		// container.setLayout(null);
		// container.add(this);
		// container.addMouseWheelListener(this);
		// scroll.setPreferredSize(new Dimension(800, 600));
		
		// JPanel colorPane = new JPanel(new GridLayout(1, 2));
		
		// JButton btnForecolor = new JButton();
		// btnForecolor.setBackground(foreColor);
		
		// JButton btnBackcolor = new JButton();
		// btnBackcolor.setBackground(backColor);
		
		// btnForecolor.setActionCommand("foreground");
		// btnForecolor.addActionListener(this);
		// btnBackcolor.setActionCommand("background");
		// btnBackcolor.addActionListener(this);
		
		// colorPane.add(btnForecolor);
		// colorPane.add(btnBackcolor);
		
		// JPanel toolPane = new JPanel(new GridLayout(20, 1));
		
		// ButtonGroup bg = new ButtonGroup();
		
		// for (Tool t : tools)
			// addTool(t, toolMenu, toolPane, bg);
		
		// for (Filter f : filters)
			// addFilter(f, filterMenu);
		
		// toolPane.add(colorPane);
		
		// fileMenu.add(itmNew);
		// fileMenu.add(itmOpen);
		// fileMenu.add(itmSave);
		// fileMenu.add(itmSaveAs);
		// fileMenu.add(new JSeparator());
		// fileMenu.add(itmExit);
		
		// JPanel eastPane = new JPanel(new BorderLayout());
		// eastPane.add(propertyPane, BorderLayout.NORTH);
		// eastPane.add(layerManager, BorderLayout.SOUTH);
		
		// DrawPanel main = this;
		
		// ActionListener fileMenuListener = new ActionListener()
		// {
			// @Override
			// public void actionPerformed(ActionEvent e)
			// {
				// if (e.getSource() == itmNew)
					// new NewImageDialog(main, null);
				// else if (e.getSource() == itmOpen)
					// open();
				// else if (e.getSource() == itmSave)
					// save();
				// else if (e.getSource() == itmSaveAs)
				// {
					// filePath = "";
					// save();
				// }
				// else if (e.getSource() == itmExit)
					// System.exit(0);
			// }
		// };
		
		// fileMenu  .setMnemonic('F');
		// itmNew    .setMnemonic('N');
		// itmOpen   .setMnemonic('O');
		// itmSave   .setMnemonic('S');
		// itmSaveAs .setMnemonic('A');
		// itmExit   .setMnemonic('E');
		
		// itmNew    .addActionListener(fileMenuListener);
		// itmOpen   .addActionListener(fileMenuListener);
		// itmSave   .addActionListener(fileMenuListener);
		// itmSaveAs .addActionListener(fileMenuListener);
		// itmExit   .addActionListener(fileMenuListener);
		
		// itmNew    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		// itmOpen   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		// itmSave   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		// itmSaveAs .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		
		// menu.add(fileMenu);
		// menu.add(editMenu);
		// menu.add(filterMenu);
		// menu.add(toolMenu);
		// menu.add(windowMenu);
		
		// mainPane.add(toolPane, BorderLayout.WEST);
		// mainPane.add(scroll, BorderLayout.CENTER);
		// mainPane.add(eastPane, BorderLayout.EAST);
		// mainPane.add(menu, BorderLayout.NORTH);
		
		// frame.add(mainPane);
		
		// addMouseWheelListener(new MouseWheelListener() { public void mouseWheelMoved(MouseWheelEvent e) { container.dispatchEvent(e); } });
		// addMouseListener(mouseListener);
		// addMouseMotionListener(mouseListener);
		// addKeyListener(keyListener);
		// removeFocus(mainPane);
		// setFocusable(true);
		// bg.getElements().nextElement().doClick();
	// }
	
	// public void removeFocus(Container root)
	// {
		// Component comp[] = root.getComponents();
		// root.setFocusable(false);
		// for (Component c : comp)
			// if (c instanceof Container)
				// removeFocus((Container)c);
			// else
				// c.setFocusable(false);
	// }
	
	// private void addFilter(Filter f, JMenu filterMenu)
	// {
		// JMenuItem itm = new JMenuItem(f.getName());
		// filterMenu.add(itm);
		// DrawPanel main = this;
		// itm.addActionListener(new ActionListener()
		// {
			// @Override
			// public void actionPerformed(ActionEvent e)
			// {
				// Graphics2D g = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
				// drawEvent.setGraphics(g);
				// drawEvent.init(null, pos);
				// f.modifyImage(drawEvent);
				// main.repaint();
			// }
		// });
	// }
	
	// private void addTool(Tool t, JMenu toolMenu, JPanel toolPane, ButtonGroup bg)
	// {
		// JToggleButton btn = new JToggleButton(t.getName());
		// JMenuItem itm = new JMenuItem(t.getName());
		// toolPane.add(btn);
		// btn.setFocusable(false);
		// bg.add(btn);
		
		// toolMenu.add(itm);
		
		// if (t.getShortcut() != 0)
			// keyToolMap.put(t.getShortcut(), btn);
		
		// itm.addActionListener(new ActionListener()
		// {
			// @Override
			// public void actionPerformed(ActionEvent e)
			// {
				// btn.doClick();
			// }
		// });
		
		// btn.addActionListener(new ActionListener()
		// {
			// @Override
			// public void actionPerformed(ActionEvent e)
			// {
				// setTool(t);
			// }
		// });
	// }
	
	public void setTool(Tool tool)
	{
		currentTool = tool;
		// propertyPane.removeAll();
		// propertyPane.add(tool.getProperty());
		// frame.pack();
		// frame.revalidate();
	}
	
	// public void registerTool(Tool tool) { tools.add(tool); }
	// public void registerFilter(Filter filter) { filters.add(filter); }
	
	public void open()
	{
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File file = fc.getSelectedFile();
		if (file == null)
			return;
		open(file);
	}
	
	public void open(File file)
	{
		try
		{
			Image originalImage = ImageIO.read(file);
			BufferedImage img = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			g.drawImage(originalImage, 0, 0, null);
			g.dispose();
			if (layerManager.getCurrentLayer() == null)
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				double width = (double)container.getWidth() / img.getWidth();
				double height = (double)container.getHeight() / img.getHeight();
				if (width < 1 && height < 1)
					setZoom((float)Math.max(width * .9, height * .9));
				else
					setSize(img.getWidth(), img.getHeight());
			} else
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				int width = Math.max(img.getWidth(), getWidth());
				int height = Math.max(img.getHeight(), getHeight());
				setSize(width, height);
			}
			
			filePath = file.getPath();
			
			layerManager.setTemp(new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB));
		} catch(IOException ex)
		{
			System.err.println("Error loading " + file.getPath());
			ex.printStackTrace();
		}
		grabFocus();
	}
	
	public void save()
	{
		if (filePath.equals(""))
		{
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(null);
			File file = fc.getSelectedFile();
			if (file == null)
				return;
			save(file);
		} else
			save(new File(filePath));
	}
	
	public void save(File file)
	{
		try
		{
			BufferedImage img = new BufferedImage(layerManager.getCurrentLayer().getImage().getWidth(), layerManager.getCurrentLayer().getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)img.getGraphics();
			layerManager.draw(g, 1);
			ImageIO.write(img, "PNG", file);
			filePath = file.getPath();
		} catch(IOException ex)
		{
			System.err.println("Error saving " + file.getPath());
			ex.printStackTrace();
		}
	}
	
	public void loadNew(int width, int height, FillType type)
	{
		setSize(width, height);
		container.setPreferredSize(new Dimension(width, height));
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
		layerManager.setTemp(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		grabFocus();
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		container.setPreferredSize(new Dimension(width, height));
		layerManager.setTemp(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		if (drawEvent.getTempG() != null)
			drawEvent.getTempG().dispose();
		drawEvent.setTempG();
		scroll.revalidate();
	}
	
	// @Override
	// public void actionPerformed(ActionEvent e)
	// {
		// JButton source = (JButton)e.getSource();
		// if (e.getActionCommand().equals("foreground"))
		// {
			// Color c = JColorChooser.showDialog(frame, "Pick Color", foreColor);
			// if (c != null)
				// foreColor = c;
			// source.setBackground(foreColor);
		// } else
		// {
			// Color c = JColorChooser.showDialog(frame, "Pick Color", backColor);
			// if (c != null)
				// backColor = c;
			// source.setBackground(backColor);
		// }
	// }
	
	public void setZoom(float zoom)
	{
		if (layerManager.getLayer(0).getImage() != null)
		{
			setSize((int)(layerManager.getLayer(0).getImage().getWidth() * zoom), (int)(layerManager.getLayer(0).getImage().getHeight() * zoom));
			this.zoom = zoom;
			drawEvent.setZoom(zoom);
		}
	}
	
	public void setZoom(float zoom, int focusX, int focusY)
	{
		if (layerManager.getLayer(0).getImage() != null)
		{
			float dzoom = zoom - this.zoom + 1;
			setSize((int)(layerManager.getLayer(0).getImage().getWidth() * zoom), (int)(layerManager.getLayer(0).getImage().getHeight() * zoom));
			Rectangle view = scroll.getViewport().getViewRect();
			view.x = (int)(view.x * dzoom);
			view.y = (int)(view.y * dzoom);
			scroll.getViewport().scrollRectToVisible(view);
			this.zoom = zoom;
			drawEvent.setZoom(zoom);
		}
	}
	
	public float getZoom() { return zoom; }
	
	public void setPos(Point p) { this.pos = p; }
	public Point getPos() { return pos; }
	public Tool getCurrentTool() { return currentTool; }
	public void setCurrent(Color current) { this.current = current; }
	public Color getCurrent() { return current; }
	public boolean isPhraseActive() { return phraseActive; }
	public void setPhraseActive() { phraseActive = true; }
	public StringBuilder getCurrentPhrase() { return currentPhrase; }
	
	public void setForeColor(Color c) { foreColor = c; }
	public void setBackColor(Color c) { backColor = c; }
	
	public Color getForeColor() { return foreColor; }
	public Color getBackColor() { return backColor; }
	
	public DrawEvent getDrawEvent() { return drawEvent; }
	
	public void clearPhrase()
	{
		phraseActive = false;
		currentPhrase.setLength(0);
	}
	
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
		
		layerManager.draw(g, zoom);
		if (drawEvent.getTempG().getClip() != null)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.scale(zoom, zoom);
			g2.setColor(Color.blue);
			Shape outline = drawEvent.getTempG().getClip();
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
			g2.draw(outline);
		}
	}
}