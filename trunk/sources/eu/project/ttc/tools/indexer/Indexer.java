package eu.project.ttc.tools.indexer;

import java.awt.Component;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteTool;

public class Indexer implements TermSuiteTool {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}

	private IndexerSettings settings;
	
	private void setSettings(File home) {
		this.settings = new IndexerSettings(home.getAbsolutePath() + File.separator + "indexer.settings");
		this.settings.getComponent().setBorder(BorderFactory.createTitledBorder("Default Settings"));
	}
	
	public IndexerSettings getSettings() {
		return this.settings;
	}
	
	private IndexerAdvancedSettings advancedSettings;
	
	private void setAdvancedSettings(File home) {
		this.advancedSettings = new IndexerAdvancedSettings(home.getAbsolutePath() + File.separator + "indexer.advanced-settings");
		this.advancedSettings.getComponent().setBorder(BorderFactory.createTitledBorder("Advanced Settings"));
	}
	
	public IndexerAdvancedSettings getAdvancedSettings() {
		return this.advancedSettings;
	}
	
	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.component.setTopComponent(this.getSettings().getComponent());
		this.component.setBottomComponent(this.getAdvancedSettings().getComponent());
		this.component.setContinuousLayout(true);
		this.component.setResizeWeight(0.5);
		this.component.setOneTouchExpandable(true);
		this.component.setDividerSize(50);
	}
	
	@Override
	public Component getComponent() {
		return this.component;
	}
		
	public Indexer(File home) {
		this.setSettings(home);
		this.setAdvancedSettings(home);
		this.setComponent();
	}
		
}
