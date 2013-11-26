/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.commons.ToolController;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceConfigurationException;
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
     * Create a IndexerController that is connected to a view and a model.
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
                        System.out.println("Indexer:view-model:language->" + evt.getNewValue());
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
                        System.out.println("Indexer:view-model:input directory->" + evt.getNewValue());
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
                        System.out.println("Indexer:view-model:output directory->" + evt.getNewValue());
                        getModel().setOutputDirectory((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setOutputDirectoryError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetOutputDirectoryError();
            }
        });
        // Association measure
        getView().addAssociationMeasureChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getAssociationMeasure() == null) ||
                        !getModel().getAssociationMeasure().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:association measure->" + evt.getNewValue());
                        getModel().setAssociationMeasure(((ClassItem) evt.getNewValue()).getClassName());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setAssociationMeasureError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetAssociationMeasureError();
            }
        });
        // Edit distance class
        getView().addEditDistanceClassChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getEditDistanceClass() == null) ||
                        !getModel().getEditDistanceClass().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:edit distance class->" + evt.getNewValue());
                        getModel().setEditDistanceClass((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setEditDistanceClassError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetEditDistanceClassError();
            }
        });
        // Edit distance ngrams
        getView().addEditDistanceNgramsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getEditDistanceNgrams() == null) ||
                        !getModel().getEditDistanceNgrams().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:edit distance ngrams->" + evt.getNewValue());
                        getModel().setEditDistanceNgrams((Integer) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setEditDistanceNgramsError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetEditDistanceNgramsError();
            }
        });
        // Edit distance threshold
        getView().addEditDistanceThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getEditDistanceThreshold() == null) ||
                        !getModel().getEditDistanceThreshold().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:edit distance threshold->" + evt.getNewValue());
                        getModel().setEditDistanceThreshold((Float) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setEditDistanceThresholdError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetEditDistanceThresholdError();
            }
        });
        // Filtering threshold
        getView().addFilteringThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getFilteringThreshold() == null) ||
                        !getModel().getFilteringThreshold().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:filtering threshold->" + evt.getNewValue());
                        getModel().setFilteringThreshold(new Float(((Number) evt.getNewValue()).floatValue()));
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setFilteringThresholdError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetFilteringThresholdError();
            }
        });
        // Filter rule
        getView().addFilterRuleChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getFilterRule() == null) ||
                        !getModel().getFilterRule().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:filter rule->" + evt.getNewValue());
                        getModel().setFilterRule((String) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setFilterRuleError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetFilterRuleError();
            }
        });
        // Frequency threshold
        getView().addOccurrenceThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if ((getModel().getOccurrenceThreshold() == null) ||
                        !getModel().getOccurrenceThreshold().equals(evt.getNewValue())) {
                    try {
                        System.out.println("Indexer:view-model:occurrence threshold->" + evt.getNewValue());
                        getModel().setOccurrenceThreshold((Integer) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setOccurrenceThresholdError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetOccurrenceThresholdError();
            }
        });
        // Ignore diacritics
        getView().addIgnoreDiacriticsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if (getModel().isIgnoreDiacritics() != evt.getNewValue()) {
                    try {
                        System.out.println("Indexer:view-model:ignore diacritics->" + evt.getNewValue());
                        getModel().setIgnoreDiacritics((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setIgnoreDiacriticsError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetIgnoreDiacriticsError();
            }
        });
        // Keep verbs
        getView().addKeepVerbsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if (getModel().isKeepVerbs() != evt.getNewValue()) {
                    try {
                        System.out.println("Indexer:view-model:keep verbs->" + evt.getNewValue());
                        getModel().setKeepVerbs((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setKeepVerbsError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetKeepVerbsError();
            }
        });
        // TSV export
        getView().addTSVExportChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if (getModel().isTSVExport() != evt.getNewValue()) {
                    try {
                        System.out.println("Indexer:view-model:tsv export->" + evt.getNewValue());
                        getModel().setTSVExport((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setTSVExportError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetTSVExportError();
            }
        });
        // Syntactic Variant detection
        getView().addSyntacticVariantDetectionChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if (getModel().isSyntacticVariantDetection() != evt.getNewValue()) {
                    try {
                        System.out.println("Indexer:view-model:syntactic variant detection->" + evt.getNewValue());
                        getModel().setSyntacticVariantDetection((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setSyntacticVariantDetectionError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetSyntacticVariantDetectionError();
            }
        });
        // Graphical Variant detection
        getView().addGraphicalVariantDetectionChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                boolean success = true;
                if (getModel().isGraphicalVariantDetection() != evt.getNewValue()) {
                    try {
                        System.out.println("Indexer:view-model:graphical variant detection->" + evt.getNewValue());
                        getModel().setGraphicalVariantDetection((Boolean) evt.getNewValue());
                    } catch (IllegalArgumentException e) {
                        success = false;
                        getView().setGraphicalVariantDetectionError(e);
                    }
                } // else, no need to reflect the change (and prevent looping)
                if (success) getView().unsetGraphicalVariantDetectionError();
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
                    System.out.println("Indexer:model-view:language->" + evt.getNewValue());
                    getView().setLanguage((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Input directory
        getModel().addInputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getInputDirectory()==null) ||
                        ! getView().getInputDirectory().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:input directory->" + evt.getNewValue());
                    getView().setInputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Output directory
        getModel().addOutputDirectoryChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ((getView().getOutputDirectory() == null) ||
                        !getView().getOutputDirectory().equals(evt.getNewValue())) {
                    System.out.println("Indexer:model-view:output directory->" + evt.getNewValue());
                    getView().setOutputDirectory((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Association measure
        getModel().addAssociationMeasureChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getAssociationMeasure()==null) ||
                        ! getView().getAssociationMeasure().equals(evt.getNewValue()) ) {
                    System.out.println("Reflecting association measure change from model->" + evt.getNewValue());
                    getView().setAssociationMeasure((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Edit distance class
        getModel().addEditDistanceClassChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getEditDistanceClass()==null) ||
                        ! getView().getEditDistanceClass().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:edit distance class->" + evt.getNewValue());
                    getView().setEditDistanceClass((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Edit distance ngrams
        getModel().addEditDistanceNgramsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getEditDistanceNgrams()==null) ||
                        ! getView().getEditDistanceNgrams().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:edit distance ngrams->" + evt.getNewValue());
                    getView().setEditDistanceNgrams((Integer) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Edit distance threshold
        getModel().addEditDistanceThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getEditDistanceThreshold()==null) ||
                        ! getView().getEditDistanceThreshold().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:edit distance threshold->" + evt.getNewValue());
                    getView().setEditDistanceThreshold((Float) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Filtering threshold
        getModel().addFilteringThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getFilteringThreshold()==null) ||
                        ! getView().getFilteringThreshold().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:filtering threshold->" + evt.getNewValue());
                    getView().setFilteringThreshold((Float) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Filter rule
        getModel().addFilterRuleChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( (getView().getFilterRule()==null) ||
                        ! getView().getFilterRule().equals(evt.getNewValue()) ) {
                    System.out.println("Indexer:model-view:filter rule->" + evt.getNewValue());
                    getView().setFilterRule((String) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Frequency threshold
        getModel().addOccurrenceThresholdChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ((getView().getOccurrenceThreshold() == null) ||
                        !getView().getOccurrenceThreshold().equals(evt.getNewValue())) {
                    System.out.println("Indexer:model-view:occurrence threshold->" + evt.getNewValue());
                    getView().setOccurrenceThreshold((Integer) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Ignore diacritics
        getModel().addIgnoreDiacriticsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isIgnoreDiacritics() != evt.getNewValue() ) {
                    System.out.println("Indexer:model-view:ignore diacritics->" + evt.getNewValue());
                    getView().setIgnoreDiacritics((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Keep verbs
        getModel().addKeepVerbsChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isKeepVerbs() != evt.getNewValue() ) {
                    System.out.println("Indexer:model-view:keep verbs->" + evt.getNewValue());
                    getView().setKeepVerbs((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // TSV export
        getModel().addTSVExportChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isTSVExport() != evt.getNewValue() ) {
                    System.out.println("Indexer:model-view:tsv export->" + evt.getNewValue());
                    getView().setTSVExport((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Graphical Variant detection
        getModel().addGraphicalVariantDetectionChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isGraphicalVariantDetection() != evt.getNewValue() ) {
                    System.out.println("Indexer:model-view:graphical variant detection->" + evt.getNewValue());
                    getView().setGraphicalVariantDetection((Boolean) evt.getNewValue());
                } // else, no need to reflect the change (and prevent looping)
            }
        });
        // Syntactic Variant detection
        getModel().addSyntacticVariantDetectionChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ( getView().isSyntacticVariantDetection() != evt.getNewValue() ) {
                    System.out.println("Indexer:model-view:syntactic variant detection->" + evt.getNewValue());
                    getView().setSyntacticVariantDetection((Boolean) evt.getNewValue());
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
        getView().addCasToBanker(cas.getJCas());
    }

    /**
     * @see eu.project.ttc.tools.commons.ToolController#synchronizeViewToModel()
     */
    @Override
    public void synchronizeViewToModel() {
        try { getModel().setAssociationMeasure( getView().getAssociationMeasure() ); }
        catch (IllegalArgumentException e) { getView().setAssociationMeasureError(e); }

        try { getModel().setEditDistanceClass( getView().getEditDistanceClass() ); }
        catch (IllegalArgumentException e) { getView().setEditDistanceThresholdError(e); }

        try { getModel().setEditDistanceNgrams( getView().getEditDistanceNgrams() ); }
        catch (IllegalArgumentException e) { getView().setEditDistanceNgramsError(e); }

        try { getModel().setEditDistanceThreshold( getView().getEditDistanceThreshold() ); }
        catch (IllegalArgumentException e) { getView().setEditDistanceThresholdError(e); }

        try { getModel().setFilteringThreshold( getView().getFilteringThreshold() ); }
        catch (IllegalArgumentException e) { getView().setFilteringThresholdError(e); }

        try { getModel().setFilterRule( getView().getFilterRule() ); }
        catch (IllegalArgumentException e) { getView().setFilterRuleError(e); }

        try { getModel().setOccurrenceThreshold(getView().getOccurrenceThreshold()); }
        catch (IllegalArgumentException e) { getView().setOccurrenceThresholdError(e); }
        catch (ClassCastException e) { getView().setOccurrenceThresholdError(e); }

        try { getModel().setIgnoreDiacritics( getView().isIgnoreDiacritics() ); }
        catch (IllegalArgumentException e) { getView().setIgnoreDiacriticsError(e); }

        try { getModel().setInputDirectory( getView().getInputDirectory() ); }
        catch (IllegalArgumentException e) { getView().setInputDirectoryError(e); }

        try { getModel().setKeepVerbs( getView().isKeepVerbs() ); }
        catch (IllegalArgumentException e) { getView().setKeepVerbsError(e); }

        try { getModel().setLanguage( getView().getLanguage() ); }
        catch (IllegalArgumentException e) { getView().setLanguageError(e); }

        try { getModel().setOutputDirectory( getView().getOutputDirectory() ); }
        catch (IllegalArgumentException e) { getView().setOutputDirectoryError(e); }

        try { getModel().setTSVExport( getView().isTSVExport() ); }
        catch (IllegalArgumentException e) { getView().setTSVExportError(e); }

        try { getModel().setSyntacticVariantDetection( getView().isSyntacticVariantDetection() ); }
        catch (IllegalArgumentException e) { getView().setSyntacticVariantDetectionError(e); }
        
        try { getModel().setGraphicalVariantDetection( getView().isGraphicalVariantDetection() ); }
        catch (IllegalArgumentException e) { getView().setGraphicalVariantDetectionError(e); }
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
    public ConfigurationParameterSettings getAESettings() throws ResourceConfigurationException {
        getModel().validate();

        // Prepare an empty ConfigurationParameterSetting
        ConfigurationParameterSettings settings = UIMAFramework
                .getResourceSpecifierFactory().createConfigurationParameterSettings();

        // Common parameters settings
        settings.setParameterValue(IndexerBinding.PRM.LANGUAGE.getParameter(),
                getModel().getLanguage());
        settings.setParameterValue(IndexerBinding.PRM.OUTPUT.getParameter(),
                getModel().getOutputDirectory());
		settings.setParameterValue("Action", "drop"); // What ?

		// TBX settings
		settings.setParameterValue(IndexerBinding.PRM.KEEPVERBS.getParameter(),
                getModel().isKeepVerbs());
		settings.setParameterValue(IndexerBinding.PRM.FILTERRULE.getParameter(),
                getModel().getFilterRule());
		settings.setParameterValue(IndexerBinding.PRM.FILTERINGTLD.getParameter(),
                getModel().getFilteringThreshold());
		settings.setParameterValue(IndexerBinding.PRM.TSV.getParameter(),
                getModel().isTSVExport());

        // Context vector settings
        settings.setParameterValue(IndexerBinding.PRM.OCCURRENCETLD.getParameter(),
                getModel().getOccurrenceThreshold());
        settings.setParameterValue(IndexerBinding.PRM.ASSOCIATIONMEASURE.getParameter(),
                getModel().getAssociationMeasure());

        // Conflation settings
        settings.setParameterValue(IndexerBinding.PRM.GRPHVARIANTDETECTION.getParameter(),
                getModel().isGraphicalVariantDetection());
        settings.setParameterValue(IndexerBinding.PRM.SYNTVARIANTDETECTION.getParameter(),
                getModel().isSyntacticVariantDetection());
        settings.setParameterValue(IndexerBinding.PRM.EDITDISTANCECLS.getParameter(),
                getModel().getEditDistanceClass());
        settings.setParameterValue(IndexerBinding.PRM.EDITDISTANCETLD.getParameter(),
                getModel().getEditDistanceThreshold());
        settings.setParameterValue(IndexerBinding.PRM.EDITDISTANCENGRAMS.getParameter(),
                getModel().getEditDistanceNgrams());
        settings.setParameterValue(IndexerBinding.PRM.IGNOREDIACRITICS.getParameter(),
                getModel().isIgnoreDiacritics());

        // FIXME Advanced settings
        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        // settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        // settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        // settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));

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
            throw new IllegalStateException("Unable to generate descriptor name for Indexer as no " +
                    "language have been specified in the model.");
        }
    }

}
