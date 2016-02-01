package main.layer;

import main.DrawPanel;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;

import java.util.List;
import java.util.ArrayList;

/**
* This class is to manage layers
*/
public class LayerManager extends JPanel implements ListSelectionListener, KeyListener, ActionListener
{
	final static long serialVersionUID = 12984791824L;
	/**
	* Stores the layers inside of the table
	*/
	private LayerModel mdl = new LayerModel();
	
	private BufferedImage temp;
	
	/**
	* The graphical component showing the list of layers
	*/
	private JTable list = new JTable(mdl);
	
	private JButton btnCopy   = new JButton("Copy");
	private JButton btnDelete = new JButton("Delete");
	private JButton btnAdd    = new JButton("Add");
	
	private DrawPanel main;
	
	/**
	* Creates a new LayerManager object, initilizing the graphical components of the layer manager JPanel
	* @param main the main class used as a reference to repaint when layers are added
	*/
	public LayerManager(DrawPanel main)
	{
		this.main = main;
		setLayout(new GridLayout(2, 1));
		JPanel buttons = new JPanel(new GridLayout(8, 1));
		list.getSelectionModel().addListSelectionListener(this);
		list.addKeyListener(this);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(200, 200));
		add(scroll);
		buttons.add(btnAdd);
		buttons.add(btnCopy);
		buttons.add(btnDelete);
		btnAdd.addActionListener(this);
		btnCopy.addActionListener(this);
		btnDelete.addActionListener(this);
		btnAdd.setFocusable(false);
		btnCopy.setFocusable(false);
		btnDelete.setFocusable(false);
		add(buttons);
	}
	
	/**
	* Adds a new layer to the project
	* @param layer the layer to add
	*/
	public void addLayer(Layer layer)
	{
		mdl.add(layer);
		setSelected(mdl.getRowCount() - 1);
		list.revalidate();
		main.repaint();
		list.scrollRectToVisible(list.getCellRect(mdl.getRowCount() - 1, 0, true));
	}
	
	/**
	* Adds a new layer to the project
	* @param layer the layer to add
	*/
	public void addLayer(Layer layer, int index)
	{
		mdl.add(layer, index);
		setSelected(index);
		list.revalidate();
		main.repaint();
		list.scrollRectToVisible(list.getCellRect(index, 0, true));
	}
	
	/**
	* Selects the specified index of the list
	*/
	public void setSelected(int index)
	{
		list.getSelectionModel().setSelectionInterval(index, index);
	}
	
	/**
	* This method is to merge a range of layers to flatten the amount of data used
	* @param start the first index
	* @param end the last index
	*/
	public void mergeRange(int start, int end)
	{
		
	}
	
	/**
	* Draws each layer to the graphics object
	* @param g The graphics object to draw to
	*/
	public void draw(Graphics g, float zoom)// BufferedImage temp, float zoom)
	{
		int index = list.getSelectionModel().getMinSelectionIndex();
		for (int i = 0; i <= index; i++)
			if (mdl.get(i).isVisible())
				g.drawImage(mdl.get(i).getImage(), mdl.get(i).getX(), mdl.get(i).getY(), (int)(mdl.get(i).getImage().getWidth() * zoom), (int)(mdl.get(i).getImage().getHeight() * zoom), null);
		if (temp != null)
			g.drawImage(temp, 0, 0, (int)(temp.getWidth() * zoom), (int)(temp.getHeight() * zoom), null);
		for (int i = index + 1; i < mdl.getRowCount(); i++)
			if (mdl.get(i).isVisible())
				g.drawImage(mdl.get(i).getImage(), mdl.get(i).getX(), mdl.get(i).getY(), (int)(mdl.get(i).getImage().getWidth() * zoom), (int)(mdl.get(i).getImage().getHeight() * zoom), null);
	}
	
	public Layer getCurrentLayer()
	{
		int index = list.getSelectionModel().getMinSelectionIndex();
		if (index == -1)
			return null;
		return mdl.get(index);
	}
	
	public BufferedImage getTemp() { return temp; }
	
	public void setTemp(BufferedImage temp) { this.temp = temp; }
	
	public Layer getLayer(int index)
	{
		return mdl.get(index);
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		main.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_DELETE:
				delete();
				break;
			case KeyEvent.VK_A:
				if (e.isControlDown())
					add();
				break;
			case KeyEvent.VK_D:
				if (e.isControlDown())
					copy();
				break;
			case KeyEvent.VK_SPACE:
				int index = list.getSelectionModel().getMinSelectionIndex();
				if (index != -1)
					mdl.setValueAt(false, index, 1);
				break;
		}
	}
	
	private void add()
	{
		if (mdl.getRowCount() > 0)
			addLayer(new Layer("New Layer", getCurrentLayer().getImage().getWidth(), getCurrentLayer().getImage().getHeight(), new Color(0, true)), list.getSelectionModel().getMinSelectionIndex() + 1);
	}
	private void copy()
	{
		if (list.getSelectionModel().getMinSelectionIndex() != -1)
			addLayer(new Layer(getCurrentLayer()), list.getSelectionModel().getMinSelectionIndex() + 1);
	}
	
	private void delete()
	{
		int index = list.getSelectionModel().getMinSelectionIndex();
		if (index != -1)
		{
			if (index > 0)
				setSelected(index - 1);
			else
				setSelected(0);
			mdl.remove(index);
		}
		list.revalidate();
		list.repaint();
		main.repaint();
	}
	
	
	@Override
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btnAdd)
			add();
		else if (e.getSource() == btnCopy)
			copy();
		else if (e.getSource() == btnDelete)
			delete();
	}
	
	public int getLayerCount() { return mdl.getRowCount(); }
	
	private class LayerModel extends AbstractTableModel
	{
		final static long serialVersionUID = 987129847l;
		private List<Layer> layers;
		
		public LayerModel()
		{
			this.layers = new ArrayList<Layer>();
		}

		public LayerModel(List<Layer> layers)
		{
			this.layers = new ArrayList<Layer>(layers);
		}
		
		public Layer get(int row) { return layers.get(row); }
		public void add(Layer layer) { layers.add(layer); }
		public void add(Layer layer, int index) { layers.add(index, layer); }
		public void remove(int index) { layers.remove(index); }
		
		public void swap(int x, int y)
		{
			Layer temp = layers.get(y);
			layers.set(y, layers.get(x));
			layers.set(x, temp);
		}

		@Override
		public int getRowCount() { return layers.size(); }

		@Override
		public int getColumnCount() { return 2; }

		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			Object value = "??";
			Layer layer = layers.get(rowIndex);
			switch (columnIndex) {
				case 0:
					value = layer.getName();
					break;
				case 1:
					value = layer.isVisible();
			
			}
			return value;
		}
		
		@Override
		public String getColumnName(int columnIndex)
		{
			switch(columnIndex)
			{
				case 0:
					return "Name";
				case 1:
					return "Visible";
				default:
					return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			switch (columnIndex)
			{
				case 0:
					return String.class;
				case 1:
					return Boolean.class;
				default:
					return null;
			}
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			switch (columnIndex)
			{
				case 0:
					layers.get(rowIndex).setName((String)value);
					break;
				case 1:
					layers.get(rowIndex).setVisible((Boolean)value);
					break;
			}
			main.repaint();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int colIndex) { return true; }
	}
}