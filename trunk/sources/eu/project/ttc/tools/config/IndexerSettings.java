package eu.project.ttc.tools.config;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import javax.swing.*;

public class IndexerSettings extends Parameters implements TermSuiteSettings {

    public static final String P_LANGUAGE = "Language";
    public static final String P_INPUT_DIRECTORY = "InputDirectory";
    public static final String P_OUTPUT_DIRECTORY = "OutputDirectory";

    /**
     * Name of the parameter that must be set to ignore diacritics in multiword
     * term conflating
     */
    public static final String P_IGNORE_DIACRITICS = "IgnoreDiacriticsInMultiwordTerms";

    /** Name of the parameter that must be set to enable variant detection */
    public static final String P_TERM_VARIANT_DETECTION = "VariantDetection";

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

    /** Name of the parameter that enables TVS output */
    public static final String P_ENABLE_TSV = "EnableTsvOutput";

	public IndexerSettings(String resource) {
		super(resource);
//        getComponent().setBorder(BorderFactory.createTitledBorder("Settings"));
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_LANGUAGE,
                ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_INPUT_DIRECTORY,
                ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_OUTPUT_DIRECTORY,
                ConfigurationParameter.TYPE_STRING, false, true);
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
        this.addParameter(declarations, P_FILTER_RULE,
                ConfigurationParameter.TYPE_STRING, false, true,
                "values:None|OccurrenceThreshold|FrequencyThreshold|SpecificityThreshold|TopNByOccurrence|TopNByFrequency|TopNBySpecificity");
        this.addParameter(declarations, P_FILTERING_THRESHOLD,
                ConfigurationParameter.TYPE_FLOAT, false, true);
        this.addParameter(declarations, P_KEEP_VERBS,
                ConfigurationParameter.TYPE_BOOLEAN, false, true);
        this.addParameter(declarations, P_ENABLE_TSV,
                ConfigurationParameter.TYPE_BOOLEAN, false, true);
	}

    @Override
    protected String[][] getParameterGroups() {
        return new String[][] {
                // Basic
                new String[] { " Basic   ", P_LANGUAGE, "false" },
                new String[] { P_INPUT_DIRECTORY, P_OUTPUT_DIRECTORY },
                // Variant detection group
                new String[] { " Conflation   ", P_TERM_VARIANT_DETECTION, "true" },
                new String[] { P_EDIT_DISTANCE_CLASS,
                        P_EDIT_DISTANCE_THRESHOLD, P_EDIT_DISTANCE_NGRAMS,
                        P_IGNORE_DIACRITICS },
                // Context vector group
                new String[] { " Context vectors   ", P_FREQUENCY_THRESHOLD,
                        "false" },
                new String[] { P_ASSOCIATION_MEASURE },
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