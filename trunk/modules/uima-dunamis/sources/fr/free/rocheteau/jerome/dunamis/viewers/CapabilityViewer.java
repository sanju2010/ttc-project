package fr.free.rocheteau.jerome.dunamis.viewers;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.resource.metadata.Capability;

import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;

public class CapabilityViewer {
	
	private JTree inputView;
	private DefaultTreeModel inputModel;
	private DefaultMutableTreeNode inputNode;
	
	private void setInputView() {
		this.inputNode = new DefaultMutableTreeNode();
		this.inputModel = new DefaultTreeModel(this.inputNode);
		this.inputView = new JTree(this.inputModel);
		this.inputView.setRootVisible(false);
	}
	
	private void resetInputView() {
		this.inputNode.removeAllChildren();
		int index = 0;
		for (Capability capability : this.getCapabilities()) {
			for (TypeOrFeature input : capability.getInputs()) {
				String name = input.getName();
				TypeViewer view = new TypeViewer(name);
				this.inputModel.insertNodeInto(view.get(),this.inputNode,index);
				index++;
			}
		}
		this.inputModel.reload();
	}
	
	private JTree getInputView() {
		return this.inputView;
	}
	
	private JTree outputView;
	private DefaultTreeModel outputModel;
	private DefaultMutableTreeNode outputNode;
	
	private void setOutputView() {
		this.outputNode = new DefaultMutableTreeNode();
		this.outputModel = new DefaultTreeModel(this.outputNode);
		this.outputView = new JTree(this.outputModel);
		this.outputView.setRootVisible(false);
	}

	private void resetOutputView() {
		this.outputNode.removeAllChildren();
		int index = 0;
		for (Capability capability : this.getCapabilities()) {
			for (TypeOrFeature output : capability.getOutputs()) {
				String name = output.getName();
				TypeViewer view = new TypeViewer(name);
				this.outputModel.insertNodeInto(view.get(),this.outputNode,index);
				index++;
			}
		}
		this.outputModel.reload();
	}
	
	private JTree getOutputView() {
		return this.outputView;
	}
	
	private Capability[] capabilities;
	
	private void setCapabilities() {
		this.capabilities = null;
	}

	public void setCapabilities(Capability[] capabilities) {
		this.capabilities = capabilities;
	}
	
	private Capability[] getCapabilities() {
		return this.capabilities;
	}

	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JScrollPane inPanel = new JScrollPane();
		TitledBorder inTitle = BorderFactory.createTitledBorder("Requires");
		inPanel.setBorder(inTitle);
		inPanel.getViewport().add(this.getInputView());
		
		JScrollPane outPanel = new JScrollPane();
		TitledBorder outTitle = BorderFactory.createTitledBorder("Provides");
		outPanel.setBorder(outTitle);
		outPanel.getViewport().add(this.getOutputView());
		
		this.component.add(inPanel);
		this.component.add(outPanel);
	}
	
	public JSplitPane getComponent() {
		return this.component;
	}

	public CapabilityViewer() {
		this.setCapabilities();
		this.setInputView();
		this.setOutputView();
		this.setComponent();
	}
	
	public void update(CasProcessor casProcessor) {
		Capability[] capabilities = casProcessor.getMetaData().getCapabilities();
		this.setCapabilities(capabilities);
		this.resetInputView();
		this.resetOutputView();
	}
	
}
