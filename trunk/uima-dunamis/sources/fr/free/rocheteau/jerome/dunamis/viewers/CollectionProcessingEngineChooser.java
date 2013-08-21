package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.listeners.CollectionProcessingEngineListener;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;

public class CollectionProcessingEngineChooser {
	
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
		this.list.setDragEnabled(true);
		this.list.setDropMode(DropMode.INSERT);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setMinimumSize(new Dimension(225,500));
	}
	
	public JList getList() {
		return this.list;
	}
	
	private JScrollPane scroll;
	
	private void setComponent() {
		this.scroll = new JScrollPane();
		TitledBorder border = new TitledBorder("Collection Processing Engines");
		this.scroll.setBorder(border);
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scroll.getViewport().add(this.getList());
	}
	
	public JScrollPane getComponent() {
		return this.scroll;
	}
	
	public CollectionProcessingEngineChooser() {
		this.setModel();
		this.setList();
		this.setComponent();
	}
	
	public void enableListeners(CollectionProcessingEngineListener listener) {
		this.getModel().addListDataListener(listener);
		this.getList().addListSelectionListener(listener);
		this.getList().setTransferHandler(listener);
	}
	
	private CollectionProcessingEngine getCollectionProcessingEngineDescription(File file) throws Exception {
		XMLInputSource xml = new XMLInputSource(file);
		XMLParser parser = UIMAFramework.getXMLParser();
		CpeDescription desc = parser.parseCpeDescription(xml);
		return new CollectionProcessingEngine(desc);
	}
	
	public void doNew(File file) {
		try {
			CpeDescription desc = CpeDescriptorFactory.produceDescriptor();
			URL url = file.toURI().toURL(); // new URL("file:" + file.getAbsolutePath());
			desc.setSourceUrl(url);
			CollectionProcessingEngine cpe = new CollectionProcessingEngine(desc,false);
			int last = this.getModel().getSize();
			this.getModel().addElement(cpe);
			this.getList().setSelectedIndex(last);
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}
	
	private boolean contains(CollectionProcessingEngine cpe) {
		for (int i = 0; i < this.getModel().getSize(); i++) {
			CollectionProcessingEngine elem = (CollectionProcessingEngine) this.getModel().getElementAt(i);
			if (elem.getDescription().getSourceUrlString().equals(cpe.getDescription().getSourceUrlString())) {
				return true;
			}
		}
		return false;
	}
	
	public void doOpen(File file) {
		try {
			CollectionProcessingEngine cpe = this.getCollectionProcessingEngineDescription(file);
			if (this.contains(cpe)) {
				int index = this.getModel().indexOf(cpe);
				this.getList().setSelectedIndex(index);
			} else {
				int last = this.getModel().getSize();
				this.getModel().addElement(cpe);
				this.getList().setSelectedIndex(last);
			}
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}
	
	public void doSave() {
		OutputStream out = null;
		try {
			int index = this.getList().getSelectedIndex();
			if (index != -1) {
				CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getModel().get(index);
				cpe.store();
				CpeDescription desc = cpe.getDescription();
				URL url = desc.getSourceUrl();
				out = new FileOutputStream(url.getPath());
				desc.toXML(out);
			} 
		} catch (Exception e) {
			Dunamis.error(e);
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				Dunamis.error(e);
			}
		}
	}
	
	public void doSaveAs(File file) {
		OutputStream out = null;
		try {
			int index = this.getList().getSelectedIndex();
			if (index != -1) {
				CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getModel().get(index);
				cpe.store();
				CpeDescription desc = cpe.getDescription();
				out = new FileOutputStream(file);
				desc.toXML(out);
				desc.setSourceUrl(file.toURI().toURL());
			} 
		} catch (Exception e) {
			Dunamis.error(e);
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				Dunamis.error(e);
			}
		}
	}
	
	public void doSaveAll() {
		for (int index = 0; index < this.getModel().getSize(); index++) {
			OutputStream out = null;
			try {
				CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getModel().get(index);
				cpe.store();
				CpeDescription desc = cpe.getDescription();
				URL url = desc.getSourceUrl();
				out = new FileOutputStream(url.getPath());
				desc.toXML(out); 
			} catch (Exception e) {
				Dunamis.error(e);
			} finally {
				try {
					if (out != null) out.close();
				} catch (IOException e) {
					Dunamis.error(e);
				}
			}
		}
	}
	
	public void doRevert() throws Exception {
		int index = this.getList().getSelectedIndex();
		if (index != -1) {
			CollectionProcessingEngine cpe = (CollectionProcessingEngine) this.getModel().get(index);
			cpe.revert();
		}
	}
	
	public void doClose() {
		int index = this.getList().getSelectedIndex();
		if (index != -1) {
			this.getModel().remove(index);
			this.getList().setSelectedIndex(-1);
		}
	}
	
	public void doCloseAll() {
		this.getModel().clear();
		this.getList().setSelectedIndex(-1);
	}
	
}
