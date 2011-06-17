package eu.project.ttc.tools;

import java.io.InputStream;
import java.util.Properties;

public class Preferences {
	
	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	public void load() throws Exception { 
		InputStream in = this.getClass().getResourceAsStream("/ttc-term-suite.properties");
		this.properties.loadFromXML(in);
		in.close();
	}
	
	public String getSummary() {
		return this.getProperties().getProperty("summary");
	}
	
	public String getLicense() {
		return this.getProperties().getProperty("license");
	}
	
	public String getTitle() {
		return this.getProperties().getProperty("title");
	}
	
	public String getVersion() {
		return this.getProperties().getProperty("version");
	}
	
	public String getIcon() {
		return this.getProperties().getProperty("icon");
	}
	
	public String getHelp() {
		return this.getProperties().getProperty("help");
	}
	
	public Preferences() {
		this.setProperties();
	}
	
}
