package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.InvalidTermSuiteConfiguration;
import eu.project.ttc.tools.commons.ToolModel;
import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.*;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

import javax.management.InvalidAttributeValueException;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Model of the Spotter tool.
 *
 * The spotter is responsible for processing documents and extract term candidates from it.
 * This model maintains the values of the parameters used by the dedicated engine.
 */
public class SpotterModel extends ToolModel implements SpotterBinding {

    public static final String P_LANGUAGE = "Language";
    public static final String P_INPUT_DIRECTORY = "InputDirectory";
    public static final String P_OUTPUT_DIRECTORY = "OutputDirectory";
    public static final String P_TREETAGGER_HOME_DIRECTORY = "TreeTaggerHomeDirectory";

    /** Language configuration parameter */
    private ConfigurationParameter pLang;
    /** Input directory where the resources to be processed will be found */
    private ConfigurationParameter pIDir;
    /** Output directory where the results of processing will be stored */
    private ConfigurationParameter pODir;
    /** Home of TreeTagger */
    private ConfigurationParameter pTtg;

    // Where the parameter value are stored
    ConfigurationParameterSettings pSettings;

    /**
     * Constructor.
     *
     * @param spotterCfg
     *      configuration file where the model is persisted
     */
    public SpotterModel(File spotterCfg) {
        super(spotterCfg);
        declareParameters();
	}

    /**
     * Declare the parameters that are used by the Spotter tool.
     */
    private void declareParameters() {
        // Language
        pLang = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pLang.setName(P_LANGUAGE);
        pLang.setType(ConfigurationParameter.TYPE_STRING);
        pLang.setMultiValued(false);
        pLang.setMandatory(true);
        pLang.setDescription("values:en|fr|de|es|ru|da|lv|zh");

        // Input directory
        pIDir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pIDir.setName(P_INPUT_DIRECTORY);
        pIDir.setType(ConfigurationParameter.TYPE_STRING);
        pIDir.setMultiValued(false);
        pIDir.setMandatory(true);

        // Output directory
        pODir = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pODir.setName(P_OUTPUT_DIRECTORY);
        pODir.setType(ConfigurationParameter.TYPE_STRING);
        pODir.setMultiValued(false);
        pODir.setMandatory(true);

        // TreeTagger Home
        pTtg = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameter();
        pTtg.setName(P_TREETAGGER_HOME_DIRECTORY);
        pTtg.setType(ConfigurationParameter.TYPE_STRING);
        pTtg.setMultiValued(false);
        pTtg.setMandatory(true);

        // Bundle everything in a ConfigurationParameterSettings to add values
        pSettings = UIMAFramework.getResourceSpecifierFactory()
                        .createConfigurationParameterSettings();
    }

    /**
     * Set all properties to their default values.
     */
    @Override
    public void initDefault() throws InvalidTermSuiteConfiguration {
        try {
            setLanguage("fr");
            setInputDirectory("data/input/wind-energy/French/txt/");
            setOutputDirectory("data/output/wind-energy_FR");
            setTreetaggerHome("libs/TreeTagger");
        } catch (IllegalArgumentException e) {
            String msg = "Unable to use the default configuration.";
            UIMAFramework.getLogger().log(Level.WARNING, msg);
            throw new InvalidTermSuiteConfiguration(msg, e);
        }
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
        if (!persistedCfg.exists())
            return;

        // Load data from the persisted file as a UIMA metadata resource
        ResourceMetaData uimaMetadata;
        XMLInputSource input = new XMLInputSource(persistedCfg);
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
                if ( pLang.getName().equals(nvp.getName()) ) {
                    setLanguage((String) nvp.getValue());
                } else if ( pIDir.getName().equals(nvp.getName()) ) {
                    setInputDirectory((String) nvp.getValue());
                } else if ( pODir.getName().equals(nvp.getName()) ) {
                    setOutputDirectory((String) nvp.getValue());
                } else if ( pTtg.getName().equals(nvp.getName()) ) {
                    setTreetaggerHome((String) nvp.getValue());
                } else {
                    UIMAFramework.getLogger().log(Level.WARNING,
                            "Ignoring parameter {} as it is not supported by the model.",
                            new String[]{nvp.getName()});
                }
            } catch (IllegalArgumentException e) {
                String msg = "Unable to correctly load the configuration persisted in file '"
                        + persistedCfg.getAbsolutePath() + "' as it contains invalid values.";
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
        uimaParamDeclarations.addConfigurationParameter(pTtg);

        // Create and populate the metadata
        ResourceMetaData uimaMetadata = UIMAFramework
                .getResourceSpecifierFactory().createResourceMetaData();
        uimaMetadata.setConfigurationParameterDeclarations(uimaParamDeclarations);
        uimaMetadata.setConfigurationParameterSettings(pSettings);

        // Persist everything
        OutputStream out = new FileOutputStream(persistedCfg);
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
        uimaParamDeclarations.addConfigurationParameter(pTtg);

        // Create and populate the metadata
        ResourceMetaData uimaMetadata = UIMAFramework
                .getResourceSpecifierFactory().createResourceMetaData();
        uimaMetadata.setConfigurationParameterDeclarations(uimaParamDeclarations);
        uimaMetadata.setConfigurationParameterSettings(pSettings);

        uimaMetadata.validateConfigurationParameterSettings();
    }

    @Override
    public void runStarts() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void runEnds() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //////////////////////////////////////////////////////////////////// ACCESSORS

    /**
     * Getter for all the parameter settings.
     *
     * @param includeLanguage
     *      if set, then the Language parameter is included as well which will
     *      generate an error if you try to run the engine from here as the
     *      corresponding parameter does not exist
     */
    @Override
    public NameValuePair[] getParameterSettings(boolean includeLanguage) {
        if (includeLanguage) {
            return pSettings.getParameterSettings();
        } else {
            ArrayList<NameValuePair> paramSettings = new ArrayList<NameValuePair>();
            for(NameValuePair p: pSettings.getParameterSettings()) {
                if ( ! pLang.equals(p.getName()) ) {
                    paramSettings.add(p);
                }
            }
            return paramSettings.toArray(new NameValuePair[paramSettings.size()]);
        }
    }

    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_LANGUAGE, listener);
    }

    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_INPUT, listener);
    }

    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_OUTPUT, listener);
    }

    @Override
    public void addTtgDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_TTGHOME, listener);
    }

    /**
     * Setter for language parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    public void setLanguage(String language) {
        if ( language.matches("en|fr|de|es|ru|da|lv|zh") ) {
            String oldValue = (String) pSettings.getParameterValue(pLang.getName());
            pSettings.setParameterValue(pLang.getName(), language);
            firePropertyChange(EVT_LANGUAGE, oldValue, language);
        } else {
            String msg = "Language parameter value '" + language
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for language property */
    public String getLanguage() {
        return (String) pSettings.getParameterValue(pLang.getName());
    }

    /**
     * Setter for input directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    public void setInputDirectory(String inputDirectory) {
        File input = new File(inputDirectory);
        if ( input.exists() && input.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(pIDir.getName());
            pSettings.setParameterValue(pIDir.getName(), inputDirectory);
            firePropertyChange(EVT_INPUT, oldValue, inputDirectory);
        } else {
            String msg = "Input directory parameter value '" + inputDirectory
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for input directory property */
    public String getInputDirectory() {
        return (String) pSettings.getParameterValue(pIDir.getName());
    }

    /**
     * Setter for output directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    public void setOutputDirectory(String outputDirectory) {
        File output = new File(outputDirectory);
        if ( ! output.exists() )
            output.mkdirs(); // make sure output directory exists
        if ( output.exists() && output.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(pODir.getName());
            pSettings.setParameterValue(pODir.getName(), outputDirectory);
            firePropertyChange(EVT_OUTPUT, oldValue, outputDirectory);
        } else {
            String msg = "Output directory parameter value '" + outputDirectory
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for output directory property */
    public String getOutputDirectory() {
        return (String) pSettings.getParameterValue(pODir.getName());
    }

    /**
     * Setter for treetagger directory parameter value.
     * If the value is valid, then the parameter value is changed in the
     * model and an event is fired indicating that the property has
     * changed in the model.
     */
    public void setTreetaggerHome(String treetaggerHome) {
        File output = new File(treetaggerHome);
        if ( output.exists() && output.isDirectory() ) {
            String oldValue = (String) pSettings.getParameterValue(pTtg.getName());
            pSettings.setParameterValue(pTtg.getName(), treetaggerHome);
            firePropertyChange(EVT_TTGHOME, oldValue, treetaggerHome);
        } else {
            String msg = "TreeTagger home parameter value '" + treetaggerHome
                    + "' is invalid. No change reflected in model.";
            UIMAFramework.getLogger().log(Level.SEVERE, msg);
            throw new IllegalArgumentException(msg);
        }
    }
    /** Getter for tree tagger home property. */
    public String getTreetaggerHome() {
        return (String) pSettings.getParameterValue(pTtg.getName());
    }

}
