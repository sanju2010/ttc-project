package eu.project.ttc.tools;

import java.awt.Component;

public interface TermSuiteTool {

	public void setParent(TermSuite parent);

	public TermSuite getParent();

	public TermSuiteConfigurationFile getSettings();
	
	public Component getComponent();

}