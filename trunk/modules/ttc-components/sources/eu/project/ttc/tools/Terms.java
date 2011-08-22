package eu.project.ttc.tools;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class Terms {

	private Dimension getDimension() {
		return new Dimension(600,400);
	}
		
	public void setModel(ListModel model) {
 		this.list.setModel(model);
	}
		
	private JList list;
	
	private void setList() {
		this.list = new JList();
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
		this.setList();
		this.setScroll();
	}
		
}
