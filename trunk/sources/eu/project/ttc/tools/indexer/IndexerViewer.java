package eu.project.ttc.tools.indexer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import fr.free.rocheteau.jerome.dunamis.fields.FieldFactory;

public class IndexerViewer {

	private Dimension getDimension() {
		return new Dimension(600, 400);
	}

	private DefaultMutableTreeNode root;

	private void setRoot() {
		this.root = new DefaultMutableTreeNode("Terminology");
	}

	private DefaultMutableTreeNode getRoot() {
		return this.root;
	}

	private DefaultTreeModel model;

	private void setModel() {
		this.model = new DefaultTreeModel(this.getRoot());
	}

	public DefaultTreeModel getModel() {
		return this.model;
	}

	private JTree tree;

	private void setTree() {
		this.tree = new JTree(this.getModel());
		this.tree.setRootVisible(false);
		this.tree.expandRow(1);
		// this.tree.setSelectionModel(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// TODO this.list.setCellRenderer(new Renderer());
	}

	private JTree getTree() {
		return this.tree;
	}

	private JScrollPane scroll;

	private void setScroll() {
		this.scroll = new JScrollPane();
		this.scroll.getViewport().add(this.getTree());
		this.scroll.setMinimumSize(this.getDimension());
	}

	public JScrollPane getComponent() {
		return this.scroll;
	}

	public IndexerViewer() {
		this.setRoot();
		this.setModel();
		this.setTree();
		this.setScroll();
		this.enableListener();
	}

	private void enableListener() {
		ActionListener listener = new Listener(this);
		JPopupMenu menu = new Menu(listener);
		this.getComponent().setComponentPopupMenu(menu);
		this.getTree().setInheritsPopupMenu(true);
	}

	public void doLoad(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		this.doLoad(inputStream);
	}

	public void doLoad(File file) throws Exception {
		InputStream inputStream = new FileInputStream(file);
		this.doLoad(inputStream);
	}

	public void doLoad(InputStream inputStream) throws Exception {
		URL url = this
				.getClass()
				.getClassLoader()
				.getResource(
						"eu/project/ttc/all/engines/indexer/TermIndexer.xml");
		XMLInputSource source = new XMLInputSource(url);
		XMLParser parser = UIMAFramework.getXMLParser();
		AnalysisEngineDescription ae = parser
				.parseAnalysisEngineDescription(source);
		CAS cas = CasCreationUtils.createCas(ae);
		XmiCasDeserializer.deserialize(inputStream, cas);
		this.doLoad(cas.getJCas());
	}

	public void doLoad(JCas cas) {
		try {
			AnnotationIndex<Annotation> index = cas
					.getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				TermAnnotation term = (TermAnnotation) iterator.next();
				if (term.getCategory() == null) {
					continue;
				} else if (term.getCategory().equals("verb")) {
					continue;
				} else {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode();
					node.setUserObject(term.getCoveredText().replaceAll("\\s+",
							" "));
					this.getRoot().add(node);
					this.addNotes(node, term);
					try {
						this.addComponents(node, cas, term);
					} catch (Exception e) {
						// ignore
					}
					this.addForms(node, cas, term);
					this.addVariants(node, cas, term);
				}
			}
			this.getModel().reload();
		} catch (CASRuntimeException e) {
			e.printStackTrace();
		}
	}

	private void addForms(DefaultMutableTreeNode root, JCas cas,
			TermAnnotation annotation) {
		try {
			if (annotation.getForms() != null) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode();
				node.setUserObject("forms");
				root.add(node);
				for (int i = 0; i < annotation.getForms().size(); i++) {
					this.addNote(node, annotation.getForms(i));
				}
			}
		} catch (Exception e) {
			// ignore
		}
	}

	private void addContext(DefaultMutableTreeNode root, JCas cas,
			TermAnnotation annotation) {
		try {
			JCas jcas = cas.getView(annotation.getCoveredText());
			String context = jcas.getDocumentText();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject("context");
			root.add(node);
			this.addContext(node, context);
		} catch (Exception e) {
			// ignore
		}
	}

	private void addContext(DefaultMutableTreeNode root, String context) {
		String[] scores = context.split("\n");
		for (String score : scores) {
			String[] items = score.trim().split("\t");
			if (items.length == 2) {
				this.addNote(root, items[0].trim(), items[1].trim());
			}
		}
	}

	private void addVariants(DefaultMutableTreeNode root, JCas cas,
			TermAnnotation annotation) {
		if (annotation.getVariants() != null) {
			String flag = "*";
			if (annotation instanceof MultiWordTermAnnotation) {
				flag += "*";
			}
			root.setUserObject(root.getUserObject() + "     (" + flag + ")");
			DefaultMutableTreeNode node = this.getvariants(root);
			if (node == null) {
				node = new DefaultMutableTreeNode();
				node.setUserObject("variants");
				root.add(node);
			}
			int length = annotation.getVariants().size();
			for (int index = 0; index < length; index++) {
				TermAnnotation variant = annotation.getVariants(index);
				this.addVariant(node, cas, variant);
			}
		}
	}

	private void addVariant(DefaultMutableTreeNode root, JCas cas,
			TermAnnotation annotation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(annotation.getCoveredText().replaceAll("\\s+", " "));
		root.add(node);
		this.addNotes(node, annotation);
	}

	private void addComponents(DefaultMutableTreeNode root, JCas cas,
			TermAnnotation annotation) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		DefaultMutableTreeNode node = null;
		while (iterator.hasNext()) {
			if (node == null) {
				node = new DefaultMutableTreeNode();
				node.setUserObject("components");
				root.add(node);
			}
			TermComponentAnnotation component = (TermComponentAnnotation) iterator
					.next();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode();
			child.setUserObject(component.getCoveredText());
			node.add(child);
			this.addNote(child, "category", component.getCategory());
			this.addNote(child, "lemma", component.getLemma());
			this.addNote(child, "stem", component.getStem());
		}
	}

	private DefaultMutableTreeNode getvariants(DefaultMutableTreeNode root) {
		int count = root.getChildCount();
		for (int index = 0; index < count; index++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) root
					.getChildAt(index);
			if (child.getUserObject().toString().equals("variants")) {
				return (DefaultMutableTreeNode) child;
			}
		}
		return null;
	}

	private void addNotes(DefaultMutableTreeNode root, TermAnnotation annotation) {
		this.addNote(root, "complexity", this.getComplexity(annotation));
		this.addNote(root, "category", annotation.getCategory());
		this.addNote(root, "occurrences", annotation.getOccurrences());
		this.addNote(root, "frequency", annotation.getFrequency());
		this.addNote(root, "specificity", annotation.getSpecificity());
	}

	private String getComplexity(TermAnnotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation) {
			SingleWordTermAnnotation swt = (SingleWordTermAnnotation) annotation;
			if (swt.getCompound()) {
				if (swt.getNeoclassical()) {
					return "neoclassical-compound";
				} else {
					return "compound";
				}
			} else {
				return "single-word";
			}
		} else {
			return "multi-word";
		}
	}

	private void addNote(DefaultMutableTreeNode root, String key, Object value) {
		if (value != null) {
			String string = key + " : " + value.toString();
			this.addNote(root, string);
		}
	}

	private void addNote(DefaultMutableTreeNode root, String value) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		if (value != null) {
			node.setUserObject(value);
			root.add(node);
		}
	}

	public void sortBy(Comparator<DefaultMutableTreeNode> comparator) {
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		for (int i = 0; i < this.getRoot().getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this
					.getRoot().getChildAt(i);
			nodes.add(node);
		}
		Collections.sort(nodes, comparator);
		int i = 0;
		for (DefaultMutableTreeNode node : nodes) {
			this.getRoot().insert(node, i);
			i++;
		}
		this.getModel().reload();
	}

	/** Compare names in a locale sensitive manner */
	private static final Collator NameCollator = Collator.getInstance();
	
	public void sortByName() {
		this.sortBy(new NameComparator());
	}

	public void sortByFreq() {
		this.sortBy(new FreqComparator());
	}

	public void sortBySpec() {
		this.sortBy(new SpecComparator());
	}

	private class NameComparator implements Comparator<DefaultMutableTreeNode> {
		
		@Override
		public int compare(DefaultMutableTreeNode source,
				DefaultMutableTreeNode target) {
			String src = (String) source.getUserObject();
			String tgt = (String) target.getUserObject();
			return NameCollator.compare(src, tgt);
		}

	}

	private class FreqComparator implements Comparator<DefaultMutableTreeNode> {

		@Override
		public int compare(DefaultMutableTreeNode source,
				DefaultMutableTreeNode target) {
			Double src = Double.valueOf(this.getFreq(source));
			Double tgt = Double.valueOf(this.getFreq(target));
			// String src = this.getFreq(source);
			// String tgt = this.getFreq(target);
			return tgt.compareTo(src);
		}

		private String getFreq(DefaultMutableTreeNode node) {
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
						.getChildAt(i);
				String label = (String) child.getUserObject();
				if (label.startsWith("frequency : ")) {
					return label.substring(12).trim();
				}
			}
			throw new NullPointerException((String) node.getUserObject());
		}

	}

	private class SpecComparator implements Comparator<DefaultMutableTreeNode> {

		@Override
		public int compare(DefaultMutableTreeNode source,
				DefaultMutableTreeNode target) {
			Double src = Double.valueOf(this.getSpec(source));
			Double tgt = Double.valueOf(this.getSpec(target));
			// String src = this.getSpec(source);
			// String tgt = this.getSpec(target);
			return tgt.compareTo(src);
		}

		private String getSpec(DefaultMutableTreeNode node) {
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
						.getChildAt(i);
				String label = (String) child.getUserObject();
				if (label.startsWith("specificity : ")) {
					return label.substring(14).trim();
				}
			}
			throw new NullPointerException((String) node.getUserObject());
		}

	}

	private class Listener implements ActionListener {

		private IndexerViewer viewer;

		public Listener(IndexerViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object instanceof JMenuItem) {
				JMenuItem source = (JMenuItem) object;
				String action = source.getActionCommand();
				if (action.equals("load")) {
					int rv = FieldFactory.getChooser().showOpenDialog(/*
																	 * this.
																	 * getFileField
																	 * ().
																	 * getComponent
																	 * ()
																	 */null);
					if (rv == JFileChooser.APPROVE_OPTION) {
						File file = FieldFactory.getChooser().getSelectedFile();
						try {
							this.viewer.doLoad(file);
						} catch (Exception e) {
							UIMAFramework.getLogger().log(Level.SEVERE,
									e.getMessage());
							e.printStackTrace();
						}
					}
				} else if (action.equals("name")) {
					this.viewer.sortByName();
				} else if (action.equals("freq")) {
					this.viewer.sortByFreq();
				} else if (action.equals("spec")) {
					this.viewer.sortBySpec();
				}
			}
		}

	}

	private class Menu extends JPopupMenu {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3988974235600538309L;

		private JMenuItem load;

		private void setLoad() {
			this.load = new JMenuItem("Load from file");
			this.load.setActionCommand("load");
		}

		private JMenuItem sortByName;

		private void setSortByName() {
			this.sortByName = new JMenuItem("Sort by name");
			this.sortByName.setActionCommand("name");
		}

		private JMenuItem sortByFreq;

		private void setSortByFreq() {
			this.sortByFreq = new JMenuItem("Sort by frequency");
			this.sortByFreq.setActionCommand("freq");
		}

		private JMenuItem sortBySpec;

		private void setSortBySpec() {
			this.sortBySpec = new JMenuItem("Sort by specificity");
			this.sortBySpec.setActionCommand("spec");
		}

		public Menu(ActionListener listsner) {
			this.setLoad();
			this.setSortByName();
			this.setSortByFreq();
			this.setSortBySpec();
			this.add(this.load);
			this.addSeparator();
			this.add(this.sortByName);
			this.add(this.sortByFreq);
			this.add(this.sortBySpec);
			this.load.addActionListener(listsner);
			this.sortByName.addActionListener(listsner);
			this.sortByFreq.addActionListener(listsner);
			this.sortBySpec.addActionListener(listsner);
		}

	}

}
