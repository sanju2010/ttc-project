package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.commons.ToolController;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

/**
 * Controller of the Indexer tool.
 *
 * The indexer tool is responsible for gathering term candidates spotted
 * by the Spotter and combine similar terms together.
 *
 * The controller conciliates the model and the view as well as providing
 * higher level features as to build analysis engine description and
 * parameters settings.
 */
public class IndexerController extends ToolController {

    /**
     * Constructor.
     * Create a SpotterController that is connected to a view and a model.
     * Double binds the view and the model so that a change in the view is
     * reflected to the model, and a change in the model is reflected in
     * the view.
     */
    public IndexerController(IndexerModel model, IndexerView view) {
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
                if ((getModel().getOutputDirectory() == null) ||
                        !getModel().getOutputDirectory().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Reflecting output directory change from view->" + evt.getNewValue());
                        getModel().setOutputDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setOutputDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetOutputDirectoryError();
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
                if ((getView().getOutputDirectory() == null) ||
                        !getView().getOutputDirectory().equals(evt.getNewValue())) {
                    System.out.println("Reflecting output directory change from model->" + evt.getNewValue());
                    getView().setOutputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
    }

    /** Getter to the model with appropriate casting */
    protected IndexerModel getModel() {
        return (IndexerModel) getToolModel();
    }

    /** Getter to the view with appropriate casting */
    protected IndexerView getView() {
        return (IndexerView) getToolView();
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#getInputSource()
     *
     * For the spotter tool, the data files to processed are text files
     * and they are located in the input directory specified as parameter.
     */
    @Override
    public InputSource getInputSource() {
        return new InputSource(getModel().getInputDirectory(), InputSource.InputSourceTypes.XMI);
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#processingCallback(org.apache.uima.cas.CAS)
     */
    @Override
    public void processingCallback(CAS cas) throws Exception {
        // FIXME
        // this.getTool().getMainApp().getBanker().doLoad(cas.getJCas());
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
        // TODO
//
//		ConfigurationParameterSettings parameters = indexer.getSettings().getMetaData().getConfigurationParameterSettings();
//		//ConfigurationParameterSettings advancedParameters = indexer.getSettings().getMetaData().getConfigurationParameterSettings();
//		String code = (String) parameters.getParameterValue("Language");
//
//
//		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
//        settings.setParameterValue("Language", code);
//        settings.setParameterValue("Directory", parameters.getParameterValue("OutputDirectory"));
//		settings.setParameterValue("Action", "drop");
//
//
////		// TBX settings
////		ConfigurationParameterSettings tbxParameters = indexer.getTBXSettings().getMetaData().getConfigurationParameterSettings();
//		settings.setParameterValue(IndexerModel.P_KEEP_VERBS,
//                Boolean.TRUE.equals(parameters.getParameterValue(IndexerModel.P_KEEP_VERBS)));
//		settings.setParameterValue(IndexerModel.P_FILTER_RULE,
//                parameters.getParameterValue(IndexerModel.P_FILTER_RULE));
//		Object threshold = parameters.getParameterValue(IndexerModel.P_FILTERING_THRESHOLD);
//		settings.setParameterValue(IndexerModel.P_FILTERING_THRESHOLD, threshold == null ? 0.0 : threshold);
//		settings.setParameterValue(IndexerModel.P_ENABLE_TSV,
//				Boolean.TRUE.equals(parameters.getParameterValue(IndexerModel.P_ENABLE_TSV)));
//
//        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
//        // settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
//        // settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
//        // settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
//
//
//		// Context vector settings
//		settings.setParameterValue(IndexerModel.P_FREQUENCY_THRESHOLD,
//                parameters.getParameterValue(IndexerModel.P_FREQUENCY_THRESHOLD));
//        settings.setParameterValue("AssociationRateClassName",
//                parameters.getParameterValue(IndexerModel.P_ASSOCIATION_MEASURE));
//
//        // Conflation parameters
//        settings.setParameterValue("EnableTermGathering",
//                Boolean.TRUE.equals(parameters.getParameterValue(IndexerModel.P_TERM_VARIANT_DETECTION)));
//        settings.setParameterValue(IndexerModel.P_EDIT_DISTANCE_CLASS,
//                parameters.getParameterValue(IndexerModel.P_EDIT_DISTANCE_CLASS));
//        settings.setParameterValue(IndexerModel.P_EDIT_DISTANCE_THRESHOLD,
//                parameters.getParameterValue(IndexerModel.P_EDIT_DISTANCE_THRESHOLD));
//        settings.setParameterValue(IndexerModel.P_EDIT_DISTANCE_NGRAMS,
//                parameters.getParameterValue(IndexerModel.P_EDIT_DISTANCE_NGRAMS));
//
//        // Addendum Sebastian Peña
//        settings.setParameterValue(IndexerModel.P_IGNORE_DIACRITICS,
//        		Boolean.valueOf(Boolean.TRUE.equals(parameters.getParameterValue(IndexerModel.P_IGNORE_DIACRITICS))));
//
        return settings;
    }

    /**
     * @return  the name of the AE descriptor resource
     */
    @Override
    public String getAEDescriptor() {
        if ( getModel().getLanguage() != null) {
            String code = getModel().getLanguage();
            String language = new Locale(code)
                    .getDisplayLanguage(Locale.ENGLISH).toLowerCase();
            return String.format("eu.project.ttc.%s.engines.indexer.%sIndexer",
                    language.toLowerCase(), WordUtils.capitalizeFully(language));
        } else {
            throw new IllegalStateException("Unable to generate descriptor name for Spotter as no " +
                    "language have been specified in the model.");
        }
    }

    @Override
    public InputSource.InputSourceTypes getInputSourceType() {
        return InputSource.InputSourceTypes.XMI;
    }
		
}
