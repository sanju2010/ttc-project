package eu.project.ttc.tools.indexer;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class TBXSettings extends Parameters implements TermSuiteSettings {	
	
	public TBXSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "FrequencyFilteringThreshold", ConfigurationParameter.TYPE_INTEGER, false, true);		
		this.addParameter(declarations, "AdjectivesAndNouns", ConfigurationParameter.TYPE_BOOLEAN, false, true);
		this.addParameter(declarations, "VerbsAndOthers", ConfigurationParameter.TYPE_BOOLEAN, false, true);
		

	}
		
}
