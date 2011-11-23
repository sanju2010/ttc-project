package eu.project.ttc.tools.katastasis;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class KatastasisSettings extends Parameters {	
	
	public KatastasisSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|ru|es|lv|zh");
		this.addParameter(declarations, "Directory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "File", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "ScopeSize", ConfigurationParameter.TYPE_INTEGER, false, false);
		this.addParameter(declarations, "AssociationRateClassName", ConfigurationParameter.TYPE_STRING, false, false, "values:eu.project.ttc.metrics.LogLikelihood|eu.project.ttc.metrics.MutualInformation");
	}
		
}
