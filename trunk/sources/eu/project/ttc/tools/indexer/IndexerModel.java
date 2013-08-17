package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.commons.InvalidTermSuiteConfiguration;
import eu.project.ttc.tools.commons.ToolModel;
import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.*;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Model of the Indexer tool.
 *
 * This model maintains the values of the parameters used by the dedicated engine.
 */
public class IndexerModel extends ToolModel implements IndexerBinding {

    /** Language configuration parameter */
    private ConfigurationParameter pLang;
    /** Input directory where the resources to be processed will be found */
    private ConfigurationParameter pIDir;
    /** Output directory where the results of processing will be stored */
    private ConfigurationParameter pODir;
    /** Parameter to ignore diacritics in multiword term conflating */
    private ConfigurationParameter pIgnoreDiacritics;
    /** Parameter to enable variant detection */
    private ConfigurationParameter pVariantDetection;
    /** Parameter for the edit distance classname */
    private ConfigurationParameter pEditDistanceClass;
    /** Parameter for the distance threshold */
    private ConfigurationParameter pEditDistanceThreshold;
    /** Parameter for the distance ngrams */
    private ConfigurationParameter pEditDistanceNgrams;
    /** Parameter to filter terms by frequency */
    private ConfigurationParameter pFrequencyThreshold;
    /** Parameter for the association measure class name */
    private ConfigurationParameter pAssociationMeasure;
    /** Parameter to filter terms by frequency */
    private ConfigurationParameter pFilteringThreshold;
    /** Parameter to filter terms by a specified criteria */
    private ConfigurationParameter pFilterRule;
    /** Parameter to keep verbs and others */
    private ConfigurationParameter pKeepVerbs;
    /** Parameter for TSV output */
    private ConfigurationParameter pTSV;

    // Where the parameter value are stored
    ConfigurationParameterSettings pSettings;

    /**
     * Constructor.
     *
     * @param indexerCfg
     *      configuration file where the model is persisted
     */
    public IndexerModel(File indexerCfg) {
        super(indexerCfg);
        declareParameters();
    }

    /**
     * Declare the parameters that are used by the Indexer tool.
     */
    private void declareParameters() {
        // Language
        pLang = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pLang.setName(CFG.LANGUAGE.getParameter());
        pLang.setType(ConfigurationParameter.TYPE_STRING);
        pLang.setMultiValued(false);
        pLang.setMandatory(true);
        pLang.setDescription("values:en|fr|de|es|ru|da|lv|zh");

        // Input directory
        pIDir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pIDir.setName(CFG.INPUT.getParameter());
        pIDir.setType(ConfigurationParameter.TYPE_STRING);
        pIDir.setMultiValued(false);
        pIDir.setMandatory(true);

        // Output directory
        pODir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pODir.setName(CFG.OUTPUT.getParameter());
        pODir.setType(ConfigurationParameter.TYPE_STRING);
        pODir.setMultiValued(false);
        pODir.setMandatory(true);

        // Variants detection
        pVariantDetection = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pVariantDetection.setName(CFG.VARIANTDETECTION.getParameter());
        pVariantDetection.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pVariantDetection.setMultiValued(false);
        pVariantDetection.setMandatory(true);

        // Edit distance class
        pEditDistanceClass = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEditDistanceClass.setName(CFG.EDITDISTANCECLS.getParameter());
        pEditDistanceClass.setType(ConfigurationParameter.TYPE_STRING);
        pEditDistanceClass.setMultiValued(false);
        pEditDistanceClass.setMandatory(true);
        pEditDistanceClass.setDescription(
                "values:eu.project.ttc.metrics.Levenshtein" +
                "|eu.project.ttc.metrics.LongestCommonSubsequence" +
                "|eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein");

        // Edit distance threshold
        pEditDistanceThreshold = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEditDistanceThreshold.setName(CFG.EDITDISTANCETLD.getParameter());
        pEditDistanceThreshold.setType(ConfigurationParameter.TYPE_FLOAT);
        pEditDistanceThreshold.setMultiValued(false);
        pEditDistanceThreshold.setMandatory(true);

        // Edit distance ngrams
        pEditDistanceNgrams = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEditDistanceNgrams.setName(CFG.EDITDISTANCENGRAMS.getParameter());
        pEditDistanceNgrams.setType(ConfigurationParameter.TYPE_INTEGER);
        pEditDistanceNgrams.setMultiValued(false);
        pEditDistanceNgrams.setMandatory(true);

        // Ignore diacritics
        pIgnoreDiacritics = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pIgnoreDiacritics.setName(CFG.IGNOREDIACRITICS.getParameter());
        pIgnoreDiacritics.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pIgnoreDiacritics.setMultiValued(false);
        pIgnoreDiacritics.setMandatory(false);

        // Frequency threshold
        pFrequencyThreshold = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pFrequencyThreshold.setName(CFG.FREQUENCYTLD.getParameter());
        pFrequencyThreshold.setType(ConfigurationParameter.TYPE_INTEGER);
        pFrequencyThreshold.setMultiValued(false);
        pFrequencyThreshold.setMandatory(true);

        // Association measure
        pAssociationMeasure = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pAssociationMeasure.setName(CFG.ASSOCIATIONMEASURE.getParameter());
        pAssociationMeasure.setType(ConfigurationParameter.TYPE_STRING);
        pAssociationMeasure.setMultiValued(false);
        pAssociationMeasure.setMandatory(true);
        pAssociationMeasure.setDescription(
                "values:eu.project.ttc.metrics.LogLikelihood" +
                        "|eu.project.ttc.metrics.MutualInformation");

        // Filtering rule
        pFilterRule = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pFilterRule.setName(CFG.FILTERRULE.getParameter());
        pFilterRule.setType(ConfigurationParameter.TYPE_STRING);
        pFilterRule.setMultiValued(false);
        pFilterRule.setMandatory(true);
        pFilterRule.setDescription(
                "values:None" +
                        "|OccurrenceThreshold" +
                        "|FrequencyThreshold" +
                        "|SpecificityThreshold" +
                        "|TopNByOccurrence" +
                        "|TopNByFrequency" +
                        "|TopNBySpecificity");

        // Filtering threshold
        pFilteringThreshold = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pFilteringThreshold.setName(CFG.FILTERINGTLD.getParameter());
        pFilteringThreshold.setType(ConfigurationParameter.TYPE_FLOAT);
        pFilteringThreshold.setMultiValued(false);
        pFilteringThreshold.setMandatory(true);

        // Keep verbs
        pKeepVerbs = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pKeepVerbs.setName(CFG.KEEPVERBS.getParameter());
        pKeepVerbs.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pKeepVerbs.setMultiValued(false);
        pKeepVerbs.setMandatory(true);

        // Enable TSV
        pTSV = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pTSV.setName(CFG.TSV.getParameter());
        pTSV.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pTSV.setMultiValued(false);
        pTSV.setMandatory(true);

        // Bundle everything in a ConfigurationParameterSettings to add values
        pSettings = UIMAFramework.getResourceSpecifierFactory()
                .createConfigurationParameterSettings();
    }


    /**
     * Load persisted parameters values from the configuration file.
     * We use UIMA metadata resource format to persist the configuration.
     *
     * @see eu.project.ttc.tools.commons.ToolModel#load()
     */
    @Override
    public void load() throws IOException, InvalidTermSuiteConfiguration {
        // Check if the file exist, not an error if it is empty
        if (!getConfigurationFile().exists())
            return;

        // Load data from the persisted file as a UIMA metadata resource
        ResourceMetaData uimaMetadata;
        XMLInputSource input = new XMLInputSource(getConfigurationFile());
        try {
            uimaMetadata = UIMAFramework.getXMLParser().parseResourceMetaData(input);
        } catch (InvalidXMLException e) {
            UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
            throw new IOException(e);
        }

        // Set the model properties accordingly
        ConfigurationParameterSettings settings =
                uimaMetadata.getConfigurationParameterSettings();
        for(NameValuePair nvp: settings.getParameterSettings()) {
            try {
                switch ( CFG.fromParameter(nvp.getName()) ) {
                    case LANGUAGE:
                        setLanguage((String) nvp.getValue());
                        break;
                    case INPUT:
                        setInputDirectory((String) nvp.getValue());
                        break;
                    case OUTPUT:
                        setOutputDirectory((String) nvp.getValue());
                        break;
                    case IGNOREDIACRITICS:
                        setIgnoreDiacritics((Boolean) nvp.getValue());
                        break;
                    case VARIANTDETECTION:
                        setVariantDetection((Boolean) nvp.getValue());
                        break;
                    case EDITDISTANCECLS:
                        setEditDistanceClass((String) nvp.getValue());
                        break;
                    case EDITDISTANCETLD:
                        setEditDistanceThreshold((Float) nvp.getValue());
                        break;
                    case EDITDISTANCENGRAMS:
                        setEditDistanceNgrams((Integer) nvp.getValue());
                        break;
                    case FREQUENCYTLD:
                        setFrequencyThreshold((Float) nvp.getValue());
                        break;
                    case ASSOCIATIONMEASURE:
                        setAssociationMeasure((String) nvp.getValue());
                        break;
                    case FILTERINGTLD:
                        setFilteringThreshold((Float) nvp.getValue());
                        break;
                    case FILTERRULE:
                        setFilterRule((String) nvp.getValue());
                        break;
                    case KEEPVERBS:
                        setKeepVerbs((Boolean) nvp.getValue());
                        break;
                    case TSV:
                        setTSVExport((Boolean) nvp.getValue());
                        break;
                    default:
                        UIMAFramework.getLogger().log(Level.WARNING,
                                "Ignoring parameter {} as it is not supported by the model.",
                                new String[]{nvp.getName()});
                }
            } catch (IllegalArgumentException e) {
                String msg = "Unable to correctly load the configuration persisted in file '"
                        + getConfigurationFile().getAbsolutePath() + "' as it contains invalid values.";
                UIMAFramework.getLogger().log(Level.SEVERE, msg);
                throw new InvalidTermSuiteConfiguration(msg, e);
            }
        }
    }

    /**
     * Persist the model as it is to a configuration file.
     * We use UIMA metadata resource format to persist the configuration.
     *
     * @see eu.project.ttc.tools.commons.ToolModel#save()
     */
    @Override
    public void save() throws IOException {
        // Create the UIMA declaration out of the properties
        ConfigurationParameterDeclarations uimaParamDeclarations = UIMAFramework
                .getResourceSpecifierFactory()
                .createConfigurationParameterDeclarations();
        uimaParamDeclarations.addConfigurationParameter(pLang);
        uimaParamDeclarations.addConfigurationParameter(pIDir);
        uimaParamDeclarations.addConfigurationParameter(pODir);
        uimaParamDeclarations.addConfigurationParameter(pIgnoreDiacritics);
        uimaParamDeclarations.addConfigurationParameter(pVariantDetection);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceClass);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceThreshold);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceNgrams);
        uimaParamDeclarations.addConfigurationParameter(pFrequencyThreshold);
        uimaParamDeclarations.addConfigurationParameter(pAssociationMeasure);
        uimaParamDeclarations.addConfigurationParameter(pFilteringThreshold);
        uimaParamDeclarations.addConfigurationParameter(pFilterRule);
        uimaParamDeclarations.addConfigurationParameter(pKeepVerbs);
        uimaParamDeclarations.addConfigurationParameter(pTSV);

        // Create and populate the metadata
        ResourceMetaData uimaMetadata = UIMAFramework
                .getResourceSpecifierFactory().createResourceMetaData();
        uimaMetadata.setConfigurationParameterDeclarations(uimaParamDeclarations);
        uimaMetadata.setConfigurationParameterSettings(pSettings);

        // Persist everything
        OutputStream out = new FileOutputStream(getConfigurationFile());
        try {
            uimaMetadata.toXML(out);
        } catch (SAXException e) {
            throw new IOException(e);
        } finally {
            out.close();
        }
    }

    @Override
    public void validate() throws ResourceConfigurationException {
        // FIXME code is redundant with save()

        // Create the UIMA declaration out of the properties
        ConfigurationParameterDeclarations uimaParamDeclarations = UIMAFramework
                .getResourceSpecifierFactory()
                .createConfigurationParameterDeclarations();
        uimaParamDeclarations.addConfigurationParameter(pLang);
        uimaParamDeclarations.addConfigurationParameter(pIDir);
        uimaParamDeclarations.addConfigurationParameter(pODir);
        uimaParamDeclarations.addConfigurationParameter(pIgnoreDiacritics);
        uimaParamDeclarations.addConfigurationParameter(pVariantDetection);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceClass);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceThreshold);
        uimaParamDeclarations.addConfigurationParameter(pEditDistanceNgrams);
        uimaParamDeclarations.addConfigurationParameter(pFrequencyThreshold);
        uimaParamDeclarations.addConfigurationParameter(pAssociationMeasure);
        uimaParamDeclarations.addConfigurationParameter(pFilteringThreshold);
        uimaParamDeclarations.addConfigurationParameter(pFilterRule);
        uimaParamDeclarations.addConfigurationParameter(pKeepVerbs);
        uimaParamDeclarations.addConfigurationParameter(pTSV);

        // Create and populate the metadata
        ResourceMetaData uimaMetadata = UIMAFramework
                .getResourceSpecifierFactory().createResourceMetaData();
        uimaMetadata.setConfigurationParameterDeclarations(uimaParamDeclarations);
        uimaMetadata.setConfigurationParameterSettings(pSettings);

        uimaMetadata.validateConfigurationParameterSettings();
    }


    //////////////////////////////////////////////////////////////////// ACCESSORS

    /**
     * Setter for language parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setLanguage(String language) {
        if ( language.matches("en|fr|de|es|ru|da|lv|zh") ) {
            String oldValue = (String) pSettings.getParameterValue(pLang.getName());
            pSettings.setParameterValue(pLang.getName(), language);
            firePropertyChange(CFG.LANGUAGE.getProperty(), oldValue, language);
        } else {
            String msg = "Language parameter value '" + language
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for language property */
    @Override
    public String getLanguage() {
        return (String) pSettings.getParameterValue(CFG.LANGUAGE.getParameter());
    }
    /** Listener binder for language property */
    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.LANGUAGE.getProperty(), listener);
    }

    /**
     * Setter for input directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setInputDirectory(String inputDirectory) {
        File input = new File(inputDirectory);
        if ( input.exists() && input.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(pIDir.getName());
            pSettings.setParameterValue(pIDir.getName(), inputDirectory);
            firePropertyChange(CFG.INPUT.getProperty(), oldValue, inputDirectory);
        } else {
            String msg = "Input directory parameter value '" + inputDirectory
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for input directory property */
    @Override
    public String getInputDirectory() {
        return (String) pSettings.getParameterValue(CFG.INPUT.getParameter());
    }
    /** Listener binder for input directory property */
    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.INPUT.getProperty(), listener);
    }

    /**
     * Setter for output directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setOutputDirectory(String outputDirectory) {
        File output = new File(outputDirectory);
        if ( ! output.exists() ) {
            output.mkdirs(); // make sure output directory exists
        }
        if ( output.exists() && output.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(pODir.getName());
            pSettings.setParameterValue(pODir.getName(), outputDirectory);
            firePropertyChange(CFG.OUTPUT.getProperty(), oldValue, outputDirectory);
        } else {
            String msg = "Output directory parameter value '" + outputDirectory
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for output directory property */
    @Override
    public String getOutputDirectory() {
        return (String) pSettings.getParameterValue(CFG.OUTPUT.getParameter());
    }
    /** Listener binder for output directory property */
    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.OUTPUT.getProperty(), listener);
    }

    /**
     * Setter for ignore diacritics parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setIgnoreDiacritics(boolean ignoreDiacritics) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(CFG.IGNOREDIACRITICS.getParameter());
        pSettings.setParameterValue(CFG.IGNOREDIACRITICS.getParameter(), ignoreDiacritics);
        firePropertyChange(CFG.IGNOREDIACRITICS.getProperty(), oldValue, ignoreDiacritics);
    }
    /** Getter for ignore diacritics property */
    @Override
    public boolean isIgnoreDiacritics() {
        return (Boolean) pSettings.getParameterValue(CFG.IGNOREDIACRITICS.getParameter());
    }
    /** Listener binder for ignore diacritics property */
    @Override
    public void addIgnoreDiacriticsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.IGNOREDIACRITICS.getProperty(), listener);
    }

    /**
     * Setter for variant detection parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setVariantDetection(boolean variantDetection) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(CFG.VARIANTDETECTION.getParameter());
        pSettings.setParameterValue(CFG.VARIANTDETECTION.getParameter(), variantDetection);
        firePropertyChange(CFG.VARIANTDETECTION.getProperty(), oldValue, variantDetection);
    }
    /** Getter for variant detection property */
    @Override
    public boolean isVariantDetection() {
        return (Boolean) pSettings.getParameterValue(CFG.VARIANTDETECTION.getParameter());
    }
    /** Listener binder for variant detection property */
    @Override
    public void addVariantDetectionChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.VARIANTDETECTION.getProperty(), listener);
    }

    /**
     * Setter for edit distance class parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setEditDistanceClass(String editDistanceClass) {
        if ( editDistanceClass.matches("eu\\.project\\.ttc\\.metrics\\.Levenshtein" +
                "|eu\\.project\\.ttc\\.metrics\\.LongestCommonSubsequence" +
                "|eu\\.project\\.ttc\\.metrics\\.DiacriticInsensitiveLevenshtein") ) {
            String oldValue = (String) pSettings.getParameterValue(CFG.EDITDISTANCECLS.getParameter());
            pSettings.setParameterValue(CFG.EDITDISTANCECLS.getParameter(), editDistanceClass);
            firePropertyChange(CFG.EDITDISTANCECLS.getProperty(), oldValue, editDistanceClass);
        } else {
            String msg = "Edit distance class parameter value '" + editDistanceClass
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for edit distance class property */
    @Override
    public String getEditDistanceClass() {
        return (String) pSettings.getParameterValue(CFG.EDITDISTANCECLS.getParameter());
    }
    /** Listener binder for edit distance class property */
    @Override
    public void addEditDistanceClassChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.EDITDISTANCECLS.getProperty(), listener);
    }

    /**
     * Setter for edit distance threshold parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setEditDistanceThreshold(Float editDistanceThreshold) {
        if ( (editDistanceThreshold >= 0) && (editDistanceThreshold <= 1.0) ) {
            Float oldValue = (Float) pSettings.getParameterValue(CFG.EDITDISTANCETLD.getParameter());
            pSettings.setParameterValue(CFG.EDITDISTANCETLD.getParameter(), editDistanceThreshold);
            firePropertyChange(CFG.EDITDISTANCETLD.getProperty(), oldValue, editDistanceThreshold);
        } else {
            String msg = "Edit distance threshold parameter value '" + editDistanceThreshold
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for edit distance threshold property */
    @Override
    public Float getEditDistanceThreshold() {
        return (Float) pSettings.getParameterValue(CFG.EDITDISTANCETLD.getParameter());
    }
    /** Listener binder for edit distance threshold property */
    @Override
    public void addEditDistanceThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.EDITDISTANCETLD.getProperty(), listener);
    }

    /**
     * Setter for edit distance ngrams parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setEditDistanceNgrams(Integer editDistanceNgrams) {
        if ( editDistanceNgrams > 0 ) {
            Integer oldValue = (Integer) pSettings.getParameterValue(CFG.EDITDISTANCENGRAMS.getParameter());
            pSettings.setParameterValue(CFG.EDITDISTANCENGRAMS.getParameter(), editDistanceNgrams);
            firePropertyChange(CFG.EDITDISTANCENGRAMS.getProperty(), oldValue, editDistanceNgrams);
        } else {
            String msg = "Edit distance ngrams parameter value '" + editDistanceNgrams
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for edit distance ngrams property */
    @Override
    public Integer getEditDistanceNgrams() {
        return (Integer) pSettings.getParameterValue(CFG.EDITDISTANCENGRAMS.getParameter());
    }
    /** Listener binder for edit distance ngrams property */
    @Override
    public void addEditDistanceNgramsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.EDITDISTANCENGRAMS.getProperty(), listener);
    }

    /**
     * Setter for frequency threshold parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setFrequencyThreshold(Float frequencyThreshold) {
        if ( (frequencyThreshold >= 0) && (frequencyThreshold <= 1.0) ) {
            Float oldValue = (Float) pSettings.getParameterValue(CFG.FREQUENCYTLD.getParameter());
            pSettings.setParameterValue(CFG.FREQUENCYTLD.getParameter(), frequencyThreshold);
            firePropertyChange(CFG.FREQUENCYTLD.getProperty(), oldValue, frequencyThreshold);
        } else {
            String msg = "Frequency threshold parameter value '" + frequencyThreshold
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for frequency threshold property */
    @Override
    public Float getFrequencyThreshold() {
        return (Float) pSettings.getParameterValue(CFG.FREQUENCYTLD.getParameter());
    }
    /** Listener binder for frequency threshold property */
    @Override
    public void addFrequencyThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FREQUENCYTLD.getProperty(), listener);
    }

    /**
     * Setter for association measure parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setAssociationMeasure(String associationMeasure) {
        if ( associationMeasure.matches("eu\\.project\\.ttc\\.metrics\\.LogLikelihood" +
                "|eu\\.project\\.ttc\\.metrics\\.MutualInformation") ) {
            String oldValue = (String) pSettings.getParameterValue(CFG.ASSOCIATIONMEASURE.getParameter());
            pSettings.setParameterValue(CFG.ASSOCIATIONMEASURE.getParameter(), associationMeasure);
            firePropertyChange(CFG.ASSOCIATIONMEASURE.getProperty(), oldValue, associationMeasure);
        } else {
            String msg = "Association measure parameter value '" + associationMeasure
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for association measure property */
    @Override
    public String getAssociationMeasure() {
        return (String) pSettings.getParameterValue(CFG.ASSOCIATIONMEASURE.getParameter());
    }
    /** Listener binder for association measure property */
    @Override
    public void addAssociationMeasureChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.ASSOCIATIONMEASURE.getProperty(), listener);
    }

    /**
     * Setter for filtering threshold parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setFilteringThreshold(Float filteringThreshold) {
        if ( filteringThreshold >= 0 ) {
            Float oldValue = (Float) pSettings.getParameterValue(CFG.FILTERINGTLD.getParameter());
            pSettings.setParameterValue(CFG.FILTERINGTLD.getParameter(), filteringThreshold);
            firePropertyChange(CFG.FILTERINGTLD.getProperty(), oldValue, filteringThreshold);
        } else {
            String msg = "Filtering threshold parameter value '" + filteringThreshold
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for filtering threshold property */
    @Override
    public Float getFilteringThreshold() {
        return (Float) pSettings.getParameterValue(CFG.FILTERINGTLD.getParameter());
    }
    /** Listener binder for filtering threshold property */
    @Override
    public void addFilteringThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FILTERINGTLD.getProperty(), listener);
    }

    /**
     * Setter for filter rule parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setFilterRule(String filterRule) {
        if ( filterRule.matches("None|OccurrenceThreshold|FrequencyThreshold"
                + "|SpecificityThreshold|TopNByOccurrence|TopNByFrequency|TopNBySpecificity") ) {
            String oldValue = (String) pSettings.getParameterValue(CFG.FILTERRULE.getParameter());
            pSettings.setParameterValue(CFG.FILTERRULE.getParameter(), filterRule);
            firePropertyChange(CFG.FILTERRULE.getProperty(), oldValue, filterRule);
        } else {
            String msg = "Filter rule parameter value '" + filterRule
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for filter rule property */
    @Override
    public String getFilterRule() {
        return (String) pSettings.getParameterValue(CFG.FILTERRULE.getParameter());
    }
    /** Listener binder for filter rule property */
    @Override
    public void addFilterRuleChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FILTERRULE.getProperty(), listener);
    }

    /**
     * Setter for keep verbs parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setKeepVerbs(Boolean keepVerbs) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(CFG.KEEPVERBS.getParameter());
        pSettings.setParameterValue(CFG.KEEPVERBS.getParameter(), keepVerbs);
        firePropertyChange(CFG.KEEPVERBS.getProperty(), oldValue, keepVerbs);
    }
    /** Getter for keep verbs property */
    @Override
    public Boolean isKeepVerbs() {
        return (Boolean) pSettings.getParameterValue(CFG.KEEPVERBS.getParameter());
    }
    /** Listener binder for keep verbs property */
    @Override
    public void addKeepVerbsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.KEEPVERBS.getProperty(), listener);
    }

    /**
     * Setter for TSV export parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setTSVExport(Boolean tsvExport) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(CFG.TSV.getParameter());
        pSettings.setParameterValue(CFG.TSV.getParameter(), tsvExport);
        firePropertyChange(CFG.TSV.getProperty(), oldValue, tsvExport);
    }
    /** Getter for TSV export property */
    @Override
    public Boolean isTSVExport() {
        return (Boolean) pSettings.getParameterValue(CFG.TSV.getParameter());
    }
    /** Listener binder for TSV export property */
    @Override
    public void addTSVExportChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.TSV.getProperty(), listener);
    }

}