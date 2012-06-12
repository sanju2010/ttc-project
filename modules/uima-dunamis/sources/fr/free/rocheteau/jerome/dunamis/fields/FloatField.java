package fr.free.rocheteau.jerome.dunamis.fields;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class FloatField implements Field {

	private String name;
	
	@Override
	public  void setName(String name) {
		this.name = name;
		this.getBorder().setTitle(FieldFactory.getTitle(name));
	}
	
	@Override
	public String getName() {
		return this.name;
	}
		
	private Float value;

	@Override
	public void setValue(Object value) {
		if (value instanceof Float) {
			this.value = (Float) value;
			// this.getText().setText(this.value.toString());
			JComponent editor = this.getSpinner().getEditor();
			if (editor instanceof JSpinner.NumberEditor) {
	            ((JSpinner.NumberEditor) editor).getTextField().setText(value.toString());
	        }
		}
	}
	
	@Override
	public Float getValue() {
		String value = null;
		JComponent editor = this.getSpinner().getEditor();
        if (editor instanceof JSpinner.NumberEditor) {
            value = ((JSpinner.NumberEditor) editor).getTextField().getText();
        } else {        	
            return null;
        }
		try {
			// value = value.replace(',','.');
			return Float.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	public FloatField() {
		this.setBorder();
		// this.setText();
		this.setModel();
		this.setSpinner();
		this.setComponent();
	}
	
	private TitledBorder border;
	
	private void setBorder() {
		Border border = BorderFactory.createEmptyBorder();
		int position = TitledBorder.DEFAULT_POSITION;
		int justrification = TitledBorder.DEFAULT_JUSTIFICATION;
		this.border = BorderFactory.createTitledBorder(border,"",justrification,position);		
	}
	
	private TitledBorder getBorder() {
		return this.border;
	}

	/*
	private JTextField text;
	
	private void setText() {
		this.text = new JTextField(33);
	}

	public JTextField getText() {
		return this.text;
	}
	*/
	
	private SpinnerNumberModel model;
	
	private void setModel() {
		this.model = new SpinnerNumberModel(0,0,1000,0.1);
	}
	
	private SpinnerNumberModel getModel() {
		return this.model;
	}
	
	private JSpinner spinner;
	
	private void setSpinner() {
		this.spinner = new JSpinner(this.getModel());
		
		NumberEditor editor = new JSpinner.NumberEditor(this.spinner);
		DecimalFormat format = editor.getFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(1);
        format.setMinimumIntegerDigits(1);
		this.spinner.setEditor(editor);
		
		this.spinner.setPreferredSize(new Dimension(300,25));
	}
	
	public JSpinner getSpinner() {
		return this.spinner;
	}
	
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel(new GridBagLayout());
		this.component.setOpaque(false);
		this.component.setBorder(this.getBorder());
		this.component.add(this.getSpinner());
	}

	@Override
	public JPanel getComponent() {
		return this.component;
	}
	
	@Override
	public boolean isModified() {
		if (this.getValue() == null) {
			return false;
		} else {
			return !this.getValue().equals(this.value);
		}
	}
	public void setListener(Field f1, Field f2, Field f3)
	{
		
	}
}
