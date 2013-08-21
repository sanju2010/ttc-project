package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;

public class InformationViewer {

	private JEditorPane name;

	public void setName() {
		this.name = new JEditorPane();
		this.name.setPreferredSize(new Dimension(550,50));
		this.name.setEditable(false);
		this.name.setBorder(BorderFactory.createTitledBorder("Name"));
	}

	public JEditorPane getName() {
		return name;
	}
	
	private JEditorPane version;

	private void setVersion() {
		this.version = new JEditorPane();
		this.version.setPreferredSize(new Dimension(550,50));
		this.version.setEditable(false);
		this.version.setBorder(BorderFactory.createTitledBorder("Version"));
	}

	private JEditorPane getVersion() {
		return version;
	}
	
	private JEditorPane vendor;
	
	private void setVendor() {
		this.vendor = new JEditorPane();
		this.vendor.setPreferredSize(new Dimension(550,50));
		this.vendor.setEditable(false);
		this.vendor.setBorder(BorderFactory.createTitledBorder("Vendor"));
	}

	private JEditorPane getVendor() {
		return vendor;
	}
	
	private JEditorPane description;
	
	private void setDescription() {
		this.description = new JEditorPane();
		this.description.setEditable(false);
		this.description.setPreferredSize(new Dimension(550,400));
		this.description.setBorder(BorderFactory.createTitledBorder("Description"));
	}
	
	public JEditorPane getDescription(){
		return this.description;
	}
	
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getName(),c);
		c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getVersion(),c);
		c.gridx = 0; c.gridy = 2; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getVendor(),c);
		c.gridx = 0; c.gridy = 3; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getDescription(),c);
	}
	
	public JPanel getComponent() {
		return this.component;
	}
	
	public InformationViewer(){
		this.setName();
		this.setVersion();
		this.setVendor();
		this.setDescription();
		this.setComponent();
	}
	
	public void update(CasProcessor casProcessor) {
		this.name.setText(casProcessor.getMetaData().getName());
		this.version.setText(casProcessor.getMetaData().getVersion());
		this.vendor.setText(casProcessor.getMetaData().getVendor());
		this.description.setText(casProcessor.getMetaData().getDescription());
	}
	
}
