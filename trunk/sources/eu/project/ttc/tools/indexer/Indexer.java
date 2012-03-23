package eu.project.ttc.tools.indexer;

import java.awt.Component;
import java.io.File;

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
	}
	
	public IndexerSettings getSettings() {
		return this.settings;
	}
	
	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
	public Indexer(File home) {
		this.setSettings(home);
	}
		
}
