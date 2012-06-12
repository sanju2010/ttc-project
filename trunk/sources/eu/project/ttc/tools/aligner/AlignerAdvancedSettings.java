package eu.project.ttc.tools.aligner;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class AlignerAdvancedSettings extends Parameters implements TermSuiteSettings {	
	
	public AlignerAdvancedSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "DistributionalMethod", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "SimilarityDistanceClassName", ConfigurationParameter.TYPE_STRING, false, false, "values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosine");
		this.addParameter(declarations, "CompositionalMethod", ConfigurationParameter.TYPE_BOOLEAN, false, false);
	}

	@Override
	protected String[][] getParameterGroups() {
		return null;
	}
		
}
