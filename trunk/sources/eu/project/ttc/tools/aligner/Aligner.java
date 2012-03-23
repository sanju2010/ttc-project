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
	
	private void setSettings(File home) {
		this.settings = new AlignerSettings(home.getAbsolutePath() + File.separator + "aligner.settings");
	}
	
	public AlignerSettings getSettings() {
		return this.settings;
	}
	
	public Aligner(File home) {
		this.setSettings(home);
	}

	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
}
