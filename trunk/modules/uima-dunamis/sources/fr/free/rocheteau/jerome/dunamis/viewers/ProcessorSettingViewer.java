package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.TitledBorder;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.listeners.ProcessorSettingListener;
import fr.free.rocheteau.jerome.dunamis.models.ProcessorSettingModel;

public class ProcessorSettingViewer {
	
	private final Dimension labelDimension = new Dimension(200,40);
	
	private final Dimension fieldDimension = new Dimension(100,25);
	
	private ProcessorSettingModel model;
	
	public void setModel(ProcessorSettingModel model) { 
		this.model = model;
		try {
			this.setCasPoolSize(model.getCasPoolSize());
			this.setProcessUnitThreadCount(model.getProcessingUnitThreadCount());
			this.setDropOnCasException(model.getDropOnCasException());
		} catch (Exception e) {
			Dunamis.error(e);
		}
	}
	
	public ProcessorSettingModel getModel() {
		return this.model;
	}

	private JLabel dropOnCasExceptionLabel;
	
	private void setDropOnCasExceptionLabel() {
		this.dropOnCasExceptionLabel = new JLabel("Drop on CAS Exception");
		this.dropOnCasExceptionLabel.setPreferredSize(this.labelDimension);
		this.dropOnCasExceptionLabel.setHorizontalAlignment(JTextField.LEFT);
	}

	private JLabel getDropOnCasExceptionLabel() {
		return this.dropOnCasExceptionLabel;
	}

	private JComboBox dropOnCasExceptionField;
	
	private void setDropOnCasExceptionField() {
		String[] values = { "true" , "false" };
		this.dropOnCasExceptionField = new JComboBox(values);
		this.dropOnCasExceptionField.setSelectedIndex(1);
		this.dropOnCasExceptionField.setEditable(false);
		this.dropOnCasExceptionField.setPreferredSize(this.fieldDimension);
		this.dropOnCasExceptionField.setName("DropOnCasException");
	}

	private JComboBox getDropOnCasExceptionField() {
		return this.dropOnCasExceptionField;
	}
	
	private JLabel casPoolSizeLabel;
	
	private void setCasPoolSizeLabel() {
		this.casPoolSizeLabel = new JLabel("CAS Pool Size");
		this.casPoolSizeLabel.setPreferredSize(this.labelDimension);
		this.casPoolSizeLabel.setHorizontalAlignment(JTextField.LEFT);
	}
	
	private JLabel getCasPoolSizeLabel() {
		return this.casPoolSizeLabel;
	}
	
	private JSpinner casPoolSizeField;
	
	private void setCasPoolSizeField() {
		SpinnerModel model = new SpinnerNumberModel(2,1,null,1);
		this.casPoolSizeField = new JSpinner(model);
		NumberEditor editor = new NumberEditor(this.casPoolSizeField);
		this.casPoolSizeField.setEditor(editor);
		editor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
		this.casPoolSizeField.setPreferredSize(this.fieldDimension);
		this.casPoolSizeField.setName("CasPoolSize");
	}
	
	public JSpinner getCasPoolSizeField() {
		return this.casPoolSizeField;
	}
	
	private JLabel processUnitThreadLabel;
	
	private void setProcessUnitThreadLabel() {
		this.processUnitThreadLabel = new JLabel("Process Unit Threads");
		this.processUnitThreadLabel.setPreferredSize(this.labelDimension);
		this.processUnitThreadLabel.setHorizontalAlignment(JTextField.LEFT);
	}
	
	private JLabel getProcessUnitThreadLabel() {
		return this.processUnitThreadLabel;
	}
	
	private JSpinner processUnitThreadField;
	
	private void setProcessUnitThreadField() {
		SpinnerModel model = new SpinnerNumberModel(2,1,null,1);
		this.processUnitThreadField = new JSpinner(model);
		NumberEditor editor = new NumberEditor(this.processUnitThreadField);
		this.processUnitThreadField.setEditor(editor);
		editor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
		this.processUnitThreadField.setPreferredSize(this.fieldDimension);
		this.processUnitThreadField.setName("ProcessUnitThread");
	}
	
	public JSpinner getProcessUnitThreadField() {
		return this.processUnitThreadField;
	}
	
	private JPanel component;

	private void setComponent() {
		this.component = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		TitledBorder border = new TitledBorder("CAS Processor Settings");
		this.component.setBorder(border);
		c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getProcessUnitThreadLabel(),c);
		c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.EAST; 
		this.component.add(this.getProcessUnitThreadField(),c);
		c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getCasPoolSizeLabel(),c);
		c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
		this.component.add(this.getCasPoolSizeField(),c);
		c.gridx = 0; c.gridy = 2; c.anchor = GridBagConstraints.WEST;
		this.component.add(this.getDropOnCasExceptionLabel(),c);
		c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST;
		this.component.add(this.getDropOnCasExceptionField(),c);
	}

	public JPanel getComponent() {
		return this.component;
	}

	private void enableListeners() {
		ProcessorSettingListener listener = new ProcessorSettingListener(this);
		this.getProcessUnitThreadField().addChangeListener(listener);
		this.getCasPoolSizeField().addChangeListener(listener);
	}
	
	public ProcessorSettingViewer() {
		this.setProcessUnitThreadLabel();
		this.setProcessUnitThreadField();
		this.setCasPoolSizeLabel();
		this.setCasPoolSizeField();
		this.setDropOnCasExceptionLabel();
		this.setDropOnCasExceptionField();
		this.setComponent();
		this.enableListeners();	
	}

	public void doEnable(boolean enabled) {
		this.getCasPoolSizeLabel().setEnabled(enabled);
		this.getCasPoolSizeField().setEnabled(enabled);
		this.getProcessUnitThreadLabel().setEnabled(enabled);
		this.getProcessUnitThreadField().setEnabled(enabled);
		this.getDropOnCasExceptionLabel().setEnabled(enabled);
		this.getDropOnCasExceptionField().setEnabled(enabled);
		this.getComponent().setEnabled(enabled);
		this.getComponent().setEnabled(enabled);
	}
	
	public boolean getDropOnCasException() {
		Object object = this.getDropOnCasExceptionField().getSelectedItem();
		if (object instanceof String) {
			String value = (String) object;
			try {
				return Boolean.parseBoolean(value);
			} catch (Exception e) {
				return false;
			}
		} else {
			// System.err.println("Unable to get the Drop on CAS Exception boolean value of " + object);
			return false;
		}
	}
	
	public void setProcessUnitThreadCount(int processUnitThreadCount) {
		this.getProcessUnitThreadField().setValue(processUnitThreadCount);
	}
	
	public int getProcessingUnitThreadCount() {
		Object object = this.getProcessUnitThreadField().getValue();
		if (object instanceof Integer) {
			Integer i = (Integer) object;
			return i;
		} else {
			// System.err.println("Unable to get the Process Count Thread integer value of " + object);
			return 2;
		}
	}
	
	public void setCasPoolSize(int casPoolSize) {
		this.getCasPoolSizeField().setValue(casPoolSize);
	}
	
	public int getCasPoolSize() {
		Object object = this.getCasPoolSizeField().getValue();
		if (object instanceof Integer) {
			Integer i = (Integer) object;
			return i;
		} else {
			// System.err.println("Unable to get the CAS Pool Size integer value of " + object);
			return 2;
		}	
	}
	
	public void setDropOnCasException(boolean dropOnCasException) {
		if (dropOnCasException) {
			this.getDropOnCasExceptionField().setSelectedIndex(0);
		} else {
			this.getDropOnCasExceptionField().setSelectedIndex(1);
		}
	}
	
}
