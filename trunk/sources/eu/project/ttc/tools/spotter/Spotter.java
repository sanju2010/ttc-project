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
	
	private void setSettings() {
		this.settings = new SpotterSettings(System.getProperty("user.home") + File.separator + ".term-suite-spotter.settings");
	}
	
	public SpotterSettings getSettings() {
		return this.settings;
	}
	
	public Spotter() {
		this.setSettings();
	}

	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
}
