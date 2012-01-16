package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;

public class CollectionProcessingEngineListener extends TransferHandler implements ListSelectionListener, ListDataListener {
	
	private static final long serialVersionUID = -3287738866405506372L;

	private Dunamis dunamis;
	
	public void setDunamis(Dunamis dunamis) {
		this.dunamis = dunamis;
	}
	
	private Dunamis getDunamis() {
		return this.dunamis;
	}
		
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			JList list = (JList) event.getSource();
			Object object = list.getSelectedValue();
			if (object instanceof CollectionProcessingEngine) {
				CollectionProcessingEngine cpe = (CollectionProcessingEngine) object;
				this.getDunamis().getEngineViewer().update(cpe);
				this.getDunamis().getToolBar().getBuild().setEnabled(true);
				this.getDunamis().getToolBar().setCollectionProcessingEngine(cpe);			
				// this.getDunamis().getTypeSystemViewer().update(cpe.getTypeSystem());
			}
		}
	}

	@Override
	public int getSourceActions(JComponent c) {
		if (c instanceof JList) {
			return TransferHandler.MOVE;
		} else {
			return TransferHandler.NONE;
		}
	}
	
	@Override
	public Transferable createTransferable(JComponent c) {
		if (c instanceof JList) {
			JList list = (JList) c;
			CollectionProcessingEngine cpe = (CollectionProcessingEngine) list.getSelectedValue();
			return cpe;
		} else {
			return null;
		}
	}

	@Override
	public boolean canImport(TransferSupport support) {
		for (DataFlavor flavor : support.getTransferable().getTransferDataFlavors()) {
			if (flavor.getHumanPresentableName().equals("UIMA Collection Processing Engine")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		try {
			Component c = support.getComponent();
			if (c instanceof JList) {
				JList.DropLocation location = (JList.DropLocation) support.getDropLocation();
				Transferable t = support.getTransferable();
				DataFlavor flavor = t.getTransferDataFlavors()[0];
				Object object = t.getTransferData(flavor);
				int target = location.getIndex();
				JList list = (JList) c;
				DefaultListModel model = (DefaultListModel) list.getModel();
				if (object instanceof CollectionProcessingEngine) {
					CollectionProcessingEngine cpe = (CollectionProcessingEngine) object;
					int source = model.indexOf(cpe);
					if (source < target) {
						model.removeElementAt(source);
						model.add(target - 1,cpe);
					} else {
						model.add(target,cpe);
						model.removeElementAt(source + 1);
					}
				}
			}
			return true;
	    } catch (Exception e) {
	    	Dunamis.error(e);
	    	return false;
	    }
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
				this.getDunamis().getEngineViewer().doEnable(true);
			}
		}
	}

	@Override
	public void intervalRemoved(ListDataEvent event) {
		Object object = event.getSource();
		if (object instanceof DefaultListModel) {
			DefaultListModel model = (DefaultListModel) object;
			if (model.getSize() <= 0) {
				this.getDunamis().getEngineViewer().doEnable(false);
			}
		}
	}
	
}
