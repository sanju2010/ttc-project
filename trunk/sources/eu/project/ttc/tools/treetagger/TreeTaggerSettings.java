package eu.project.ttc.tools.treetagger;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class TreeTaggerSettings extends Parameters {	
	
	public TreeTaggerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TreeTaggerHomeDirectory", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}
