package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;

import fr.free.rocheteau.jerome.dunamis.fields.BooleanField;
import fr.free.rocheteau.jerome.dunamis.fields.Field;
import fr.free.rocheteau.jerome.dunamis.fields.FieldFactory;

public class SettingViewer {

	private final HashMap<String, Field> fields = new HashMap<String, Field>();

	public List<Field> getFields() {
		return new ArrayList<Field>(fields.values());
	}

	private GridBagConstraints constraint;

	private JPanel component;

	private void setComponent() {
		this.component = new JPanel();
		this.component.setLayout(new GridBagLayout());
		this.constraint = new GridBagConstraints();
		this.constraint.fill = GridBagConstraints.VERTICAL;
		this.constraint.anchor = GridBagConstraints.LINE_START;
		this.constraint.gridx = 0;

	}

	public JPanel getComponent() {
		return this.component;
	}

	public SettingViewer() {
		this.setComponent();
	}

	public void update(ResourceMetaData metadata,
			CasProcessorConfigurationParameterSettings overrides) {

		// These panels will contain parameter groups, if any
		createGroupPanels();
		HashMap<String, JPanel> groups = new HashMap<String, JPanel>();
		JPanel grpPanel;

		this.constraint.gridwidth = GridBagConstraints.REMAINDER;

		// Extra layout constraints for groups
		boolean hasGroups = !groupHeads.isEmpty();
		if (hasGroups) {
			this.constraint.fill = GridBagConstraints.HORIZONTAL;
			this.constraint.ipadx = 1;
		}

		fields.clear();

		ConfigurationParameterDeclarations declarations = metadata
				.getConfigurationParameterDeclarations();
		ConfigurationParameter[] parameters = declarations
				.getConfigurationParameters();
		ConfigurationParameterSettings settings = metadata
				.getConfigurationParameterSettings();

		if (parameters == null || settings == null)
			return;

		for (int index = 0; index < parameters.length; index++) {
			String name = parameters[index].getName();
			String type = parameters[index].getType();
			boolean multi = parameters[index].isMultiValued();
			Object value = settings.getParameterValue(name);
			Object override = overrides.getParameterValue(name);
			String description = parameters[index].getDescription();
			if (override != null) {
				value = override;
			}

			Field field = FieldFactory.getComponent(type, name, multi, value,
					description);
			String fieldGroup = getGroup(name);
			if (field != null) {

				this.fields.put(name, field);

				// If field is in no group
				if (fieldGroup == null) {
					this.getComponent().add(field.getComponent(), this.constraint);

				} else {

					// Add group if necessary
					grpPanel = getGroupPanel(fieldGroup);
					if (!groups.containsKey(fieldGroup)) {
						groups.put(fieldGroup, grpPanel);
						this.getComponent().add(grpPanel, this.constraint);
					}

					// Add panel to its group
					grpPanel.add(field.getComponent());
				}

			}
		}

		setListeners();
	}

	public void update(ResourceMetaData metadata) {

		createGroupPanels();
		HashMap<String, JPanel> groups = new HashMap<String, JPanel>();
		JPanel grpPanel;

		this.constraint.gridwidth = GridBagConstraints.REMAINDER;

		// Extra layout constraints for groups
		boolean hasGroups = !groupHeads.isEmpty();
		if (hasGroups) {
			this.constraint.fill = GridBagConstraints.HORIZONTAL;
			this.constraint.ipadx = 0;
		}

		fields.clear();

		ConfigurationParameterDeclarations declarations = metadata
				.getConfigurationParameterDeclarations();
		ConfigurationParameter[] parameters = declarations
				.getConfigurationParameters();
		ConfigurationParameterSettings settings = metadata
				.getConfigurationParameterSettings();

		if (parameters == null || settings == null)
			return;

		for (int index = 0; index < parameters.length; index++) {
			String name = parameters[index].getName();
			String type = parameters[index].getType();
			boolean multi = parameters[index].isMultiValued();
			Object value = settings.getParameterValue(name);
			String description = parameters[index].getDescription();

			Field field = FieldFactory.getComponent(type, name, multi, value, description);
			String fieldGroup = getGroup(name);
			if (field != null) {

				this.fields.put(name, field);

				// If field is in no group
				if (fieldGroup == null) {

					this.getComponent().add(field.getComponent(),
							this.constraint);

				} else {

					// Add group if necessary
					grpPanel = getGroupPanel(fieldGroup);
					if (!groups.containsKey(fieldGroup)) {
						groups.put(fieldGroup, grpPanel);
						this.getComponent().add(grpPanel, this.constraint);
					}

					// Add panel to its group
					grpPanel.add(field.getComponent());
				}
			}

		}
		setListeners();
	}

	// ////////////////////////////////////////////////////////////////////////
	// Button group support

	/**
	 * Adds a logical button group. I.e. buttons in the group can only be
	 * selected one at time.
	 * 
	 * @param buttons
	 *            The name of the buttons in the group.
	 */
	public void addButtonGroup(String... buttons) {
		Field buttonField;
		ButtonGroup group = new ButtonGroup();
		for (String button : buttons) {
			buttonField = fields.get(button);
			if (buttonField instanceof BooleanField) {
				group.add(((BooleanField) buttonField).getComponent());
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// Parameter groups support

	/** Stores field name -> group name associations */
	private final HashMap<String, String> groupMembers = new HashMap<String, String>();

	/** Stores groupe name -> head name associations */
	private final HashMap<String, String> groupHeads = new HashMap<String, String>();

	/** Stores head name -> fields associations */
	private final HashMap<String, String[]> headFields = new HashMap<String, String[]>();

	/** Stores group panels */
	private HashMap<String, JPanel> groupPanels;

	/**
	 * Adds a graphic parameter group.
	 * 
	 * @param label
	 *            The group name.
	 * @param headName
	 *            The name of the first field in the group.
	 * @param disableIfHeadFalse
	 *            If <code>true</code>, all fields in the group will have their
	 *            status changed as the value of the head changes.
	 * @param fieldNames
	 *            The name of the other fields in the group.
	 */
	public void addParameterGroup(String label, String headName,
			boolean disableIfHeadFalse, String... fieldNames) {

		for (String field : fieldNames) {
			groupMembers.put(field, label);
		}

		groupHeads.put(headName, label);
		headFields.put(headName, fieldNames);
	}

	/**
	 * Creates the map that associates group name to {@link JPanel}s.
	 * 
	 * @return The group/panel map
	 */
	private HashMap<String, JPanel> createGroupPanels() {
		if (groupPanels != null)
			return groupPanels;

		groupPanels = new HashMap<String, JPanel>();
		for (String group : groupHeads.values())
			groupPanels.put(group, createGroupPanel(group));

		return groupPanels;
	}

	/**
	 * Creates the {@link JPanel} component of the specified group.
	 * 
	 * @param groupLabel
	 *            The group label
	 * @return A {@link JPanel} instance
	 */
	private JPanel createGroupPanel(String groupLabel) {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder(groupLabel));
		GridLayout gl = new GridLayout(0, 1);
		jp.setLayout(gl);
		return jp;
	}

	/**
	 * Returns the panel of the specified group.
	 * 
	 * @param groupLabel
	 *            The group label
	 * @return The {@link JPanel} component of the specified group.
	 */
	private JPanel getGroupPanel(String groupLabel) {
		return groupPanels.get(groupLabel);
	}

	/**
	 * Returns the group the specified field <code>name</code> belongs to.
	 * 
	 * @param name
	 *            The field name
	 * @return The group the specified field <code>name</code> belongs to.
	 */
	private String getGroup(String name) {
		String grp = groupHeads.get(name);
		return grp == null ? groupMembers.get(name) : grp;
	}

	// ////////////////////////////////////////////////////////////////////////
	// Head listeners

	/**
	 * Sets up listeners for group head fields. If the field is boolean, group
	 * fields will be disabled if head is <code>false</code>.
	 */
	private void setListeners() {
		Field headField;
		for (String head : groupHeads.keySet()) {
			headField = fields.get(head);
			if (headField instanceof BooleanField) {
				addDisableListener((JCheckBox) headField.getComponent(),
						headFields.get(head));
			}
		}

	}

	/**
	 * Adds a listener to the specified <code>checkbox</code>, that
	 * enable/disable the given <code>fields</code>.
	 * 
	 * @param checkbox
	 *            The checkbox to listen to
	 * @param fields
	 *            The fields to enable/disable
	 */
	private void addDisableListener(final JCheckBox checkbox,
			final String[] fields) {
		checkbox.setSelected(!checkbox.isSelected());
		checkbox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				Field field;
				for (String f : fields) {
					field = SettingViewer.this.fields.get(f);
					JComponent jc = (JComponent) field.getComponent();
					jc.setEnabled(checkbox.isSelected());
					for (Component c : jc.getComponents()) {
						c.setEnabled(checkbox.isSelected());
					}
				}
			}
		});
		checkbox.setSelected(!checkbox.isSelected());
	}
}
