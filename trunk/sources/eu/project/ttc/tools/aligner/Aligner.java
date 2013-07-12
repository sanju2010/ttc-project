package eu.project.ttc.tools.aligner;

import java.awt.Component;

import javax.swing.*;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.ToolController;
import eu.project.ttc.tools.config.AlignerSettings;
import eu.project.ttc.tools.config.TermSuiteSettings;

// FIXME
// public class Aligner implements ToolController {
public class Aligner {

	private TermSuite parent;
    private AlignerSettings settings;

    public Aligner(TermSuiteSettings tsConfig) {
        // Save the references to the settings
        this.settings = tsConfig.getAlignerSettings();

        this.setComponent();
    }
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}
	
	public AlignerSettings getSettings() {
		return this.settings;
	}

    private JScrollPane component;

    private void setComponent() {
        this.component=this.getSettings().getComponent();
    }

	public Component getView() {
		return this.component;
	}
		
}
