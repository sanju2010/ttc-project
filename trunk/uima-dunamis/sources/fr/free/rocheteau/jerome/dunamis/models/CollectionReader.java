package fr.free.rocheteau.jerome.dunamis.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.TypeSystemDescription;

public class CollectionReader implements CasProcessor {

	public CollectionReader(CollectionReaderDescription description,CasProcessorConfigurationParameterSettings settings) {
		this.setDescription(description);
		this.setSettings(settings);
	}
	
	private CollectionReaderDescription description;
	
	private void setDescription(CollectionReaderDescription description) {
		this.description = description;
	}

	public CollectionReaderDescription getDescription() {
		return description;
	}

	private CasProcessorConfigurationParameterSettings settings;
	
	public void setSettings(CasProcessorConfigurationParameterSettings settings) {
		if (settings == null) {
			this.settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		} else {
			this.settings = settings;
		}
	}

	@Override
	public void updateSettings() {
		ConfigurationParameterSettings settings = this.getMetaData().getConfigurationParameterSettings();
		NameValuePair[] parameters = settings.getParameterSettings();
		for (NameValuePair parameter : parameters) {
			this.getSettings().setParameterValue(parameter.getName(),parameter.getValue());
		}
	}
	
	public CasProcessorConfigurationParameterSettings getSettings() {
		return this.settings;
	}
	
	public ProcessingResourceMetaData getMetaData() {
		return this.getDescription().getCollectionReaderMetaData();
	}
	
	public String toString() {
		return this.getDescription().getCollectionReaderMetaData().getName();
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (this.isDataFlavorSupported(flavor)) {
			return this;	
		} else {
			return null;
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor flavor = new DataFlavor(this.getClass(),"UIMA Collection Reader");
		return new DataFlavor[]{ flavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		String fst = flavor.getHumanPresentableName();
		String snd = "UIMA Collection Reader";
		if (fst == null || snd == null) {
			return false;
		} else {
			return fst.equals(snd);
		}
	}
	
	public CollectionReader clone() {
		CollectionReaderDescription desc = (CollectionReaderDescription) this.getDescription().clone();
		return new CollectionReader(desc,null);
	}

	@Override
	public TypeSystemDescription getTypeSystem() {
		return this.getDescription().getCollectionReaderMetaData().getTypeSystem();
	}
	
	@Override
	public int compareTo(CasProcessor processor) {
		return this.toString().compareTo(processor.toString());
	}

	public boolean equals(Object object) {
		if (object instanceof AnalysisEngine) {
			AnalysisEngine ae = (AnalysisEngine) object;
			return this.compareTo(ae) == 0;
		} else {
			return false;
		}
	}

	@Override
	public void setName(String name) {
		this.getMetaData().setName(name);
	}
	
}
