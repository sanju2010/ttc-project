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
 * export of the terms bank.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class ConfigPanelExport extends JPanel {

    protected final static String LBL_TSV = "Export in TSV format";
    protected final static String LBL_KEEPVERBS = "Add verbs and adverbs to terminology";
    protected final static String LBL_FILTERRULE = "Rule to filter terms";
    protected final static String LBL_FILTERINGTLD = "Number of terms to be exported";
    protected final static String LBL_FREQUENCYTLD_OCC = "Minimal number of occurrences";
    protected static final String LBL_FREQUENCYTLD_FREQ = "Frequency threshold";
    protected static final String LBL_FREQUENCYTLD_SPEC = "Specificity threshold";
    protected static final String LBL_ASSOCMEASURE = "Association measure";

    // TSV export parameter
    private JLabel lblTSV;
    private JCheckBox cbTSV;
    private JEditorPane epTSV;

    // Keep verbs parameter
    private JLabel lblAddVerbs;
    private JCheckBox cbAddVerbs;
    private JEditorPane epAddVerbs;

    // Filter rule parameter
    private JLabel lblFilterRule;
    private JComboBox cbFilterRule;
    private JEditorPane epFilterRule;

    // Filtering threshold parameter
    private JLabel lblFilteringTld;
    private JSpinner spFilteringTld;
    private JEditorPane epFilteringTld;

    // Association measure parameter
    private JLabel lblAssociationMeasure;
    private JComboBox cbAssociationMeasure;
    private JEditorPane epAssociationMeasure;

    // Frequency threshold parameter
    private JLabel lblFrequencyTld;
    private JSpinner spFrequencyTld;
    private JEditorPane epFrequencyTld;

    private GroupLayout cfgLayout;

    public ConfigPanelExport() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createTSVComponents(pWidth);
        createKeepVerbsComponents(pWidth);
        createFilterRuleComponents(pWidth);
        createAssociationMeasureComponents(pWidth);
        createFilteringTldComponents(pWidth);
        createFrequencyTldComponents(pWidth);
        updateFilterParameters((String) cbFilterRule.getSelectedItem());

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
                        // TSV parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbTSV)
                                .addComponent(lblTSV)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epTSV))
                                // Keep verbs parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addComponent(cbAddVerbs)
                                .addComponent(lblAddVerbs)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epAddVerbs))
                                // Filter rule parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterRule)
                                        .addComponent(cbFilterRule))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFilterRule))
                                // Association measure parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblAssociationMeasure)
                                        .addComponent(cbAssociationMeasure))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epAssociationMeasure))
                                // Filtering threshold parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilteringTld)
                                        .addComponent(spFilteringTld))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFilteringTld))
                                // Frequency threshold parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFrequencyTld)
                                        .addComponent(spFrequencyTld))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFrequencyTld))
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // TSV parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbTSV,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTSV)
                                .addComponent(epTSV))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Keep verbs parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cbAddVerbs,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAddVerbs)
                                .addComponent(epAddVerbs))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Filter rule parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblFilterRule)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbFilterRule,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epFilterRule))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Association measure parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblAssociationMeasure)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbAssociationMeasure,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epAssociationMeasure))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Filtering threshold parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblFilteringTld)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spFilteringTld,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epFilteringTld))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Frequency threshold parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblFrequencyTld)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spFrequencyTld,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epFrequencyTld))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

        add(config, BorderLayout.NORTH);
    }

    /**
     * Create the components related to the TSV export parameter.
     */
    public void createTSVComponents(int preferredWidth) {
        // Label
        lblTSV = new JLabel("<html><b>"+LBL_TSV+"</b></html>");
        lblTSV .setPreferredSize(new Dimension(
                (int) lblTSV.getPreferredSize().getHeight(),
                preferredWidth));

        // Checkbox as it is a boolean
        cbTSV = new JCheckBox();
        cbTSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a TSV change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.TSV.getProperty(),
                        !cbTSV.isSelected(), cbTSV.isSelected());
            }
        });

        // Editor pane to display help
        epTSV = new JEditorPane();
        epTSV.setEditable(false);
        epTSV.setOpaque(false);
        epTSV.setPreferredSize(new Dimension(
                (int) epTSV.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.tsv.html");
            epTSV.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the keep verbs parameter.
     */
    public void createKeepVerbsComponents(int preferredWidth) {
        // Label
        lblAddVerbs = new JLabel("<html><b>"+LBL_KEEPVERBS+"</b></html>");
        lblAddVerbs.setPreferredSize(new Dimension(
                (int) lblAddVerbs.getPreferredSize().getHeight(),
                preferredWidth));

        // Checkbox as it is a boolean
        cbAddVerbs = new JCheckBox();
        cbAddVerbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a keep verb change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.KEEPVERBS.getProperty(),
                        cbAddVerbs.isSelected(), !cbAddVerbs.isSelected());
            }
        });

        // Editor pane to display help
        epAddVerbs = new JEditorPane();
        epAddVerbs.setEditable(false);
        epAddVerbs.setOpaque(false);
        epAddVerbs.setPreferredSize(new Dimension(
                (int) epAddVerbs.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.filterverbs.html");
            epAddVerbs.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the filter rule parameter.
     */
    public void createFilterRuleComponents(int preferredWidth) {
        // Label
        lblFilterRule = new JLabel("<html><b>"+LBL_FILTERRULE+"</b></html>");
        lblFilterRule.setPreferredSize(new Dimension(
                (int) lblFilterRule.getPreferredSize().getHeight(),
                preferredWidth ));

        // Comboxbox as it is a limited list of choices
        cbFilterRule = new JComboBox();
        cbFilterRule.addItem("OccurrenceThreshold");
        cbFilterRule.addItem("FrequencyThreshold");
        cbFilterRule.addItem("SpecificityThreshold");
        cbFilterRule.addItem("TopNByOccurrence");
        cbFilterRule.addItem("TopNByFrequency");
        cbFilterRule.addItem("TopNBySpecificity");
        cbFilterRule.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Detected a filter rule change, fire property change.");
                    firePropertyChange(IndexerBinding.PRM.FILTERRULE.getProperty(), null,
                            cbFilterRule.getSelectedItem());
                    updateFilterParameters((String) cbFilterRule.getSelectedItem());
                }
            }
        });

        // Editor pane to display help
        epFilterRule = new JEditorPane();
        epFilterRule.setEditable(false);
        epFilterRule.setOpaque(false);
        epFilterRule.setPreferredSize(new Dimension(
                (int) epFilterRule.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.filterrule.html");
            epFilterRule.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Reflect the change of filter rule to the configuration parameters.
     *
     * @param selectedRule
     *      the currently selected rule
     */
    private void updateFilterParameters(String selectedRule) {
        if ( "OccurrenceThreshold".equals(selectedRule) ) {
            setOccurenceThresholdMode();
        } else if ( "FrequencyThreshold".equals(selectedRule) ) {
            setFrequencyThresholdMode();
        } else if ( "SpecificityThreshold".equals(selectedRule) ) {
            setSpecificityThresholdMode();
        } else if ( "TopNByOccurrence".equals(selectedRule) ) {
            setTopNByOccurrenceMode();
        } else if ( "TopNByFrequency".equals(selectedRule) ) {
            setTopNByFrequencyMode();
        } else if ( "TopNBySpecificity".equals(selectedRule) ) {
            setTopNBySpecificityMode();
        } else {
            // Unknown filter rule... show everything...
            lblFilteringTld.setVisible(true);
            lblFrequencyTld.setVisible(true);
            lblAssociationMeasure.setVisible(true);
            spFrequencyTld.setVisible(true);
            spFilteringTld.setVisible(true);
            cbAssociationMeasure.setVisible(true);
            epFilteringTld.setVisible(true);
            epFrequencyTld.setVisible(true);
            epAssociationMeasure.setVisible(true);
        }
    }

    /**
     * Mode when the TopNByOccurrence filter is chosen.
     */
    private void setTopNByOccurrenceMode() {
        // Only display filtering threshold
        lblFilteringTld.setText("<html><b>"+LBL_FILTERINGTLD+"</b></html>");
        lblFilteringTld.setVisible(true);
        spFilteringTld.setVisible(true);
        epFilteringTld.setVisible(true);

        // ... hide the rest
        lblAssociationMeasure.setVisible(false);
        cbAssociationMeasure.setVisible(false);
        epAssociationMeasure.setVisible(false);
        lblFrequencyTld.setVisible(false);
        spFrequencyTld.setVisible(false);
        epFrequencyTld.setVisible(false);
    }

    /**
     * Mode when the TopNByFrequency filter is chosen.
     */
    private void setTopNByFrequencyMode() {
        // Only display filtering threshold
        lblFilteringTld.setText("<html><b>"+LBL_FILTERINGTLD+"</b></html>");
        lblFilteringTld.setVisible(true);
        spFilteringTld.setVisible(true);
        epFilteringTld.setVisible(true);

        // ... hide the rest
        lblAssociationMeasure.setVisible(false);
        cbAssociationMeasure.setVisible(false);
        epAssociationMeasure.setVisible(false);
        lblFrequencyTld.setVisible(false);
        spFrequencyTld.setVisible(false);
        epFrequencyTld.setVisible(false);
    }

    /**
     * Mode when the TopNBySpecificity filter is chosen.
     */
    private void setTopNBySpecificityMode() {
        // Only display filtering threshold and association measure
        lblFilteringTld.setText("<html><b>"+LBL_FILTERINGTLD+"</b></html>");
        lblFilteringTld.setVisible(true);
        spFilteringTld.setVisible(true);
        epFilteringTld.setVisible(true);
        lblAssociationMeasure.setVisible(true);
        cbAssociationMeasure.setVisible(true);
        epAssociationMeasure.setVisible(true);

        // ... hide the rest
        lblFrequencyTld.setVisible(false);
        spFrequencyTld.setVisible(false);
        epFrequencyTld.setVisible(false);
    }

    /**
     * Mode when the OccurrenceThreshold filter is chosen.
     */
    private void setOccurenceThresholdMode() {
        // Only display frequency threshold
        lblFrequencyTld.setText("<html><b>"+LBL_FREQUENCYTLD_OCC+"</b></html>");
        lblFrequencyTld.setVisible(true);
        spFrequencyTld.setVisible(true);
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.frequencytld.occurrence.html");
            epFrequencyTld.setPage(res);
        } catch (IOException e){} // No help
        epFrequencyTld.setVisible(true);

        // ... hide the rest
        lblFilteringTld.setVisible(false);
        spFilteringTld.setVisible(false);
        epFilteringTld.setVisible(false);
        lblAssociationMeasure.setVisible(false);
        cbAssociationMeasure.setVisible(false);
        epAssociationMeasure.setVisible(false);
    }

    /**
     * Mode when the FrequencyThreshold filter is chosen.
     */
    private void setFrequencyThresholdMode() {
        // Only display frequency threshold
        lblFrequencyTld.setText("<html><b>"+LBL_FREQUENCYTLD_FREQ+"</b></html>");
        lblFrequencyTld.setVisible(true);
        spFrequencyTld.setVisible(true);
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.frequencytld.frequency.html");
            epFrequencyTld.setPage(res);
        } catch (IOException e){} // No help
        epFrequencyTld.setVisible(true);

        // ... hide the rest
        lblFilteringTld.setVisible(false);
        spFilteringTld.setVisible(false);
        epFilteringTld.setVisible(false);
        lblAssociationMeasure.setVisible(false);
        cbAssociationMeasure.setVisible(false);
        epAssociationMeasure.setVisible(false);
    }

    /**
     * Mode when the SpecificityThreshold filter is chosen.
     */
    private void setSpecificityThresholdMode() {
        // Only display frequency threshold and association measure
        lblFrequencyTld.setText("<html><b>"+LBL_FREQUENCYTLD_SPEC+"</b></html>");
        lblFrequencyTld.setVisible(true);
        spFrequencyTld.setVisible(true);
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.frequencytld.specificity.html");
            epFilteringTld.setPage(res);
        } catch (IOException e){} // No help
        epFrequencyTld.setVisible(true);

        lblAssociationMeasure.setVisible(true);
        cbAssociationMeasure.setVisible(true);
        epAssociationMeasure.setVisible(true);

        // ... hide the rest
        lblFilteringTld.setVisible(false);
        spFilteringTld.setVisible(false);
        epFilteringTld.setVisible(false);
    }

    /**
     * Create the components related to the filtering threshold parameter.
     */
    public void createFilteringTldComponents(int preferredWidth) {
        // Label
        lblFilteringTld = new JLabel("<html><b>"+LBL_FILTERINGTLD+"</b></html>");
        lblFilteringTld.setPreferredSize(new Dimension(
                (int) lblFilteringTld.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spFilteringTld = new JSpinner(
                new SpinnerNumberModel(new Float(0.5f), new Float(0f), new Float(Float.MAX_VALUE), new Float(0.05f)));
        spFilteringTld.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected a filtering threshold change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.FILTERINGTLD.getProperty(), null,
                        spFilteringTld.getValue());
            }
        });

        // Editor pane to display help
        epFilteringTld = new JEditorPane();
        epFilteringTld.setEditable(false);
        epFilteringTld.setOpaque(false);
        epFilteringTld.setPreferredSize(new Dimension(
                (int) epFilteringTld.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.filteringtld.html");
            epFilteringTld.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the association measure parameter.
     */
    public void createAssociationMeasureComponents(int preferredWidth) {
        // Label
        lblAssociationMeasure = new JLabel("<html><b>"+LBL_ASSOCMEASURE+"</b></html>");
        lblAssociationMeasure.setPreferredSize(new Dimension(
                (int) lblAssociationMeasure.getPreferredSize().getHeight(),
                preferredWidth ));

        // Comboxbox as it is a limited list of choices
        cbAssociationMeasure = new JComboBox();
        cbAssociationMeasure.addItem(new ClassItem(
                "eu.project.ttc.metrics.LogLikelihood", "Log likelyhood"));
        cbAssociationMeasure.addItem(new ClassItem(
                "eu.project.ttc.metrics.MutualInformation", "Mutual information"));
        cbAssociationMeasure.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Detected an association measurechange, fire property change.");
                    firePropertyChange(IndexerBinding.PRM.ASSOCIATIONMEASURE.getProperty(), null,
                            cbAssociationMeasure.getSelectedItem());
                }
            }
        });

        // Editor pane to display help
        epAssociationMeasure = new JEditorPane();
        epAssociationMeasure.setEditable(false);
        epAssociationMeasure.setOpaque(false);
        epAssociationMeasure.setPreferredSize(new Dimension(
                (int) epAssociationMeasure.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.associationmeasure.html");
            epAssociationMeasure.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the frequency threshold parameter.
     */
    public void createFrequencyTldComponents(int preferredWidth) {
        // Label
        lblFrequencyTld = new JLabel("<html><b>"+LBL_FREQUENCYTLD_OCC+"</b></html>");
        lblFrequencyTld.setPreferredSize(new Dimension(
                (int) lblFrequencyTld.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spFrequencyTld = new JSpinner(
                new SpinnerNumberModel(new Float(1f), new Float(0f), new Float(1f), new Float(0.05)));
        spFrequencyTld.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected a frequency threshold change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.FREQUENCYTLD.getProperty(), null,
                        spFrequencyTld.getValue());
            }
        });

        // Editor pane to display help
        epFrequencyTld = new JEditorPane();
        epFrequencyTld.setEditable(false);
        epFrequencyTld.setOpaque(false);
        epFrequencyTld.setPreferredSize(new Dimension(
                (int) epFrequencyTld.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.frequencytld.html");
            epFrequencyTld.setPage(res);
        } catch (IOException e){} // No help
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setTSVExport(boolean tsvExport) {
        cbTSV.setSelected(tsvExport);
    }
    public void setTSVExportError(Throwable e) {
        lblTSV.setText("<html><b>"+LBL_TSV+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetTSVExportError() {
        lblTSV.setText("<html><b>"+LBL_TSV+"</b></html>");
    }
    public boolean isTSVExport() {
        return cbTSV.isSelected();
    }

    public void setKeepVerbs(Boolean keepVerbs) {
        cbAddVerbs.setSelected(keepVerbs);
    }
    public void setKeepVerbsError(Throwable e) {
        lblAddVerbs.setText("<html><b>" + LBL_KEEPVERBS + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetKeepVerbsError() {
        lblAddVerbs.setText("<html><b>" + LBL_KEEPVERBS + "</b></html>");
    }
    public Boolean isKeepVerbs() {
        return cbAddVerbs.isSelected();
    }

    public void setFilterRule(String filterRule) {
        for(int i=0 ; i < cbFilterRule.getItemCount() ; i++) {
            String item = (String) cbFilterRule.getItemAt(i);
            if ( item.equals(filterRule) ) {
                if (cbFilterRule.getSelectedIndex() != i) {
                    cbFilterRule.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + filterRule + "' as I do not handle this value.");
    }
    public void setFilterRuleError(Throwable e) {
        lblFilterRule.setText("<html><b>"+LBL_FILTERRULE+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetFilterRuleError() {
        lblFilterRule.setText("<html><b>"+LBL_FILTERRULE+"</b></html>");
    }
    public String getFilterRule() {
        return (String) cbFilterRule.getSelectedItem();
    }

    public void setFrequencyThreshold(Float frequencyThreshold) {
        // FIXME check values
        spFrequencyTld.setValue(frequencyThreshold);
    }
    public void setFrequencyThresholdError(Throwable e) {
        lblFrequencyTld.setText("<html><b>"+getFrequencyThresholdLbl()+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetFrequencyThresholdError() {
        lblFrequencyTld.setText("<html><b>"+getFrequencyThresholdLbl()+"</b></html>");
    }
    public Float getFrequencyThreshold() {
        return (Float) spFrequencyTld.getValue();
    }
    private String getFrequencyThresholdLbl() {
        if( "OccurrenceThreshold".equals(getFilterRule()) ) {
            return LBL_FREQUENCYTLD_OCC;
        } else if ( "FrequencyThreshold".equals(getFilterRule()) ) {
            return LBL_FREQUENCYTLD_FREQ;
        } else if ( "SpecificityThreshold".equals(getFilterRule()) ) {
            return LBL_FREQUENCYTLD_SPEC;
        } else  {
            return "NA";
        }
    }

    public void setAssociationMeasure(String associationMeasure) {
        for(int i=0 ; i < cbAssociationMeasure.getItemCount() ; i++) {
            ClassItem item = (ClassItem) cbAssociationMeasure.getItemAt(i);
            if ( item.getClassName().equals(associationMeasure) ) {
                if (cbAssociationMeasure.getSelectedIndex() != i) {
                    cbAssociationMeasure.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + associationMeasure + "' as I do not handle this value.");
    }
    public void setAssociationMeasureError(Throwable e) {
        lblAssociationMeasure.setText("<html><b>"+LBL_ASSOCMEASURE+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetAssociationMeasureError() {
        lblAssociationMeasure.setText("<html><b>"+LBL_ASSOCMEASURE+"</b></html>");
    }
    public String getAssociationMeasure() {
        return ((ClassItem) cbAssociationMeasure.getSelectedItem()).getClassName();
    }


    public void setFilteringThreshold(Float filteringThreshold) {
        // FIXME check values
        spFilteringTld.setValue(filteringThreshold);
    }
    public void setFilteringThresholdError(Throwable e) {
        lblFilteringTld.setText("<html><b>"+LBL_FILTERINGTLD+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetFilteringThresholdError() {
        lblFilteringTld.setText("<html><b>"+LBL_FILTERINGTLD+"</b></html>");
    }
    public Float getFilteringThreshold() {
        return (Float) spFilteringTld.getValue();
    }
}
