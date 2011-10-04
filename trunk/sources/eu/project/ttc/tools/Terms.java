package eu.project.ttc.tools;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.CompoundWordTermAnnotation;
import org.apache.uima.MultiWordTermAnnotation;
import org.apache.uima.TermAnnotation;
import org.apache.uima.TermComponentAnnotation;
import org.apache.uima.TermEntryAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class Terms {

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
		
	public Terms() {
		this.setRoot();
		this.setModel();
		this.setTree();
		this.setScroll();
	}
	
	public void doLoad(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermEntryAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermEntryAnnotation annotation = (TermEntryAnnotation) iterator.next();
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject(annotation.getReCoveredText());
			this.getRoot().add(node);
			
			if (annotation.getOccurrences() != null) {
				int size = annotation.getOccurrences().size();
				for (int i = 0; i < size; i++) {
					TermAnnotation term = annotation.getOccurrences(i);
					if (term != null) {
						this.addEntry(node, term);
					}
				}			
			}
			
		}
		this.getModel().reload();
	}
	
	private void addEntry(DefaultMutableTreeNode root,TermAnnotation annotation) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject("occurrence of \"" + annotation.getReCoveredText() + "\"");
		root.add(node);
		this.addNotes(node, annotation);
		if (annotation instanceof MultiWordTermAnnotation) {
			this.addComponents(node, (MultiWordTermAnnotation) annotation);
		} else if (annotation instanceof CompoundWordTermAnnotation) {
			this.addComponents(node, (CompoundWordTermAnnotation) annotation);
		}
	}
	
	private void addComponents(DefaultMutableTreeNode root,MultiWordTermAnnotation annotation) {
		if (annotation.getComponents() != null) {
			int size = annotation.getComponents().size();
			for (int index = 0; index < size; index++) {
				TermComponentAnnotation component = annotation.getComponents(index);
				if (component != null) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode();
					node.setUserObject(component.getReCoveredText());
					root.add(node);
					this.addNotes(node, component);					
				}
			}			
		}
	}
	
	private void addComponents(DefaultMutableTreeNode root,CompoundWordTermAnnotation annotation) {
		if (annotation.getComponents() != null) {
			int size = annotation.getComponents().size();
			for (int index = 0; index < size; index++) {
				TermComponentAnnotation component = annotation.getComponents(index);
				if (component != null) {
					DefaultMutableTreeNode node = new DefaultMutableTreeNode();
					node.setUserObject(component.getReCoveredText());
					root.add(node);
					this.addNotes(node, component);					
				}
			}			
		}
	}

	private void addNotes(DefaultMutableTreeNode root,TermComponentAnnotation annotation) {
		this.addNote(root, "language", annotation.getLanguage());
		this.addNote(root, "begin", new Integer(annotation.getBegin()));
		this.addNote(root, "end", new Integer(annotation.getEnd()));
		this.addNote(root, "category", annotation.getCategory());
		this.addNote(root, "lemma", annotation.getLemma());
	}
	
	private void addNotes(DefaultMutableTreeNode root,TermAnnotation annotation) {
		this.addNote(root, "complexity", annotation.getComplexity());
		this.addNote(root, "language", annotation.getLanguage());
		this.addNote(root, "document", annotation.getDocument());
		this.addNote(root, "begin", new Integer(annotation.getBegin()));
		this.addNote(root, "end", new Integer(annotation.getEnd()));
		this.addNote(root, "category", annotation.getCategory());
		this.addNote(root, "lemma", annotation.getLemma());
	}
	
	private void addNote(DefaultMutableTreeNode root,String key,Object value) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		if (value != null) {
			String string = key + " : " + value.toString();
			node.setUserObject(string);
			root.add(node);
		}	
	}
		
}
