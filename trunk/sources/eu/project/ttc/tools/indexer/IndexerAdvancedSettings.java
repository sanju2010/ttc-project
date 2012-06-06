package eu.project.ttc.tools.indexer;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class IndexerAdvancedSettings extends Parameters implements TermSuiteSettings {	
	
	public IndexerAdvancedSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		// TODO ADVANCED
	//	this.addParameter(declarations, "HapaxFilteringThreshold", ConfigurationParameter.TYPE_INTEGER, false, true);
	//	this.addParameter(declarations, "AssociationRateClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.LogLikelihood|eu.project.ttc.metrics.MutualInformation");
		this.addParameter(declarations, "VariantsDetection", ConfigurationParameter.TYPE_BOOLEAN, false, true);
		this.addParameter(declarations, "EditDistanceClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.Levenshtein|eu.project.ttc.metrics.LongestCommonSubsequence|eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein");
		this.addParameter(declarations, "EditDistanceThreshold", ConfigurationParameter.TYPE_FLOAT, false, true);
		this.addParameter(declarations, "EditDistanceNgrams", ConfigurationParameter.TYPE_INTEGER, false, true);
		this.addParameter(declarations, IndexerEngine.P_IGNORE_DIACRITICS, ConfigurationParameter.TYPE_BOOLEAN, false, false);
	}
		
}
