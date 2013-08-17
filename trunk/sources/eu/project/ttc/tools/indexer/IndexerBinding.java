package eu.project.ttc.tools.indexer;

import java.beans.PropertyChangeListener;

/**
 * This interface declares the parameters as well as the associated methods
 * (accessor and listeners) that should be available by any view or model
 * related to the Indexer tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 15/08/13
 */
public interface IndexerBinding {

    /** Prefix of the Indexer properties */
    public final static String EVT_PREFIX = "indexer.";

    /** Enum of all the properties handled by the Indexer models and views */
    public static enum CFG {
        /** Language parameter */
        LANGUAGE            ("Language", "indexer.language"),
        /** Input directory parameter */
        INPUT               ("InputDirectory", "indexer.inputdirectory"),
        /** Output directory parameter */
        OUTPUT              ("OutputDirectory", "indexer.outputdirectory"),
        /** Ignore diacritics parameter */
        IGNOREDIACRITICS    ("IgnoreDiacriticsInMultiwordTerms", "indexer.ignorediacritics"),
        /** Variant detection parameter */
        VARIANTDETECTION    ("VariantDetection", "indexer.variantdetection"),
        /** Edit distance class parameter */
        EDITDISTANCECLS     ("EditDistanceClassName", "indexer.editdistanceclass"),
        /** Edit distance threshold parameter */
        EDITDISTANCETLD     ("EditDistanceThreshold", "indexer.editdistancethreshold"),
        /** Edit distance ngrams parameter */
        EDITDISTANCENGRAMS  ("EditDistanceNgrams", "indexer.editdistancengrams"),
        /** Frequency threshold parameter */
        FREQUENCYTLD        ("OccurrenceThreshold", "indexer.frequencythreshold"),
        /** Association measure parameter */
        ASSOCIATIONMEASURE  ("AssociationMeasure", "indexer.associationmeasure"),
        /** Filtering threshold parameter */
        FILTERINGTLD        ("FilterRuleThreshold", "indexer.filteringthreshold"),
        /** Filter rule parameter */
        FILTERRULE          ("FilterRule", "indexer.filterrule"),
        /** Keep verbs parameter */
        KEEPVERBS           ("KeepVerbsAndOthers", "indexer.keepverbs"),
        /** TSV export parameter */
        TSV                 ("EnableTsvOutput", "indexer.tsv");

        private final String property;
        private final String parameter;

        CFG(String uimaParameter, String property) {
            this.parameter = uimaParameter;
            this.property = property;
        }

        public String getProperty() {
            return this.property;
        }

        public String getParameter() {
            return this.parameter;
        }

        /**
         * Get a particular CFG from a parameter name.
         *
         * @param parameter
         *      name of the parameter we want the corresponding CFG of
         * @return
         *      either the CFG if found, null otherwise
         */
        public static CFG fromParameter(String parameter) {
            if (parameter != null) {
                for (CFG c : CFG.values()) {
                    if (parameter.equals(c.getParameter())) {
                        return c;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Available filter rules for the indexer.
     */
    public static enum FilterRules {
        None, OccurrenceThreshold, FrequencyThreshold, SpecificityThreshold,
        TopNByOccurrence, TopNByFrequency, TopNBySpecificity
    }

    ///////////////////////////////////////////////////////////// LANGUAGE

    /**
     * Setter for the language parameter value.
     * @param language  two letters language code
     */
    void setLanguage(String language);

    /**
     * Accessor to the language parameter value.
     */
    String getLanguage();

    /**
     * Listener to a change regarding the language parameter.
     */
    void addLanguageChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// INPUT DIRECTORY

    /**
     * Setter for the input directory parameter value.
     * @param inputDirectory  path to the directory to use
     */
    void setInputDirectory(String inputDirectory);

    /**
     * Accessor to the input directory parameter value.
     */
    String getInputDirectory();

    /**
     * Listener to a change regarding the input directory parameter.
     */
    void addInputDirectoryChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// OUTPUT DIRECTORY

    /**
     * Setter for the output directory parameter value.
     * @param outputDirectory  path to the directory to use
     */
    void setOutputDirectory(String outputDirectory);

    /**
     * Accessor to the output directory parameter value.
     */
    String getOutputDirectory();

    /**
     * Listener to a change regarding the output directory parameter.
     */
    void addOutputDirectoryChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// IGNORE DIACRITICS

    /**
     * Setter for the ignore diacritics parameter value.
     * @param ignoreDiacritics  flag for ignoring diacritics or not
     */
    void setIgnoreDiacritics(boolean ignoreDiacritics);

    /**
     * Accessor to the ignore diacritics parameter value.
     */
    boolean isIgnoreDiacritics();

    /**
     * Listener to a change regarding the ignore diacritics parameter.
     */
    void addIgnoreDiacriticsChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// VARIANT DETECTIONS

    /**
     * Setter for the variant detection parameter value.
     * @param variantDetection  flag for variant detection or not
     */
    void setVariantDetection(boolean variantDetection);

    /**
     * Accessor to the variant detection parameter value.
     */
    boolean isVariantDetection();

    /**
     * Listener to a change regarding the variant detection parameter.
     */
    void addVariantDetectionChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// EDIT DISTANCE CLASS

    /**
     * Setter for the edit distance class parameter value.
     * @param editDistanceClass  name of the class for computing edit distance
     */
    void setEditDistanceClass(String editDistanceClass);

    /**
     * Accessor to the edit distance class parameter value.
     */
    String getEditDistanceClass();

    /**
     * Listener to a change regarding the edit distance class parameter.
     */
    void addEditDistanceClassChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// EDIT DISTANCE THRESHOLD

    /**
     * Setter for the edit distance threshold parameter value.
     * @param editDistanceThreshold threshold value for the edit distance
     */
    void setEditDistanceThreshold(Float editDistanceThreshold);

    /**
     * Accessor to the edit distance threshold parameter value.
     */
    Float getEditDistanceThreshold();

    /**
     * Listener to a change regarding the edit distance threshold parameter.
     */
    void addEditDistanceThresholdChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// EDIT DISTANCE NGRAMS

    /**
     * Setter for the edit distance ngrams parameter value.
     * @param editDistanceNgrams    number of ngrams for the edit distance
     */
    void setEditDistanceNgrams(Integer editDistanceNgrams);

    /**
     * Accessor to the edit distance ngrams parameter value.
     */
    Integer getEditDistanceNgrams();

    /**
     * Listener to a change regarding the edit distance ngrams parameter.
     */
    void addEditDistanceNgramsChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// FREQUENCY THRESHOLD

    /**
     * Setter for the frequency threshold parameter value.
     * @param frequencyThreshold    threshold for the frequency filtering
     */
    void setFrequencyThreshold(Float frequencyThreshold);

    /**
     * Accessor to the frequency threshold parameter value.
     */
    Float getFrequencyThreshold();

    /**
     * Listener to a change regarding the frequency threshold parameter.
     */
    void addFrequencyThresholdChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// ASSOCIATION MEASURE

    /**
     * Setter for the association measure parameter value.
     * @param associationMeasure    association measure to use
     */
    void setAssociationMeasure(String associationMeasure);

    /**
     * Accessor to the association measure parameter value.
     */
    String getAssociationMeasure();

    /**
     * Listener to a change regarding the association measure parameter.
     */
    void addAssociationMeasureChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// FILTERING THRESHOLD

    /**
     * Setter for the filtering threshold parameter value.
     * @param filteringThreshold    threshold for the filtering
     */
    void setFilteringThreshold(Float filteringThreshold);

    /**
     * Accessor to the filtering threshold parameter value.
     */
    Float getFilteringThreshold();

    /**
     * Listener to a change regarding the filtering threshold parameter.
     */
    void addFilteringThresholdChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// FILTERING RULE

    /**
     * Setter for the filter rule parameter value.
     * @param filterRule    the filter rule
     */
    void setFilterRule(String filterRule);

    /**
     * Accessor to the filter rule parameter value.
     */
    String getFilterRule();

    /**
     * Listener to a change regarding the filter rule parameter.
     */
    void addFilterRuleChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// KEEP VERBS

    /**
     * Setter for the keep verbs parameter value.
     * @param keepVerbs    the keep verbs flag
     */
    void setKeepVerbs(Boolean keepVerbs);

    /**
     * Accessor to the keep verbs parameter value.
     */
    Boolean isKeepVerbs();

    /**
     * Listener to a change regarding the keep verbs parameter.
     */
    void addKeepVerbsChangeListener(PropertyChangeListener listener);

    ///////////////////////////////////////////////////////////// TSV

    /**
     * Setter for the TSV export parameter value.
     * @param tsvExport the TSV export flag
     */
    void setTSVExport(Boolean tsvExport);

    /**
     * Accessor to the TSV export parameter value.
     */
    Boolean isTSVExport();

    /**
     * Listener to a change regarding the TSV export parameter.
     */
    void addTSVExportChangeListener(PropertyChangeListener listener);

}
