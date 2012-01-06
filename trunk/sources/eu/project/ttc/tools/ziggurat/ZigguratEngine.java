package eu.project.ttc.tools.ziggurat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.SwingWorker;

import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.collection.metadata.CpeIntegratedCasProcessor;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

public class ZigguratEngine extends SwingWorker<CpeDescription,Void> {

	private Ziggurat ziggurat;
	
	public void setZiggurat(Ziggurat ziggurat) {
		this.ziggurat = ziggurat;
	}
	
	private Ziggurat getZiggurat() {
		return this.ziggurat;
	}
		
	private CpeDescription collectionProcessingEngine;
	
	private void setCollectionProcessingEngine() throws CpeDescriptorException {
		this.collectionProcessingEngine = CpeDescriptorFactory.produceDescriptor();
		Runtime runtime = Runtime.getRuntime();
        int threads = runtime.availableProcessors();
        threads = threads <= 1 ? 1 : threads - 1;
		this.collectionProcessingEngine.setProcessingUnitThreadCount(threads);
		this.collectionProcessingEngine.getCpeCasProcessors().setPoolSize(threads);
		this.collectionProcessingEngine.getCpeCasProcessors().setConcurrentPUCount(threads);
	}

	@Override
	protected CpeDescription doInBackground() throws Exception {
		this.setCollectionProcessingEngine();
		this.setCollectionReader();
		this.setAnalysisEngine();
		File file = File.createTempFile("ziggurat-cpe-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.collectionProcessingEngine.toXML(stream);
		return this.collectionProcessingEngine;
	}
	
	private void setCollectionReader() throws Exception {
		ConfigurationParameterSettings parameters = this.getZiggurat().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/ZigguratCollectionReader.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("File",parameters.getParameterValue("EvaluationListFile"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCollectionReader(termSuiteCollector);
	}
	
	private void setAnalysisEngine() throws Exception {
		ConfigurationParameterSettings parameters = this.getZiggurat().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/ZigguratAnalysisEngine.xml");
		CpeIntegratedCasProcessor termSuiteTranslator = CpeDescriptorFactory.produceCasProcessor("Ziggurat Analysis Engine");
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(url.toURI().toString());
		termSuiteTranslator.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("SourceLanguage",parameters.getParameterValue("SourceLanguage"));
		settings.setParameterValue("TargetLanguage",parameters.getParameterValue("TargetLanguage"));
		settings.setParameterValue("InterlingualSimilarity",parameters.getParameterValue("InterlingualSimilarity"));
		settings.setParameterValue("InterlingualSimilaritySize",parameters.getParameterValue("InterlingualSimilaritySize"));
		settings.setParameterValue("SimilarityDistanceClassName",parameters.getParameterValue("SimilarityDistanceClassName"));
		settings.setParameterValue("DictionaryFile",parameters.getParameterValue("DictionaryFile"));
		settings.setParameterValue("SourceTermContextIndexFile",parameters.getParameterValue("SourceTermContextIndexFile"));
		settings.setParameterValue("TargetTermContextIndexFile",parameters.getParameterValue("TargetTermContextIndexFile"));
		settings.setParameterValue("SourceTerminologyFile",parameters.getParameterValue("SourceTerminologyFile"));
		settings.setParameterValue("TargetTerminologyFile",parameters.getParameterValue("TargetTerminologyFile"));
		settings.setParameterValue("SourceTermSimilarityDirectory",parameters.getParameterValue("SourceTermSimilarityDirectory"));
		settings.setParameterValue("TargetTermSimilarityDirectory",parameters.getParameterValue("TargetTermSimilarityDirectory"));
		termSuiteTranslator.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCasProcessor(termSuiteTranslator);
	}
	
}
