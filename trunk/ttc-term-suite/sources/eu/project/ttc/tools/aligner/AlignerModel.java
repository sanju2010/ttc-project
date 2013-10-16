package eu.project.ttc.tools.aligner;

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
 * Model of the Aligner tool.
 *
 * This model maintains the values of the parameters used by the dedicated engine.
 */
public class AlignerModel extends ToolModel implements AlignerBinding {

    /** Source language parameter */
    private ConfigurationParameter pSrcLang;
    /** Target language parameter */
    private ConfigurationParameter pTgtLang;
    /** Output directory parameter */
    private ConfigurationParameter pOutput;
    /** Source terminology XMI file parameter */
    private ConfigurationParameter pSrcTermino;
    /** Target terminology XMI file parameter */
    private ConfigurationParameter pTgtTermino;
    /** Bilingual dictionary parameter */
    private ConfigurationParameter pDico;
    /** Evaluation directory parameter */
    private ConfigurationParameter pEvalDir;
    /** Compositional method parameter */
    private ConfigurationParameter pCompoMthd;
    /** Distributional method */
    private ConfigurationParameter pDistrMthd;
    /** Semidistributional method */
    private ConfigurationParameter pSemidistrMthd;
    /** Similarity distance parameter */
    private ConfigurationParameter pSim;
    /** Max candidates parameter */
    private ConfigurationParameter pMxCdt;

    // Where the parameter value are stored
    ConfigurationParameterSettings pSettings;

    /**
     * Constructor.
     *
     * @param alignerCfg
     *      configuration file where the model is persisted
     */
    public AlignerModel(File alignerCfg) {
        super(alignerCfg);
        declareParameters();
    }

    /**
     * Declare the parameters that are used by the Indexer tool.
     */
    private void declareParameters() {
        // Source language parameter
        pSrcLang = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pSrcLang.setName(PRM.SRCLANGUAGE.getParameter());
        pSrcLang.setType(ConfigurationParameter.TYPE_STRING);
        pSrcLang.setMultiValued(false);
        pSrcLang.setMandatory(true);
        pSrcLang.setDescription("values:en|fr|de|es|ru|da|lv|zh");

        // Target language parameter
        pTgtLang = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pTgtLang.setName(PRM.TGTLANGUAGE.getParameter());
        pTgtLang.setType(ConfigurationParameter.TYPE_STRING);
        pTgtLang.setMultiValued(false);
        pTgtLang.setMandatory(true);
        pTgtLang.setDescription("values:en|fr|de|es|ru|da|lv|zh");

        // Output directory parameter
        pOutput = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pOutput.setName(PRM.OUTPUT.getParameter());
        pOutput.setType(ConfigurationParameter.TYPE_STRING);
        pOutput.setMultiValued(false);
        pOutput.setMandatory(true);

        // Source terminology XMI file parameter
        pSrcTermino = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pSrcTermino.setName(PRM.SRCTERMINOLOGY.getParameter());
        pSrcTermino.setType(ConfigurationParameter.TYPE_STRING);
        pSrcTermino.setMultiValued(false);
        pSrcTermino.setMandatory(true);

        // Target terminology XMI file parameter
        pTgtTermino = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pTgtTermino.setName(PRM.TGTTERMINOLOGY.getParameter());
        pTgtTermino.setType(ConfigurationParameter.TYPE_STRING);
        pTgtTermino.setMultiValued(false);
        pTgtTermino.setMandatory(true);

        // Bilingual dictionary parameter
        pDico = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pDico.setName(PRM.DICTIONARY.getParameter());
        pDico.setType(ConfigurationParameter.TYPE_STRING);
        pDico.setMultiValued(false);
        pDico.setMandatory(false);

        // Evaluation directory parameter
        pEvalDir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pEvalDir.setName(PRM.EVALDIR.getParameter());
        pEvalDir.setType(ConfigurationParameter.TYPE_STRING);
        pEvalDir.setMultiValued(false);
        pEvalDir.setMandatory(true);

        // Compositional method parameter
        pCompoMthd = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pCompoMthd.setName(PRM.COMPOSITIONAL.getParameter());
        pCompoMthd.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pCompoMthd.setMultiValued(false);
        pCompoMthd.setMandatory(false);

        // Distributional method
        pDistrMthd = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pDistrMthd.setName(PRM.DISTRIBUTIONAL.getParameter());
        pDistrMthd.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pDistrMthd.setMultiValued(false);
        pDistrMthd.setMandatory(false);

        // Distributional method
        pSemidistrMthd = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pSemidistrMthd.setName(PRM.SEMIDISTRIBUTIONAL.getParameter());
        pSemidistrMthd.setType(ConfigurationParameter.TYPE_BOOLEAN);
        pSemidistrMthd.setMultiValued(false);
        pSemidistrMthd.setMandatory(false);
        
        // Similarity distance parameter
        pSim = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pSim.setName(PRM.SIMILARITY.getParameter());
        pSim.setType(ConfigurationParameter.TYPE_STRING);
        pSim.setMultiValued(false);
        pSim.setMandatory(false);
        pSim.setDescription("values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosine");

        // Max candidates parameter
        pMxCdt = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pMxCdt.setName(PRM.MAXCANDIDATES.getParameter());
        pMxCdt.setType(ConfigurationParameter.TYPE_INTEGER);
        pMxCdt.setMultiValued(false);
        pMxCdt.setMandatory(false);

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
                switch ( AlignerBinding.PRM.fromParameter(nvp.getName()) ) {
                    case SRCLANGUAGE:
                        setSourceLanguage((String) nvp.getValue());
                        break;
                    case TGTLANGUAGE:
                        setTargetLanguage((String) nvp.getValue());
                        break;
                    case OUTPUT:
                        setOutputDirectory((String) nvp.getValue());
                        break;
                    case SRCTERMINOLOGY:
                        setSourceTerminology((String) nvp.getValue());
                        break;
                    case TGTTERMINOLOGY:
                        setTargetTerminology((String) nvp.getValue());
                        break;
                    case DICTIONARY:
                        setBilingualDictionary((String) nvp.getValue());
                        break;
                    case EVALDIR:
                        setEvaluationDirectory((String) nvp.getValue());
                        break;
                    case COMPOSITIONAL:
                        setCompositionalMethod((Boolean) nvp.getValue());
                        break;
                    case DISTRIBUTIONAL:
                        setDistributionalMethod((Boolean) nvp.getValue());
                        break;
                    case SEMIDISTRIBUTIONAL:
                        setSemidistributionalMethod((Boolean) nvp.getValue());
                        break;
                    case SIMILARITY:
                        setSimilarityDistanceClass((String) nvp.getValue());
                        break;
                    case MAXCANDIDATES:
                        setMaxCandidates((Integer) nvp.getValue());
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
        uimaParamDeclarations.addConfigurationParameter(pSrcLang);
        uimaParamDeclarations.addConfigurationParameter(pTgtLang);
        uimaParamDeclarations.addConfigurationParameter(pOutput);
        uimaParamDeclarations.addConfigurationParameter(pSrcTermino);
        uimaParamDeclarations.addConfigurationParameter(pTgtTermino);
        uimaParamDeclarations.addConfigurationParameter(pDico);
        uimaParamDeclarations.addConfigurationParameter(pEvalDir);
        uimaParamDeclarations.addConfigurationParameter(pCompoMthd);
        uimaParamDeclarations.addConfigurationParameter(pDistrMthd);
        uimaParamDeclarations.addConfigurationParameter(pSemidistrMthd);
        uimaParamDeclarations.addConfigurationParameter(pSim);
        uimaParamDeclarations.addConfigurationParameter(pMxCdt);

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
        uimaParamDeclarations.addConfigurationParameter(pSrcLang);
        uimaParamDeclarations.addConfigurationParameter(pTgtLang);
        uimaParamDeclarations.addConfigurationParameter(pOutput);
        uimaParamDeclarations.addConfigurationParameter(pSrcTermino);
        uimaParamDeclarations.addConfigurationParameter(pTgtTermino);
        uimaParamDeclarations.addConfigurationParameter(pDico);
        uimaParamDeclarations.addConfigurationParameter(pEvalDir);
        uimaParamDeclarations.addConfigurationParameter(pCompoMthd);
        uimaParamDeclarations.addConfigurationParameter(pDistrMthd);
        uimaParamDeclarations.addConfigurationParameter(pSemidistrMthd);
        uimaParamDeclarations.addConfigurationParameter(pSim);
        uimaParamDeclarations.addConfigurationParameter(pMxCdt);

        // Create and populate the metadata
        ResourceMetaData uimaMetadata = UIMAFramework
                .getResourceSpecifierFactory().createResourceMetaData();
        uimaMetadata.setConfigurationParameterDeclarations(uimaParamDeclarations);
        uimaMetadata.setConfigurationParameterSettings(pSettings);

        uimaMetadata.validateConfigurationParameterSettings();
    }

    //////////////////////////////////////////////////////////////////// ACCESSORS

    /**
     * Setter for source language parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setSourceLanguage(String language) {
        if ( language.matches("en|fr|de|es|ru|da|lv|zh") ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.SRCLANGUAGE.getParameter());
            pSettings.setParameterValue(PRM.SRCLANGUAGE.getParameter(), language);
            firePropertyChange(PRM.SRCLANGUAGE.getProperty(), oldValue, language);
        } else {
            String msg = "Source language parameter value '" + language + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for language property */
    @Override
    public String getSourceLanguage() {
        return (String) pSettings.getParameterValue(PRM.SRCLANGUAGE.getParameter());
    }
    /** Listener binder for language property */
    @Override
    public void addSourceLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SRCLANGUAGE.getProperty(), listener);
    }
    @Override
    public void setSourceLanguageError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetSourceLanguageError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for target language parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setTargetLanguage(String language) {
        if ( language.matches("en|fr|de|es|ru|da|lv|zh") ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.TGTLANGUAGE.getParameter());
            pSettings.setParameterValue(PRM.TGTLANGUAGE.getParameter(), language);
            firePropertyChange(PRM.TGTLANGUAGE.getProperty(), oldValue, language);
        } else {
            String msg = "Target language parameter value '" + language + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for language property */
    @Override
    public String getTargetLanguage() {
        return (String) pSettings.getParameterValue(PRM.TGTLANGUAGE.getParameter());
    }
    /** Listener binder for language property */
    @Override
    public void addTargetLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TGTLANGUAGE.getProperty(), listener);
    }
    @Override
    public void setTargetLanguageError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetTargetLanguageError() {
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
            final boolean mkdirs = output.mkdirs();// make sure output directory exists
        }
        if ( output.exists() && output.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.OUTPUT.getParameter());
            pSettings.setParameterValue(PRM.OUTPUT.getParameter(), outputDirectory);
            firePropertyChange(PRM.OUTPUT.getProperty(), oldValue, outputDirectory);
        } else {
            String msg = "Output directory parameter value '" + outputDirectory + "' is invalid.";
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
     * Setter for source terminology parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setSourceTerminology(String sourceTerminology) {
        File output = new File(sourceTerminology);
        if ( output.exists() && output.isFile() ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.SRCTERMINOLOGY.getParameter());
            pSettings.setParameterValue(PRM.SRCTERMINOLOGY.getParameter(), sourceTerminology);
            firePropertyChange(PRM.SRCTERMINOLOGY.getProperty(), oldValue, sourceTerminology);
        } else {
            String msg = "Source terminology parameter value '" + sourceTerminology + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for source terminology parameter */
    @Override
    public String getSourceTerminology() {
        return (String) pSettings.getParameterValue(PRM.SRCTERMINOLOGY.getParameter());
    }
    /** Listener binder for output directory property */
    @Override
    public void addSourceTerminologyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SRCTERMINOLOGY.getProperty(), listener);
    }
    @Override
    public void setSourceTerminologyError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetSourceTerminologyError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for target terminology parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setTargetTerminology(String targetTerminology) {
        File output = new File(targetTerminology);
        if ( output.exists() && output.isFile() ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.TGTTERMINOLOGY.getParameter());
            pSettings.setParameterValue(PRM.TGTTERMINOLOGY.getParameter(), targetTerminology);
            firePropertyChange(PRM.TGTTERMINOLOGY.getProperty(), oldValue, targetTerminology);
        } else {
            String msg = "Target terminology parameter value '" + targetTerminology + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for target terminology parameter */
    @Override
    public String getTargetTerminology() {
        return (String) pSettings.getParameterValue(PRM.TGTTERMINOLOGY.getParameter());
    }
    /** Listener binder for output directory property */
    @Override
    public void addTargetTerminologyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TGTTERMINOLOGY.getProperty(), listener);
    }
    @Override
    public void setTargetTerminologyError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetTargetTerminologyError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for bilingual dictionary parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setBilingualDictionary(String bilingualDictionary) {
        String oldValue = (String) pSettings.getParameterValue(PRM.DICTIONARY.getParameter());

        if ( bilingualDictionary == null ) {
            // Restore to default value
            pSettings.setParameterValue(PRM.DICTIONARY.getParameter(), null);
            firePropertyChange(PRM.DICTIONARY.getProperty(), oldValue, null);
        } else {
            File output = new File(bilingualDictionary);
            if ( output.exists() && output.isFile() ) {
                pSettings.setParameterValue(PRM.DICTIONARY.getParameter(), bilingualDictionary);
                firePropertyChange(PRM.DICTIONARY.getProperty(), oldValue, bilingualDictionary);
            } else {
                String msg = "Bilingual dictionary parameter value '" + bilingualDictionary+ "' is invalid.";
                UIMAFramework.getLogger().log(Level.SEVERE, msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }
    /** Getter for bilingual dictionary parameter */
    @Override
    public String getBilingualDictionary() {
        return (String) pSettings.getParameterValue(PRM.DICTIONARY.getParameter());
    }
    /** Listener binder for bilingual dictionary parameter */
    @Override
    public void addBilingualDictionaryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.DICTIONARY.getProperty(), listener);
    }
    @Override
    public void setBilingualDictionaryError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetBilingualDictionaryError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for evaluation directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setEvaluationDirectory(String evaluationDirectory) {
        File output = new File(evaluationDirectory);
        if ( ! output.exists() ) {
            final boolean mkdirs = output.mkdirs();// make sure output directory exists
        }
        if ( output.exists() && output.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.EVALDIR.getParameter());
            pSettings.setParameterValue(PRM.EVALDIR.getParameter(), evaluationDirectory);
            firePropertyChange(PRM.EVALDIR.getProperty(), oldValue, evaluationDirectory);
        } else {
            String msg = "Evaluation directory parameter value '" + evaluationDirectory + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for evaluation directory property */
    @Override
    public String getEvaluationDirectory() {
        return (String) pSettings.getParameterValue(PRM.EVALDIR.getParameter());
    }
    /** Listener binder for evaluation directory property */
    @Override
    public void addEvaluationDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EVALDIR.getProperty(), listener);
    }
    @Override
    public void setEvaluationDirectoryError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetEvaluationDirectoryError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for compositional method parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setCompositionalMethod(boolean compositionalMethod) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.COMPOSITIONAL.getParameter());
        pSettings.setParameterValue(PRM.COMPOSITIONAL.getParameter(), compositionalMethod);
        firePropertyChange(PRM.COMPOSITIONAL.getProperty(), oldValue, compositionalMethod);
    }
    /** Getter for compositional method parameter */
    @Override
    public Boolean isCompositionalMethod() {
        Boolean isCompo = (Boolean) pSettings.getParameterValue(PRM.COMPOSITIONAL.getParameter());
        return isCompo==null ? (Boolean) PRM.COMPOSITIONAL.getDefaultValue() : isCompo;
    }
    /** Listener binder for compositional method parameter */
    @Override
    public void addCompositionalMethodChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.COMPOSITIONAL.getProperty(), listener);
    }
    @Override
    public void setCompositionalMethodError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetCompositionalMethodError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for distributional method parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setDistributionalMethod(boolean distributionalMethod) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.DISTRIBUTIONAL.getParameter());
        pSettings.setParameterValue(PRM.DISTRIBUTIONAL.getParameter(), distributionalMethod);
        firePropertyChange(PRM.DISTRIBUTIONAL.getProperty(), oldValue, distributionalMethod);
    }
    /** Getter for compositional method parameter */
    @Override
    public Boolean isDistributionalMethod() {
        Boolean isDistrib = (Boolean) pSettings.getParameterValue(PRM.DISTRIBUTIONAL.getParameter());
        return isDistrib==null ? (Boolean) PRM.DISTRIBUTIONAL.getDefaultValue() : isDistrib;
    }
    /** Listener binder for compositional method parameter */
    @Override
    public void addDistributionalMethodChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.DISTRIBUTIONAL.getProperty(), listener);
    }
    @Override
    public void setDistributionalMethodError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetDistributionalMethodError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for semidistributional method parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setSemidistributionalMethod(boolean semidistributionalMethod) {
        Boolean oldValue = (Boolean) pSettings.getParameterValue(PRM.SEMIDISTRIBUTIONAL.getParameter());
        pSettings.setParameterValue(PRM.SEMIDISTRIBUTIONAL.getParameter(), semidistributionalMethod);
        firePropertyChange(PRM.SEMIDISTRIBUTIONAL.getProperty(), oldValue, semidistributionalMethod);
    }
    /** Getter for compositional method parameter */
    @Override
    public Boolean isSemidistributionalMethod() {
        Boolean isDistrib = (Boolean) pSettings.getParameterValue(PRM.SEMIDISTRIBUTIONAL.getParameter());
        return isDistrib==null ? (Boolean) PRM.SEMIDISTRIBUTIONAL.getDefaultValue() : isDistrib;
    }
    /** Listener binder for compositional method parameter */
    @Override
    public void addSemidistributionalMethodChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SEMIDISTRIBUTIONAL.getProperty(), listener);
    }
    @Override
    public void setSemidistributionalMethodError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetSemidistributionalMethodError() {
        // TODO how to handle errors in the model ?
    }
    
    /**
     * Setter for similarity distance parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setSimilarityDistanceClass(String similarityClass) {
        if ( similarityClass.matches("eu\\.project\\.ttc\\.metrics\\.Jaccard" +
                "|eu\\.project\\.ttc\\.metrics\\.Cosine") ) {
            String oldValue = (String) pSettings.getParameterValue(PRM.SIMILARITY.getParameter());
            pSettings.setParameterValue(PRM.SIMILARITY.getParameter(), similarityClass);
            firePropertyChange(PRM.SIMILARITY.getProperty(), oldValue, similarityClass);
        } else {
            String msg = "Similarity distance parameter value '" + similarityClass + "' is invalid.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for similarity distance parameter */
    @Override
    public String getSimilarityDistanceClass() {
        String className = (String) pSettings.getParameterValue(PRM.SIMILARITY.getParameter());
        return className==null ? (String) PRM.SIMILARITY.getDefaultValue() : className;
    }
    /** Listener binder for similarity distance parameter */
    @Override
    public void addSimilarityDistanceClassChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SIMILARITY.getProperty(), listener);
    }
    @Override
    public void setSimilarityDistanceClassError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetSimilarityDistanceClassError() {
        // TODO how to handle errors in the model ?
    }

    /**
     * Setter for max candidates parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    @Override
    public void setMaxCandidates(Integer maxCandidates) {
        Integer oldValue = (Integer) pSettings.getParameterValue(PRM.MAXCANDIDATES.getParameter());
        pSettings.setParameterValue(PRM.MAXCANDIDATES.getParameter(), maxCandidates);
        firePropertyChange(PRM.MAXCANDIDATES.getProperty(), oldValue, maxCandidates);
    }
    /** Getter for max candidates parameter */
    @Override
    public Integer getMaxCandidates() {
        Integer maxCandidates = (Integer) pSettings.getParameterValue(PRM.MAXCANDIDATES.getParameter());
        return maxCandidates==null ? (Integer) PRM.MAXCANDIDATES.getDefaultValue() : maxCandidates;
    }
    /** Listener binder for max candidates parameter */
    @Override
    public void addMaxCandidatesChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.MAXCANDIDATES.getProperty(), listener);
    }
    @Override
    public void setMaxCandidatesError(Throwable e) {
        // TODO how to handle errors in the model ?
    }
    @Override
    public void unsetMaxCandidatesError() {
        // TODO how to handle errors in the model ?
    }

}
