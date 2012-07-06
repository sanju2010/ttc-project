package eu.project.ttc.tools.indexer;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class IndexerAdvancedSettings extends Parameters implements
		TermSuiteSettings {

	/**
	 * Name of the parameter that must be set to ignore diacritics in multiword
	 * term conflating
	 */
	public static final String P_IGNORE_DIACRITICS = "IgnoreDiacriticsInMultiwordTerms";

	/** Name of the parameter that must be set to enable variant detection */
	public static final String P_TERM_VARIANT_DETECTION = "VariantsDetection";

	/** Name of the parameter that must be set to the edit distance classname */
	public static final String P_EDIT_DISTANCE_CLASS = "EditDistanceClassName";

	/** Name of the parameter that must be set to the distance threshold */
	public static final String P_EDIT_DISTANCE_THRESHOLD = "EditDistanceThreshold";

	/** Name of the parameter that must be set to the distance ngrams */
	public static final String P_EDIT_DISTANCE_NGRAMS = "EditDistanceNgrams";

	/** Name of the parameter that must be set to filter terms by frequency */
	public static final String P_FREQUENCY_THRESHOLD = "OccurrenceThreshold";

	/** Name of the parameter that must be set to association measure class name */
	public static final String P_ASSOCIATION_MEASURE = "AssociationMeasure";

	public IndexerAdvancedSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_TERM_VARIANT_DETECTION,
				ConfigurationParameter.TYPE_BOOLEAN, false, true);
		this.addParameter(declarations, P_EDIT_DISTANCE_CLASS,
				ConfigurationParameter.TYPE_STRING, false, true,
				"values:eu.project.ttc.metrics.Levenshtein|eu.project.ttc.metrics.LongestCommonSubsequence|eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein");
		this.addParameter(declarations, P_EDIT_DISTANCE_THRESHOLD,
				ConfigurationParameter.TYPE_FLOAT, false, true);
		this.addParameter(declarations, P_EDIT_DISTANCE_NGRAMS,
				ConfigurationParameter.TYPE_INTEGER, false, true);
		this.addParameter(declarations, P_IGNORE_DIACRITICS,
				ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, P_FREQUENCY_THRESHOLD,
				ConfigurationParameter.TYPE_INTEGER, false, true);
		this.addParameter(declarations, P_ASSOCIATION_MEASURE,
				ConfigurationParameter.TYPE_STRING, false, true,
				"values:eu.project.ttc.metrics.LogLikelihood|eu.project.ttc.metrics.MutualInformation");
	}

	@Override
	protected String[][] getParameterGroups() {
		return new String[][] {
				// Variant detection group
				new String[] { " Conflation   ", P_TERM_VARIANT_DETECTION, "true" },
				new String[] { P_EDIT_DISTANCE_CLASS,
						P_EDIT_DISTANCE_THRESHOLD, P_EDIT_DISTANCE_NGRAMS,
						P_IGNORE_DIACRITICS },
				// Context vector group
				new String[] { " Context vectors   ", P_FREQUENCY_THRESHOLD,
						"false" }, 
				new String[] { P_ASSOCIATION_MEASURE } 
		};
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}

}
