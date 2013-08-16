package eu.project.ttc.tools.commons;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ProcessingResultListener implements ListSelectionListener, ListDataListener, ChangeListener, TreeSelectionListener {
	
	private ProcessingResultViewer viewer;
	
	public void setViewer(ProcessingResultViewer viewer) {
		this.viewer = viewer;
	}
	
	private ProcessingResultViewer getViewer() {
		return this.viewer;
	}
		
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) { 
			JList list = (JList) event.getSource();
			Object object = list.getSelectedValue();
			try {
				if (object instanceof ProcessingResult) {
					final ProcessingResult result = (ProcessingResult) object;
					this.doUpdate(result);
					
				} else if (object instanceof Type) {
					final Type type = (Type) object;
					this.doUpdate(type);
				} 
			} catch (Exception e) {
				// FIXME, what now ?
			}
		}
	}

	/*
	private void doUpdate(AnnotationFS annotation) throws Exception {
		this.getViewer().doUpdate(annotation);
	}
	*/

	private void doUpdate(Type type) throws Exception {
		this.getViewer().doUpdate(type);
	}
	
	private void doUpdate(ProcessingResult result) throws Exception {
		this.getViewer().doUpdate(result);
	}

	@Override
	public void contentsChanged(ListDataEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intervalAdded(ListDataEvent event) {
		Object object = event.getSource();
		if (object instanceof DefaultListModel) {
			DefaultListModel model = (DefaultListModel) object;
			if (model.getSize() > 0) {
				this.getViewer().doEnable(true);
			}
		}
	}

	@Override
	public void intervalRemoved(ListDataEvent event) {
		Object object = event.getSource();
		if (object instanceof DefaultListModel) {
			DefaultListModel model = (DefaultListModel) object;
			if (model.getSize() <= 0) {
				this.getViewer().doEnable(false);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		JTabbedPane tab = (JTabbedPane) event.getSource();
		int index = tab.getSelectedIndex();
		if (index == 0) {

		} else if (index == 1) {
			
		} else if (index == 3) {
			
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		JTree tree = (JTree) event.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node != null) {
		    Object object = node.getUserObject();
		    if (object instanceof AnnotationFS) {
		    	AnnotationFS annotation = (AnnotationFS) object;
		    	try {
		    		this.getViewer().doUpdate(annotation);
		    	} catch (Exception e) { /* ignore */ }
		    }			
		}
	}
	
}
