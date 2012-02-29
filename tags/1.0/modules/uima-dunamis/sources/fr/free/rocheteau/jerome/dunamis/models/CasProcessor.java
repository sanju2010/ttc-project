package fr.free.rocheteau.jerome.dunamis.models;

import java.awt.datatransfer.Transferable;

import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.TypeSystemDescription;

public interface CasProcessor extends Transferable, Comparable<CasProcessor> {

	public void setName(String name);
	
	public void setSettings(CasProcessorConfigurationParameterSettings settings);
	
	public CasProcessorConfigurationParameterSettings getSettings();
	
	public ProcessingResourceMetaData getMetaData();
	
	public CasProcessor clone();

	public TypeSystemDescription getTypeSystem();

	public void updateSettings();
	
}
