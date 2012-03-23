package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.util.InvalidXMLException;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;
import fr.free.rocheteau.jerome.dunamis.viewers.MetaDataViewer;
import fr.free.rocheteau.jerome.dunamis.viewers.ParameterViewer;

public class CasProcessorListener extends TransferHandler implements MouseListener {
	
	private static final long serialVersionUID = -1070046872734471657L;
	
	private JList collectionProcessingEngines;
	
	private Dunamis dunamis;
	
	public void setDunamis(Dunamis dunamis) {
		this.dunamis = dunamis;
	}
	
	private JFrame getFrame() {
		return this.dunamis.getFrame();
	}
	
	public void setCollectionProcessingEngines(JList list) {
		this.collectionProcessingEngines = list;
	}
	
	public JList getCollectionProcessingEngines() {
		return this.collectionProcessingEngines;
	}
	
	private DefaultTreeModel availableCasProcessors;
	
	public void setAvailableCasProcessors(DefaultTreeModel model) {
		this.availableCasProcessors = model;
	}
	
	@SuppressWarnings("unused")
	private DefaultTreeModel getAvailableCasProcessors() {
		return this.availableCasProcessors;
	}
	
	private DefaultListModel selectedCasProcessors;
	
	public void setSelectedCasProcessors(DefaultListModel model) {
		this.selectedCasProcessors = model;
	}
	
	private DefaultListModel getSelectedCasProcessors() {
		return this.selectedCasProcessors;
	}
	
	private void show(CasProcessor proc) {
		ParameterViewer viewer = new ParameterViewer(proc);
		viewer.show();
	}
	
	private void info(CasProcessor proc) {
		try {
			MetaDataViewer viewer = new MetaDataViewer(proc);
			viewer.show();
		} catch (InvalidXMLException e) {
			Dunamis.error(e);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Object object = event.getSource();
			if (object instanceof JList && this.getCollectionProcessingEngines().getSelectedIndex() != -1) {
				JList list = (JList) object;
				Object value = list.getSelectedValue();
				if (value instanceof CasProcessor) {
					CasProcessor proc = (CasProcessor) value;
					this.show(proc);
				} 
			} else if (object instanceof JTree) {
				JTree tree = (JTree) object;
				Object value = tree.getLastSelectedPathComponent();
				if (value instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
					Object userObject = node.getUserObject();
					if (userObject instanceof CasProcessor) {
						CasProcessor proc = (CasProcessor) userObject;
						this.info(proc);
					}
				}
			} 
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub

	}
	
	private final int NONE = TransferHandler.NONE;
	private final int AVAILABLE = TransferHandler.COPY_OR_MOVE;
	private final int SELECTED = TransferHandler.MOVE;
	
	@Override
	public int getSourceActions(JComponent c) {
		if (c instanceof JList) {
			return SELECTED;
		} else if (c instanceof JTree) {
				return AVAILABLE;
		} else {
			return NONE;
		}
	}
	
	@Override
	public Transferable createTransferable(JComponent c) {
		if (c instanceof JList) {
			JList list = (JList) c;
			CasProcessor proc = (CasProcessor) list.getSelectedValue();
			return proc.clone();
		} else if (c instanceof JTree) { 
			JTree tree = (JTree) c;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			CasProcessor proc = (CasProcessor) node.getUserObject();
			return proc.clone();
		} else { 
			return null;
		}
	}

	@Override
	public boolean canImport(TransferSupport support) {
		for (DataFlavor flavor : support.getTransferable().getTransferDataFlavors()) {
			if (flavor.getHumanPresentableName().equals("UIMA Analysis Engine")) {
				return true;
			} else if (flavor.getHumanPresentableName().equals("UIMA Collection Reader")) {
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
			Transferable t = support.getTransferable();			
			int action = support.getSourceDropActions();
			Component c = support.getComponent();
			if (c instanceof JList) {
				JList.DropLocation location = (JList.DropLocation) support.getDropLocation();
				int index = location.getIndex();
				if (action == AVAILABLE) {
					if (this.getCollectionProcessingEngines().getSelectedIndex() != -1) {
						this.doInsertInSelected(t,index);	
					}						
				} else if (action == SELECTED) {
					this.doMoveInSelected(t,index);
				}   
			} else if (c instanceof JTree) {
				if (action == SELECTED) {
					this.doRemoveFromSelected(t);
				}  
			}
			return true;
	    } catch (Exception e) {
	    	Dunamis.error(e);
	    	return false;
	    }
	}
	
	private void doRemoveFromSelected(Transferable t) {
		try {
			DataFlavor flavor = t.getTransferDataFlavors()[0];
			Object object = t.getTransferData(flavor);
			if (object instanceof CasProcessor) {
				CasProcessor proc = (CasProcessor) object;
				int source = this.getSelectedCasProcessors().indexOf(proc);
				if (source != -1) {
					this.getSelectedCasProcessors().removeElementAt(source);					
				}
			} 
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}

	private void doInsertInSelected(Transferable t,int target) {
		try {
			DataFlavor flavor = t.getTransferDataFlavors()[0];
			Object object = t.getTransferData(flavor);
			if (object instanceof CasProcessor) {
				CasProcessor processor = (CasProcessor) object;
				this.addCasProcessor(processor);
			} 
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}
	
	private void addCasProcessor(CasProcessor processor) {
		if (this.getSelectedCasProcessors().contains(processor)) {
			String message = "A CAS Processor called '" + processor.toString() + "' is already in use.\n" ;
			message += "Please give another name:";
			String title = "Renaming CAS Processor";
			JFrame frame = this.getFrame();
			String name = (String)JOptionPane.showInputDialog(frame,message,title,JOptionPane.PLAIN_MESSAGE,null,null,processor.toString()); 
			if (name == null) {
				
			} else {
				processor.setName(name);
				this.addCasProcessor(processor);
			}
		} else {
			this.getSelectedCasProcessors().addElement(processor);
		}
	}
	
	private void doMoveInSelected(Transferable t,int target) {
		try {
			DataFlavor flavor = t.getTransferDataFlavors()[0];
			Object object = t.getTransferData(flavor);
			if (object instanceof CasProcessor) {
				CasProcessor proc = (CasProcessor) object;
				int source = this.getSelectedCasProcessors().indexOf(proc);
				if (source < target) {
					this.getSelectedCasProcessors().removeElementAt(source);
					this.getSelectedCasProcessors().add(target - 1,proc);
				} else {
					this.getSelectedCasProcessors().add(target,proc);
					this.getSelectedCasProcessors().removeElementAt(source + 1);
				}
			} 
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}
	
}
