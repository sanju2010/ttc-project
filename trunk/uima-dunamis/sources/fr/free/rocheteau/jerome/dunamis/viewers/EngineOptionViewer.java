package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;

public class EngineOptionViewer {

	private ProcessorSettingViewer processorSettings;
	
	private void setProcessorSettings() {
		this.processorSettings = new ProcessorSettingViewer();
	}
	
	private ProcessorSettingViewer getProcessorSettings() {
		return this.processorSettings;
	}
	
	private JPanel component;

	private void setComponent() {
		this.component = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.NORTHWEST;
		this.component.add(this.getProcessorSettings().getComponent(),c);
	}
	
	public JPanel getComponent() {
		return this.component;
	}
	
	public EngineOptionViewer() {
		this.setProcessorSettings();
		this.setComponent();
	}

	public void doEnable(boolean enabled) {
		this.getComponent().setEnabled(enabled);
	}

	public void setModel(CollectionProcessingEngine cpe) {
		this.getProcessorSettings().setModel(cpe);
	}
	
	public void enableListsners(Dunamis dunamis) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.NORTHWEST;
		this.getComponent().add(dunamis.getReport().getComponent(),c);
	}
	
}
