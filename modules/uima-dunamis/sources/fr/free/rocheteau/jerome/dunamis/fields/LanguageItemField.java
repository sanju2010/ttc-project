package fr.free.rocheteau.jerome.dunamis.fields;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class LanguageItemField implements Field {

	private Map<String, String> codes;
	
	private void setCodes() {
		this.codes = new HashMap<String, String>();
	}
	
	private Map<String, String> getCodes() {
		return this.codes;
	}
	
	private Map<String, String> languages;
	
	private void setLanguages() {
		this.languages = new HashMap<String, String>();
	}
	
	private Map<String, String> getLanguages() {
		return this.languages;
	}

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
			this.getText().setSelectedItem(this.getCodes().get(this.value));
		}
	}
	
	@Override
	public String getValue() {
		return this.getLanguages().get((String) this.getText().getSelectedItem());
	}

	public LanguageItemField(String[] items) {
		this.setCodes();
		this.setLanguages();
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
		String[] languages = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			Locale locale = new Locale(items[i]);
			languages[i] = locale.getDisplayName(Locale.ENGLISH);
			this.getCodes().put(items[i], languages[i]);
			this.getLanguages().put(languages[i], items[i]);
		}
		this.text = new JComboBox(languages);
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
