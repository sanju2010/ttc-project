package fr.free.rocheteau.jerome.dunamis.fields;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class StringItemField implements Field {

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
		
	private String value;
	
	@Override
	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = (String) value;
			this.getText().setSelectedItem(this.value);
		}
	}

	@Override
	public String getValue() {
		return (String) this.getText().getSelectedItem();
	}

	public StringItemField(String[] items) {
		this.setBorder();
		this.setText(items);
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

	private JComboBox text;
	
	private void setText(String[] items) {
		this.text = new JComboBox(items);
	}

	public JComboBox getText() {
		return this.text;
	}
	
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel();
		this.component.setOpaque(false);
		this.component.setBorder(this.getBorder());
		this.component.add(this.getText());
	}

	@Override
	public JPanel getComponent() {
		return this.component;
	}
	
	@Override
	public boolean isModified() {
		return !this.getValue().equals(this.value);
	}
}
