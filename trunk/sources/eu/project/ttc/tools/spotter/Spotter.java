package eu.project.ttc.tools.spotter;

import java.awt.Component;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteSettings;
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
	
	private SpotterAdvancedSettings advancedSettings;
	
	private void setAdvancedSettings(File home) {
		this.advancedSettings = new SpotterAdvancedSettings(home.getAbsolutePath() + File.separator + "spotter.advanced-settings");
	}
	
	public SpotterAdvancedSettings getAdvancedSettings() {
		return this.advancedSettings;
	}
	
	public Spotter(File home) {
		this.setSettings(home);
		this.setAdvancedSettings(home);
		this.setComponent();
	}

	private JPanel component;
	
	private void setComponent() {

			this.component=new JPanel();
			
			this.component.add(this.getSettings().getComponent());

	}
	
	@Override
	public Component getComponent() {
		return this.component;
	}
		
}
