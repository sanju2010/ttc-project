// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

import eu.project.ttc.tools.commons.ToolView;

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
    private final ResultsPanel compResults;
    private final ConfigPanelExport compEConfig;

    private JPanel configTitlePanel;
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
     *
     * Most
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
        configTabs.addTab("Variant", new JScrollPane(compVConfig));
        compEConfig = new ConfigPanelExport();
        compEConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Export", new JScrollPane(compEConfig));


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
        } catch (IOException e) {} // No help available

        configTitlePanel = new JPanel(new BorderLayout());
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

    //////////////////////////////////////////////////////// TOOLVIEW

    @Override
    public void runStarts() {
        // TODO
        // compResults.getResultModel().clear();
        // compResults.setEnabled(false);
    }

    @Override
    public void runEnds() {
        // TODO
        // compResults.setEnabled(true);
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
    public void setLanguageError(IllegalArgumentException e) {
        compBConfig.setLanguageError(e);
    }
    public void unsetLanguageError() {
        compBConfig.unsetLanguageError();
    }
    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.LANGUAGE.getProperty(), listener);
    }

    @Override
    public void setInputDirectory(String inputDirectory) {
        compBConfig.setInputDirectory(inputDirectory);
    }
    @Override
    public String getInputDirectory() {
        return compBConfig.getInputDirectory();
    }
    public void setInputDirectoryError(IllegalArgumentException e) {
        compBConfig.setInputDirectoryError(e);
    }
    public void unsetInputDirectoryError() {
        compBConfig.unsetInputDirectoryError();
    }
    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.INPUT.getProperty(), listener);
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        compBConfig.setOutputDirectory(outputDirectory);
    }
    @Override
    public String getOutputDirectory() {
        return compBConfig.getOutputDirectory();
    }
    public void setOutputDirectoryError(IllegalArgumentException e) {
        compBConfig.setOutputDirectoryError(e);
    }
    public void unsetOutputDirectoryError() {
        compBConfig.unsetOutputDirectoryError();
    }
    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.OUTPUT.getProperty(), listener);
    }

    @Override
    public void setIgnoreDiacritics(boolean ignoreDiacritics) {
        compVConfig.setIgnoreDiacritics(ignoreDiacritics);
    }
    @Override
    public boolean isIgnoreDiacritics() {
        return compVConfig.isIgnoreDiacritics();
    }
    @Override
    public void addIgnoreDiacriticsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.IGNOREDIACRITICS.getProperty(), listener);
    }

    @Override
    public void setVariantDetection(boolean variantDetection) {
        compVConfig.setVariantDetection(variantDetection);
    }
    @Override
    public boolean isVariantDetection() {
        return compVConfig.isVariantDetection();
    }
    @Override
    public void addVariantDetectionChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.VARIANTDETECTION.getProperty(), listener);
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
        addPropertyChangeListener(CFG.EDITDISTANCECLS.getProperty(), listener);
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
        addPropertyChangeListener(CFG.EDITDISTANCETLD.getProperty(), listener);
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
        addPropertyChangeListener(CFG.EDITDISTANCENGRAMS.getProperty(), listener);
    }

    @Override
    public void setFrequencyThreshold(Float frequencyThreshold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public Float getFrequencyThreshold() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addFrequencyThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FREQUENCYTLD.getProperty(), listener);
    }

    @Override
    public void setAssociationMeasure(String associationMeasure) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public String getAssociationMeasure() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addAssociationMeasureChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.ASSOCIATIONMEASURE.getProperty(), listener);
    }

    @Override
    public void setFilteringThreshold(Float filteringThreshold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public Float getFilteringThreshold() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addFilteringThresholdChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FILTERINGTLD.getProperty(), listener);
    }

    @Override
    public void setFilterRule(String filterRule) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public String getFilterRule() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addFilterRuleChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.FILTERRULE.getProperty(), listener);
    }

    @Override
    public void setKeepVerbs(Boolean keepVerbs) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public Boolean isKeepVerbs() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addKeepVerbsChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.KEEPVERBS.getProperty(), listener);
    }

    @Override
    public void setTSVExport(Boolean tsvExport) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public Boolean isTSVExport() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    public void addTSVExportChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(CFG.TSV.getProperty(), listener);
    }

}
