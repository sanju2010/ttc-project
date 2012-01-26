package eu.project.ttc.tools.acabit;

import java.awt.Dimension;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

public class AcabitViewer {

	private Dimension getDimension() {
		return new Dimension(600,400);
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
		
	public AcabitViewer() {
		this.setRoot();
		this.setModel();
		this.setTree();
		this.setScroll();
	}
	
	public void doLoad(JCas cas) {
		try {
			Map<Double, Set<TermAnnotation>> annotations = new TreeMap<Double, Set<TermAnnotation>>(new FrequencyComparator());
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				double frequency = annotation.getFrequency();
				Set<TermAnnotation> terms = annotations.get(frequency);
				if (terms == null) {
					terms = new HashSet<TermAnnotation>();
					annotations.put(new Double(frequency), terms);
				}
				terms.add(annotation);
			}
			for (Double frequency : annotations.keySet()) {
				Set<TermAnnotation> terms = annotations.get(frequency);
				for (TermAnnotation term : terms) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode();
					node.setUserObject(term.getCoveredText());
					this.getRoot().add(node);
					this.addNotes(node, term);
					this.addComponents(node, cas, term);
					this.addVariants(node, cas, term);
				}
			}
			this.getModel().reload();
		} catch (CASRuntimeException e) {
			e.printStackTrace();
		}
	}
	
	private void addVariants(DefaultMutableTreeNode root, JCas cas, TermAnnotation annotation) {
		if (annotation.getVariants() != null) {
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
	
	private void addVariant(DefaultMutableTreeNode root, JCas cas, TermAnnotation annotation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(annotation.getCoveredText());
		root.add(node);
		this.addNotes(node, annotation);
	}
	
	private void addComponents(DefaultMutableTreeNode root, JCas cas, TermAnnotation annotation) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		DefaultMutableTreeNode node = null;
		while (iterator.hasNext()) {
			if (node == null) {
				node = new DefaultMutableTreeNode();
				node.setUserObject("components");
				root.add(node);
			}
			TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
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
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(index);
			if (child.getUserObject().toString().equals("variants")) {
				return (DefaultMutableTreeNode ) child;
			}
		}
		return null;
	}

	private void addNotes(DefaultMutableTreeNode root,TermAnnotation annotation) {
		this.addNote(root, "complexity", annotation.getComplexity());
		this.addNote(root, "category", annotation.getCategory());
		this.addNote(root, "frequency", annotation.getFrequency());
		this.addNote(root, "specificity", annotation.getSpecificity());
	}
	
	private void addNote(DefaultMutableTreeNode root,String key,Object value) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		if (value != null) {
			String string = key + " : " + value.toString();
			node.setUserObject(string);
			root.add(node);
		}	
	}
	
	private class FrequencyComparator implements Comparator<Double> {
		
		@Override
		public int compare(Double source, Double target) {
			return target.compareTo(source);
		}
		
	}

		
}
