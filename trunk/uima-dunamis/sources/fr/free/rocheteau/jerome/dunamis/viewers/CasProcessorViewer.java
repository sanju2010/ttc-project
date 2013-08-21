package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.listeners.CasProcessorListener;

public class CasProcessorViewer {
	
	private Dimension dimension;
	
	private void setDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screen.width / 5;
		int height = width * 3;
		this.dimension = new Dimension(width,height);
	}
	
	private Dimension getDimension() {
		return this.dimension;
	}
	
	public CasProcessorViewer(String title) {
		this.setTitle(title);
		this.setDimension();
		this.setAvailableRoot();
		this.setAvailableModel();
		this.setAvailableTree();
		this.setAvailableScroll();
		this.setSelectedList();
		this.setSelectedScroll();
		this.setSplit();
	}
	
	private DefaultMutableTreeNode availableRoot;
	
	private void setAvailableRoot() {
		this.availableRoot = new DefaultMutableTreeNode();
	}
	
	private DefaultMutableTreeNode getAvailableRoot() {
		return this.availableRoot;
	}
	
	private DefaultTreeModel availableModel;

	public void setAvailableModel() {
		this.availableModel = new DefaultTreeModel(this.getAvailableRoot());
		
	}

	public DefaultTreeModel getAvailableModel() {
		return availableModel;
	}

	private JTree availableTree;
	
	private void setAvailableTree() {
		this.availableTree = new JTree();
		this.availableTree.setModel(this.getAvailableModel());
		this.availableTree.setRootVisible(false);
		this.availableTree.setDragEnabled(true);
		this.availableTree.setDropMode(DropMode.INSERT);
	}

	private JTree getAvailableTree() {
		return this.availableTree;
	}

	private JScrollPane availableScroll; 
	
	private void setAvailableScroll() {
		this.availableScroll = new JScrollPane();
		this.availableScroll.getViewport().add(this.getAvailableTree());
	    TitledBorder availableTitle = BorderFactory.createTitledBorder("Available " + this.getTitle());
	    this.availableScroll .setBorder(availableTitle);
	    this.availableScroll.setPreferredSize(this.getDimension());
	}
	
	private JScrollPane getAvailableScroll() {
		return this.availableScroll;
	}
	
	public void setSelectedModel(DefaultListModel model) {
		this.getSelectedList().setModel(model);
		this.listener.setSelectedCasProcessors(model);
	}
	
	private JList selectedList;
	
	private void setSelectedList() {
		this.selectedList = new JList();
		this.selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.selectedList.setDragEnabled(true);
		this.selectedList.setDropMode(DropMode.INSERT);
	}

	private JList getSelectedList() {
		return this.selectedList;
	}

	private JScrollPane selectedScroll; 
	
	private void setSelectedScroll() {
		this.selectedScroll = new JScrollPane();
		this.selectedScroll.getViewport().add(this.getSelectedList());
	    TitledBorder selectedTitle = BorderFactory.createTitledBorder("Selected " + this.getTitle());
	    this.selectedScroll.setBorder(selectedTitle);
	    this.selectedScroll.setPreferredSize(this.getDimension());
	}
	
	private JScrollPane getSelectedScroll() {
		return this.selectedScroll;
	}

	private JSplitPane split;
	
	private void setSplit() {
		this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.split.setLeftComponent(this.getAvailableScroll());
		this.split.setRightComponent(this.getSelectedScroll());
		this.split.setResizeWeight(0.5);
		// this.split.setEnabled(false);
		// this.split.setDividerSize(0);
	}

	public JSplitPane getComponent() {
		return this.split;
	}
	
	private CasProcessorListener listener;
	
	public void enableListeners(Dunamis dunamis,JList collectionProcessingEngines) {
		this.listener = new CasProcessorListener();
		this.listener.setDunamis(dunamis);
		this.listener.setCollectionProcessingEngines(collectionProcessingEngines);
		this.listener.setAvailableCasProcessors(this.getAvailableModel());
		this.getAvailableTree().addMouseListener(this.listener);
		this.getAvailableTree().setTransferHandler(this.listener);
		this.getSelectedList().addMouseListener(this.listener);
		this.getSelectedList().setTransferHandler(this.listener);
	}
	
	private String title;
	
	private void setTitle(String title) {
		this.title = title;
	}
	
	private String getTitle() {
		return this.title;
	}
	
	public void doEnable(boolean enabled) {
		this.getAvailableTree().setEnabled(enabled);
		this.getSelectedList().setEnabled(enabled);
	}
		
}
