// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.commons.ToolView;
import org.apache.uima.jcas.JCas;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

/**
 * Main view of the Indexer tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class IndexerView extends JTabbedPane implements ToolView, IndexerBinding {

    private final ConfigPanelBasic compBConfig;
    private final ConfigPanelVariants compVConfig;
    private final ConfigPanelExport compEConfig;
    private final ConfigPanelContextVectors compCVConfig;
    private final ResultsPanel compResults;


    private JTabbedPane configTabs;
    private Box configGUI;

    /**
     * Create the view.
     * Instantiate both panels:
     * <ul>
     *     <li>the configuration panel, responsible for exposing the
     *     tool parameters to the user, which is itself splitted in three
     *     config panels (as tabs),</li>
     *     <li>the result panel, responsible for exposing the results
     *     of the processing (this one or another) to the user.</li>
     * </ul>
     */
    public IndexerView() {
        super(JTabbedPane.TOP);

        // Prepare GUI frame for configuration
        createConfigGUI();
        // Build and bind each config GUI part
        compBConfig = new ConfigPanelBasic();
        compBConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Basic", new JScrollPane(compBConfig));
        compVConfig = new ConfigPanelVariants();
        compVConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Variant detection", new JScrollPane(compVConfig));
        compEConfig = new ConfigPanelExport();
        compEConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Filtering and Export", new JScrollPane(compEConfig));
        compCVConfig = new ConfigPanelContextVectors();
        compCVConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Context vectors", new JScrollPane(compCVConfig));


        // Plug to main GUI part Config and results
        addTab(" Configure ",
                new ImageIcon(ClassLoader.getSystemResource("eu/project/ttc/gui/icons/cog_24x24.png")),
                configGUI);
        compResults = new ResultsPanel();
        addTab(" Visualize Indexer results ",
                new ImageIcon(ClassLoader.getSystemResource("eu/project/ttc/gui/icons/eye_24x18.png")),
                compResults);
    }


    private void createConfigGUI() {
        // Create the title panel
        JEditorPane epConfigTitle = new JEditorPane();
        epConfigTitle.setEditable(false);
        epConfigTitle.setOpaque(false);
        try {
            URL resHelp = getClass().getResource("/eu/project/ttc/gui/texts/indexer/mainhelp.html");
            epConfigTitle.setPage(resHelp);
        } catch (IOException ignored) {} // No help available

        JPanel configTitlePanel = new JPanel(new BorderLayout());
        configTitlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        configTitlePanel.setOpaque(false);
        configTitlePanel.add(epConfigTitle);

        configTabs = new JTabbedPane(JTabbedPane.TOP);

        configGUI = Box.createVerticalBox();
        configGUI.setOpaque(false);
        configGUI.add(configTitlePanel);
        configGUI.add( Box.createRigidArea( new Dimension(0, 15) ) );
        configGUI.add(configTabs);
    }

    public void addCasToBanker(JCas jCas) {
        compResults.addCasToBanker(jCas);
    }

    //////////////////////////////////////////////////////// TOOLVIEW

    @Override
    public void runStarts() {
        compResults.clearBanker();
        compResults.setEnabled(false);
    }

    @Override
    public void runEnds() {
        compResults.setEnabled(true);
    }

    //////////////////////////////////////////////////////// INDEXER BINDINGS

    @Override
    public void setLanguage(String language) {
        compBConfig.setLanguage(language);
    }
    @Override
    public String getLanguage() {
        return compBConfig.getLanguage();
    }
    @Override
    public void setLanguageError(Throwable e) {
        compBConfig.setLanguageError(e);
    }
    @Override
    public void unsetLanguageError() {
        compBConfig.unsetLanguageError();
    }
    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.LANGUAGE.getProperty(), listener);
    }

    @Override
    public void setInputDirectory(String inputDirectory) {
        compBConfig.setInputDirectory(inputDirectory);
    }
    @Override
    public String getInputDirectory() {
        return compBConfig.getInputDirectory();
    }
    @Override
    public void setInputDirectoryError(Throwable e) {
        compBConfig.setInputDirectoryError(e);
    }
    @Override
    public void unsetInputDirectoryError() {
        compBConfig.unsetInputDirectoryError();
    }
    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.INPUT.getProperty(), listener);
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        compBConfig.setOutputDirectory(outputDirectory);
    }
    @Override
    public String getOutputDirectory() {
        return compBConfig.getOutputDirectory();
    }
    @Override
    public void setOutputDirectoryError(Throwable e) {
        compBConfig.setOutputDirectoryError(e);
    }
    @Override
    public void unsetOutputDirectoryError() {
        compBConfig.unsetOutputDirectoryError();
    }
    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.OUTPUT.getProperty(), listener);
    }

    @Override
    public void setIgnoreDiacritics(boolean ignoreDiacritics) {
        compVConfig.setIgnoreDiacritics(ignoreDiacritics);
    }
    @Override
    public Boolean isIgnoreDiacritics() {
        return compVConfig.isIgnoreDiacritics();
    }
    @Override
    public void addIgnoreDiacriticsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.IGNOREDIACRITICS.getProperty(), listener);
    }
    @Override
    public void setIgnoreDiacriticsError(Throwable e) {
        compVConfig.setIgnoreDiacriticsError(e);
    }
    @Override
    public void unsetIgnoreDiacriticsError() {
        compVConfig.unsetIgnoreDiacriticsError();
    }

    @Override
    public void setVariantDetection(boolean variantDetection) {
        compVConfig.setVariantDetection(variantDetection);
    }
    @Override
    public Boolean isVariantDetection() {
        return compVConfig.isVariantDetection();
    }
    @Override
    public void addVariantDetectionChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.VARIANTDETECTION.getProperty(), listener);
    }
    @Override
    public void setVariantDetectionError(Throwable e) {
        compVConfig.setVariantDetectionError(e);
    }
    @Override
    public void unsetVariantDetectionError() {
        compVConfig.unsetVariantDetectionError();
    }

    @Override
    public void setEditDistanceClass(String editDistanceClass) {
        compVConfig.setEditDistanceClass(editDistanceClass);
    }
    @Override
    public String getEditDistanceClass() {
        return compVConfig.getEditDistanceClass();
    }
    @Override
    public void addEditDistanceClassChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCECLS.getProperty(), listener);
    }
    @Override
    public void setEditDistanceClassError(Throwable e) {
        compVConfig.setEditDistanceClassError(e);
    }
    @Override
    public void unsetEditDistanceClassError() {
        compVConfig.unsetEditDistanceClassError();
    }

    @Override
    public void setEditDistanceThreshold(Float editDistanceThreshold) {
        compVConfig.setEditDistanceTld(editDistanceThreshold);
    }
    @Override
    public Float getEditDistanceThreshold() {
        return compVConfig.getEditDistanceTld();
    }
    @Override
    public void addEditDistanceThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCETLD.getProperty(), listener);
    }
    @Override
    public void setEditDistanceThresholdError(Throwable e) {
        compVConfig.setEditDistanceTldError(e);
    }
    @Override
    public void unsetEditDistanceThresholdError() {
        compVConfig.unsetEditDistanceTldError();
    }

    @Override
    public void setEditDistanceNgrams(Integer editDistanceNgrams) {
        compVConfig.setEditDistanceNgrams(editDistanceNgrams);
    }
    @Override
    public Integer getEditDistanceNgrams() {
        return compVConfig.getEditDistanceNgrams();
    }
    @Override
    public void addEditDistanceNgramsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EDITDISTANCENGRAMS.getProperty(), listener);
    }
    @Override
    public void setEditDistanceNgramsError(Throwable e) {
        compVConfig.setEditDistanceNgramsError(e);
    }
    @Override
    public void unsetEditDistanceNgramsError() {
        compVConfig.unsetEditDistanceNgramsError();
    }

    @Override
    public void setFrequencyThreshold(Float frequencyThreshold) {
        compEConfig.setFrequencyThreshold(frequencyThreshold);
    }
    @Override
    public Float getFrequencyThreshold() {
        return compEConfig.getFrequencyThreshold();
    }
    @Override
    public void addFrequencyThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FREQUENCYTLD.getProperty(), listener);
    }
    @Override
    public void setFrequencyThresholdError(Throwable e) {
        compEConfig.setFrequencyThresholdError(e);
    }
    @Override
    public void unsetFrequencyThresholdError() {
        compEConfig.unsetFrequencyThresholdError();
    }

    @Override
    public void setAssociationMeasure(String associationMeasure) {
        compCVConfig.setAssociationMeasure(associationMeasure);
    }
    @Override
    public String getAssociationMeasure() {
        return compCVConfig.getAssociationMeasure();
    }
    @Override
    public void addAssociationMeasureChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.ASSOCIATIONMEASURE.getProperty(), listener);
    }
    @Override
    public void setAssociationMeasureError(Throwable e) {
        compCVConfig.setAssociationMeasureError(e);
    }
    @Override
    public void unsetAssociationMeasureError() {
        compCVConfig.unsetAssociationMeasureError();
    }

    @Override
    public void setFilteringThreshold(Float filteringThreshold) {
        compEConfig.setFilteringThreshold(filteringThreshold);
    }
    @Override
    public Float getFilteringThreshold() {
        return compEConfig.getFilteringThreshold();
    }
    @Override
    public void addFilteringThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FILTERINGTLD.getProperty(), listener);
    }
    @Override
    public void setFilteringThresholdError(Throwable e) {
        compEConfig.setFilteringThresholdError(e);
    }
    @Override
    public void unsetFilteringThresholdError() {
        compEConfig.unsetFilteringThresholdError();
    }

    @Override
    public void setFilterRule(String filterRule) {
        compEConfig.setFilterRule(filterRule);
    }
    @Override
    public String getFilterRule() {
        return compEConfig.getFilterRule();
    }
    @Override
    public void addFilterRuleChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.FILTERRULE.getProperty(), listener);
    }
    @Override
    public void setFilterRuleError(Throwable e) {
        compEConfig.setFilterRuleError(e);
    }
    @Override
    public void unsetFilterRuleError() {
        compEConfig.unsetFilterRuleError();
    }

    @Override
    public void setKeepVerbs(Boolean keepVerbs) {
        compEConfig.setKeepVerbs(keepVerbs);
    }
    @Override
    public Boolean isKeepVerbs() {
        return compEConfig.isKeepVerbs();
    }
    @Override
    public void addKeepVerbsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.KEEPVERBS.getProperty(), listener);
    }
    @Override
    public void setKeepVerbsError(Throwable e) {
        compEConfig.setKeepVerbsError(e);
    }
    @Override
    public void unsetKeepVerbsError() {
        compEConfig.unsetKeepVerbsError();
    }

    @Override
    public void setTSVExport(Boolean tsvExport) {
        compEConfig.setTSVExport(tsvExport);
    }
    @Override
    public Boolean isTSVExport() {
        return compEConfig.isTSVExport();
    }
    @Override
    public void addTSVExportChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TSV.getProperty(), listener);
    }
    @Override
    public void setTSVExportError(Throwable e) {
        compEConfig.setTSVExportError(e);
    }
    @Override
    public void unsetTSVExportError() {
        compEConfig.unsetTSVExportError();
    }
}
