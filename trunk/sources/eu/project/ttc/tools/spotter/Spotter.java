package eu.project.ttc.tools.spotter;

import java.awt.Component;
import java.io.File;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteTool;

public class Spotter implements TermSuiteTool {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}
	
	private SpotterSettings settings;
	
	private void setSettings(File home) {
		this.settings = new SpotterSettings(home.getAbsolutePath() + File.separator + "spotter.settings");
	}
	
	public SpotterSettings getSettings() {
		return this.settings;
	}
	
	public Spotter(File home) {
		this.setSettings(home);
	}

	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
}
