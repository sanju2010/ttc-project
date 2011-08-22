package eu.project.ttc.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

public class Parameters {
	
	private Properties properties;
	
	private void setProperties() {
		this.properties = new Properties();
	}
	
	private Properties getProperties() {
		return this.properties;
	}
	
	private String sourceLanguage;
	
	private void setSourceLanguage() {
		String sourceLanguage = this.getProperties().getProperty("SourceLanguage");
		if (sourceLanguage == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Missing parameter 'SourceLanguage'!");
		} else {
			this.sourceLanguage = sourceLanguage;
		}
	}
	
	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}
	
	public String getSourceLanguage() {
		return this.sourceLanguage;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage() {
		String targetLanguage = this.getProperties().getProperty("TargetLanguage");
		if (targetLanguage == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Missing parameter 'TargetLanguage'!");
		} else {
			this.targetLanguage = targetLanguage;
		}
	}
	
	public void setTargetLanguage(String targetLanguage) {
		this.targetLanguage = targetLanguage;
	}
	
	public String getTargetLanguage() {
		return this.targetLanguage;
	}
	
	private String[] sourceDirectories;
	
	private void setSourceDirectories() {
		String[] sourceDirectories = this.getProperties().getProperty("SourceDirectories").split("\\s+");
		if (sourceDirectories == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Missing parameter 'SourceDirectories'!");
		} else {
			this.sourceDirectories = sourceDirectories;
		}
	}
	
	public void setSourceDirectories(String[] sourceDirectories) {
		this.sourceDirectories = sourceDirectories;
	}
	
	public String[] getSourceDirectories() {
		return this.sourceDirectories;
	}
	
	private String[] targetDirectories;
	
	private void setTargetDirectories() {
		String[] targetDirectories = this.getProperties().getProperty("TargetDirectories").split("\\s+");
		if (targetDirectories == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Missing parameter 'TargetDirectories'!");
		} else {
			this.targetDirectories = targetDirectories;
		}
	}
	
	public void setTargetDirectories(String[] targetDirectories) {
		this.targetDirectories = targetDirectories;
	}
	
	public String[] getTargetDirectories() {
		return this.targetDirectories;
	}
	
	private String treeTaggerHomeDirectory;
	
	private void setTreeTaggerHomeDirectory() {
		String treeTaggerHomeDirectory = this.getProperties().getProperty("TreeTaggerHomeDirectory");
		if (treeTaggerHomeDirectory == null) {
			UIMAFramework.getLogger().log(Level.WARNING,"Missing parameter 'TreeTaggerHomeDirectory'!");
		} else {
			this.treeTaggerHomeDirectory = treeTaggerHomeDirectory;
		}
	}
	
	public void setTreeTaggerHomeDirectory(String treeTaggerHomeDirectory) {
		this.treeTaggerHomeDirectory = treeTaggerHomeDirectory;
	}
	
	public String getTreeTaggerHomeDirectory() {
		return this.treeTaggerHomeDirectory;
	}
	
	public Parameters() {
		this.setProperties();
	}

	public void doLoad(String path) throws IOException {
		FileInputStream inStream = new FileInputStream(path);
		this.getProperties().load(inStream);
		this.setSourceLanguage();
		this.setSourceDirectories();
		this.setTargetLanguage();
		this.setTargetDirectories();
		this.setTreeTaggerHomeDirectory();
	}
	
}
