package fr.univnantes.lina.uima.models;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class TreeTaggerParameter implements SharedResourceObject {

	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	private String getFile() throws IOException {
		return this.getProperties().getProperty("file");
	}
	
	private String getEncoding() {
		return this.getProperties().getProperty("encoding");
	}
	
	public String getModel() throws IOException {
		return this.getFile() + ":" + this.getEncoding();
	}
	
	private void doLoad(InputStream inputStream) throws IOException {
		this.getProperties().loadFromXML(inputStream);
	}

	private boolean loaded;
	
	private void enableLoaded(boolean enabled) {
		this.loaded = enabled;
	}
	
	private boolean isLoaded() {
		return this.loaded;
	}
	
	public TreeTaggerParameter() {
		this.enableLoaded(false);
		this.setProperties();
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException {
		try {
			if (!this.isLoaded()) {
				this.enableLoaded(true);
				this.doLoad(data.getInputStream());
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	public void override(String parameter) throws IOException {
		if (parameter != null) {
			InputStream inputStream = new FileInputStream(parameter);
			this.doLoad(inputStream);			
		}
	}
			
}
