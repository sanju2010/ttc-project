package eu.project.ttc.tools.help;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class HelpTab {

	private URL url;
	
	private void setUrl(String name, String locale) throws IOException {
		String path = "eu/project/ttc/" + locale.toLowerCase() + "/documents/" + name + ".rtf";
		this.url = this.getClass().getClassLoader().getResource(path);
	}
	
	private URL getUrl() {
		return this.url;
	}
	
	private JEditorPane text;
	
	private void setText() throws IOException {
		this.text = new JEditorPane();
		this.text.setPage(this.getUrl());
	}
	
	private JEditorPane getText() {
		return this.text;
	}
	
	private JScrollPane component;
	
	private void setComponent() {
		this.component = new JScrollPane(this.getText());
	}
	
	public JScrollPane getComponent() {
		return this.component;
	}
	
	public HelpTab(String name, String locale) throws IOException {
		this.setUrl(name, locale);
		this.setText();
		this.setComponent();
	}
	
}
