package eu.project.ttc.tools.aligner;

import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.commons.ToolController;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controller of the Aligner tool.
 *
 * The aligner tool is responsible for aligning terms from different corpus.
 *
 * The controller conciliates the model and the view as well as providing
 * higher level features as to build analysis engine description and
 * parameters settings.
 */
public class AlignerController extends ToolController {

    /**
     * Constructor.
     * Create a AlignerController that is connected to a view and a model.
     * Double binds the view and the model so that a change in the view is
     * reflected to the model, and a change in the model is reflected in
     * the view.
     */
    public AlignerController(AlignerModel model, AlignerView view) {
        super(model, view);
        bindViewToModel();
        bindModelToView();
    }

    /**
     * Bind view listeners to model changes so that any change to the view
     * is reflected to the model.
     */
    private void bindViewToModel() {
        // Bilingual dictionary
        getView().addBilingualDictionaryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getBilingualDictionary() == null) ||
                        !getModel().getBilingualDictionary().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:bilingual dictionary->" + evt.getNewValue());
                        getModel().setBilingualDictionary((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setBilingualDictionaryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetBilingualDictionaryError();
            }
        });
        // Compositional method
        getView().addCompositionalMethodChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().isCompositionalMethod() == null) ||
                        !getModel().isCompositionalMethod().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:compositional method->" + evt.getNewValue());
                        getModel().setCompositionalMethod((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setCompositionalMethodError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetCompositionalMethodError();
            }
        });
        // Distributional method
        getView().addDistributionalMethodChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().isDistributionalMethod() == null) ||
                        !getModel().isDistributionalMethod().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:distributional method->" + evt.getNewValue());
                        getModel().setDistributionalMethod((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setDistributionalMethodError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetDistributionalMethodError();
            }
        });
        // Evaluation directory
        getView().addEvaluationDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getEvaluationDirectory() == null) ||
                        !getModel().getEvaluationDirectory().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:evaluation directory->" + evt.getNewValue());
                        getModel().setEvaluationDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setEvaluationDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetEvaluationDirectoryError();
            }
        });
        // Max candidates
        getView().addMaxCandidatesChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getMaxCandidates() == null) ||
                        !getModel().getMaxCandidates().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:max candidates->" + evt.getNewValue());
                        getModel().setMaxCandidates((Integer) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setMaxCandidatesError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetMaxCandidatesError();
            }
        });
        // Output directory
        getView().addOutputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getOutputDirectory() == null) ||
                        !getModel().getOutputDirectory().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:output directory->" + evt.getNewValue());
                        getModel().setOutputDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setOutputDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetOutputDirectoryError();
            }
        });
        // Similarity distance
        getView().addSimilarityDistanceClassChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getSimilarityDistanceClass() == null) ||
                        !getModel().getSimilarityDistanceClass().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:similarity distance->" + evt.getNewValue());
                        getModel().setSimilarityDistanceClass((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setSimilarityDistanceClassError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetSimilarityDistanceClassError();
            }
        });
        // Source language
        getView().addSourceLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getSourceLanguage() == null) ||
                        !getModel().getSourceLanguage().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:source language->" + evt.getNewValue());
                        getModel().setSourceLanguage((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setSourceLanguageError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetSourceLanguageError();
            }
        });
        // Target language
        getView().addTargetLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getTargetLanguage() == null) ||
                        !getModel().getTargetLanguage().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:target language->" + evt.getNewValue());
                        getModel().setTargetLanguage((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setTargetLanguageError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetTargetLanguageError();
            }
        });
        // Source terminology
        getView().addSourceTerminologyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getSourceTerminology() == null) ||
                        !getModel().getSourceTerminology().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:source terminology->" + evt.getNewValue());
                        getModel().setSourceTerminology((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setSourceTerminologyError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetSourceTerminologyError();
            }
        });
        // Target terminology
        getView().addTargetTerminologyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getTargetTerminology() == null) ||
                        !getModel().getTargetTerminology().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Aligner:view-model:target terminology->" + evt.getNewValue());
                        getModel().setTargetTerminology((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setTargetTerminologyError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetTargetTerminologyError();
            }
        });
    }

    /**
     * Bind model listeners to view changes so that any change to the model
     * is reflected to the view.
     */
    private void bindModelToView() {
        // Bilingual dictionary
        getModel().addBilingualDictionaryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getBilingualDictionary()==null) ||
                        ! getView().getBilingualDictionary().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:bilingual dictionary->" + evt.getNewValue());
                    getView().setBilingualDictionary((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Compositional method
        getModel().addCompositionalMethodChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isCompositionalMethod() ) {
                    System.out.println("Aligner:model-view:compositional method->" + evt.getNewValue());
                    getView().setCompositionalMethod((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Distributional method
        getModel().addDistributionalMethodChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isDistributionalMethod() ) {
                    System.out.println("Aligner:model-view:distributional method->" + evt.getNewValue());
                    getView().setDistributionalMethod((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Evaluation directory
        getModel().addEvaluationDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getEvaluationDirectory()==null) ||
                        ! getView().getEvaluationDirectory().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:evaluation directory->" + evt.getNewValue());
                    getView().setEvaluationDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Max candidates
        getModel().addMaxCandidatesChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getMaxCandidates()==null) ||
                        ! getView().getMaxCandidates().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:max candidates->" + evt.getNewValue());
                    getView().setMaxCandidates((Integer) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Output directory
        getModel().addOutputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getOutputDirectory()==null) ||
                        ! getView().getOutputDirectory().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:output directory->" + evt.getNewValue());
                    getView().setOutputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Similarity distance
        getModel().addSimilarityDistanceClassChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getSimilarityDistanceClass()==null) ||
                        ! getView().getSimilarityDistanceClass().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:similarity distance->" + evt.getNewValue());
                    getView().setSimilarityDistanceClass((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Source language
        getModel().addSourceLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getSourceLanguage()==null) ||
                        ! getView().getSourceLanguage().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:source language->" + evt.getNewValue());
                    getView().setSourceLanguage((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Target language
        getModel().addTargetLanguageChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getTargetLanguage()==null) ||
                        ! getView().getTargetLanguage().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:target language->" + evt.getNewValue());
                    getView().setTargetLanguage((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Source terminology
        getModel().addSourceTerminologyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getSourceTerminology()==null) ||
                        ! getView().getSourceTerminology().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:source terminology->" + evt.getNewValue());
                    getView().setSourceTerminology((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Target terminology
        getModel().addTargetTerminologyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getTargetTerminology()==null) ||
                        ! getView().getTargetTerminology().equals(evt.getNewValue()) ) {
                    System.out.println("Aligner:model-view:target terminology->" + evt.getNewValue());
                    getView().setTargetTerminology((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
    }

    /** Getter to the model with appropriate casting */
    protected AlignerModel getModel() {
        return (AlignerModel) getToolModel();
    }

    /** Getter to the view with appropriate casting */
    protected AlignerView getView() {
        return (AlignerView) getToolView();
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#getInputSource()
     *
     * For the aligner tool, the data files to be processed are text files
     * and they are located in the evaluation directory specified as parameter.
     */
    @Override
    public InputSource getInputSource() {
        // FIXME what is the input source in this case ?
        return new InputSource(getModel().getEvaluationDirectory(),
                    InputSource.InputSourceTypes.TXT);
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#processingCallback(org.apache.uima.cas.CAS)
     */
    @Override
    public void processingCallback(CAS cas) throws Exception {
        // FIXME this.getTool().getParent().getMixer().doLoad(cas.getJCas());
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#getLanguage()
     */
    @Override
    public String getLanguage() {
        return getModel().getSourceLanguage(); // FIXME is it correct for getLanguage ?
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
    public ConfigurationParameterSettings getAESettings() throws ResourceConfigurationException {
        getModel().validate();

        // Prepare an empty ConfigurationParameterSetting
        ConfigurationParameterSettings settings = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameterSettings();

        settings.setParameterValue(AlignerBinding.PRM.EVALDIR.getParameter(),
                getModel().getEvaluationDirectory());
        settings.setParameterValue(AlignerBinding.PRM.OUTPUT.getParameter(),
                getModel().getOutputDirectory());
        settings.setParameterValue("Action", "drop");

		settings.setParameterValue(AlignerBinding.PRM.SRCLANGUAGE.getParameter(),
                getModel().getSourceLanguage());
		settings.setParameterValue(AlignerBinding.PRM.TGTLANGUAGE.getParameter(),
                getModel().getTargetLanguage());
        settings.setParameterValue(AlignerBinding.PRM.DICTIONARY.getParameter(),
                getModel().getBilingualDictionary());
        settings.setParameterValue(AlignerBinding.PRM.SRCTERMINOLOGY.getParameter(),
                getModel().getSourceTerminology());
        settings.setParameterValue(AlignerBinding.PRM.TGTTERMINOLOGY.getParameter(),
                getModel().getTargetTerminology());
        settings.setParameterValue(AlignerBinding.PRM.SIMILARITY.getParameter(),
                getModel().getSimilarityDistanceClass());
        settings.setParameterValue(AlignerBinding.PRM.DISTRIBUTIONAL.getParameter(),
                getModel().isDistributionalMethod());
        settings.setParameterValue(AlignerBinding.PRM.COMPOSITIONAL.getParameter(),
                getModel().isCompositionalMethod());
        settings.setParameterValue(AlignerBinding.PRM.MAXCANDIDATES.getParameter(),
                getModel().getMaxCandidates());

        return settings;
    }

    /**
     * @return  the name of the AE descriptor resource
     */
    @Override
    public String getAEDescriptor() {
        return "eu.project.ttc.all.engines.aligner.Aligner";
    }

    @Override
    public InputSource.InputSourceTypes getInputSourceType() {
        return InputSource.InputSourceTypes.XMI;
    }
		
}
