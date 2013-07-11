package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.ToolModel;
import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.metadata.*;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Model of the Spotter tool.
 *
 * The spotter is responsible for processing documents and extract term candidates from it.
 * This model maintains the values of the parameters used by the dedicated engine.
 */
public class SpotterModel extends ToolModel {

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
    public void initDefault() throws FileNotFoundException {
        setLanguage("fr");
        setInputDirectory("data/input/wind-energy/French/txt/");
        setOutputDirectory("data/output/wind-energy_FR");
        setTreetaggerHome("libs/TreeTagger");
    }

    /**
     * Load persisted parameters values from the configuration file.
     * We use UIMA metadata resource format to persist the configuration.
     *
     * @see eu.project.ttc.tools.ToolModel#load()
     */
    @Override
    public void load() throws IOException {
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
        }
    }

    /**
     * Persist the model as it is to a configuration file.
     * We use UIMA metadata resource format to persist the configuration.
     *
     * @see eu.project.ttc.tools.ToolModel#save()
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

    /** Getter for all the parameter settings. */
    @Override
    public NameValuePair[] getParameterSettings() {
        return pSettings.getParameterSettings();
    }

    /** Setter for language property. */
    public void setLanguage(String language) {
        pSettings.setParameterValue(pLang.getName(), language);
    }
    /** Getter for language property */
    public String getLanguage() {
        return (String) pSettings.getParameterValue(pLang.getName());
    }

    /** Setter for input directory property. */
    public void setInputDirectory(String inputDirectory) {
        pSettings.setParameterValue(pIDir.getName(), inputDirectory);
    }
    /** Getter for input directory property */
    public String getInputDirectory() {
        return (String) pSettings.getParameterValue(pIDir.getName());
    }

    /** Setter for output directory property. */
    public void setOutputDirectory(String outputDirectory) {
        pSettings.setParameterValue(pODir.getName(), outputDirectory);
    }
    /** Getter for output directory property */
    public String getOutputDirectory() {
        return (String) pSettings.getParameterValue(pODir.getName());
    }

    /** Setter for tree tagger home property. */
    public void setTreetaggerHome(String treetaggerHome) throws FileNotFoundException {
        File treeTaggerHomeDirectory = new File(treetaggerHome);
        if (treeTaggerHomeDirectory.exists()) {
            pSettings.setParameterValue(pTtg.getName(), treetaggerHome);
        } else {
            throw new FileNotFoundException("Unable to find TreeTagger home directory : " + treetaggerHome);
        }
    }
    /** Getter for tree tagger home property. */
    public String getTreetaggerHome() {
        return (String) pSettings.getParameterValue(pTtg.getName());
    }


    // FIXME get rid of these
//
//    @Override
//    public void doUpdate() {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void validate() throws ResourceConfigurationException {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void doSave() throws IOException, SAXException {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public ResourceMetaData getMetaData() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

}
