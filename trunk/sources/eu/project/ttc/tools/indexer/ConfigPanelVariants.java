// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;

/**
 * This JPanel exposes the configuration of the Indexer tool regarding the
 * variant detection.
 * These configuration parameters are used to combine terms together.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class ConfigPanelVariants extends JPanel {

    // Ignore diacritics parameter
    private JLabel lblIgnoreDiacritics;
    private JCheckBox cbIgnoreDiacritics;
    private JEditorPane epIgnoreDiacritics;

    // Variant detection parameter
    private JLabel lblVariantDetection;
    private JCheckBox cbVariantDetection;
    private JEditorPane epVariantDetection;

    // Edit distance class parameter
    private JLabel lblEditDistanceClass;
    private JComboBox cbEditDistanceClass;
    private JEditorPane epEditDistanceClass;

    // Edit distance threshold parameter
    private JLabel lblEditDistanceTld;
    private JSpinner spEditDistanceTld;
    private JEditorPane epEditDistanceTld;

    // Edit distance ngrams parameter
    private JLabel lblEditDistanceNgrams;
    private JSpinner spEditDistanceNgrams;
    private JEditorPane epEditDistanceNgrams;

    private GroupLayout cfgLayout;

    public ConfigPanelVariants() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createIgnoreDiacriticsComponents(pWidth);
        createVariantDetectionComponents(pWidth);
        createEditDistanceClassComponents(pWidth);
        createEditDistanceThresholdComponents(pWidth);
        createEditDistanceNgramsComponents(pWidth);
        toggleVariantDetectionParameters(cbVariantDetection.isSelected());

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
                        // Ignore diacritics parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbIgnoreDiacritics)
                                .addComponent(lblIgnoreDiacritics)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epIgnoreDiacritics))
                                // Variant detection parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbVariantDetection)
                                .addComponent(lblVariantDetection)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epVariantDetection))
                                // Edit distance class parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblEditDistanceClass)
                                        .addComponent(cbEditDistanceClass))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epEditDistanceClass))
                                // Edit distance threshold parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblEditDistanceTld)
                                        .addComponent(spEditDistanceTld))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epEditDistanceTld))
                                // Edit distance ngrams parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblEditDistanceNgrams)
                                        .addComponent(spEditDistanceNgrams))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epEditDistanceNgrams))
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // Ignore diacritics parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbIgnoreDiacritics,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIgnoreDiacritics)
                                .addComponent(epIgnoreDiacritics))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Variant detection parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbVariantDetection,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblVariantDetection)
                                .addComponent(epVariantDetection))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Edit distance class parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblEditDistanceClass)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbEditDistanceClass,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epEditDistanceClass))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Edit distance threshold parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblEditDistanceTld)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spEditDistanceTld,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epEditDistanceTld))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Edit distance ngrams parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblEditDistanceNgrams)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spEditDistanceNgrams,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epEditDistanceNgrams))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

        add(config, BorderLayout.NORTH);
    }

    /**
     * Create the components related to the ignore diacritics parameter.
     */
    public void createIgnoreDiacriticsComponents(int preferredWidth) {
        // Label
        lblIgnoreDiacritics = new JLabel("<html><b>Group graphical variants</b></html>");
        lblIgnoreDiacritics.setPreferredSize(new Dimension(
                (int) lblIgnoreDiacritics.getPreferredSize().getHeight(),
                preferredWidth ));

        // Checkbox as it is a boolean
        cbIgnoreDiacritics = new JCheckBox();
        cbIgnoreDiacritics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a ignore diacritics change, fire property change.");
                firePropertyChange(IndexerBinding.CFG.IGNOREDIACRITICS.getProperty(),
                        !cbIgnoreDiacritics.isSelected(), cbIgnoreDiacritics.isSelected());
            }
        });

        // Editor pane to display help
        epIgnoreDiacritics = new JEditorPane();
        epIgnoreDiacritics .setEditable(false);
        epIgnoreDiacritics .setOpaque(false);
        epIgnoreDiacritics .setPreferredSize(new Dimension(
                (int) epIgnoreDiacritics .getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.ignorediacritics.html");
            epIgnoreDiacritics.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the variant detection parameter.
     */
    public void createVariantDetectionComponents(int preferredWidth) {
        // Label
        lblVariantDetection = new JLabel("<html><b>Variant detection</b></html>");
        lblVariantDetection.setPreferredSize(new Dimension(
                (int) lblVariantDetection.getPreferredSize().getHeight(),
                preferredWidth ));

        // Checkbox as it is a boolean
        cbVariantDetection = new JCheckBox();
        cbVariantDetection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a variant detection change, fire property change.");
                firePropertyChange(IndexerBinding.CFG.VARIANTDETECTION.getProperty(),
                        !cbVariantDetection.isSelected(), cbVariantDetection.isSelected());
                toggleVariantDetectionParameters(cbVariantDetection.isSelected());
            }
        });

        // Editor pane to display help
        epVariantDetection = new JEditorPane();
        epVariantDetection .setEditable(false);
        epVariantDetection .setOpaque(false);
        epVariantDetection .setPreferredSize(new Dimension(
                (int) epVariantDetection .getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.variantdetection.html");
            epVariantDetection.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Method responsible for enabling or disabling the variant detection
     * parameters depending or whether or not the variant detection is
     * activated.
     *
     * @param variantDetectionEnabled
     *      flag indicating the status of the variant detection
     */
    private void toggleVariantDetectionParameters(boolean variantDetectionEnabled) {
        // Toggle edit distance class
        lblEditDistanceClass.setEnabled(variantDetectionEnabled);
        lblEditDistanceClass.setVisible(variantDetectionEnabled);
        cbEditDistanceClass.setEnabled(variantDetectionEnabled);
        cbEditDistanceClass.setVisible(variantDetectionEnabled);
        epEditDistanceClass.setEnabled(variantDetectionEnabled);
        epEditDistanceClass.setVisible(variantDetectionEnabled);
        // Toggle edit distance threshold
        lblEditDistanceTld.setEnabled(variantDetectionEnabled);
        lblEditDistanceTld.setVisible(variantDetectionEnabled);
        spEditDistanceTld.setEnabled(variantDetectionEnabled);
        spEditDistanceTld.setVisible(variantDetectionEnabled);
        epEditDistanceTld.setEnabled(variantDetectionEnabled);
        epEditDistanceTld.setVisible(variantDetectionEnabled);
        // Toggle edit distance ngrams
        lblEditDistanceNgrams.setEnabled(variantDetectionEnabled);
        lblEditDistanceNgrams.setVisible(variantDetectionEnabled);
        spEditDistanceNgrams.setEnabled(variantDetectionEnabled);
        spEditDistanceNgrams.setVisible(variantDetectionEnabled);
        epEditDistanceNgrams.setEnabled(variantDetectionEnabled);
        epEditDistanceNgrams.setVisible(variantDetectionEnabled);
    }

    /**
     * Create the components related to the edit distance class parameter.
     */
    public void createEditDistanceClassComponents(int preferredWidth) {
        // Label
        lblEditDistanceClass = new JLabel("<html><b>Edit distance</b></html>");
        lblEditDistanceClass.setPreferredSize(new Dimension(
                (int) lblEditDistanceClass.getPreferredSize().getHeight(),
                preferredWidth ));

        // Comboxbox as it is a limited list of choices
        cbEditDistanceClass = new JComboBox();
        cbEditDistanceClass.addItem( new EditDistanceClassItem(
                "eu.project.ttc.metrics.Levenshtein",
                "Levenshtein") );
        cbEditDistanceClass.addItem( new EditDistanceClassItem(
                "eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein",
                "Diacritic insensitive Levenshtein") );
        cbEditDistanceClass.addItem( new EditDistanceClassItem(
                "eu.project.ttc.metrics.LongestCommonSubsequence",
                "Longest common subsequence") );
        cbEditDistanceClass.setPreferredSize(new Dimension(
                (int) cbEditDistanceClass.getPreferredSize().getHeight(),
                preferredWidth ));
        cbEditDistanceClass.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    EditDistanceClassItem item = (EditDistanceClassItem) e.getItem();
                    System.out.println("Detected a edit distance class change, fire property change.");
                    firePropertyChange(IndexerBinding.CFG.EDITDISTANCECLS.getProperty(), null, item.getClassName());
                }
            }
        });

        // Editor pane to display help
        epEditDistanceClass = new JEditorPane();
        epEditDistanceClass .setEditable(false);
        epEditDistanceClass .setOpaque(false);
        epEditDistanceClass .setPreferredSize(new Dimension(
                (int) epEditDistanceClass .getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.editdistancecls.html");
            epEditDistanceClass.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the edit distance threshold parameter.
     */
    public void createEditDistanceThresholdComponents(int preferredWidth) {
        // Label
        lblEditDistanceTld = new JLabel("<html><b>Edit distance threshold</b></html>");
        lblEditDistanceTld.setPreferredSize(new Dimension(
                (int) lblEditDistanceTld.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spEditDistanceTld = new JSpinner(
                new SpinnerNumberModel(0.5d, 0d, 1d, 0.05));
        spEditDistanceTld.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected a edit distance threshold change, fire property change.");
                firePropertyChange(IndexerBinding.CFG.EDITDISTANCETLD.getProperty(), null,
                        spEditDistanceTld.getValue());
            }
        });

        // Editor pane to display help
        epEditDistanceTld = new JEditorPane();
        epEditDistanceTld .setEditable(false);
        epEditDistanceTld .setOpaque(false);
        epEditDistanceTld .setPreferredSize(new Dimension(
                (int) epEditDistanceTld .getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.editdistancetld.html");
            epEditDistanceTld.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the edit distance ngrams parameter.
     */
    public void createEditDistanceNgramsComponents(int preferredWidth) {
        // Label
        lblEditDistanceNgrams = new JLabel("<html><b>Edit distance ngrams</b></html>");
        lblEditDistanceNgrams.setPreferredSize(new Dimension(
                (int) lblEditDistanceNgrams.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spEditDistanceNgrams = new JSpinner(
                new SpinnerNumberModel(3, 1, 100, 1));
        spEditDistanceNgrams.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected a edit distance ngrams change, fire property change.");
                firePropertyChange(IndexerBinding.CFG.EDITDISTANCENGRAMS.getProperty(), null,
                        spEditDistanceNgrams.getValue());
            }
        });

        // Editor pane to display help
        epEditDistanceNgrams = new JEditorPane();
        epEditDistanceNgrams .setEditable(false);
        epEditDistanceNgrams .setOpaque(false);
        epEditDistanceNgrams .setPreferredSize(new Dimension(
                (int) epEditDistanceNgrams .getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.editdistancengrams.html");
            epEditDistanceNgrams.setPage(res);
        } catch (IOException e){} // No help
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setIgnoreDiacritics(boolean ignoreDiacritics) {
        cbIgnoreDiacritics.setSelected(ignoreDiacritics);
    }

    public void setIgnoreDiacriticsError(IllegalArgumentException e) {
        lblIgnoreDiacritics.setText("<html><b>Group graphical variants</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetIgnoreDiacriticsError() {
        lblIgnoreDiacritics.setText("<html><b>Group graphical variants</b></html>");
    }

    public boolean isIgnoreDiacritics() {
        return cbIgnoreDiacritics.isSelected();
    }

    public void setVariantDetection(boolean variantDetection) {
        cbVariantDetection.setSelected(variantDetection);
    }

    public void setVariantDetectionError(IllegalArgumentException e) {
        lblVariantDetection.setText("<html><b>Variant detection</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetVariantDetectionError() {
        lblVariantDetection.setText("<html><b>Variant detection</b></html>");
    }

    public boolean isVariantDetection() {
        return cbVariantDetection.isSelected();
    }

    public void setEditDistanceClass(String className) {
        for(int i=0 ; i < cbEditDistanceClass.getItemCount() ; i++) {
            EditDistanceClassItem item = (EditDistanceClassItem) cbEditDistanceClass.getItemAt(i);
            if ( item.getClassName().equals(className) ) {
                if (cbEditDistanceClass.getSelectedIndex() != i) {
                    cbEditDistanceClass.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + className + "' as I do not handle this value.");
    }

    public void setEditDistanceClassError(IllegalArgumentException e) {
        lblEditDistanceClass.setText("<html><b>Edit distance</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetEditDistanceClassError() {
        lblEditDistanceClass.setText("<html><b>Edit distance</b></html>");
    }

    public String getEditDistanceClass() {
        return ((EditDistanceClassItem) cbEditDistanceClass.getSelectedItem()).getClassName();
    }

    public void setEditDistanceTld(Float threshold) {
        // FIXME handle bad values ?
        spEditDistanceTld.setValue(threshold);
    }

    public void setEditDistanceTldError(IllegalArgumentException e) {
        lblEditDistanceTld.setText("<html><b>Edit distance threshold</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetEditDistanceTldError() {
        lblEditDistanceTld.setText("<html><b>Edit distance threshold</b></html>");
    }

    public Float getEditDistanceTld() {
        return (Float) spEditDistanceTld.getValue();
    }

    public void setEditDistanceNgrams(int size) {
        // FIXME handle bad values ?
        spEditDistanceNgrams.setValue(size);
    }

    public void setEditDistanceNgramsError(IllegalArgumentException e) {
        lblEditDistanceNgrams.setText("<html><b>Edit distance ngrams</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetEditDistanceNgramsError() {
        lblEditDistanceNgrams.setText("<html><b>Edit distance ngrams</b></html>");
    }

    public int getEditDistanceNgrams() {
        return (Integer) spEditDistanceNgrams.getValue();
    }

}
