package eu.project.ttc.tools.tildetagger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

import javax.swing.SwingWorker;

import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.collection.metadata.CpeIntegratedCasProcessor;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

public class TildeTaggerEngine extends SwingWorker<CpeDescription,Void> {

	private TildeTagger tildeTagger;
	
	public void setTildeTagger(TildeTagger treeTagger) {
		this.tildeTagger = treeTagger;
	}
	
	private TildeTagger getTildeTagger() {
		return this.tildeTagger;
	}
		
	private CpeDescription collectionProcessingEngine;
	
	private void setCollectionProcessingEngine() throws CpeDescriptorException {
		this.collectionProcessingEngine = CpeDescriptorFactory.produceDescriptor();
		Runtime runtime = Runtime.getRuntime();
        int threads = runtime.availableProcessors();
		this.collectionProcessingEngine.setProcessingUnitThreadCount(threads);
		this.collectionProcessingEngine.getCpeCasProcessors().setPoolSize(threads);
		this.collectionProcessingEngine.getCpeCasProcessors().setConcurrentPUCount(threads);
	}

	@Override
	protected CpeDescription doInBackground() throws Exception {
		this.setCollectionProcessingEngine();
		this.setCollectionReader();
		this.setAnalysisEngine();
		File file = File.createTempFile("treetagger-cpe-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.collectionProcessingEngine.toXML(stream);
		return this.collectionProcessingEngine;
	}
	
	private void setCollectionReader() throws Exception {
		ConfigurationParameterSettings parameters = this.getTildeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/TildeTaggerCollectionReader.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Language", parameters.getParameterValue("Language"));
		settings.setParameterValue("Directory",parameters.getParameterValue("InputDirectory"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCollectionReader(termSuiteCollector);
	}

	private void setAnalysisEngine() throws Exception {
		ConfigurationParameterSettings parameters = this.getTildeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		String path = "eu/project/ttc/" + language.toLowerCase() + "/engines/" + language + "TildeTaggerAnalysisEngine.xml";
        URL url = this.getClass().getClassLoader().getResource(path);
        CpeIntegratedCasProcessor termMateAnnotator = CpeDescriptorFactory.produceCasProcessor(language + " TildeTagger Analysis Engine");
        CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(url.toURI().toString());
        termMateAnnotator.setCpeComponentDescriptor(desc);
        CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
        settings.setParameterValue("ScriptFile", (String) parameters.getParameterValue("ScriptFile"));
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
        termMateAnnotator.setConfigurationParameterSettings(settings);
        this.collectionProcessingEngine.addCasProcessor(termMateAnnotator);
	}

}
