package eu.project.ttc.tools;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

public class Terms {

	private Dimension getDimension() {
		return new Dimension(600,400);
	}
		
	public void setModel(TreeModel model) {
 		this.tree.setModel(model);
	}
	
	private JTree tree;
	
	private void setTree() {
		this.tree = new JTree();
		this.tree.setRootVisible(false);
		this.tree.expandRow(1);
		// this.tree.setSelectionModel(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// TODO this.list.setCellRenderer(new Renderer());
	}
	
	private JTree getTree() {
		return this.tree;
	}
	
	/*
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
	*/
	
	private JScrollPane scroll;
	
	private void setScroll() {
		this.scroll = new JScrollPane();
	    this.scroll.getViewport().add(this.getTree());
		this.scroll.setMinimumSize(this.getDimension());
	}
	
	public JScrollPane getComponent() {
		return this.scroll;
	}
	
	public void enableListeners(ListSelectionListener listener) {
		// this.getList().addListSelectionListener(listener);
	}
		
	public Terms() {
		this.setTree();
		this.setScroll();
	}
		
}
