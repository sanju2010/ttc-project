package eu.project.ttc.tools.acabit;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import eu.project.ttc.types.TermEntryAnnotation;

import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

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
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermEntryAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermEntryAnnotation entry = (TermEntryAnnotation) iterator.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode();
			node.setUserObject(entry.getTerm());
			this.getRoot().add(node);
			this.addNotes(node, entry);
		}
		this.getModel().reload();
		} catch (CASRuntimeException e) {
			e.printStackTrace();
		}
	}
	
	private void addNotes(DefaultMutableTreeNode root,TermEntryAnnotation annotation) {
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
		
}
