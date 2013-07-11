// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.ToolView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Main view of the Spotter tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class SpotterView extends JTabbedPane implements ToolView {

    public SpotterView() {
        super(JTabbedPane.TOP);

        // Prepare components
        ConfigPanel compConfig = new ConfigPanel();
        compConfig.setPreferredSize(this.getPreferredSize());
        ResultsPanel compResults = new ResultsPanel();

        // Set up tabs
        JScrollPane scrollConfig = new JScrollPane(compConfig,
               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addTab(" Configure ", scrollConfig);
        addTab(" Processing Results ", compResults);
    }

    public void modelPropertyChange(PropertyChangeEvent evt) {
        // TODO route to a change in the right field
    }

//    public void update(ResourceMetaData metadata,
//                       CasProcessorConfigurationParameterSettings overrides) {
//
//        // These panels will contain parameter groups, if any
//        createGroupPanels();
//        HashMap<String, JPanel> groups = new HashMap<String, JPanel>();
//        JPanel grpPanel;
//
//        this.constraint.gridwidth = GridBagConstraints.REMAINDER;
//
//        // Extra layout constraints for groups
//        boolean hasGroups = !groupHeads.isEmpty();
//        if (hasGroups) {
//            this.constraint.fill = GridBagConstraints.HORIZONTAL;
//            this.constraint.ipadx = 1;
//        }
//
//        fields.clear();
//
//        ConfigurationParameterDeclarations declarations = metadata
//                .getConfigurationParameterDeclarations();
//        ConfigurationParameter[] parameters = declarations
//                .getConfigurationParameters();
//        ConfigurationParameterSettings settings = metadata
//                .getConfigurationParameterSettings();
//
//        if (parameters == null || settings == null)
//            return;
//
//        for (int index = 0; index < parameters.length; index++) {
//            String name = parameters[index].getName();
//            String type = parameters[index].getType();
//            boolean multi = parameters[index].isMultiValued();
//            Object value = settings.getParameterValue(name);
//            Object override = overrides.getParameterValue(name);
//            String description = parameters[index].getDescription();
//            if (override != null) {
//                value = override;
//            }
//
//            Field field = FieldFactory.getComponent(type, name, multi, value,
//                    description);
//            String fieldGroup = getGroup(name);
//            if (field != null) {
//
//                this.fields.put(name, field);
//
//                // If field is in no group
//                if (fieldGroup == null) {
//                    this.getComponent().add(field.getComponent(), this.constraint);
//
//                } else {
//
//                    // Add group if necessary
//                    grpPanel = getGroupPanel(fieldGroup);
//                    if (!groups.containsKey(fieldGroup)) {
//                        groups.put(fieldGroup, grpPanel);
//                        this.getComponent().add(grpPanel, this.constraint);
//                    }
//
//                    // Add panel to its group
//                    grpPanel.add(field.getComponent());
//                }
//
//            }
//        }
//
//        setListeners();
//    }
//
//    public void update(ResourceMetaData metadata) {
//
//        createGroupPanels();
//        HashMap<String, JPanel> groups = new HashMap<String, JPanel>();
//        JPanel grpPanel;
//
//        this.constraint.gridwidth = GridBagConstraints.REMAINDER;
//
//        // Extra layout constraints for groups
//        boolean hasGroups = !groupHeads.isEmpty();
//        if (hasGroups) {
//            this.constraint.fill = GridBagConstraints.HORIZONTAL;
//            this.constraint.ipadx = 0;
//        }
//
//        fields.clear();
//
//        ConfigurationParameterDeclarations declarations = metadata
//                .getConfigurationParameterDeclarations();
//        ConfigurationParameter[] parameters = declarations
//                .getConfigurationParameters();
//        ConfigurationParameterSettings settings = metadata
//                .getConfigurationParameterSettings();
//
//        if (parameters == null || settings == null)
//            return;
//
//        for (int index = 0; index < parameters.length; index++) {
//            String name = parameters[index].getName();
//            String type = parameters[index].getType();
//            boolean multi = parameters[index].isMultiValued();
//            Object value = settings.getParameterValue(name);
//            String description = parameters[index].getDescription();
//
//            Field field = FieldFactory.getComponent(type, name, multi, value, description);
//            String fieldGroup = getGroup(name);
//            if (field != null) {
//
//                this.fields.put(name, field);
//
//                // If field is in no group
//                if (fieldGroup == null) {
//
//                    this.getComponent().add(field.getComponent(),
//                            this.constraint);
//
//                } else {
//
//                    // Add group if necessary
//                    grpPanel = getGroupPanel(fieldGroup);
//                    if (!groups.containsKey(fieldGroup)) {
//                        groups.put(fieldGroup, grpPanel);
//                        this.getComponent().add(grpPanel, this.constraint);
//                    }
//
//                    // Add panel to its group
//                    grpPanel.add(field.getComponent());
//                }
//            }
//
//        }
//        setListeners();
//    }
//
//    // ////////////////////////////////////////////////////////////////////////
//    // Button group support
//
//    /**
//     * Adds a logical button group. I.e. buttons in the group can only be
//     * selected one at time.
//     *
//     * @param buttons
//     *            The name of the buttons in the group.
//     */
//    public void addButtonGroup(String... buttons) {
//        Field buttonField;
//        ButtonGroup group = new ButtonGroup();
//        for (String button : buttons) {
//            buttonField = fields.get(button);
//            if (buttonField instanceof BooleanField) {
//                group.add(((BooleanField) buttonField).getComponent());
//            }
//        }
//    }
//
//    // ////////////////////////////////////////////////////////////////////////
//    // Parameter groups support
//
//    /** Stores field name -> group name associations */
//    private final HashMap<String, String> groupMembers = new HashMap<String, String>();
//
//    /** Stores groupe name -> head name associations */
//    private final HashMap<String, String> groupHeads = new HashMap<String, String>();
//
//    /** Stores head name -> fields associations */
//    private final HashMap<String, String[]> headFields = new HashMap<String, String[]>();
//
//    /** Stores group panels */
//    private HashMap<String, JPanel> groupPanels;
//
//    /**
//     * Adds a graphic parameter group.
//     *
//     * @param label
//     *            The group name.
//     * @param headName
//     *            The name of the first field in the group.
//     * @param disableIfHeadFalse
//     *            If <code>true</code>, all fields in the group will have their
//     *            status changed as the value of the head changes.
//     * @param fieldNames
//     *            The name of the other fields in the group.
//     */
//    public void addParameterGroup(String label, String headName,
//                                  boolean disableIfHeadFalse, String... fieldNames) {
//
//        for (String field : fieldNames) {
//            groupMembers.put(field, label);
//        }
//
//        groupHeads.put(headName, label);
//        headFields.put(headName, fieldNames);
//    }
//
//    /**
//     * Creates the map that associates group name to {@link javax.swing.JPanel}s.
//     *
//     * @return The group/panel map
//     */
//    private HashMap<String, JPanel> createGroupPanels() {
//        if (groupPanels != null)
//            return groupPanels;
//
//        groupPanels = new HashMap<String, JPanel>();
//        for (String group : groupHeads.values())
//            groupPanels.put(group, createGroupPanel(group));
//
//        return groupPanels;
//    }
//
//    /**
//     * Creates the {@link javax.swing.JPanel} component of the specified group.
//     *
//     * @param groupLabel
//     *            The group label
//     * @return A {@link javax.swing.JPanel} instance
//     */
//    private JPanel createGroupPanel(String groupLabel) {
//        JPanel jp = new JPanel();
//        jp.setBorder(BorderFactory.createTitledBorder(groupLabel));
//        GridLayout gl = new GridLayout(0, 1);
//        jp.setLayout(gl);
//        return jp;
//    }
//
//    /**
//     * Returns the panel of the specified group.
//     *
//     * @param groupLabel
//     *            The group label
//     * @return The {@link javax.swing.JPanel} component of the specified group.
//     */
//    private JPanel getGroupPanel(String groupLabel) {
//        return groupPanels.get(groupLabel);
//    }
//
//    /**
//     * Returns the group the specified field <code>name</code> belongs to.
//     *
//     * @param name
//     *            The field name
//     * @return The group the specified field <code>name</code> belongs to.
//     */
//    private String getGroup(String name) {
//        String grp = groupHeads.get(name);
//        return grp == null ? groupMembers.get(name) : grp;
//    }
//
//    // ////////////////////////////////////////////////////////////////////////
//    // Head listeners
//
//    /**
//     * Sets up listeners for group head fields. If the field is boolean, group
//     * fields will be disabled if head is <code>false</code>.
//     */
//    private void setListeners() {
//        Field headField;
//        for (String head : groupHeads.keySet()) {
//            headField = fields.get(head);
//            if (headField instanceof BooleanField) {
//                addDisableListener((JCheckBox) headField.getComponent(),
//                        headFields.get(head));
//            }
//        }
//
//    }
//
//    /**
//     * Adds a listener to the specified <code>checkbox</code>, that
//     * enable/disable the given <code>fields</code>.
//     *
//     * @param checkbox
//     *            The checkbox to listen to
//     * @param fields
//     *            The fields to enable/disable
//     */
//    private void addDisableListener(final JCheckBox checkbox,
//                                    final String[] fields) {
//        checkbox.setSelected(!checkbox.isSelected());
//        checkbox.addItemListener(new ItemListener() {
//
//            public void itemStateChanged(ItemEvent e) {
//                Field field;
//                for (String f : fields) {
//                    field = eu.project.ttc.tools.config.SettingViewer.this.fields.get(f);
//                    JComponent jc = (JComponent) field.getComponent();
//                    jc.setEnabled(checkbox.isSelected());
//                    for (Component c : jc.getComponents()) {
//                        c.setEnabled(checkbox.isSelected());
//                    }
//                }
//            }
//        });
//        checkbox.setSelected(!checkbox.isSelected());
//    }

//    /**
//     * Group all the available parameters in a logic way so they are presented to the used
//     * in a logic manner.
//     */
//    private void groupParameters() {
//        // Default group of parameters with all parameters
//        GroupOfParameters defaultGrp = new GroupOfParameters("Settings", "TO BE DESCRIBED");
//        defaultGrp.addParameter(pLang);
//        defaultGrp.addParameter(pIDir);
//        defaultGrp.addParameter(pODir);
//        defaultGrp.addParameter(pTtg);
//        // Set
//        groupsOfParameters = new GroupOfParameters[]{defaultGrp};
//    }
}
