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

    protected final static String LBL_FLEXIONNALVARIANTS = "Detect flexionnal variants";
    protected final static String LBL_GRAPHICALVARIANTS  = "Detect graphical variants";
    protected final static String LBL_MISPELLINGVARIANTS = "Detect mispelling variants";

    // Flexional variants parameter
    private JLabel lblFlexionnalVariants;
    private JCheckBox cbFlexionnalVariants;
    private JEditorPane epFlexionnalVariants;

    // Ignore diacritics parameter
    private JLabel lblGraphicalVariants;
    private JCheckBox cbGraphicalVariants;
    private JEditorPane epGraphicalVariants;

    // Variant detection parameter
    private JLabel lblMispellingVariants;
    private JCheckBox cbMispellingVariants;
    private JEditorPane epMispellingVariants;

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
        createFlexionnalVariantsComponents(pWidth);
        createIgnoreDiacriticsComponents(pWidth);
        createVariantDetectionComponents(pWidth);
        createEditDistanceClassComponents(pWidth);
        createEditDistanceThresholdComponents(pWidth);
        createEditDistanceNgramsComponents(pWidth);
        toggleVariantDetectionParameters(cbMispellingVariants.isSelected());

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
                        // Flexionnal variants parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbFlexionnalVariants)
                                .addComponent(lblFlexionnalVariants)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFlexionnalVariants))
                        // Ignore diacritics parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbGraphicalVariants)
                                .addComponent(lblGraphicalVariants)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epGraphicalVariants))
                                // Variant detection parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbMispellingVariants)
                                .addComponent(lblMispellingVariants)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epMispellingVariants))
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
                        // Flexionnal variants parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbFlexionnalVariants,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFlexionnalVariants)
                                .addComponent(epFlexionnalVariants))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Ignore diacritics parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbGraphicalVariants,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblGraphicalVariants)
                                .addComponent(epGraphicalVariants))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Variant detection parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbMispellingVariants,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMispellingVariants)
                                .addComponent(epMispellingVariants))
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
    public void createFlexionnalVariantsComponents(int preferredWidth) {
        // Label
        lblFlexionnalVariants = new JLabel("<html><b>"+LBL_FLEXIONNALVARIANTS+"</b></html>");
        lblFlexionnalVariants.setPreferredSize(new Dimension(
                (int) lblFlexionnalVariants.getPreferredSize().getHeight(),
                preferredWidth ));

        // Checkbox as it is a boolean
        cbFlexionnalVariants = new JCheckBox();
        cbFlexionnalVariants.setSelected(true);
        cbFlexionnalVariants.setEnabled(false); // no binding, it is by default

        // Editor pane to display help
        epFlexionnalVariants = new JEditorPane();
        epFlexionnalVariants.setEditable(false);
        epFlexionnalVariants.setOpaque(false);
        epFlexionnalVariants.setPreferredSize(new Dimension(
                (int) epFlexionnalVariants.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.flexionnalvariants.html");
            epFlexionnalVariants.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the ignore diacritics parameter.
     */
    public void createIgnoreDiacriticsComponents(int preferredWidth) {
        // Label
        lblGraphicalVariants = new JLabel("<html><b>"+LBL_GRAPHICALVARIANTS+"</b></html>");
        lblGraphicalVariants.setPreferredSize(new Dimension(
                (int) lblGraphicalVariants.getPreferredSize().getHeight(),
                preferredWidth ));

        // Checkbox as it is a boolean
        cbGraphicalVariants = new JCheckBox();
        cbGraphicalVariants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a ignore diacritics change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.IGNOREDIACRITICS.getProperty(),
                        !cbGraphicalVariants.isSelected(), cbGraphicalVariants.isSelected());
            }
        });

        // Editor pane to display help
        epGraphicalVariants = new JEditorPane();
        epGraphicalVariants.setEditable(false);
        epGraphicalVariants.setOpaque(false);
        epGraphicalVariants.setPreferredSize(new Dimension(
                (int) epGraphicalVariants.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.graphicalvariants.html");
            epGraphicalVariants.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the variant detection parameter.
     */
    public void createVariantDetectionComponents(int preferredWidth) {
        // Label
        lblMispellingVariants = new JLabel("<html><b>"+LBL_MISPELLINGVARIANTS+"</b></html>");
        lblMispellingVariants.setPreferredSize(new Dimension(
                (int) lblMispellingVariants.getPreferredSize().getHeight(),
                preferredWidth ));

        // Checkbox as it is a boolean
        cbMispellingVariants = new JCheckBox();
        cbMispellingVariants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a variant detection change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.VARIANTDETECTION.getProperty(),
                        !cbMispellingVariants.isSelected(), cbMispellingVariants.isSelected());
                toggleVariantDetectionParameters(cbMispellingVariants.isSelected());
            }
        });

        // Editor pane to display help
        epMispellingVariants = new JEditorPane();
        epMispellingVariants.setEditable(false);
        epMispellingVariants.setOpaque(false);
        epMispellingVariants.setPreferredSize(new Dimension(
                (int) epMispellingVariants.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.mispellingvariants.html");
            epMispellingVariants.setPage(res);
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
        cbEditDistanceClass.addItem( new ClassItem(
                "eu.project.ttc.metrics.Levenshtein",
                "Levenshtein") );
        cbEditDistanceClass.addItem( new ClassItem(
                "eu.project.ttc.metrics.DiacriticInsensitiveLevenshtein",
                "Diacritic insensitive Levenshtein") );
        cbEditDistanceClass.addItem( new ClassItem(
                "eu.project.ttc.metrics.LongestCommonSubsequence",
                "Longest common subsequence") );
        cbEditDistanceClass.setPreferredSize(new Dimension(
                (int) cbEditDistanceClass.getPreferredSize().getHeight(),
                preferredWidth ));
        cbEditDistanceClass.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ClassItem item = (ClassItem) e.getItem();
                    System.out.println("Detected a edit distance class change, fire property change.");
                    firePropertyChange(IndexerBinding.PRM.EDITDISTANCECLS.getProperty(), null, item.getClassName());
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
                new SpinnerNumberModel(new Float(0.5d), new Float(0.d), new Float(1d), new Float(0.05d)));
        spEditDistanceTld.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected a edit distance threshold change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.EDITDISTANCETLD.getProperty(), null,
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
                firePropertyChange(IndexerBinding.PRM.EDITDISTANCENGRAMS.getProperty(), null,
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
        cbGraphicalVariants.setSelected(ignoreDiacritics);
    }

    public void setIgnoreDiacriticsError(Throwable e) {
        lblGraphicalVariants.setText("<html><b>"+LBL_GRAPHICALVARIANTS+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetIgnoreDiacriticsError() {
        lblGraphicalVariants.setText("<html><b>"+LBL_GRAPHICALVARIANTS+"</b></html>");
    }

    public boolean isIgnoreDiacritics() {
        return cbGraphicalVariants.isSelected();
    }

    public void setVariantDetection(boolean variantDetection) {
        cbMispellingVariants.setSelected(variantDetection);
    }

    public void setVariantDetectionError(Throwable e) {
        lblMispellingVariants.setText("<html><b>"+LBL_MISPELLINGVARIANTS+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetVariantDetectionError() {
        lblMispellingVariants.setText("<html><b>"+LBL_MISPELLINGVARIANTS+"</b></html>");
    }

    public boolean isVariantDetection() {
        return cbMispellingVariants.isSelected();
    }

    public void setEditDistanceClass(String className) {
        for(int i=0 ; i < cbEditDistanceClass.getItemCount() ; i++) {
            ClassItem item = (ClassItem) cbEditDistanceClass.getItemAt(i);
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

    public void setEditDistanceClassError(Throwable e) {
        lblEditDistanceClass.setText("<html><b>Edit distance</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetEditDistanceClassError() {
        lblEditDistanceClass.setText("<html><b>Edit distance</b></html>");
    }

    public String getEditDistanceClass() {
        return ((ClassItem) cbEditDistanceClass.getSelectedItem()).getClassName();
    }

    public void setEditDistanceTld(Float threshold) {
        // FIXME handle bad values ?
        spEditDistanceTld.setValue(threshold);
    }

    public void setEditDistanceTldError(Throwable e) {
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

    public void setEditDistanceNgramsError(Throwable e) {
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
