package eu.project.ttc.tools.aligner;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class ZigguratSettings extends Parameters {	
	
	public ZigguratSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "SourceLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "SourceTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SourceTermContextIndexFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SourceTermSimilarityDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "TargetTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetTermContextIndexFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetTermSimilarityDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SimilarityDistanceClassName", ConfigurationParameter.TYPE_STRING, false, false, "values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosine");
		this.addParameter(declarations, "InterlingualSimilarity", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "InterlingualSimilaritySize", ConfigurationParameter.TYPE_INTEGER, false, false);
		this.addParameter(declarations, "DictionaryFile", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "EvaluationListFile", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}
