package eu.project.ttc.tools.converter;

import java.io.IOException;

import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.xml.sax.SAXException;

public interface ConverterSettings {

	public void doUpdate();
	
	public void validate() throws ResourceConfigurationException;
	
	public void doSave() throws IOException, SAXException;
	
	public ResourceMetaData getMetaData();
	
}