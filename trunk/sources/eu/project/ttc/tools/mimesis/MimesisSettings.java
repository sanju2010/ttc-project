package eu.project.ttc.tools.mimesis;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class MimesisSettings extends Parameters {	
	
	public MimesisSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|ru|es|lv|zh");
		this.addParameter(declarations, "File", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "Directory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SimilarityDistanceClassName", ConfigurationParameter.TYPE_STRING, false, false, "values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosinus");
	}
		
}