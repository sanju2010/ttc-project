package eu.project.ttc.tools.aligner;

import java.awt.Component;
import java.io.File;

import eu.project.ttc.tools.TermSuite;
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
	
	private void setSettings() {
		this.settings = new AlignerSettings(System.getProperty("user.home") + File.separator + ".term-suite-aligner.settings");
	}
	
	public AlignerSettings getSettings() {
		return this.settings;
	}
	
	public Aligner() {
		this.setSettings();
	}

	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
}
