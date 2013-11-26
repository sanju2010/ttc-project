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
package eu.project.ttc.tools.aligner;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.uima.jcas.JCas;

/**
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 20/08/13
 */
@SuppressWarnings("serial")
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
        mixerGUI.clear();
    }
}
