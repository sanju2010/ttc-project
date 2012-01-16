package fr.free.rocheteau.jerome.dunamis.utils;

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
		InputStream in = this.getClass().getResourceAsStream("/dunamis.properties");
		this.properties.loadFromXML(in);
		in.close();
	}
	
	public String getSummary() {
		return this.getProperties().getProperty("dunamis:summary");
	}
	
	public String getLicense() {
		return this.getProperties().getProperty("dunamis:license");
	}
	
	public String getTitle() {
		return this.getProperties().getProperty("dunamis:title");
	}
	
	public String getVersion() {
		return this.getProperties().getProperty("dunamis:version");
	}
	
	public String getIcon() {
		return this.getProperties().getProperty("dunamis:icon");
	}
	
	public String getHelp() {
		return this.getProperties().getProperty("dunamis:help");
	}
	
	public Preferences() {
		this.setProperties();
	}
	
}
