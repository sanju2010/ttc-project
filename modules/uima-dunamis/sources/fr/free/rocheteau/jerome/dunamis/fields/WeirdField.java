package fr.free.rocheteau.jerome.dunamis.fields;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class WeirdField implements Field {

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
			
	@Override
	public void setValue(Object value) {
		this.getText().setText("Weird parameter");
	}
	
	@Override
	public Object getValue() {
		return null;
	}

	public WeirdField() {
		this.setBorder();
		this.setText();
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

	private JTextField text;
	
	private void setText() {
		this.text = new JTextField(33);
	}

	public JTextField getText() {
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
		return false;
	}
	public void setListener(Field f1, Field f2, Field f3)
	{
		
	}
}
