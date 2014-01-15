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

import org.apache.uima.UIMAFramework;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 16/08/13
 */
@SuppressWarnings("serial")
public class ResultsPanel extends JPanel {

    private JPanel toolbarGUI;
    private TermsBankViewer bankerGUI;

    private static final JFileChooser jfc;
    static {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public ResultsPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15 ,15 ,15));

        // Prepare components
        createToolbar();
        createBanker();

        // Assemble the GUI
        add(toolbarGUI);
        add(Box.createRigidArea(new Dimension(0,5)));
        add( new JScrollPane(bankerGUI) );
    }

    private void createBanker() {
        bankerGUI = new TermsBankViewer();

        ActionListener listener = new Listener();
        JPopupMenu menu = new Menu(listener);
        setComponentPopupMenu(menu);
        bankerGUI.setComponentPopupMenu(menu);
        bankerGUI.setInheritsPopupMenu(true);
    }

    private void createToolbar() {
        toolbarGUI = new JPanel();
        toolbarGUI.setLayout(new FlowLayout());

        toolbarGUI.add(new JLabel("<html>You can visualise the results by loading the language-terminology.xmi file.<br/>Press the right mouse button, a load menu will appear. Choose a language-terminology.xmi file.</html>"));
    }

    public void addCasToBanker(JCas jCas) {
        bankerGUI.doLoad(jCas);
    }

    public void clearBanker() {
        bankerGUI.clearData();
    }

    private class Menu extends JPopupMenu {

        /**
         *
         */
        private static final long serialVersionUID = -3988974235600538309L;

        private JMenuItem load;

        private void setLoad() {
            this.load = new JMenuItem("Load from file");
            this.load.setActionCommand("load");
        }

        private JMenuItem sortByName;

        private void setSortByName() {
            this.sortByName = new JMenuItem("Sort by name");
            this.sortByName.setActionCommand("name");
        }

        private JMenuItem sortByFreq;

        private void setSortByFreq() {
            this.sortByFreq = new JMenuItem("Sort by frequency");
            this.sortByFreq.setActionCommand("freq");
        }

        private JMenuItem sortBySpec;

        private void setSortBySpec() {
            this.sortBySpec = new JMenuItem("Sort by specificity");
            this.sortBySpec.setActionCommand("spec");
        }

        public Menu(ActionListener listsner) {
            this.setLoad();
            this.setSortByName();
            this.setSortByFreq();
            this.setSortBySpec();
            this.add(this.load);
            this.addSeparator();
            this.add(this.sortByName);
            this.add(this.sortByFreq);
            this.add(this.sortBySpec);
            this.load.addActionListener(listsner);
            this.sortByName.addActionListener(listsner);
            this.sortByFreq.addActionListener(listsner);
            this.sortBySpec.addActionListener(listsner);
        }

    }


    private class Listener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object object = event.getSource();
            if (object instanceof JMenuItem) {
                JMenuItem source = (JMenuItem) object;
                String action = source.getActionCommand();
                if (action.equals("load")) {
                    int rv = jfc.showOpenDialog(null);
					if (rv == JFileChooser.APPROVE_OPTION) {
                        File file = jfc.getSelectedFile();
						try {
							bankerGUI.doLoad(file);
						} catch (Exception e) {
							UIMAFramework.getLogger().log(Level.SEVERE,
									e.getMessage());
							e.printStackTrace();
						}
					}
                } else if (action.equals("name")) {
                    bankerGUI.sortByName();
                } else if (action.equals("freq")) {
                    bankerGUI.sortByFreq();
                } else if (action.equals("spec")) {
                    bankerGUI.sortBySpec();
                }
            }
        }

    }
}
