package eu.project.ttc.tools.spotter;

import java.io.IOException;
import java.util.Locale;

import eu.project.ttc.tools.InputSource;
import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.ToolModel;
import eu.project.ttc.tools.ToolController;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

/**
 * Controller of the Spotter tool.
 *
 * The spotter tool is responsible for processing documents and extract
 * term candidates from it.
 *
 * The controller conciliates the model and the view as well as providing
 * higher level features as to build analysis engine description and
 * parameters settings.
 */
public class SpotterController extends ToolController {


    /**
     * Constructor.
     * Create a new empty SpotterController that is simply connected to
     * a model and does not have any view yet.
     */
    public SpotterController(SpotterModel model, SpotterView view) {
        super(model, view);

        // Bind model and view
//        view.addLanguageListener(this);
//        view.addInputDirectoryListener(this);
//        view.addOutputDirectoryListener(this);
//        view.addTtgDirectoryListener(this);
    }

    /** Getter to the model with appropriate casting */
    protected SpotterModel getModel() {
        return (SpotterModel) registeredModel;
    }

    /**
     * @see eu.project.ttc.tools.ToolController#getInputSource()
     *
     * For the spotter tool, the data files to processed are text files
     * and they are located in the input directory specified as parameter.
     */
    @Override
    public InputSource getInputSource() {
        return new InputSource(getModel().getInputDirectory(), InputSource.InputSourceTypes.TXT);
    }

    /**
     * @see ToolController#processingCallback(org.apache.uima.cas.CAS)
     */
    @Override
    public void processingCallback(CAS cas) throws Exception {
        // FIXME remove dependency if possible
        ProcessingResult result = new ProcessingResult();
        result.setCas(cas);
        // FIXME this.getTool().getParent().getViewer().getResultModel().addElement(result);
    }

    /**
     * @see eu.project.ttc.tools.ToolController#getLanguage()
     */
    @Override
    public String getLanguage() {
        return getModel().getLanguage();
    }

    /**
     * Build a ConfigurationParameterSettings used to configure an instance of
     * the corresponding AE.
     * The returned ConfigurationParameterSettings is configured using the data
     * in the model.
     *
     * @see eu.project.ttc.tools.ToolController#getAESettings()
     */
    @Override
    public ConfigurationParameterSettings getAESettings() {
        // Prepare an empty ConfigurationParameterSetting
        ConfigurationParameterSettings settings = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameterSettings();

        // Populate the settings with data from the model
        settings.setParameterSettings( getModel().getParameterSettings() );
        // TODO why ?
        settings.setParameterValue("Directory", getModel().getOutputDirectory());

        return settings;
    }

    /**
     * @see eu.project.ttc.tools.ToolController#getAEDescription()
     */
    @Override
    public AnalysisEngineDescription getAEDescription() {
        // Compute the name of the AE descriptor file
        String descPath = null;
        if ( getModel().getLanguage() != null) {
            String code = getModel().getLanguage();
            String language = new Locale(code)
                    .getDisplayLanguage(Locale.ENGLISH).toLowerCase();
            descPath = String.format("eu/project/ttc/%1/engines/spotter/%1SpotterController.xml",
                    language.toLowerCase(), language);
        }
        if (descPath == null) {
            // FIXME throw exception
        }

        // Build descriptor
        try {
            XMLInputSource in = new XMLInputSource(descPath);
            ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
            AnalysisEngineDescription descAE = (AnalysisEngineDescription) specifier;
            descAE.getAnalysisEngineMetaData().setConfigurationParameterSettings( getAESettings() );
            return descAE;
        } catch (IOException e) {
            RuntimeException e2 = new RuntimeException("Unable to access the Analysis Engine Descriptor: " + descPath);
            e2.initCause(e);
            throw e2;
        } catch (InvalidXMLException e) {
            RuntimeException e2 = new RuntimeException("Unable to parse the Analysis Engine Descriptor: " + descPath);
            e2.initCause(e);
            throw e2;
        }
    }


    // FIXME remove all these

    @Override
    public void setParent(TermSuite parent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TermSuite getParent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ToolModel getSettings() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


//    /** Parent container = the term suite application */
//    private TermSuite mainApp;
//
//    /** Model */
//    private SpotterModel model;
//    /** View */
//    private SpotterView view;


	
//	public TermSuite getMainApp() {
//		return this.mainApp;
//	}

    public void doUpdate() {
//        ConfigurationParameterSettings settings = this.getMetaData()
//                .getConfigurationParameterSettings();
//        List<Field> fields = this.getViewer().getFields();
//        for (Field field : fields) {
//            if (field.isModified()) {
//                String name = field.getName();
//                Object value = field.getValue();
//                settings.setParameterValue(name, value);
//            }
//        }
    }
		
}
