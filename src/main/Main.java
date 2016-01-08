package main;

import main.dialog.*;
import main.tool.*;
import main.filter.*;
import main.layer.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class Main extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, KeyListener
{
	final static long serialVersionUID = 214897289174L;
	
	private ArrayList<Tool> tools = new ArrayList<Tool>();
	private ArrayList<Filter> filters = new ArrayList<Filter>();
	private LayerManager layerManager = new LayerManager(this);
	
	private HashMap<Integer, JToggleButton> keyToolMap = new HashMap<Integer, JToggleButton>();
	
	private String filePath = "";
	
	// private BufferedImage temp;
	
	private JFrame frame = new JFrame("Paint");
	
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
	
	private JPanel container = new JPanel();
	private JScrollPane scroll = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private float zoom = 1;
	
	private Tool currentTool;
	private Color foreColor = Color.BLACK, backColor = Color.WHITE;
	
	private DrawEvent drawEvent = new DrawEvent(layerManager);
	
	private int mouseX, mouseY;
	
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
		
		// try
		// {
			// Thread.sleep(1000);
		// } catch(Exception ex){}
		// grabFocus();
	}
	
	private void init()
	{
		ToolRegister.registerTools(this);
		FilterRegister.registerFilters(this);
		
		JPanel mainPane = new JPanel(new BorderLayout());
		
		container.setLayout(null);
		container.add(this);
		container.addMouseWheelListener(this);
		scroll.setPreferredSize(new Dimension(800, 600));
		
		JPanel colorPane = new JPanel(new GridLayout(1, 2));
		
		JButton btnForecolor = new JButton();
		btnForecolor.setBackground(foreColor);
		
		JButton btnBackcolor = new JButton();
		btnBackcolor.setBackground(backColor);
		
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
		eastPane.add(layerManager, BorderLayout.SOUTH);
		
		Main main = this;
		
		ActionListener fileMenuListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == itmNew)
					new NewImageDialog(main, null);
				else if (e.getSource() == itmOpen)
					open();
				else if (e.getSource() == itmSave)
					save();
				else if (e.getSource() == itmSaveAs)
				{
					filePath = "";
					save();
				}
				else if (e.getSource() == itmExit)
					System.exit(0);
			}
		};
		
		fileMenu  .setMnemonic('F');
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
		
		itmNew    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		itmOpen   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		itmSave   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		itmSaveAs .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(filterMenu);
		menu.add(toolMenu);
		menu.add(windowMenu);
		
		mainPane.add(toolPane, BorderLayout.WEST);
		mainPane.add(scroll, BorderLayout.CENTER);
		mainPane.add(eastPane, BorderLayout.EAST);
		mainPane.add(menu, BorderLayout.NORTH);
		
		frame.add(mainPane);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseWheelListener(new MouseWheelListener() { public void mouseWheelMoved(MouseWheelEvent e) { container.dispatchEvent(e); } });
		removeFocus(mainPane);
		setFocusable(true);
		bg.getElements().nextElement().doClick();
	}
	
	public void removeFocus(Container root)
	{
		Component comp[] = root.getComponents();
		root.setFocusable(false);
		for (Component c : comp)
			if (c instanceof Container)
				removeFocus((Container)c);
			else
				c.setFocusable(false);
	}
	
	private void addFilter(Filter f, JMenu filterMenu)
	{
		JMenuItem itm = new JMenuItem(f.getName());
		filterMenu.add(itm);
		Main main = this;
		itm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				f.modifyImage(layerManager);
				main.repaint();
			}
		});
	}
	
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
	
	public void setTool(Tool tool)
	{
		currentTool = tool;
		propertyPane.removeAll();
		propertyPane.add(tool.getProperty());
		frame.pack();
		frame.revalidate();
	}
	
	public void registerTool(Tool tool) { tools.add(tool); }
	public void registerFilter(Filter filter) { filters.add(filter); }
	
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
			BufferedImage img = ImageIO.read(file);
			if (layerManager.getCurrentLayer() == null)
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				double width = (double)container.getWidth() / img.getWidth();
				double height = (double)container.getHeight() / img.getHeight();
				// setSize(1, 1);
				if (width < 1 && height < 1)
					setZoom((float)Math.max(width * .9, height * .9));
				else
					setSize(img.getWidth(), img.getHeight());
			} else
			{
				layerManager.addLayer(new Layer(file.getName(), img));
				int width = (int)Math.max(img.getWidth(), getWidth());
				int height = (int)Math.max(img.getHeight(), getHeight());
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
	
	public DrawEvent getEvent(MouseEvent e)
	{
		// Graphics2D bufG   = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
		// Graphics2D tempG  = (Graphics2D)temp.getGraphics();
		// tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		// tempG.setColor(new Color(0, 0, 0, 0));
		// tempG.fillRect(0, 0, temp.getWidth(), temp.getHeight());
		// tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		// DrawEvent event = new DrawEvent(e, bufG, tempG, zoom, layerManager);
		// event.setColor(current);
		// return event;
		return null;
	}
	
	public DrawEvent getEvent(KeyEvent e)
	{
		// Graphics2D bufG   = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
		// Graphics2D tempG  = (Graphics2D)temp.getGraphics();
		// tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		// tempG.setColor(new Color(0, 0, 0, 0));
		// tempG.fillRect(0, 0, temp.getWidth(), temp.getHeight());
		// tempG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		// DrawEvent event = new DrawEvent(e, bufG, tempG, zoom, mouseX, mouseY, layerManager);
		// event.setColor(current);
		// return event;
		return null;
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		container.setPreferredSize(new Dimension(width, height));
		layerManager.setTemp(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		if (drawEvent.getTempG() != null)
			drawEvent.getTempG().dispose();
		// drawEvent.setTempG((Graphics2D)layerManager.getTemp().getGraphics());
		drawEvent.setTempG();
		scroll.revalidate();
	}
	
	Color current;
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible()) return;
		drawEvent.setTempG();
		switch(e.getButton())
		{
			case 1:
				current = foreColor;
				break;
			case 3:
				current = backColor;
				break;
			default: current = null; return;
		}
		if (drawEvent.getGraphics() != null)
			drawEvent.getGraphics().dispose();
		Graphics2D g = (Graphics2D)layerManager.getCurrentLayer().getImage().getGraphics();
		drawEvent.setGraphics(g);
		// DrawEvent event = getEvent(e);
		drawEvent.init(e);
		drawEvent.setColor(current);
		currentTool.mouseDown(drawEvent);
		// event.dispose();
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || current == null) return;
		// DrawEvent event = getEvent(e);
		drawEvent.init(e);
		currentTool.mouseDrag(drawEvent);
		// event.dispose();
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || current == null) return;
		// DrawEvent event = getEvent(e);
		drawEvent.init(e);
		currentTool.mouseUp(drawEvent);
		repaint();
		// event.dispose();
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int dr = e.getWheelRotation();	// 1 down, -1 up
		if (e.isControlDown())
		{
			float zoom = this.zoom * (float)Math.pow(.99, dr);
			// setZoom(zoom);
			setZoom(zoom, e.getX(), e.getY());
		}
		else
			scroll.dispatchEvent(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		JToggleButton btn = keyToolMap.get(e.getKeyCode());
		if (btn != null)
		{
			btn.doClick();
			return;
		}
		if (e.isControlDown())
		{
			int index = e.getKeyChar() - '1';
			if (index < Math.min(9, layerManager.getLayerCount()) && index >= 0)
				layerManager.setSelected(index);
		}
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || current == null) return;
		// DrawEvent event = getEvent(e);
		drawEvent.init(e, mouseX, mouseY);
		currentTool.keyDown(drawEvent);
		repaint();
		// event.dispose();
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (layerManager.getCurrentLayer() == null || !layerManager.getCurrentLayer().isVisible() || current == null) return;
		// DrawEvent event = getEvent(e);
		drawEvent.init(e, mouseX, mouseY);
		currentTool.keyUp(drawEvent);
		repaint();
		// event.dispose();
	}
	
	@Override
	public void keyTyped(KeyEvent e){}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		JColorChooser chooser = new JColorChooser();
		JButton source = (JButton)e.getSource();
		if (e.getActionCommand().equals("foreground"))
		{
			Color c = chooser.showDialog(frame, "Pick Color", foreColor);
			if (c != null)
				foreColor = c;
			source.setBackground(foreColor);
		} else
		{
			Color c = chooser.showDialog(frame, "Pick Color", backColor);
			if (c != null)
				backColor = c;
			source.setBackground(backColor);
		}
	}
	
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
	
	@Override
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
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
	}
	
	public static void main(String args[])
	{
		new Main();
	}
}