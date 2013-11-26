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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This JPanel exposes the configuration of the Indexer tool regarding the
 * export of the terms bank.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
@SuppressWarnings("serial")
public class ConfigPanelExport extends JPanel {

    protected final static String LBL_TSV = "Export in TSV format";
    protected final static String LBL_KEEPVERBS = "Add verbs and adverbs to terminology";
    protected final static String LBL_OCCURRENCETLD = "Occurrence threshold";
    protected final static String LBL_FILTERRULE = "Filter and Sort terms by";
    protected final static String LBL_FREQUENCYTLD_OCC = "Minimal number of occurrences";
    protected static final String LBL_FREQUENCYTLD_FREQ = "Frequency threshold";
    protected static final String LBL_FREQUENCYTLD_SPEC = "Specificity threshold";
    protected final static String LBL_FILTERINGTLD_TOPOCC = "Number of terms to be exported";
    protected final static String LBL_FILTERINGTLD_TOPFREQ = "Number of terms to be exported";
    protected final static String LBL_FILTERINGTLD_TOPSPEC = "Number of terms to be exported";

    // TSV export parameter
    private JLabel lblTSV;
    private JCheckBox cbTSV;
    private JEditorPane epTSV;

    // Keep verbs parameter
    private JLabel lblKeepVerbs;
    private JCheckBox cbKeepVerbs;
    private JEditorPane epKeepVerbs;

    // Filter rule parameter
    private JLabel lblFilterRule;
    private JComboBox<String> cbFilterRule;
    private JEditorPane epFilterRule;

    // Filtering threshold parameter
    private JLabel lblFilteringTld;
    private JSpinner spFilteringTld;
    private JEditorPane epFilteringTld;

    // Occurrence threshold parameter
    private JLabel lblOccurrenceTld;
    private JSpinner spOccurrenceTld;
    private JEditorPane epOccurrenceTld;

    private GroupLayout cfgLayout;

    /** A model for frequency and or specificity, not bound. Although frequency is [0, 1], specificity might not. */
    // FIXME Check specificity boundaries
    private final SpinnerNumberModel floatSpinnerModel = new SpinnerNumberModel(new Float(0.5f), new Float(0f), new Float(Math.log(Float.MAX_VALUE)), new Float(0.05f));
    
    /** A model for step by 1 integer values */
    private final SpinnerNumberModel integerSpinnerModel = new SpinnerNumberModel(new Integer(3), new Integer(1), new Integer(Integer.MAX_VALUE), new Integer(1));
    
    /** A model for step by 5 integer values */
    private final SpinnerNumberModel topNSpinnerModel = new SpinnerNumberModel(new Integer(50), new Integer(1), new Integer(Integer.MAX_VALUE), new Integer(5));

    public ConfigPanelExport() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createTSVComponents(pWidth);
        createKeepVerbsComponents(pWidth);
        createFilterRuleComponents(pWidth);
        createFilteringTldComponents(pWidth);
        createOccurrenceTldComponents(pWidth);
        updateFilterParameters();

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
                                .addComponent(cbKeepVerbs)
                                .addComponent(lblKeepVerbs)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epKeepVerbs))
                                // Occurrence threshold parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblOccurrenceTld)
                                        .addComponent(spOccurrenceTld))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epOccurrenceTld))
                                // Filter rule parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterRule)
                                        .addComponent(cbFilterRule))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFilterRule))
                                // Filtering threshold parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilteringTld)
                                        .addComponent(spFilteringTld))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epFilteringTld))
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
                                .addComponent(cbKeepVerbs,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblKeepVerbs)
                                .addComponent(epKeepVerbs))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Occurrence threshold parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblOccurrenceTld)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spOccurrenceTld,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epOccurrenceTld))
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
        lblKeepVerbs = new JLabel("<html><b>"+LBL_KEEPVERBS+"</b></html>");
        lblKeepVerbs.setPreferredSize(new Dimension(
                (int) lblKeepVerbs.getPreferredSize().getHeight(),
                preferredWidth));

        // Checkbox as it is a boolean
        cbKeepVerbs = new JCheckBox();
        cbKeepVerbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Detected a keep verb change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.KEEPVERBS.getProperty(),
                        !cbKeepVerbs.isSelected(), cbKeepVerbs.isSelected());
            }
        });

        // Editor pane to display help
        epKeepVerbs = new JEditorPane();
        epKeepVerbs.setEditable(false);
        epKeepVerbs.setOpaque(false);
        epKeepVerbs.setPreferredSize(new Dimension(
                (int) epKeepVerbs.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.filterverbs.html");
            epKeepVerbs.setPage(res);
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
        cbFilterRule = new JComboBox<String>();
        cbFilterRule.addItem("None");
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
                    updateFilterParameters();
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
     */
    private void updateFilterParameters() {
        IndexerBinding.FilterRules filterRule = IndexerBinding.FilterRules.valueOf(getFilterRule());
        final Number oldValue = (Number) spFilteringTld.getModel().getValue();
        switch(filterRule) {
            case OccurrenceThreshold:
			updateFilterModel(integerSpinnerModel, new Integer(oldValue.intValue()),
					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.occurrence.html");
                return;
            case FrequencyThreshold:
            	updateFilterModel(floatSpinnerModel, new Float(oldValue.floatValue()),
    					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.frequency.html");
                return;
            case SpecificityThreshold:
            	updateFilterModel(floatSpinnerModel, new Float(oldValue.floatValue()),
    					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.specificity.html");
                return;
            case TopNByOccurrence:
            	updateFilterModel(topNSpinnerModel, new Integer(oldValue.intValue()),
    					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.topoccurrence.html");
                return;
            case TopNByFrequency:
            	updateFilterModel(topNSpinnerModel, new Integer(oldValue.intValue()),
    					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.topfrequency.html");
                return;
            case TopNBySpecificity:
            	updateFilterModel(topNSpinnerModel, new Integer(oldValue.intValue()),
    					"/eu/project/ttc/gui/texts/indexer/param.filteringtld.topspecificity.html");
                return;
            case None:
            default:
                lblFilteringTld.setVisible(false);
                spFilteringTld.setVisible(false);
                epFilteringTld.setVisible(false);
                return;
        }
    }

    /**
     * Create the components related to the filtering threshold parameter.
     */
    public void createFilteringTldComponents(int preferredWidth) {
        // Label
        lblFilteringTld = new JLabel("<html><b>No filtering rule specified</b></html>");
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
     * Create the components related to the occurrence threshold parameter.
     */
    public void createOccurrenceTldComponents(int preferredWidth) {
        // Label
        lblOccurrenceTld = new JLabel("<html><b>"+LBL_OCCURRENCETLD+"</b></html>");
        lblOccurrenceTld.setPreferredSize(new Dimension(
                (int) lblOccurrenceTld.getPreferredSize().getHeight(),
                preferredWidth ));

        // Spinner as it is an incremental value
        spOccurrenceTld = new JSpinner(
                new SpinnerNumberModel(new Integer(3), new Integer(1), new Integer(Integer.MAX_VALUE), new Integer(1)));
        spOccurrenceTld.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Detected an occurrence threshold change, fire property change.");
                firePropertyChange(IndexerBinding.PRM.OCCURRENCETLD.getProperty(), null,
                        spOccurrenceTld.getValue());
            }
        });

        // Editor pane to display help
        epOccurrenceTld = new JEditorPane();
        epOccurrenceTld.setEditable(false);
        epOccurrenceTld.setOpaque(false);
        epOccurrenceTld.setPreferredSize(new Dimension(
                (int) epOccurrenceTld.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.occurrencetld.html");
            epOccurrenceTld.setPage(res);
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
        cbKeepVerbs.setSelected(keepVerbs);
    }
    public void setKeepVerbsError(Throwable e) {
        lblKeepVerbs.setText("<html><b>" + LBL_KEEPVERBS + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetKeepVerbsError() {
        lblKeepVerbs.setText("<html><b>" + LBL_KEEPVERBS + "</b></html>");
    }
    public Boolean isKeepVerbs() {
        return cbKeepVerbs.isSelected();
    }

    public void setFilterRule(String filterRule) {
        for(int i=0 ; i < cbFilterRule.getItemCount() ; i++) {
            String item = cbFilterRule.getItemAt(i);
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

    public void setOccurrenceThreshold(Integer frequencyThreshold) {
        // FIXME check values
        spOccurrenceTld.setValue(frequencyThreshold);
    }
    public void setOccurrenceThresholdError(Throwable e) {
        lblOccurrenceTld.setText("<html><b>" + LBL_OCCURRENCETLD + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetOccurrenceThresholdError() {
        lblOccurrenceTld.setText("<html><b>" + LBL_OCCURRENCETLD + "</b></html>");
    }
    public Integer getOccurrenceThreshold() {
        return (Integer) spOccurrenceTld.getValue();
    }

    public void setFilteringThreshold(Float filteringThreshold) {
        // FIXME check values
        spFilteringTld.setValue(filteringThreshold);
    }
    public void setFilteringThresholdError(Throwable e) {
        lblFilteringTld.setText("<html><b>"+getFilteringThresholdLbl()+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetFilteringThresholdError() {
        lblFilteringTld.setText("<html><b>"+getFilteringThresholdLbl()+"</b></html>");
    }
    public Float getFilteringThreshold() {
        if ( spFilteringTld.getValue() instanceof Integer )
            return new Float((Integer) spFilteringTld.getValue());
        else
            return (Float) spFilteringTld.getValue();
    }
    private String getFilteringThresholdLbl() {
        IndexerBinding.FilterRules filterRule = IndexerBinding.FilterRules.valueOf(getFilterRule());
        switch (filterRule) {
            case OccurrenceThreshold:
                return LBL_FREQUENCYTLD_OCC;
            case FrequencyThreshold:
                return LBL_FREQUENCYTLD_FREQ;
            case SpecificityThreshold:
                return LBL_FREQUENCYTLD_SPEC;
            case TopNByFrequency:
                return LBL_FILTERINGTLD_TOPFREQ;
            case TopNByOccurrence:
                return LBL_FILTERINGTLD_TOPOCC;
            case TopNBySpecificity:
                return LBL_FILTERINGTLD_TOPSPEC;
            default:
                return "NA";
        }
    }
    
    //////////////////////////////////////////////////////////////////// Helpers
    
    private void updateFilterModel(SpinnerNumberModel newModel, Number oldValue, String labelResource) {
    	lblFilteringTld.setText("<html><b>"+getFilteringThresholdLbl()+"</b></html>");
        lblFilteringTld.setVisible(true);
        newModel.setValue(oldValue);
        spFilteringTld.setModel(newModel);
        spFilteringTld.setVisible(true);
        try {
            URL res = getClass().getResource(labelResource);
            epFilteringTld.setPage(res);
            epFilteringTld.setVisible(true);
        } catch (IOException e){} // No help
    }
}
