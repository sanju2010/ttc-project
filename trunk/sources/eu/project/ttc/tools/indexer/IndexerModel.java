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
        pLang.setName(PRM.LANGUAGE.getParameter());
        pLang.setType(ConfigurationParameter.TYPE_STRING);
        pLang.setMultiValued(false);
        pLang.setMandatory(true);
        pLang.setDescription("values:en|fr|de|es|ru|da|lv|zh");

        // Input directory
        pIDir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pIDir.setName(PRM.INPUT.getParameter());
        pIDir.setType(ConfigurationParameter.TYPE_STRING);
        pIDir.setMultiValued(false);
        pIDir.setMandatory(true);

        // Output directory
        pODir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pODir.setName(PRM.OUTPUT.getParameter());
        pODir.setType(ConfigurationParameter.TYPE_STRING);
        pODir.setMultiValued(false);
        pODir.setMandatory(true);

        // Variants detection
        pVariantDetection = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pVariantDetection.setName(PRM.VARIANTDETECTION.getParameter());
        pVariantDetection.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pVariantDetection.setMultiValued(false);
        pVariantDetection.setMandatory(true);

        // Edit distance class
        pEditDistanceClass = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEditDistanceClass.setName(PRM.EDITDISTANCECLS.getParameter());
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
        pEditDistanceThreshold.setName(PRM.EDITDISTANCETLD.getParameter());
        pEditDistanceThreshold.setType(ConfigurationParameter.TYPE_FLOAT);
        pEditDistanceThreshold.setMultiValued(false);
        pEditDistanceThreshold.setMandatory(true);

        // Edit distance ngrams
        pEditDistanceNgrams = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEditDistanceNgrams.setName(PRM.EDITDISTANCENGRAMS.getParameter());
        pEditDistanceNgrams.setType(ConfigurationParameter.TYPE_INTEGER);
        pEditDistanceNgrams.setMultiValued(false);
        pEditDistanceNgrams.setMandatory(true);

        // Ignore diacritics
        pIgnoreDiacritics = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pIgnoreDiacritics.setName(PRM.IGNOREDIACRITICS.getParameter());
        pIgnoreDiacritics.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pIgnoreDiacritics.setMultiValued(false);
        pIgnoreDiacritics.setMandatory(false);

        // Frequency threshold
        pFrequencyThreshold = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pFrequencyThreshold.setName(PRM.FREQUENCYTLD.getParameter());
        pFrequencyThreshold.setType(ConfigurationParameter.TYPE_INTEGER);
        pFrequencyThreshold.setMultiValued(false);
        pFrequencyThreshold.setMandatory(true);

        // Association measure
        pAssociationMeasure = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pAssociationMeasure.setName(PRM.ASSOCIATIONMEASURE.getParameter());
        pAssociationMeasure.setType(ConfigurationParameter.TYPE_STRING);
        pAssociationMeasure.setMultiValued(false);
        pAssociationMeasure.setMandatory(true);
        pAssociationMeasure.setDescription(
                "values:eu.project.ttc.metrics.LogLikelihood" +
                        "|eu.project.ttc.metrics.MutualInformation");

        // Filtering rule
        pFilterRule = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pFilterRule.setName(PRM.FILTERRULE.getParameter());
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
        pFilteringThreshold.setName(PRM.FILTERINGTLD.getParameter());
        pFilteringThreshold.setType(ConfigurationParameter.TYPE_FLOAT);
        pFilteringThreshold.setMultiValued(false);
        pFilteringThreshold.setMandatory(true);

        // Keep verbs
        pKeepVerbs = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pKeepVerbs.setName(PRM.KEEPVERBS.getParameter());
        pKeepVerbs.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pKeepVerbs.setMultiValued(false);
        pKeepVerbs.setMandatory(true);

        // Enable TSV
        pTSV = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pTSV.setName(PRM.TSV.getParameter());
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
                switch ( PRM.fromParameter(nvp.getName()) ) {
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
                        setFrequencyThreshold((Integer) nvp.getValue());
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
            firePropertyChange(PRM.LANGUAGE.getProperty(), oldValue, language);
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
        return (String) pSettings.getParameterValue(PRM.LANGUAGE.getParameter());
    }
    /** Listener binder for language property */
    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.LANGUAGE.getProperty(), listener);
    }
    @Override
    public void setLanguageError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetLanguageError() {
        // TODO how to handle errors in the model ?
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
            firePropertyChange(PRM.INPUT.getProperty(), oldValue, inputDirectory);
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
        return (String) pSettings.getParameterValue(PRM.INPUT.getParameter());
    }
    /** Listener binder for input directory property */
    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.INPUT.getProperty(), listener);
    }
    @Override
    public void setInputDirectoryError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetInputDirectoryError() {
        // TODO how to handle errors in the model ?
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
            firePropertyChange(PRM.OUTPUT.getProperty(), oldValue, outputDirectory);
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
        return (String) pSettings.getParameterValue(PRM.OUTPUT.getParameter());
    }
    /** Listener binder for output directory property */
    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.OUTPUT.getProperty(), listener);
    }
    @Override
    public void setOutputDirectoryError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetOutputDirectoryError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for ignore diacritics parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setIgnoreDiacritics(boolean ignoreDiacritics) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.IGNOREDIACRITICS.getParameter());
        pSettings.setParameterValue(PRM.IGNOREDIACRITICS.getParameter(), ignoreDiacritics);
        firePropertyChange(PRM.IGNOREDIACRITICS.getProperty(), oldValue, ignoreDiacritics);
    }
    /** Getter for ignore diacritics property */
    @Override
    public Boolean isIgnoreDiacritics() {
        Boolean isIgnore = (Boolean) pSettings.getParameterValue(PRM.IGNOREDIACRITICS.getParameter());
        return isIgnore==null ? (Boolean) PRM.IGNOREDIACRITICS.getDefaultValue() : isIgnore;
    }
    /** Listener binder for ignore diacritics property */
    @Override
    public void addIgnoreDiacriticsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.IGNOREDIACRITICS.getProperty(), listener);
    }
    @Override
    public void setIgnoreDiacriticsError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetIgnoreDiacriticsError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for variant detection parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setVariantDetection(boolean variantDetection) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.VARIANTDETECTION.getParameter());
        pSettings.setParameterValue(PRM.VARIANTDETECTION.getParameter(), variantDetection);
        firePropertyChange(PRM.VARIANTDETECTION.getProperty(), oldValue, variantDetection);
    }
    /** Getter for variant detection property */
    @Override
    public Boolean isVariantDetection() {
        Boolean isVariantDetection = (Boolean) pSettings.getParameterValue(PRM.VARIANTDETECTION.getParameter());
        return isVariantDetection==null ? false : isVariantDetection;
    }
    /** Listener binder for variant detection property */
    @Override
    public void addVariantDetectionChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.VARIANTDETECTION.getProperty(), listener);
    }
    @Override
    public void setVariantDetectionError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetVariantDetectionError() {
        // TODO how to handle errors in the model ?
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
            String oldValue = (String) pSettings.getParameterValue(PRM.EDITDISTANCECLS.getParameter());
            pSettings.setParameterValue(PRM.EDITDISTANCECLS.getParameter(), editDistanceClass);
            firePropertyChange(PRM.EDITDISTANCECLS.getProperty(), oldValue, editDistanceClass);
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
        String className = (String) pSettings.getParameterValue(PRM.EDITDISTANCECLS.getParameter());
        return className==null ? (String) PRM.EDITDISTANCECLS.getDefaultValue() : className;
    }
    /** Listener binder for edit distance class property */
    @Override
    public void addEditDistanceClassChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCECLS.getProperty(), listener);
    }
    @Override
    public void setEditDistanceClassError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetEditDistanceClassError() {
        // TODO how to handle errors in the model ?
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
            Float oldValue = (Float) pSettings.getParameterValue(PRM.EDITDISTANCETLD.getParameter());
            pSettings.setParameterValue(PRM.EDITDISTANCETLD.getParameter(), editDistanceThreshold);
            firePropertyChange(PRM.EDITDISTANCETLD.getProperty(), oldValue, editDistanceThreshold);
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
        Float threshold = (Float) pSettings.getParameterValue(PRM.EDITDISTANCETLD.getParameter());
        return threshold==null ? 0f : threshold;
    }
    /** Listener binder for edit distance threshold property */
    @Override
    public void addEditDistanceThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCETLD.getProperty(), listener);
    }
    @Override
    public void setEditDistanceThresholdError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetEditDistanceThresholdError() {
        // TODO how to handle errors in the model ?
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
            Integer oldValue = (Integer) pSettings.getParameterValue(PRM.EDITDISTANCENGRAMS.getParameter());
            pSettings.setParameterValue(PRM.EDITDISTANCENGRAMS.getParameter(), editDistanceNgrams);
            firePropertyChange(PRM.EDITDISTANCENGRAMS.getProperty(), oldValue, editDistanceNgrams);
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
        Integer ngrams = (Integer) pSettings.getParameterValue(PRM.EDITDISTANCENGRAMS.getParameter());
        return ngrams==null ? 3 : ngrams;
    }
    /** Listener binder for edit distance ngrams property */
    @Override
    public void addEditDistanceNgramsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCENGRAMS.getProperty(), listener);
    }
    @Override
    public void setEditDistanceNgramsError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetEditDistanceNgramsError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for frequency threshold parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setFrequencyThreshold(Integer frequencyThreshold) {
        if ( (frequencyThreshold >= 0) && (frequencyThreshold <= 1.0) ) {
            Float oldValue = (Float) pSettings.getParameterValue(PRM.FREQUENCYTLD.getParameter());
            pSettings.setParameterValue(PRM.FREQUENCYTLD.getParameter(), frequencyThreshold);
            firePropertyChange(PRM.FREQUENCYTLD.getProperty(), oldValue, frequencyThreshold);
        } else {
            String msg = "Frequency threshold parameter value '" + frequencyThreshold
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for frequency threshold property */
    @Override
    public Integer getFrequencyThreshold() {
        Integer threshold = (Integer) pSettings.getParameterValue(PRM.FREQUENCYTLD.getParameter());
        return threshold==null ? (Integer) PRM.FREQUENCYTLD.getDefaultValue() : threshold;
    }
    /** Listener binder for frequency threshold property */
    @Override
    public void addFrequencyThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FREQUENCYTLD.getProperty(), listener);
    }
    @Override
    public void setFrequencyThresholdError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetFrequencyThresholdError() {
        // TODO how to handle errors in the model ?
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
            String oldValue = (String) pSettings.getParameterValue(PRM.ASSOCIATIONMEASURE.getParameter());
            pSettings.setParameterValue(PRM.ASSOCIATIONMEASURE.getParameter(), associationMeasure);
            firePropertyChange(PRM.ASSOCIATIONMEASURE.getProperty(), oldValue, associationMeasure);
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
        String className = (String) pSettings.getParameterValue(PRM.ASSOCIATIONMEASURE.getParameter());
        return className==null ? (String) PRM.ASSOCIATIONMEASURE.getDefaultValue() : className;
    }
    /** Listener binder for association measure property */
    @Override
    public void addAssociationMeasureChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.ASSOCIATIONMEASURE.getProperty(), listener);
    }
    @Override
    public void setAssociationMeasureError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetAssociationMeasureError() {
        // TODO how to handle errors in the model ?
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
            Float oldValue = (Float) pSettings.getParameterValue(PRM.FILTERINGTLD.getParameter());
            pSettings.setParameterValue(PRM.FILTERINGTLD.getParameter(), filteringThreshold);
            firePropertyChange(PRM.FILTERINGTLD.getProperty(), oldValue, filteringThreshold);
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
        Float threshold = (Float) pSettings.getParameterValue(PRM.FILTERINGTLD.getParameter());
        return threshold == null ? 0.0f : threshold;
    }
    /** Listener binder for filtering threshold property */
    @Override
    public void addFilteringThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FILTERINGTLD.getProperty(), listener);
    }
    @Override
    public void setFilteringThresholdError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetFilteringThresholdError() {
        // TODO how to handle errors in the model ?
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
            String oldValue = (String) pSettings.getParameterValue(PRM.FILTERRULE.getParameter());
            pSettings.setParameterValue(PRM.FILTERRULE.getParameter(), filterRule);
            firePropertyChange(PRM.FILTERRULE.getProperty(), oldValue, filterRule);
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
        String filter = (String) pSettings.getParameterValue(PRM.FILTERRULE.getParameter());
        return filter==null ? (String) PRM.FILTERRULE.getDefaultValue() : filter;
    }
    /** Listener binder for filter rule property */
    @Override
    public void addFilterRuleChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FILTERRULE.getProperty(), listener);
    }
    @Override
    public void setFilterRuleError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetFilterRuleError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for keep verbs parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setKeepVerbs(Boolean keepVerbs) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.KEEPVERBS.getParameter());
        pSettings.setParameterValue(PRM.KEEPVERBS.getParameter(), keepVerbs);
        firePropertyChange(PRM.KEEPVERBS.getProperty(), oldValue, keepVerbs);
    }
    /** Getter for keep verbs property */
    @Override
    public Boolean isKeepVerbs() {
        Boolean isKeepVerbs = (Boolean) pSettings.getParameterValue(PRM.KEEPVERBS.getParameter());
        return isKeepVerbs==null ? (Boolean) PRM.KEEPVERBS.getDefaultValue() : isKeepVerbs;
    }
    /** Listener binder for keep verbs property */
    @Override
    public void addKeepVerbsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.KEEPVERBS.getProperty(), listener);
    }
    @Override
    public void setKeepVerbsError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetKeepVerbsError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for TSV export parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setTSVExport(Boolean tsvExport) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.TSV.getParameter());
        pSettings.setParameterValue(PRM.TSV.getParameter(), tsvExport);
        firePropertyChange(PRM.TSV.getProperty(), oldValue, tsvExport);
    }
    /** Getter for TSV export property */
    @Override
    public Boolean isTSVExport() {
        Boolean isTSV = (Boolean) pSettings.getParameterValue(PRM.TSV.getParameter());
        return isTSV==null ? (Boolean) PRM.TSV.getDefaultValue() : isTSV;
    }
    /** Listener binder for TSV export property */
    @Override
    public void addTSVExportChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TSV.getProperty(), listener);
    }
    @Override
    public void setTSVExportError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetTSVExportError() {
        // TODO how to handle errors in the model ?
    }

}