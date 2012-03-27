package eu.project.ttc.tools.spotter;

import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class SpotterAdvancedSettings extends Parameters implements TermSuiteSettings {	
	
	public SpotterAdvancedSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
	}
		
}
