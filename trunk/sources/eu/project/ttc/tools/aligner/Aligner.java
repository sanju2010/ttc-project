package eu.project.ttc.tools.aligner;

import java.awt.Component;
import java.io.File;

import javax.swing.JSplitPane;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.TermSuiteTool;

public class Aligner implements TermSuiteTool {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}	

	private AlignerSettings settings;
	
	private void setSettings(File home) {
		this.settings = new AlignerSettings(home.getAbsolutePath() + File.separator + "aligner.settings");
	}
	
	public AlignerSettings getSettings() {
		return this.settings;
	}
	
	private AlignerAdvancedSettings advancedSettings;
	
	private void setAdvancedSettings(File home) {
		this.advancedSettings = new AlignerAdvancedSettings(home.getAbsolutePath() + File.separator + "aligner.advanced-settings");
	}
	
	@Override
	public AlignerAdvancedSettings getAdvancedSettings() {
		return this.advancedSettings;
	}
	
	public Aligner(File home) {
		this.setSettings(home);
		this.setAdvancedSettings(home);
		this.setComponent();
	}

	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.component.setTopComponent(this.getSettings().getComponent());
		this.component.setBottomComponent(this.getAdvancedSettings().getComponent());
	}
	
	@Override
	public Component getComponent() {
		return this.component;
	}
		
}
