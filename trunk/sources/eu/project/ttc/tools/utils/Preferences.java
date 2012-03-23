package eu.project.ttc.tools.utils;

import java.io.InputStream;
import java.util.Properties;

public class Preferences {

	private String resource;
	
	private void setResource(String resource) {
		this.resource = resource;
	}
	
	protected String getResource() {
		return this.resource;
	}	
	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	public void load() throws Exception { 
		InputStream in = this.getClass().getResourceAsStream("/" + this.getResource());
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
	
	public Preferences(String resource) {
		this.setResource(resource);
		this.setProperties();
	}
	
}
