package eu.project.ttc.tools.ziggurat;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class ZigguratSettings extends Parameters {	
	
	public ZigguratSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "SourceLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|ru|es|lv|zh");
		this.addParameter(declarations, "SourceTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SourceTermContextIndexFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetLanguage", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|ru|es|lv|zh");
		this.addParameter(declarations, "TargetTerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetTermContextIndexFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SimilarityDistanceClassName", ConfigurationParameter.TYPE_STRING, false, false, "values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosinus");
		this.addParameter(declarations, "DictionaryFile", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "EvaluationListFile", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}
