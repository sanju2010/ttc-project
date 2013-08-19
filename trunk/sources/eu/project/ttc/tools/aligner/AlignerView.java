package eu.project.ttc.tools.aligner;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import eu.project.ttc.tools.commons.ToolView;

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
        // FIXME
        //compResults.clearBanker();
        //compResults.setEnabled(false);
    }

    @Override
    public void runEnds() {
        // FIXME
        //compResults.setEnabled(true);
    }

    //////////////////////////////////////////////////////// ALIGNER BINDINGS

    @Override
    public void setSourceLanguage(String language) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSourceLanguage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addSourceLanguageChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSourceLanguageError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetSourceLanguageError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTargetLanguage(String language) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTargetLanguage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addTargetLanguageChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTargetLanguageError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetTargetLanguageError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getOutputDirectory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOutputDirectoryError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetOutputDirectoryError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSourceTerminology(String sourceTerminology) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSourceTerminology() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addSourceTerminologyChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSourceTerminologyError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetSourceTerminologyError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTargetTerminology(String targetTerminology) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTargetTerminology() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addTargetTerminologyChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTargetTerminologyError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetTargetTerminologyError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBilingualDictionary(String bilingualDictionary) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getBilingualDictionary() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addBilingualDictionaryChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBilingualDictionaryError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetBilingualDictionaryError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setEvaluationDirectory(String evaluationDirectory) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEvaluationDirectory() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addEvaluationDirectoryChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setEvaluationDirectoryError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetEvaluationDirectoryError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCompositionalMethod(boolean isCompositionalMethod) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean isCompositionalMethod() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addCompositionalMethodChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCompositionalMethodError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetCompositionalMethodError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDistributionalMethod(boolean isDistributionalMethod) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean isDistributionalMethod() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addDistributionalMethodChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDistributionalMethodError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetDistributionalMethodError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSimilarityDistanceClass(String similarityDistanceClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSimilarityDistanceClass() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addSimilarityDistanceClassChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSimilarityDistanceClassError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetSimilarityDistanceClassError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMaxCandidates(Integer maxCandidates) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getMaxCandidates() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addMaxCandidatesChangeListener(PropertyChangeListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMaxCandidatesError(Throwable e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsetMaxCandidatesError() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
	
}
