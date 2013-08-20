package eu.project.ttc.tools.aligner;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import eu.project.ttc.tools.commons.ToolView;
import org.apache.uima.jcas.JCas;

/**
 * Main view of the Aligner tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class AlignerView extends JTabbedPane implements ToolView, AlignerBinding {


    private final ConfigPanelBasic compBConfig;
    private final ConfigPanelAdvanced compAConfig;
    private final ResultsPanel compResults;

    private JTabbedPane configTabs;
    private Box configGUI;

    /**
     * Create the view.
     * Instantiate both panels:
     * <ul>
     *     <li>the configuration panel, responsible for exposing the
     *     tool parameters to the user, which is itself splitted in two
     *     config panels (as tabs),</li>
     *     <li>the result panel, responsible for exposing the results
     *     of the processing (this one or another) to the user.</li>
     * </ul>
     */
    public AlignerView() {
        super(JTabbedPane.TOP);

        // Prepare GUI frame for configuration
        createConfigGUI();

        // Build and bind the basic configuration tab
        compBConfig = new ConfigPanelBasic();
        compBConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Basic", new JScrollPane(compBConfig));

        // Build and bind the advanced configuration tab
        compAConfig = new ConfigPanelAdvanced();
        compAConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith(EVT_PREFIX))
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
        configTabs.addTab("Advanced", new JScrollPane(compAConfig));

        // Plug to main GUI part Config and results
        addTab(" Configure ",
                new ImageIcon(ClassLoader.getSystemResource("eu/project/ttc/gui/icons/cog_24x24.png")),
                configGUI);
        compResults = new ResultsPanel();
        addTab(" Visualize Aligner results ",
                new ImageIcon(ClassLoader.getSystemResource("eu/project/ttc/gui/icons/eye_24x18.png")),
                compResults);
    }


    private void createConfigGUI() {
        // Create the title panel
        JEditorPane epConfigTitle = new JEditorPane();
        epConfigTitle.setEditable(false);
        epConfigTitle.setOpaque(false);
        try {
            URL resHelp = getClass().getResource("/eu/project/ttc/gui/texts/aligner/mainhelp.html");
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

    //////////////////////////////////////////////////////// TOOLVIEW

    @Override
    public void runStarts() {
        compResults.clearMixer();
        compResults.setEnabled(false);
    }

    @Override
    public void runEnds() {
        compResults.setEnabled(true);
    }

    //////////////////////////////////////////////////////// ALIGNER BINDINGS

    @Override
    public void setSourceLanguage(String language) {
        compBConfig.setSourceLanguage(language);
    }
    @Override
    public String getSourceLanguage() {
        return compBConfig.getSourceLanguage();
    }
    @Override
    public void addSourceLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SRCLANGUAGE.getProperty(), listener);
    }
    @Override
    public void setSourceLanguageError(Throwable e) {
        compBConfig.setSourceLanguageError(e);
    }
    @Override
    public void unsetSourceLanguageError() {
        compBConfig.unsetSourceLanguageError();
    }

    @Override
    public void setTargetLanguage(String language) {
        compBConfig.setTargetLanguage(language);
    }
    @Override
    public String getTargetLanguage() {
        return compBConfig.getTargetLanguage();
    }
    @Override
    public void addTargetLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TGTLANGUAGE.getProperty(), listener);
    }
    @Override
    public void setTargetLanguageError(Throwable e) {
        compBConfig.setTargetLanguageError(e);
    }
    @Override
    public void unsetTargetLanguageError() {
        compBConfig.unsetTargetLanguageError();
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
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.OUTPUT.getProperty(), listener);
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
    public void setSourceTerminology(String sourceTerminology) {
        compBConfig.setSourceTerminology(sourceTerminology);
    }
    @Override
    public String getSourceTerminology() {
        return compBConfig.getSourceTerminology();
    }
    @Override
    public void addSourceTerminologyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SRCTERMINOLOGY.getProperty(), listener);
    }
    @Override
    public void setSourceTerminologyError(Throwable e) {
        compBConfig.setSourceTerminologyError(e);
    }
    @Override
    public void unsetSourceTerminologyError() {
        compBConfig.unsetSourceTerminologyError();
    }

    @Override
    public void setTargetTerminology(String targetTerminology) {
        compBConfig.setTargetTerminology(targetTerminology);
    }
    @Override
    public String getTargetTerminology() {
        return compBConfig.getTargetTerminology();
    }
    @Override
    public void addTargetTerminologyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TGTTERMINOLOGY.getProperty(), listener);
    }
    @Override
    public void setTargetTerminologyError(Throwable e) {
        compBConfig.setTargetTerminologyError(e);
    }
    @Override
    public void unsetTargetTerminologyError() {
        compBConfig.unsetTargetTerminologyError();
    }

    @Override
    public void setEvaluationDirectory(String evaluationDirectory) {
        compBConfig.setEvaluationDirectory(evaluationDirectory);
    }
    @Override
    public String getEvaluationDirectory() {
        return compBConfig.getEvaluationDirectory();
    }
    @Override
    public void addEvaluationDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.EVALDIR.getProperty(), listener);
    }
    @Override
    public void setEvaluationDirectoryError(Throwable e) {
        compBConfig.setEvaluationDirectoryError(e);
    }
    @Override
    public void unsetEvaluationDirectoryError() {
        compBConfig.unsetEvaluationDirectoryError();
    }

    @Override
    public void setBilingualDictionary(String bilingualDictionary) {
        compAConfig.setBilingualDictionary(bilingualDictionary);
    }
    @Override
    public String getBilingualDictionary() {
        return compAConfig.getBilingualDictionary();
    }
    @Override
    public void addBilingualDictionaryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.DICTIONARY.getProperty(), listener);
    }
    @Override
    public void setBilingualDictionaryError(Throwable e) {
        compAConfig.setBilingualDictionaryError(e);
    }
    @Override
    public void unsetBilingualDictionaryError() {
        compAConfig.unsetBilingualDictionaryError();
    }

    @Override
    public void setCompositionalMethod(boolean isCompositionalMethod) {
        compAConfig.setCompositionalMethod(isCompositionalMethod);
    }
    @Override
    public Boolean isCompositionalMethod() {
        return compAConfig.isCompositionalMethod();
    }
    @Override
    public void addCompositionalMethodChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.COMPOSITIONAL.getProperty(), listener);
    }
    @Override
    public void setCompositionalMethodError(Throwable e) {
        compAConfig.setCompositionalMethodError(e);
    }
    @Override
    public void unsetCompositionalMethodError() {
        compAConfig.unsetCompositionalMethodError();
    }

    @Override
    public void setDistributionalMethod(boolean isDistributionalMethod) {
        compAConfig.setDistributionalMethod(isDistributionalMethod);
    }
    @Override
    public Boolean isDistributionalMethod() {
        return compAConfig.isDistributionalMethod();
    }
    @Override
    public void addDistributionalMethodChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.DISTRIBUTIONAL.getProperty(), listener);
    }
    @Override
    public void setDistributionalMethodError(Throwable e) {
        compAConfig.setDistributionalMethodError(e);
    }
    @Override
    public void unsetDistributionalMethodError() {
        compAConfig.unsetDistributionalMethodError();
    }

    @Override
    public void setSimilarityDistanceClass(String similarityDistanceClass) {
        compAConfig.setSimilarityDistanceClass(similarityDistanceClass);
    }
    @Override
    public String getSimilarityDistanceClass() {
        return compAConfig.getSimilarityDistanceClass();
    }
    @Override
    public void addSimilarityDistanceClassChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.SIMILARITY.getProperty(), listener);
    }
    @Override
    public void setSimilarityDistanceClassError(Throwable e) {
        compAConfig.setSimilarityDistanceClassError(e);
    }
    @Override
    public void unsetSimilarityDistanceClassError() {
        compAConfig.unsetSimilarityDistanceClassError();
    }

    @Override
    public void setMaxCandidates(Integer maxCandidates) {
        compAConfig.setMaxCandidates(maxCandidates);
    }
    @Override
    public Integer getMaxCandidates() {
        return compAConfig.getMaxCandidates();
    }
    @Override
    public void addMaxCandidatesChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.MAXCANDIDATES.getProperty(), listener);
    }
    @Override
    public void setMaxCandidatesError(Throwable e) {
        compAConfig.setMaxCandidatesError(e);
    }
    @Override
    public void unsetMaxCandidatesError() {
        compAConfig.unsetMaxCandidatesError();
    }

    public void addCasToMixer(JCas jCas) {
        compResults.addCasToMixer(jCas);
    }
}
