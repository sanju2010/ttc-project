package eu.project.ttc.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Languages {
	
	public List<Language> getSelection() {
		List<Language> languages = new ArrayList<Language>();
		for (int index = 0; index < this.getModel().size(); index++) {
			Language language = (Language) this.getModel().getElementAt(index);
			if (language.isSelected()) {
				languages.add(language);
			}
		}
		return languages;
	}
	
	private Dimension getDimension() {
		return new Dimension(200,350);
	}
	
	private DefaultListModel model;
	
	private void setModel() {
		this.model = new DefaultListModel();
		this.model.addElement(new Language("English","en"));
		this.model.addElement(new Language("German","de"));
		this.model.addElement(new Language("French","fr"));
		this.model.addElement(new Language("Spanish","es"));
		this.model.addElement(new Language("Latvian","lv"));
		this.model.addElement(new Language("Russian","ru"));
		this.model.addElement(new Language("Chinese","zh"));
	}
	
	public DefaultListModel getModel() {
		return this.model;
	}
	
	private JList list;
	
	private void setList() {
		this.list = new JList();
		this.list.setModel(this.getModel());
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.list.addListSelectionListener(new Listener());
		this.list.setCellRenderer(new Renderer());
	}
	
	private JList getList() {
		return this.list;
	}
	
	private JScrollPane scroll;
	
	private void setScroll() {
		this.scroll = new JScrollPane();
	    this.scroll.getViewport().add(this.getList());
		this.scroll.setMinimumSize(this.getDimension());
	}
	
	public JScrollPane getComponent() {
		return this.scroll;
	}
			
	public Languages() {
		this.setModel();
		this.setList();
		this.setScroll();
	}
	
	public class Language {
		
		private String name;
		
		private void setName(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		private String code;
		
		private void setCode(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return this.code;
		}
		
		private boolean selected;
		
		public void doEnable(boolean enabled) {
			this.selected = enabled;
		}
		
		public boolean isSelected() {
			return this.selected;
		}
		
		public Language(String name,String code) {
			this.setName(name);
			this.setCode(code);
			this.doEnable(false);
		}
		
	}

	private final String[] allowedLanguages = new String[] {"English", "French", "German"};
	
	private List<String> getAllowedLanguages() {
		return Arrays.asList(this.allowedLanguages);
	}
	
	private class Listener implements ListSelectionListener {
		
		@Override
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				JList list = (JList) event.getSource();
				Language language = (Language) list.getSelectedValue();
				if (getAllowedLanguages().contains(language.toString())) {
					language.doEnable(!language.isSelected());
				}
			}
		}

	}

	private class Renderer implements ListCellRenderer {
				
		@Override
		public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
			Language language = (Language) value;
			JCheckBox checkBox = new JCheckBox(language.toString(),language.isSelected());
			if (getAllowedLanguages().contains(value.toString())) {
				checkBox.setEnabled(true);
			} else {
				checkBox.setEnabled(false);
			}
			return checkBox;
		}
		
	}
	
}
