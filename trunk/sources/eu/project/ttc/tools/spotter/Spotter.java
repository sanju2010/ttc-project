package eu.project.ttc.tools.spotter;

import java.awt.Component;

import javax.swing.JPanel;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteTool;
import eu.project.ttc.tools.config.SpotterSettings;
import eu.project.ttc.tools.config.TermSuiteSettings;

/**
 * GUI to configure the Spotter tool.
 *
 * The spotter is responsible for processing documents and extract term candidates from it.
 */
public class Spotter implements TermSuiteTool {

	private TermSuite parent;
    private SpotterSettings settings;

    public Spotter(TermSuiteSettings tsConfig) {
        // Save the references to the settings
        this.settings = tsConfig.getSpotterSettings();
        this.setComponent();
    }

	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}

    public SpotterSettings getSettings() {
        return this.settings;
    }
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////// GUI SET UP

	private JPanel component;
	
	private void setComponent() {

			this.component=new JPanel();
			
			this.component.add(this.getSettings().getComponent());

	}

	public Component getComponent() {
		return this.component;
	}
		
}
