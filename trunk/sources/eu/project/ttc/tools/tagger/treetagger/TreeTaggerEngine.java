package eu.project.ttc.tools.tagger.treetagger;

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

import eu.project.ttc.tools.tagger.TaggerEngine;
import eu.project.ttc.tools.tagger.TaggerTool;

public class TreeTaggerEngine extends SwingWorker<CpeDescription,Void> implements TaggerEngine {

	private TaggerTool taggerTool;
	
	public void setTaggerTool(TaggerTool taggerTool) {
		this.taggerTool = taggerTool;
	}
	
	private TaggerTool getTaggerTool() {
		return this.taggerTool;
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
		ConfigurationParameterSettings parameters = this.getTaggerTool().getSettings().getMetaData().getConfigurationParameterSettings();
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/TextCollectionReader.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Language", parameters.getParameterValue("Language"));
		settings.setParameterValue("Directory",parameters.getParameterValue("InputDirectory"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.collectionProcessingEngine.addCollectionReader(termSuiteCollector);
	}

	private void setAnalysisEngine() throws Exception {
		ConfigurationParameterSettings parameters = this.getTaggerTool().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		String path = "eu/project/ttc/" + language.toLowerCase() + "/engines/" + language + "TreeTaggerAnalysisEngine.xml";
        URL url = this.getClass().getClassLoader().getResource(path);
        CpeIntegratedCasProcessor termMateAnnotator = CpeDescriptorFactory.produceCasProcessor(language + " TreeTagger Analysis Engine");
        CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(url.toURI().toString());
        termMateAnnotator.setCpeComponentDescriptor(desc);
        CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
        settings.setParameterValue("TreeTaggerHomeDirectory", (String) parameters.getParameterValue("TreeTaggerHomeDirectory"));
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
        termMateAnnotator.setConfigurationParameterSettings(settings);
        this.collectionProcessingEngine.addCasProcessor(termMateAnnotator);
	}

}
