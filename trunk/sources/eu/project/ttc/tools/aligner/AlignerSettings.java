package eu.project.ttc.tools.aligner;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class AlignerSettings extends Parameters implements TermSuiteSettings {	
	
	public AlignerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "EvaluationInputOutputTranslationDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SourceLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "InputSourceTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "OutputTargetTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "BilingualDictionaryFile", ConfigurationParameter.TYPE_STRING, false, false);

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
