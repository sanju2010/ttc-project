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
		// this.addParameter(declarations, "TerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "AssociationRateClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.LogLikelihood|eu.project.ttc.metrics.MutualInformation");
		/*
		this.addParameter(declarations, "MultiWordPatternRuleFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TermVariationRuleFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "NeoclassicalElementFile", ConfigurationParameter.TYPE_STRING, false, true);
		*/
		this.addParameter(declarations, "EnableTermConflating", ConfigurationParameter.TYPE_BOOLEAN, false, true);
		this.addParameter(declarations, "EditDistanceClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.Levenshtein|eu.project.ttc.metrics.LongestCommonSubsequence");
		this.addParameter(declarations, "EditDistanceThreshold", ConfigurationParameter.TYPE_FLOAT, false, true);
		this.addParameter(declarations, "EditDistanceNgrams", ConfigurationParameter.TYPE_INTEGER, false, true);
	}
		
}
