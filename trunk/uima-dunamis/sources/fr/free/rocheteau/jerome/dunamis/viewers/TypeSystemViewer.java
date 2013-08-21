package fr.free.rocheteau.jerome.dunamis.viewers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.xml.sax.SAXException;

import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;

public class TypeSystemViewer {

	private TypeSystemDescription typeSystem;

	private void setTypeSystem(TypeSystemDescription typeSystem) {
		if (typeSystem != null) {
			try {
				typeSystem.resolveImports();
				this.typeSystem = typeSystem;
			} catch (InvalidXMLException e) {
				// TODO
			}
		}
	}
	
	private TypeSystemDescription getTypeSystem() {
		return this.typeSystem;
	}
		
	private DefaultMutableTreeNode node;

	private void setNode() {
		this.node = new DefaultMutableTreeNode();
	}
	
	private DefaultMutableTreeNode getNode() {
		return this.node;
	}
	
	private DefaultTreeModel model;
	
	private void setModel() {
		this.model = new DefaultTreeModel(this.getNode());
	}
	
	private DefaultTreeModel getModel() {
		return this.model;
	}
	
	private JTree tree;
	
	private void setTree() {
		this.tree = new JTree(this.getModel());
		this.tree.setRootVisible(false);
	}
	
	public JTree getComponent() {
		return this.tree;
	}
	
	public void clear() {
		this.getNode().removeAllChildren();
	} 
	
	private void update() {
		int index = 0;
		this.clear();
		for (TypeDescription type : this.getTypeSystem().getTypes()) {
			TypeViewer view = new TypeViewer(type);
			this.getModel().insertNodeInto(view.get(),this.getNode(),index);
			index++;
		}
     this.getModel().reload();
	} 
		
	public void update(CasProcessor casProcessor) {
		TypeSystemDescription typeSystem = casProcessor.getTypeSystem();
		this.update(typeSystem);
	}
	
	public void update(TypeSystemDescription typeSystem) {
		if (typeSystem == null) {
			this.setTypeSystem(null);
			this.clear();
			this.getModel().reload();
		} else {
			this.setTypeSystem(typeSystem);
			this.update();
		}
	}

	public TypeSystemViewer() {
		this.setNode();
		this.setModel();
		this.setTree();
	}
	
	public void doExport(File file) throws IOException, SAXException {
		FileOutputStream stream = new FileOutputStream(file);
		this.getTypeSystem().toXML(stream);
		stream.close();
	}
	
}
