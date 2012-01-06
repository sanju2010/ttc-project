package eu.project.ttc.tools.katastasis;

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

public class KatastasisEngine extends SwingWorker<CpeDescription,Void> {

	private Katastasis katastasis;
	
	public void setKatastasis(Katastasis katastasis) {
		this.katastasis = katastasis;
	}
	
	private Katastasis getKatastasis() {
		return this.katastasis;
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
		File file = File.createTempFile("katastasis-cpe-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.collectionProcessingEngine.toXML(stream);
		return this.collectionProcessingEngine;
	}
	
	private void setCollectionReader() throws Exception {
		ConfigurationParameterSettings parameters = this.getKatastasis().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/KatastasisCollectionReader.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Language", parameters.getParameterValue("Language"));
		settings.setParameterValue("Directory",parameters.getParameterValue("Directory"));
		settings.setParameterValue("TerminologyFile",parameters.getParameterValue("TerminologyFile"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCollectionReader(termSuiteCollector);
	}
	
	private void setAnalysisEngine() throws Exception {
		ConfigurationParameterSettings parameters = this.getKatastasis().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/KatastasisAnalysisEngine.xml");
		CpeIntegratedCasProcessor termSuiteTranslator = CpeDescriptorFactory.produceCasProcessor("Katastasis Analysis Engine");
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(url.toURI().toString());
		termSuiteTranslator.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Language", parameters.getParameterValue("Language"));
		settings.setParameterValue("ScopeSize",parameters.getParameterValue("ScopeSize"));
		settings.setParameterValue("AssociationRateClassName",parameters.getParameterValue("AssociationRateClassName"));
		settings.setParameterValue("File",parameters.getParameterValue("File"));
		termSuiteTranslator.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCasProcessor(termSuiteTranslator);
	}
	
}
