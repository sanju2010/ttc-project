package fr.free.rocheteau.jerome.dunamis.fields;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class BooleanField implements Field {

	private String name;
	
	@Override
	public  void setName(String name) {
		this.name = name;
		this.getComponent().setText(FieldFactory.getTitle(name));
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	private Boolean value;
	
	@Override
	public void setValue(Object value) {
		if (value == null) {
			this.value = Boolean.FALSE;
		} else if (value instanceof Boolean) {
			this.value = Boolean.valueOf(value.toString());
		}
		this.getComponent().setSelected(this.value.booleanValue());
	}
	
	@Override
	public Boolean getValue() {
		return new Boolean(this.getComponent().isSelected());
	}

	public BooleanField() {
		this.setComponent();
	}

	private JCheckBox component;
	
	private void setComponent() {
		this.component = new JCheckBox();
		this.component.setBorder(BorderFactory.createEmptyBorder(5, 7, 10, 0));
	}

	@Override
	public JCheckBox getComponent() {
		return this.component;
	}
		
	@Override
	public boolean isModified() {
		return !this.getValue().equals(this.value);
	}

}
