// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.aligner;

import eu.project.ttc.tools.commons.TTCFileChooser;
import eu.project.ttc.tools.indexer.ClassItem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This JPanel exposes the basic configuration part of the Indexer tool.
 * That is only the parameters common to every tool and nothing specific
 * so to keep it really simple.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class ConfigPanelAdvanced extends JPanel {

    private static final String LBL_DICTIONARY = "Bilingual dictionary";
    private static final String LBL_COMPOSITIONAL = "Compositional method";
    private static final String LBL_DISTRIBUTIONAL = "Distributional method";
    private static final String LBL_MAXCANDIDATES = "Max number of translation candidates";
    private static final String LBL_SIMILARITY = "Similarity distance";

    // Bilingual dictionary parameter
    private JLabel lblDictionary;
    private JEditorPane epDictionary;
    private TTCFileChooser fcDictionary;

    // Compositional method parameter
    private JLabel lblCompositional;
    private JCheckBox cbCompositional;
    private JEditorPane epCompositional;

    // Distributional method parameter
    private JLabel lblDistributional;
    private JCheckBox cbDistributional;
    private JEditorPane epDistributional;

    // Max candidates parameter
    private JLabel lblMaxCandidates;
    private JSpinner spMaxCandidates;
    private JEditorPane epMaxCandidates;

    // Similarity distance parameter
    private JLabel lblSimilarity;
    private JComboBox cbSimilarity;
    private JEditorPane epSimilarity;

    private GroupLayout cfgLayout;

    public ConfigPanelAdvanced() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createMaxCandidatesComponents(pWidth);
        createDictionaryComponents(pWidth);
        createMethodComponents(pWidth);
        createSimilarityComponents(pWidth);

        // Layout the components
        layoutComponents();
    }

    /**
     * This HUGE method is responsible to layout the panel by grouping
     * together parameters labels, fields and descriptions.
     *
     * Yes, this is quite massive and it looks messy, but I didn't find
     * a better way to do it as there are side effects regarding the
     * group layout.
     * So please, do not intend to change things unless you're sure you
     * plainly understand the group layout!
     */
    private void layoutComponents() {
        JPanel config = new JPanel();

        // Init the group layout
        cfgLayout = new GroupLayout(config);
        config.setLayout(cfgLayout);

        // Configure the horizontal layout
        cfgLayout.setHorizontalGroup(
                cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        // Max candidates parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblMaxCandidates)
                                        .addComponent(spMaxCandidates))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epMaxCandidates))
                        // Compositional parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbCompositional)
                                .addComponent(lblCompositional)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epCompositional))
                        // Distributional parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbDistributional)
                                .addComponent(lblDistributional)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epDistributional))
                        // Dictionary parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDictionary)
                                        .addComponent(fcDictionary))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epDictionary))
                        // Similarity parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSimilarity)
                                        .addComponent(cbSimilarity))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epSimilarity))
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // Max candidates parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblMaxCandidates)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spMaxCandidates,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epMaxCandidates))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Compositional parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbCompositional,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblCompositional)
                                .addComponent(epCompositional))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Distributional parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbDistributional,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDistributional)
                                .addComponent(epDistributional))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Dictionary parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblDictionary)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcDictionary,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epDictionary))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Similarity parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblSimilarity)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbSimilarity,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epSimilarity))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

        add(config, BorderLayout.NORTH);
    }

    /**
     * Create the components related to the bilingual dictionary parameter.
     */
    public void createDictionaryComponents(int preferredWidth) {
        // Dictionary label
        lblDictionary = new JLabel("<html><b>" + LBL_DICTIONARY + "</b></html>");
        lblDictionary.setPreferredSize(new Dimension(
                (int) lblDictionary.getPreferredSize().getHeight(),
                preferredWidth ));

        // Input directory field
        fcDictionary = new TTCFileChooser("Choose the evaluation directory");
        fcDictionary.setPreferredSize(new Dimension(
                (int) fcDictionary.getPreferredSize().getHeight(),
                preferredWidth ));
        fcDictionary.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(AlignerBinding.PRM.DICTIONARY.getProperty(),
                            evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help panel
        epDictionary = new JEditorPane();
        epDictionary.setEditable(false);
        epDictionary.setOpaque(false);
        epDictionary.setPreferredSize( new Dimension(
                (int) epDictionary.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.dictionary.html");
            epDictionary.setPage(res);
        } catch (IOException e) {} // No help
    }

    /**
     * Create the components related to the compositional parameter.
     */
    private void createMethodComponents(int preferredWidth) {
        // Label
        lblCompositional = new JLabel("<html><b>" + LBL_COMPOSITIONAL + "</b></html>");
        lblCompositional.setPreferredSize(new Dimension(
                (int) lblCompositional.getPreferredSize().getHeight(),
                preferredWidth ));
        lblDistributional = new JLabel("<html><b>" + LBL_DISTRIBUTIONAL + "</b></html>");
        lblDistributional.setPreferredSize(new Dimension(
                (int) lblDistributional.getPreferredSize().getHeight(),
                preferredWidth ));

        // Value field
        cbCompositional = new JCheckBox();
        cbCompositional.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firePropertyChange(AlignerBinding.PRM.COMPOSITIONAL.getProperty(),
                        !cbCompositional.isSelected(), cbCompositional.isSelected());
            }
        });
        cbDistributional = new JCheckBox();
        cbDistributional.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firePropertyChange(AlignerBinding.PRM.DISTRIBUTIONAL.getProperty(),
                        !cbDistributional.isSelected(), cbDistributional.isSelected());
            }
        });

        // Help panel
        epCompositional = new JEditorPane();
        epCompositional.setEditable(false);
        epCompositional.setOpaque(false);
        epCompositional.setPreferredSize( new Dimension(
                (int) epCompositional.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.compositional.html");
            epCompositional.setPage(res);
        } catch (IOException e) {} // No help
        epDistributional = new JEditorPane();
        epDistributional.setEditable(false);
        epDistributional.setOpaque(false);
        epDistributional.setPreferredSize( new Dimension(
                (int) epDistributional.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.distributional.html");
            epDistributional.setPage(res);
        } catch (IOException e) {} // No help
    }

    /**
     * Create the components related to the max candidates parameter.
     */
    public void createMaxCandidatesComponents(int preferredWidth) {
        // Label
        lblMaxCandidates = new JLabel("<html><b>" + LBL_MAXCANDIDATES + "</b></html>");
        lblMaxCandidates.setPreferredSize(new Dimension(
                (int) lblMaxCandidates.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spMaxCandidates = new JSpinner(
                new SpinnerNumberModel(10, 1d, Short.MAX_VALUE, 5));
        spMaxCandidates.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                firePropertyChange(AlignerBinding.PRM.MAXCANDIDATES.getProperty(), null,
                        spMaxCandidates.getValue());
            }
        });

        // Editor pane to display help
        epMaxCandidates = new JEditorPane();
        epMaxCandidates.setEditable(false);
        epMaxCandidates.setOpaque(false);
        epMaxCandidates.setPreferredSize(new Dimension(
                (int) epMaxCandidates.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.maxcandidates.html");
            epMaxCandidates.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the distance parameter.
     */
    private void createSimilarityComponents(int preferredWidth) {
        // Label
        lblSimilarity = new JLabel("<html><b>" + LBL_SIMILARITY + "</b></html>");
        lblSimilarity.setPreferredSize(new Dimension(
                (int) lblSimilarity.getPreferredSize().getHeight(),
                preferredWidth ));

        // Comboxbox as it is a limited list of choices
        cbSimilarity = new JComboBox();
        cbSimilarity.addItem(new ClassItem(
                "eu.project.ttc.metrics.Jaccard", "Jaccard"));
        cbSimilarity.addItem(new ClassItem(
                "eu.project.ttc.metrics.Cosine", "Cosine"));
        cbSimilarity.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    firePropertyChange(AlignerBinding.PRM.SIMILARITY.getProperty(), null,
                            cbSimilarity.getSelectedItem());
                }
            }
        });

        // Editor pane to display help
        epSimilarity = new JEditorPane();
        epSimilarity.setEditable(false);
        epSimilarity.setOpaque(false);
        epSimilarity.setPreferredSize(new Dimension(
                (int) epSimilarity.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.similarity.html");
            epSimilarity.setPage(res);
            epSimilarity.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    try {
                        if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        }
                    } catch (Exception exc) {} // ignore
                }
            });
        } catch (IOException e) {} // No help
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setBilingualDictionary(String bilingualDictionary) {
        fcDictionary.setPath(bilingualDictionary);
    }
    public String getBilingualDictionary() {
        return fcDictionary.getPath();
    }
    public void setBilingualDictionaryError(Throwable e) {
        lblDictionary.setText("<html><b>" + LBL_DICTIONARY + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetBilingualDictionaryError() {
        lblDictionary.setText("<html><b>" + LBL_DICTIONARY + "</b></html>");
    }

    public void setCompositionalMethod(boolean isCompositionalMethod) {
        cbCompositional.setSelected(isCompositionalMethod);
    }
    public Boolean isCompositionalMethod() {
        return cbCompositional.isSelected();
    }
    public void setCompositionalMethodError(Throwable e) {
        lblCompositional.setText("<html><b>" + LBL_COMPOSITIONAL + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetCompositionalMethodError() {
        lblCompositional.setText("<html><b>" + LBL_COMPOSITIONAL + "</b></html>");
    }

    public void setDistributionalMethod(boolean isDistributionalMethod) {
        cbDistributional.setSelected(isDistributionalMethod);
    }
    public Boolean isDistributionalMethod() {
        return cbDistributional.isSelected();
    }
    public void setDistributionalMethodError(Throwable e) {
        lblDistributional.setText("<html><b>" + LBL_DISTRIBUTIONAL + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetDistributionalMethodError() {
        lblDistributional.setText("<html><b>" + LBL_DISTRIBUTIONAL + "</b></html>");
    }

    public void setMaxCandidates(Integer maxCandidates) {
        spMaxCandidates.setValue(maxCandidates);
    }
    public Integer getMaxCandidates() {
        return (Integer) spMaxCandidates.getValue();
    }
    public void setMaxCandidatesError(Throwable e) {
        lblMaxCandidates.setText("<html><b>" + LBL_MAXCANDIDATES + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetMaxCandidatesError() {
        lblMaxCandidates.setText("<html><b>" + LBL_MAXCANDIDATES + "</b></html>");
    }

    public void setSimilarityDistanceClass(String similarityDistanceClass) {
        for(int i=0 ; i < cbSimilarity.getItemCount() ; i++) {
            ClassItem item = (ClassItem) cbSimilarity.getItemAt(i);
            if ( item.getClassName().equals(similarityDistanceClass) ) {
                if (cbSimilarity.getSelectedIndex() != i) {
                    cbSimilarity.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + similarityDistanceClass + "' as I do not handle this value.");
    }
    public String getSimilarityDistanceClass() {
        return ((ClassItem) cbSimilarity.getSelectedItem()).getClassName();
    }
    public void setSimilarityDistanceClassError(Throwable e) {
        lblSimilarity.setText("<html><b>" + LBL_SIMILARITY + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetSimilarityDistanceClassError() {
        lblSimilarity.setText("<html><b>" + LBL_SIMILARITY + "</b></html>");
    }

}
