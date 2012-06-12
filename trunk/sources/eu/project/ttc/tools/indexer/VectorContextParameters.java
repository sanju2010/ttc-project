package eu.project.ttc.tools.indexer;


import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class VectorContextParameters extends Parameters implements TermSuiteSettings {	
	
	public VectorContextParameters(String resource) {
		super(resource);
	}
	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		// TODO ADVANCED
		this.addParameter(declarations, "FrequencyFilteringThreshold", ConfigurationParameter.TYPE_INTEGER, false, true);
		this.addParameter(declarations, "AssociationMeasure", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.LogLikelihood|eu.project.ttc.metrics.MutualInformation");
	}
	@Override
	protected String[][] getParameterGroups() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
