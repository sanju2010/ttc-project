package eu.project.ttc.tools.aligner;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class AlignerAdvancedSettings extends Parameters implements
		TermSuiteSettings {

	/**
	 * Name of the parameter that must be set to enable the compositional method
	 */
	public static final String P_METHOD_COMPOSITIONAL = "CompositionalMethod";

	/**
	 * Name of the parameter that must be set to enable the distributional
	 * method
	 */
	public static final String P_METHOD_DISTRIBUTIONAL = "DistributionalMethod";

	/**
	 * Name of the parameter that must be set to enable the distributional
	 * method
	 */
	public static final String P_SIMILARITY_DISTANCE = "SimilarityDistanceClassName";

	public AlignerAdvancedSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_METHOD_COMPOSITIONAL,
				ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, P_METHOD_DISTRIBUTIONAL,
				ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, P_SIMILARITY_DISTANCE,
				ConfigurationParameter.TYPE_STRING, false, false,
				"values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosine");

	}

	@Override
	protected String[][] getParameterGroups() {
		return null;
	}

	@Override
	protected String[][] getButtonGroups() {
		return new String[][] {
				// Group different methods
				new String[] { P_METHOD_COMPOSITIONAL, P_METHOD_DISTRIBUTIONAL } 
		};
	}

}
