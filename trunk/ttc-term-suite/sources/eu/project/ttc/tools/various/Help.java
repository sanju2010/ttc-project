package eu.project.ttc.tools.various;

import java.awt.Component;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JTabbedPane;

public class Help {

	private Locale locale;
	
	private void setLocale() {
		Locale locale = Locale.getDefault();
		if (locale == null) {
			locale = Locale.ENGLISH;
		} else {
			this.locale = locale;
		}
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	private Component getHelp(String name, String locale) {
		try { 
			HelpTab help = new HelpTab(name, locale);
			return help.getComponent();
		} catch (IOException e) {
			try {
				HelpTab help = new HelpTab(name, Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
				return help.getComponent();	
			} catch (IOException ee) {
				ee.printStackTrace();
				return null;
			}
		}
	}
	
	private JTabbedPane component;
	
	private void setComponent() {		
		this.component = new JTabbedPane();
		this.component.setTabPlacement(JTabbedPane.RIGHT);
		String locale = this.getLocale().getDisplayLanguage(Locale.ENGLISH);
		this.component.addTab("   Term Suite   ",this.getHelp("TermSuite", locale));
		this.component.addTab("   TreeTagger   ",this.getHelp("TreeTagger", locale));
		this.component.addTab("     Termer     ",this.getHelp("Termer", locale));
		this.component.addTab(" Contextualizer ",this.getHelp("Contextualizer", locale));
		this.component.addTab("    Aligner     ",this.getHelp("Aligner", locale));
		this.component.addTab("   Converter    ",this.getHelp("Converter", locale));
	}
	
	public JTabbedPane getComponent() {
		return this.component;
	}
	
	public Help() {
		this.setLocale();
		this.setComponent();
	}

	public void selectTermSuite() {
		this.getComponent().setSelectedIndex(0);
	}
	
	public void selectTreeTagger() {
		this.getComponent().setSelectedIndex(1);
	}
	
	public void selectTermer() {
		this.getComponent().setSelectedIndex(2);
	}
	
	public void selectContextualizer() {
		this.getComponent().setSelectedIndex(3);
	}
	
	public void selectAligner() {
		this.getComponent().setSelectedIndex(4);
	}
	
	public void selectConverter() {
		this.getComponent().setSelectedIndex(5);
	}
	
}
