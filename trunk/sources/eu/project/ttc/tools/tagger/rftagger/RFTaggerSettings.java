package eu.project.ttc.tools.tagger.rftagger;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.tagger.TaggerSettings;
import eu.project.ttc.tools.utils.Parameters;

public class RFTaggerSettings extends Parameters implements TaggerSettings {	
	
	public RFTaggerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:de|cs|hu|sl");
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}
