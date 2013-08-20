// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.aligner;

import org.apache.uima.jcas.JCas;

import javax.swing.*;
import java.awt.*;

/**
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 20/08/13
 */
public class ResultsPanel extends JPanel {

    private JPanel toolbarGUI;
    private AlignerMixerViewer mixerGUI;

    private static final JFileChooser jfc;
    static {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

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
    public ResultsPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15 ,15 ,15));

        // Prepare components
        createToolbar();
        createMixer();

        // Assemble the GUI
        add(toolbarGUI);
        add(Box.createRigidArea(new Dimension(0,5)));
        add( new JScrollPane(mixerGUI) );
    }

    private void createMixer() {
        mixerGUI = new AlignerMixerViewer();
    }

    private void createToolbar() {
        toolbarGUI = new JPanel();
        toolbarGUI.setLayout(new FlowLayout());
        toolbarGUI.add(new JLabel("You can visualize the result of the processing below..."));
    }

    public void addCasToMixer(JCas jCas) {
        mixerGUI.doLoad(jCas);
    }

    public void clearMixer() {
        // FIXME mixerGUI.getMixer().clear();
    }
}
