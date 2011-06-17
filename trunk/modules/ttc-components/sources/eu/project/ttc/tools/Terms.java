package eu.project.ttc.tools;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class Terms {

	private Dimension getDimension() {
		return new Dimension(600,400);
	}
	
	private DefaultListModel model;
	
	private void setModel() {
		this.model = new DefaultListModel();
	}
	
	public DefaultListModel getModel() {
		return this.model;
	}
	
	private JList list;
	
	private void setList() {
		this.list = new JList();
		this.list.setModel(this.getModel());
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// TODO this.list.setCellRenderer(new Renderer());
	}
	
	private JList getList() {
		return this.list;
	}
	
	private JScrollPane scroll;
	
	private void setScroll() {
		this.scroll = new JScrollPane();
	    this.scroll.getViewport().add(this.getList());
		this.scroll.setMinimumSize(this.getDimension());
	}
	
	public JScrollPane getComponent() {
		return this.scroll;
	}
	
	public void enableListeners(ListSelectionListener listener) {
		this.getList().addListSelectionListener(listener);
	}
		
	public Terms() {
		this.setModel();
		this.setList();
		this.setScroll();
	}
		
}
