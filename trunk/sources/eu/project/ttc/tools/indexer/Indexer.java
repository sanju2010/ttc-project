package eu.project.ttc.tools.indexer;

import java.awt.Component;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
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
		this.settings.getComponent().setBorder(BorderFactory.createTitledBorder("Settings"));
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
	
	private TBXSettings TBXSettings;
	
	private void setTBXSettings(File home) {
		this.TBXSettings = new TBXSettings(home.getAbsolutePath() + File.separator + "indexer.tbx-settings");
		this.TBXSettings.getComponent().setBorder(BorderFactory.createTitledBorder("TBX Settings"));
	}
	
	public TBXSettings getTBXSettings() {
		return this.TBXSettings;
	}
	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JScrollPane pp=this.getSettings().getComponent();
		this.component.setTopComponent(pp);
		JSplitPane componentAdvanced;
		componentAdvanced = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		componentAdvanced.setTopComponent(this.getAdvancedSettings().getComponent());
		componentAdvanced.setBottomComponent(this.getTBXSettings().getComponent());
		componentAdvanced.setResizeWeight(0.7);
		this.component.setBottomComponent(componentAdvanced);
		this.component.setContinuousLayout(true);
		this.component.setResizeWeight(0.6);
		this.component.setOneTouchExpandable(true);
	//	this.component.setDividerSize(1);
	}
	
	@Override
	public Component getComponent() {
		return this.component;
	}

	public Indexer(File home) 
	{
		this.setSettings(home);
		this.setAdvancedSettings(home);
		this.setTBXSettings(home); 
		this.setComponent();
	}
		
}