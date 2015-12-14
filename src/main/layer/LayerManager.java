package main.layer;

import main.Main;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.util.List;
import java.util.ArrayList;

/**
* This class is to manage layers
*/
public class LayerManager extends JPanel implements ListSelectionListener, KeyListener
{
	/**
	* Stores the layers inside of the table
	*/
	private LayerModel mdl = new LayerModel();
	
	/**
	* The graphical component showing the list of layers
	*/
	private JTable list = new JTable(mdl);
	
	private Main main;
	
	/**
	* Creates a new LayerManager object, initilizing the graphical components of the layer manager JPanel
	* @param main the main class used as a reference to repaint when layers are added
	*/
	public LayerManager(Main main)
	{
		this.main = main;
		list.getSelectionModel().addListSelectionListener(this);
		list.addKeyListener(this);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(200, 200));
		add(scroll);
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
	public void draw(Graphics g, BufferedImage temp, float zoom)
	{
		int index = list.getSelectionModel().getLeadSelectionIndex();
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
		int index = list.getSelectionModel().getLeadSelectionIndex();
		if (index == -1)
			return null;
		return mdl.get(index);
	}
	
	public Layer getLayer(int index)
	{
		return mdl.get(index);
	}
	
	// /**
	// * Returns the layer at the specified index
	// * @param index The index to retrieve
	// */
	// public Layer getLayer(int index) { return layers.get(index); }
	
	public void valueChanged(ListSelectionEvent e)
	{
		main.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	private class LayerModel extends AbstractTableModel
	{
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