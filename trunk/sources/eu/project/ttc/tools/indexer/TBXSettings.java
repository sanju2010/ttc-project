package eu.project.ttc.tools.indexer;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class TBXSettings extends Parameters implements TermSuiteSettings {

	/** Name of the parameter that must be set to filter terms by frequency */
	public static final String P_FILTERING_THRESHOLD = "FilterRuleThreshold";

	/**
	 * Name of the parameter that must be set to filter terms by a specified
	 * criteria
	 */
	public static final String P_FILTER_RULE = "FilterRule";

	/**
	 * Name of the parameter that must be set to filter terms by a specified
	 * criteria
	 */
	public static final String P_KEEP_VERBS = "KeepVerbsAndOthers";

	// FrequencyFilteringThreshold

	public TBXSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_FILTER_RULE,
				ConfigurationParameter.TYPE_STRING, false, true,
				"values:None|OccurrenceThreshold|FrequencyThreshold|SpecificityThreshold|TopNByOccurrence|TopNByFrequency|TopNBySpecificity");
		this.addParameter(declarations, P_FILTERING_THRESHOLD,
				ConfigurationParameter.TYPE_FLOAT, false, true);
		this.addParameter(declarations, P_KEEP_VERBS,
				ConfigurationParameter.TYPE_BOOLEAN, false, true);

	}

	@Override
	protected String[][] getParameterGroups() {
		return new String[][] {
				// Filtering settings
				new String[] { " Statistical filtering  ", P_FILTER_RULE, "false" }, 
				new String[] { P_FILTERING_THRESHOLD },
				// Keep verbs
				new String[] { " Grammatical categories  ", P_KEEP_VERBS, "false" },
				new String[] {} 
		};
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}
	
	/** Defines filter rules accepted for TBX output */
	public static enum FilterRules {
		None, OccurrenceThreshold ,FrequencyThreshold, SpecificityThreshold, 
		TopNByOccurrence, TopNByFrequency, TopNBySpecificity;
	}
}
