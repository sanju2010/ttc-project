package fr.free.rocheteau.jerome.dunamis.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import fr.free.rocheteau.jerome.dunamis.Dunamis;

public class CollectionProcessingEngine implements Transferable, ProcessorSettingModel {

	public CollectionProcessingEngine(CpeDescription description) throws Exception {
		this(description,true);
	}
	
	public CollectionProcessingEngine(CpeDescription description,boolean load) throws Exception {
		this.setCollectionReaders();
		this.setAnalysisEngines();
		this.setDescription(description);
		if (load) {
			this.load();
		} else {
			this.getDescription().setCpeCasProcessors(CpeDescriptorFactory.produceCasProcessors());
		}
	}
	
	private TypeSystemDescription typeSystem;
	
	public void setTypeSystem(TypeSystemDescription typeSystem) {
		this.typeSystem = typeSystem;
	}
	
	private void setTypeSystem() throws ResourceInitializationException, InvalidXMLException {
		List<TypeSystemDescription> list = new ArrayList<TypeSystemDescription>();
		for (int index = 0; index < this.getCollectionReaders().getSize(); index++) {
			Object object = this.getCollectionReaders().get(index);
			if (object instanceof CollectionReader) {
				CollectionReader cr = (CollectionReader) object;
				TypeSystemDescription ts = cr.getMetaData().getTypeSystem();
				if (ts != null) {
					list.add(ts);
				}
			}
		}
		for (int index = 0; index < this.getAnalysisEngines().getSize(); index++) {
			Object object = this.getAnalysisEngines().get(index);
			if (object instanceof AnalysisEngine) {
				AnalysisEngine ae = (AnalysisEngine) object; 
				if (ae.getDescription().isPrimitive()) {
					list.add(ae.getDescription().getAnalysisEngineMetaData().getTypeSystem());
				} else {
					// TODO
				}
			} 
		}
		this.typeSystem = CasCreationUtils.mergeTypeSystems(list);
		this.typeSystem.resolveImports();
	}
	
	public TypeSystemDescription getTypeSystem() {
		return this.typeSystem;
	}

	
	private CpeDescription description;
	
	private void setDescription(CpeDescription cpe) throws Exception {
		this.description = cpe;
	}
	
	private void load() throws CpeDescriptorException, MalformedURLException, IOException, InvalidXMLException, ResourceInitializationException {
		this.loadCollectionReader();
		this.loadAnalysisEngines();
		this.setTypeSystem();
	}

	private void loadAnalysisEngines() throws CpeDescriptorException, MalformedURLException, IOException, InvalidXMLException {
		CpeCasProcessors casProcessors = this.getDescription().getCpeCasProcessors();
		for (CpeCasProcessor casProcessor : casProcessors.getAllCpeCasProcessors()) {
			String name = casProcessor.getName();
			CasProcessorConfigurationParameterSettings settings = casProcessor.getConfigurationParameterSettings();
			String src = (String) casProcessor.getCpeComponentDescriptor().getInclude().getAttributeValue("href");
			URL url = new URL(src);
			XMLInputSource xml = new XMLInputSource(url);
			XMLParser parser = UIMAFramework.getXMLParser();
			AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(xml);
			AnalysisEngine analysisEngine = new AnalysisEngine(ae,settings);
			if (name != null) {
				analysisEngine.setName(name);
			}
			this.getAnalysisEngines().addElement(analysisEngine);
		}
	}

	private void loadCollectionReader() throws CpeDescriptorException, MalformedURLException, IOException, InvalidXMLException {
		CpeCollectionReader[] collectionReaders = this.getDescription().getAllCollectionCollectionReaders();
		for (CpeCollectionReader cpeCollectionReader : collectionReaders) {
			CasProcessorConfigurationParameterSettings settings = cpeCollectionReader.getConfigurationParameterSettings();
			String src = (String) cpeCollectionReader.getCollectionIterator().getDescriptor().getInclude().getAttributeValue("href");
			URL url = new URL(src);
			XMLInputSource xml = new XMLInputSource(url);
			XMLParser parser = UIMAFramework.getXMLParser();
			CollectionReaderDescription cr = parser.parseCollectionReaderDescription(xml);
			CollectionReader collectionReader = new CollectionReader(cr,settings);
			this.getCollectionReaders().addElement(collectionReader);
		}
	}

	public void store() throws Exception {
		this.getDescription().getCpeCasProcessors().removeAllCpeCasProcessors();
		CollectionReader cr = (CollectionReader) this.getCollectionReaders().getElementAt(0);
		String crPath = cr.getDescription().getSourceUrlString();
		CpeCollectionReader collectionReader = CpeDescriptorFactory.produceCollectionReader(crPath);
		collectionReader.setConfigurationParameterSettings(cr.getSettings());
		this.getDescription().addCollectionReader(collectionReader);
		for (int i = 0; i < this.getAnalysisEngines().getSize(); i++) {
			AnalysisEngine ae = (AnalysisEngine) this.getAnalysisEngines().getElementAt(i);
			String aePath = ae.getDescription().getSourceUrlString();
			CpeComponentDescriptor proc = CpeDescriptorFactory.produceComponentDescriptor(aePath);
			String name = ae.toString();
			CpeCasProcessor processor = CpeDescriptorFactory.produceCasProcessor(name);
			processor.setCpeComponentDescriptor(proc);
			CasProcessorConfigurationParameterSettings settings = ae.getSettings();
			processor.setConfigurationParameterSettings(settings);
			this.getDescription().addCasProcessor(i,processor);
		}
	}
		
	public void revert() throws Exception {
		this.getCollectionReaders().clear();
		this.getAnalysisEngines().clear();
		this.load();
	}
	
	public CpeDescription getDescription() {
		return description;
	}

	private DefaultListModel collectionReaders;
	
	private void setCollectionReaders() {
		this.collectionReaders = new DefaultListModel();
	}
	
	public DefaultListModel getCollectionReaders() {
		return this.collectionReaders;
	}
	
	private DefaultListModel analysisEngines;
	
	private void setAnalysisEngines() {
		this.analysisEngines = new DefaultListModel();
	}
	
	public DefaultListModel getAnalysisEngines() {
		return this.analysisEngines;
	}
	
	public String toString() {
		String path = this.getDescription().getSourceUrlString(); 
		int i = path.lastIndexOf("/");
		int j = path.lastIndexOf(".");
		if (0 <= i && i < j && j < path.length()) {
			return path.substring(i+1,j);
		} else {
			Exception e = new Exception (path);
			Dunamis.error(e);
			return null;
		}
	}

	@Override
	public int getCasPoolSize() throws Exception {
		return this.getDescription().getCpeCasProcessors().getCasPoolSize();
	}

	@Override
	public void setCasPoolSize(int casPoolSize) throws Exception {
        this.getDescription().getCpeCasProcessors().setPoolSize(casPoolSize);
    }

	@Override
	public int getProcessingUnitThreadCount() throws Exception {
		return this.getDescription().getCpeCasProcessors().getConcurrentPUCount();
	}
	
	@Override
    public void setProcessingUnitThreadCount(int processingUnitThreadCount) throws Exception {
    	this.getDescription().setProcessingUnitThreadCount(processingUnitThreadCount);
        this.getDescription().getCpeCasProcessors().setConcurrentPUCount(processingUnitThreadCount);
    }
	
    @Override
	public boolean getDropOnCasException() throws Exception {
		return (Boolean) this.getDescription().getCpeCasProcessors().getAttributeValue("dropCasOnException");
	}
	
	@Override
    public void setDropOnCasException(boolean dropOnCasException) throws Exception {
        this.getDescription().getCpeCasProcessors().setAttributeValue("dropCasOnException",dropOnCasException);
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
		DataFlavor flavor = new DataFlavor(this.getClass(),"UIMA Collection Processing Engine");
		return new DataFlavor[]{ flavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		String fst = flavor.getHumanPresentableName();
		String snd = "UIMA Collection Processing Engine";
		if (fst == null || snd == null) {
			return false;
		} else {
			return fst.equals(snd);
		}
	}
	
}
