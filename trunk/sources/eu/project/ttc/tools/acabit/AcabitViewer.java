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
				}
			}
			this.getModel().reload();
		} catch (CASRuntimeException e) {
			e.printStackTrace();
		}
	}
	
	private void addComponents(DefaultMutableTreeNode root, JCas cas, TermAnnotation annotation) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
		FSIterator<Annotation> iterator = index.subiterator(annotation);
		while (iterator.hasNext()) {
			TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject(component.getCoveredText());
			root.add(node);
			this.addNote(node, "category", component.getCategory());
			this.addNote(node, "lemma", component.getLemma());
		}
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
