package eu.project.ttc.tools.spotter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.commons.ToolModel;
import eu.project.ttc.tools.commons.ToolController;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import org.apache.commons.lang3.text.WordUtils;

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
     * Create a SpotterController that is connected to a view and a model.
     * Double binds the view and the model so that a change in the view is
     * reflected to the model, and a change in the model is reflected in
     * the view.
     */
    public SpotterController(SpotterModel model, SpotterView view) {
        super(model, view);
        bindViewToModel();
        bindModelToView();
    }

    /**
     * Bind view listeners to model changes so that any change to the view
     * is reflected to the model.
     */
    private void bindViewToModel() {
        // Language
        getView().addLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getLanguage() == null) ||
                        !getModel().getLanguage().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Reflecting language change from view->" + evt.getNewValue());
                        getModel().setLanguage((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setLanguageError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetLanguageError();
            }
        });
        // Input directory
        getView().addInputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ( (getModel().getInputDirectory()==null) ||
                        ! getModel().getInputDirectory().equals(evt.getNewValue()) ) {
                    try {
                        System.out.println("Reflecting input directory change from view->" + evt.getNewValue());
                        getModel().setInputDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setInputDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if ( success ) getView().unsetInputDirectoryError();
            }
        });
        // Output directory
        getView().addOutputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ( (getModel().getOutputDirectory()==null) ||
                        ! getModel().getOutputDirectory().equals(evt.getNewValue()) ) {
                    try {
                        System.out.println("Reflecting output directory change from view->" + evt.getNewValue());
                        getModel().setOutputDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setOutputDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if ( success ) getView().unsetOutputDirectoryError();
            }
        });
        // TreeTagger directory
        getView().addTtgDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getTreetaggerHome() == null) ||
                        !getModel().getTreetaggerHome().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Reflecting treetagger directory change from view->" + evt.getNewValue());
                        getModel().setTreetaggerHome((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setTreetaggerHomeError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetTreetaggerHomeError();
            }
        });
    }

    /**
     * Bind model listeners to view changes so that any change to the model
     * is reflected to the view.
     */
    private void bindModelToView() {
        // Language
        getModel().addLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getLanguage()==null) ||
                        ! getView().getLanguage().equals(evt.getNewValue()) ) {
                    System.out.println("Reflecting language change from model->" + evt.getNewValue());
                    getView().setLanguage((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Input directory
        getModel().addInputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getInputDirectory()==null) ||
                        ! getView().getInputDirectory().equals(evt.getNewValue()) ) {
                    System.out.println("Reflecting input directory change from model->" + evt.getNewValue());
                    getView().setInputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Output directory
        getModel().addOutputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getOutputDirectory()==null) ||
                        ! getView().getOutputDirectory().equals(evt.getNewValue()) ) {
                    System.out.println("Reflecting output directory change from model->" + evt.getNewValue());
                    getView().setOutputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // TreeTagger directory
        getModel().addTtgDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getTreetaggerHome()==null) ||
                        ! getView().getTreetaggerHome().equals(evt.getNewValue()) ) {
                    System.out.println("Reflecting ttg directory change from model->" + evt.getNewValue());
                    getView().setTreetaggerHome((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
    }

    /** Getter to the model with appropriate casting */
    protected SpotterModel getModel() {
        return (SpotterModel) registeredModel;
    }

    /** Getter to the view with appropriate casting */
    protected SpotterView getView() {
        return (SpotterView) registeredView;
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#getInputSource()
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
     * @see eu.project.ttc.tools.commons.ToolController#getLanguage()
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
     * @see eu.project.ttc.tools.commons.ToolController#getAESettings()
     */
    @Override
    public ConfigurationParameterSettings getAESettings() {
        // Prepare an empty ConfigurationParameterSetting
        ConfigurationParameterSettings settings = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameterSettings();
        // Only set parameters needed by the Spotter AE
        settings.setParameterValue("Directory", getModel().getOutputDirectory());
        settings.setParameterValue("TreeTaggerHomeDirectory", getModel().getTreetaggerHome());
//
//        // Populate the settings with data from the model
//        settings.setParameterSettings(getModel().getParameterSettings(false));
//        // TODO why ?
//        settings.setParameterValue("Directory", getModel().getOutputDirectory());

        return settings;
    }

    /**
     * @return  the name of the AE descriptor file
     */
    @Override
    public String getAEDescriptor() {
        if ( getModel().getLanguage() != null) {
            String code = getModel().getLanguage();
            String language = new Locale(code)
                    .getDisplayLanguage(Locale.ENGLISH).toLowerCase();
//            return String.format("eu/project/ttc/%s/engines/spotter/%sSpotter.xml",
//                    language.toLowerCase(), WordUtils.capitalizeFully(language));
            return String.format("eu.project.ttc.%s.engines.spotter.%sSpotter",
                    language.toLowerCase(), WordUtils.capitalizeFully(language));
        } else {
            throw new IllegalStateException("Unable to generate descriptor name for Spotter as no " +
                    "language have been specified in the model.");
        }
    }

    @Override
    public InputSource.InputSourceTypes getInputSourceType() {
        return InputSource.InputSourceTypes.TXT;
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