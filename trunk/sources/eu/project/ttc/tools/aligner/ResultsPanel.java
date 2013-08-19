// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.aligner;

import eu.project.ttc.tools.indexer.TermsBankViewer;
import org.apache.uima.UIMAFramework;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author grdscarabe
 * @date 16/08/13
 */
public class ResultsPanel extends JPanel {

    private JPanel toolbarGUI;
    private TermsBankViewer bankerGUI;

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

        toolbarGUI.add(new Label("You can visualize the result of the processing below..."));
    }

    public void addCasToBanker(JCas jCas) {
        bankerGUI.doLoad(jCas);
    }

    public void clearBanker() {
        // FIXME
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
