package eu.project.ttc.tools.acabit;

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

public class AcabitEngine extends SwingWorker<CpeDescription,Void> {

	private Acabit acabit;
	
	public void setAcabit(Acabit acabit) {
		this.acabit = acabit;
	}
	
	private Acabit getAcabit() {
		return this.acabit;
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
		File file = File.createTempFile("acabit-cpe-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.collectionProcessingEngine.toXML(stream);
		return this.collectionProcessingEngine;
	}
	
	private void setCollectionReader() throws Exception {
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/XmiCollectionReader.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Directory",parameters.getParameterValue("InputDirectory"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCollectionReader(termSuiteCollector);
	}

	private void setAnalysisEngine() throws Exception {
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		String path = "eu/project/ttc/" + language.toLowerCase() + "/engines/" + language + "AcabitAnalysisEngine.xml";
        URL url = this.getClass().getClassLoader().getResource(path);
        CpeIntegratedCasProcessor termMateAnnotator = CpeDescriptorFactory.produceCasProcessor(language + " Acabit Analysis Engine");
        CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(url.toURI().toString());
        termMateAnnotator.setCpeComponentDescriptor(desc);
        CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
        settings.setParameterValue("Language", code);
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
        settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        settings.setParameterValue("Type", (String) parameters.getParameterValue("TermAnnotationType"));
        settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
        settings.setParameterValue("EditDistanceClassName", (String) parameters.getParameterValue("EditDistanceClassName"));
        settings.setParameterValue("EnableTermGathering", (Boolean) parameters.getParameterValue("EnableTermGathering"));
        termMateAnnotator.setConfigurationParameterSettings(settings);
        this.collectionProcessingEngine.addCasProcessor(termMateAnnotator);
	}

}
