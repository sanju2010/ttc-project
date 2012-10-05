package eu.project.ttc.tools.indexer;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class IndexerSettings extends Parameters implements TermSuiteSettings {	
	
	public IndexerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
	}

	@Override
	protected String[][] getParameterGroups() {
		return null;
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}
		
}