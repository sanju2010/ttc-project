package eu.project.ttc.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

public class Corpora {

	private Dimension getDimension() {
		return new Dimension(240,480);
	}	
	
	private static Map<String,Locale> languages;
	
	static {
		languages = new HashMap<String,Locale>();
		languages.put(Locale.ENGLISH.getDisplayName(Locale.ENGLISH),Locale.ENGLISH);
		languages.put(Locale.FRENCH.getDisplayName(Locale.ENGLISH),Locale.FRENCH);
	}
	
	private static Set<String> getLanguages() {
		return languages.keySet();
	}
	
	private ComboBoxModel comboBoxModel;
	
	private void setComboBoxModel() {
		this.comboBoxModel = new DefaultComboBoxModel(getLanguages().toArray()); 
	}	
	
	private ComboBoxModel getComboBoxModel() {
		return this.comboBoxModel;
	}
	
	private JComboBox comboBox;
	
	private void setComboBox() {
		this.comboBox = new JComboBox(this.getComboBoxModel());
	}
	
	private JComboBox getComboBox() {
		return this.comboBox;
	}
	
	private DefaultListModel model;
	
	private void setModel() {
		this.model = new DefaultListModel();
	}
	
	public DefaultListModel getModel() {
		return this.model;
	}
	
	public String getLanguage() {
		String key = (String) this.getComboBox().getSelectedItem();
		return languages.get(key).getLanguage();
	}
	
	public String[] getDirectories() {
		int size = this.getModel().size();
		String[] corpora = new String[size];
		for (int index = 0; index < size; index++) {
			Corpus corpus = (Corpus) this.getModel().getElementAt(index);
			corpora[index] = corpus.getFile().getAbsolutePath();
		}
		return corpora;
	}
	
	private JList list;
	
	private void setList() {
		this.list = new JList();
		this.list.setModel(this.getModel());
		this.list.setDragEnabled(true);
		this.list.setDropMode(DropMode.INSERT);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Handler handler = new Handler();
		handler.setCorpora(this);
		this.list.setTransferHandler(handler);
		// TODO this.list.setCellRenderer(new CorporaRenderer());
	}
	
	private JList getList() {
		return this.list;
	}
	
	private JScrollPane scroll;
	
	private void setScroll() {
		this.scroll = new JScrollPane();
	    this.scroll.getViewport().add(this.getList());
		this.scroll.setPreferredSize(this.getDimension());
	}
	
	private JScrollPane getScroll() {
		return this.scroll;
	}
	
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel();
		this.component.add(this.getComboBox());
		this.component.add(this.getScroll());
	}
	
	public JPanel getComponent() {
		return this.component;
	}
		
	public Corpora() {
		this.setComboBoxModel();
		this.setComboBox();
		this.setModel();
		this.setList();
		this.setScroll();
		this.setComponent();
	}
	
	private class Corpus implements Transferable, Comparable<Corpus> {
		
		private String name;
		
		private void setName(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		private File file;
		
		private void setFile(File file) {
			this.file = file;
		}
		
		public File getFile() {
			return this.file;
		}
		
		public Corpus(File file) {
			this.setFile(file);
			this.setName(file.getName());
		}

		@Override 
		public int compareTo(Corpus corpus) {
			return this.getFile().getAbsolutePath().compareTo(corpus.getFile().getAbsolutePath());
		}
		
		@Override
		public boolean equals(Object object) {
			if (object instanceof Corpus) {
				Corpus corpus = (Corpus) object;
				return this.compareTo(corpus) == 0;
			} else {
				return false;
			}
		}
		
		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return null;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.stringFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.stringFlavor);
		}
		
	}
	
	private class Handler extends TransferHandler {
		
		private static final long serialVersionUID = -707782383293756058L;
		
		private Corpora corpora;
		
		public void setCorpora(Corpora corpora) {
			this.corpora = corpora;
		}
		
		private Corpora getCorpora() {
			return this.corpora;
		}
		
		@Override
		public int getSourceActions(JComponent c) {
		    return COPY_OR_MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			if (c instanceof JList) {
				JList list = (JList) c;
				Corpus corpus = (Corpus) list.getSelectedValue();
				return corpus;
			} else {
				return null;
			}
		}

		@Override
		protected void exportDone(JComponent c, Transferable t, int action) {
		    if (action == MOVE || action == COPY) {
		    	Corpus corpus = (Corpus) t;
		    	this.getCorpora().getModel().removeElement(corpus);
		    }
		}
		
		@Override
		public boolean canImport(TransferSupport support) {
			return this.hasFlavors(support.getTransferable().getTransferDataFlavors());
		}
		
		private boolean hasFlavors(DataFlavor[] flavors) {
		    for (int i = 0; i < flavors.length; i++) {
		    	if (DataFlavor.stringFlavor.equals(flavors[i])) {
		    		return true;
		    	}
		    }
		    return false;
		  }

		@Override
		public boolean importData(TransferSupport support) {
			if (!this.canImport(support)) {
				return false;
			} else {
				Transferable t = support.getTransferable();
				Component c = support.getComponent();
				if (c instanceof JList) {
					JList.DropLocation location = (JList.DropLocation) support.getDropLocation();
					int index = location.getIndex();
					try {
						Object object = t.getTransferData(DataFlavor.stringFlavor);
						this.doMove((String) object,index);					
					} catch (UnsupportedFlavorException e) {
						// e.printStackTrace();
					} catch (IOException e) {
						// e.printStackTrace();
					} catch (URISyntaxException e) {
						// e.printStackTrace();
					}
				}
				return true;
		    } 
		}
		
		private void doMove(String paths,int index) throws URISyntaxException, MalformedURLException {
			for (File file : this.doFilter(paths)) {
				Corpus corpus = new Corpus(file);
				if (!this.getCorpora().getModel().contains(corpus)) {
					this.getCorpora().getModel().addElement(corpus);
				}
				index++;
			}
		}
		
		private List<File> doFilter(String paths) throws URISyntaxException, MalformedURLException {
			List<File> files = new ArrayList<File>();
			String sep = System.getProperty("line.separator");
			String[] names = paths.trim().split(sep);
			for (String name : names) {
				URI uri = new URI(name);
				File file = new File(uri.toURL().getFile());
				if (file.isDirectory()) {
					files.add(file);
				}
			}
			return files;
		}
		
	}
		
}
