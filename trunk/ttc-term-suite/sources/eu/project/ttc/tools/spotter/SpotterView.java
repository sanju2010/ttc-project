package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.ToolView;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Main view of the Spotter tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class SpotterView extends JTabbedPane implements ToolView, SpotterBinding {

    private final ConfigPanel compConfig;
    private final ResultsPanel compResults;

    /**
     * Create the view.
     * Instantiate both panels:
     * <ul>
     *     <li>the configuration panel, responsible for exposing the
     *     tool parameters to the user,</li>
     *     <li>the result panel, responsible for exposing the results
     *     of the processing (this one or another) to the user.</li>
     * </ul>
     *
     * Most
     */
    public SpotterView() {
        super(JTabbedPane.TOP);

        // Prepare components
        compConfig = new ConfigPanel();
        compConfig.setPreferredSize(this.getPreferredSize());
        compResults = new ResultsPanel();

        compConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( evt.getPropertyName().startsWith(EVT_PREFIX) )
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        // Set up tabs
        JScrollPane scrollConfig = new JScrollPane(compConfig,
               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addTab(" Configure ",
                new ImageIcon( ClassLoader.getSystemResource("eu/project/ttc/gui/icons/cog_24x24.png")),
                scrollConfig);
        addTab(" Visualize Spotter results ",
                new ImageIcon( ClassLoader.getSystemResource("eu/project/ttc/gui/icons/eye_24x18.png")),
                compResults);
    }

    //////////////////////////////////////////////////////// TOOLVIEW

    @Override
    public void runStarts() {
        compResults.getResultModel().clear();
        compResults.setEnabled(false);
    }

    @Override
    public void runEnds() {
        compResults.setEnabled(true);
    }

    //////////////////////////////////////////////////////// SPOTTER BINDINGS

    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.LANGUAGE.getProperty(), listener);
    }

    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.INPUT.getProperty(), listener);
    }

    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.OUTPUT.getProperty(), listener);
    }

    @Override
    public void addTtgDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.TTGHOME.getProperty(), listener);
    }

    @Override
    public void addEnableTsvOutputChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(PRM.ENABLETSV.getProperty(), listener);
    }
    
    @Override
    public void setLanguage(String language) {
        compConfig.setLanguage(language);
    }

    @Override
    public String getLanguage() {
        return compConfig.getLanguage();
    }

    public void setLanguageError(IllegalArgumentException e) {
        compConfig.setLanguageError(e);
    }

    public void unsetLanguageError() {
        compConfig.unsetLanguageError();
    }

    @Override
    public void setInputDirectory(String inputDirectory) {
        compConfig.setInputDirectory(inputDirectory);
    }

    @Override
    public String getInputDirectory() {
        return compConfig.getInputDirectory();
    }

    public void setInputDirectoryError(IllegalArgumentException e) {
        compConfig.setInputDirectoryError(e);
    }

    public void unsetInputDirectoryError() {
        compConfig.unsetInputDirectoryError();
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        compConfig.setOutputDirectory(outputDirectory);
    }

    @Override
    public String getOutputDirectory() {
        return compConfig.getOutputDirectory();
    }

    public void setOutputDirectoryError(IllegalArgumentException e) {
        compConfig.setOutputDirectoryError(e);
    }

    public void unsetOutputDirectoryError() {
        compConfig.unsetOutputDirectoryError();
    }

    @Override
    public void setTreetaggerHome(String treetaggerHome) {
        compConfig.setTreetaggerHome(treetaggerHome);
    }

    @Override
    public String getTreetaggerHome() {
        return compConfig.getTreetaggerHome();
    }

    public void setTreetaggerHomeError(IllegalArgumentException e) {
        compConfig.setTreetaggerHomeError(e);
    }

    public void unsetTreetaggerHomeError() {
        compConfig.unsetTreetaggerHomeError();
    }

    @Override
    public void setEnableTsvOutput(boolean enableTsv) {
        compConfig.setEnableTsvOutput(enableTsv);
    }

    @Override
    public Boolean isEnableTsvOutput() {
        return compConfig.isEnableTsvOutput();
    }
    
    public void setEnableTsvOutputError(Exception e) {
        compConfig.setEnableTsvOutputError(e);        
    }
    
    public void unsetEnableTsvOutputError() {
        compConfig.unsetEnableTsvOutputError();
    }
    
    /**
     * Add a processing result into the ResultsPanel.
     * @param result
     *      a ProcessingResult from the current running that should
     *      be added to the list of ProcessingResult in the ResultsPanel
     */
    public void addProcessingResult(ProcessingResult result) {
        compResults.addResult(result);
    }
}
