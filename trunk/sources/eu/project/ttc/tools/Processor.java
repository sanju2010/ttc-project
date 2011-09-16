package eu.project.ttc.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

import javax.swing.SwingWorker;

import org.apache.uima.TermAnnotation;
import org.apache.uima.analysis_engine.metadata.FlowControllerDeclaration;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.collection.metadata.CpeIntegratedCasProcessor;
import org.apache.uima.resource.ExternalResourceDependency;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.FileResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ExternalResourceBinding;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;

import fr.univnantes.lina.uima.engines.NeighbourAnnotator;
import fr.univnantes.lina.uima.engines.TermBankIndexer;
import fr.univnantes.lina.uima.engines.TermBankWriter;
import fr.univnantes.lina.uima.models.TermBank;
import fr.univnantes.lina.uima.models.TermBankResource;

public class Processor extends SwingWorker<CpeDescription,Void> {

	private TermSuite termSuite;
	
	public void setTermSuite(TermSuite termSuite) {
		this.termSuite = termSuite;
	}
	
	private TermSuite getTermSuite() {
		return this.termSuite;
	}
		
	private CpeDescription termSuiteProcessor;
	
	private void setTermSuiteProcessor() throws CpeDescriptorException {
		this.termSuiteProcessor = CpeDescriptorFactory.produceDescriptor();
		Runtime runtime = Runtime.getRuntime();
        int threads = runtime.availableProcessors();
		this.termSuiteProcessor.setProcessingUnitThreadCount(threads);
		this.termSuiteProcessor.getCpeCasProcessors().setPoolSize(threads);
		this.termSuiteProcessor.getCpeCasProcessors().setConcurrentPUCount(threads);
	}

	@Override
	protected CpeDescription doInBackground() throws Exception {
		this.setTermSuiteProcessor();
		this.setTermSuiteCollector();
		this.setTermSuiteAnnotators();
		this.setTermSuiteExtractor();
		File file = File.createTempFile("TermSuiteProcessor-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.termSuiteProcessor.toXML(stream);
		return this.termSuiteProcessor;
	}
	
	private void setTermSuiteCollector() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/engines/TermSuiteCollector.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		ConfigurationParameterSettings parameters = this.getTermSuite().getParameters().getMetaData().getConfigurationParameterSettings();
		settings.setParameterValue("SourceLanguage", parameters.getParameterValue("SourceLanguage"));
		settings.setParameterValue("SourceDirectories",parameters.getParameterValue("SourceDirectories"));
		settings.setParameterValue("TargetLanguage",parameters.getParameterValue("TargetLanguage"));
		settings.setParameterValue("TargetDirectories",parameters.getParameterValue("TargetDirectories"));
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCollectionReader(termSuiteCollector);
	}
	
	private void setTermSuiteExtractor() throws Exception {
		Annotator extractor = new TermSuiteExtractor();		
		CpeIntegratedCasProcessor termSuiteExtractor = CpeDescriptorFactory.produceCasProcessor(extractor.getName());
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(extractor.getFile().toURI().toString());
		termSuiteExtractor.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		termSuiteExtractor.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCasProcessor(termSuiteExtractor);
	}
	
	private void setTermSuiteAnnotators() throws Exception {
		Annotator annotators = new TermSuiteAnnotators();
		CpeIntegratedCasProcessor termSuiteAnnotators = CpeDescriptorFactory.produceCasProcessor(annotators.getName());
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(annotators.getFile().toURI().toString());
		termSuiteAnnotators.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		termSuiteAnnotators.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCasProcessor(termSuiteAnnotators);
	}
		
	private class TermSuiteAnnotators extends Annotator {

		public TermSuiteAnnotators() throws Exception {
			super("Term Suite Annotators");
		}

		@Override 
		protected void setFlowController() {
			Import fcImport = this.getFactory().createImport();
			fcImport.setName("eu/project/ttc/controllers/LanguageFlowController");
			FlowControllerDeclaration fcDecl = this.getFactory().createFlowControllerDeclaration(); 
			fcDecl.setImport(fcImport);
			fcDecl.setKey("LanguageFlowController");
			this.getAnalysisEngineDescription().setFlowControllerDeclaration(fcDecl);
		}
		
		@Override
		protected void setConfigurationParameterDeclarations() {
			this.setParameter("TreeTaggerHomeDirectory", ConfigurationParameter.TYPE_STRING);
		}
		
		@Override
		protected NameValuePair[] getNameValuePairs() {
			NameValuePair home = this.getFactory().createNameValuePair();
			home.setName("TreeTaggerHomeDirectory");
			ConfigurationParameterSettings settings = getTermSuite().getParameters().getMetaData().getConfigurationParameterSettings();
			String value = (String) settings.getParameterValue(home.getName());
			home.setValue(value);
			return new NameValuePair[] { home };
		}

		@Override
		protected String[] getFlow() {
			String[] flows = new String[2];
			ConfigurationParameterSettings settings = getTermSuite().getParameters().getMetaData().getConfigurationParameterSettings();
			flows[0] = this.getAnnotator(settings, "SourceLanguage");
			flows[1] = this.getAnnotator(settings, "TargetLanguage");;
			return flows;
		}

	private String getAnnotator(ConfigurationParameterSettings settings,String name) {
		String code = (String) settings.getParameterValue(name);
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		this.getAnalysisEngineDescription().getAnalysisEngineMetaData().getCapabilities()[0].addSupportedLanguage(code);
		return "eu.project.ttc.engines." + language.toLowerCase() + "." + language + "LinguisticAnalysis";
	}
	
	}
	
	private class TermSuiteExtractor extends Annotator {

		public TermSuiteExtractor() throws Exception {
			super("Term Suite Extractor");
		}
		
		@Override
		protected void setExternalResources() {
			ExternalResourceDescription resource = this.getFactory().createExternalResourceDescription();
			resource.setName(TermBankResource.class.getSimpleName());
			resource.setImplementationName(TermBankResource.class.getCanonicalName());
			FileResourceSpecifier specifier = this.getFactory().createFileResourceSpecifier();
			String path = TermBankResource.class.getCanonicalName().replaceAll("\\.","/") + ".class";
			URL url = this.getClass().getClassLoader().getResource(path);
			specifier.setFileUrl(url.toString());
			resource.setResourceSpecifier(specifier);
			ExternalResourceDescription[] resources = new ExternalResourceDescription[] { resource };
			ExternalResourceDependency dependency = this.getFactory().createExternalResourceDependency();
			dependency.setKey(TermBank.class.getSimpleName());
			dependency.setInterfaceName(TermBank.class.getCanonicalName());
			ExternalResourceDependency[] dependencies = new ExternalResourceDependency[] { dependency };
			ExternalResourceBinding binding = this.getFactory().createExternalResourceBinding();
			binding.setKey(TermBank.class.getSimpleName());
			binding.setResourceName(TermBankResource.class.getSimpleName());
			ExternalResourceBinding[] bindings = new ExternalResourceBinding[] { binding };
			this.getAnalysisEngineDescription().setExternalResourceDependencies(dependencies);
			ResourceManagerConfiguration cfg = this.getFactory().createResourceManagerConfiguration();
			this.getAnalysisEngineDescription().setResourceManagerConfiguration(cfg);
			this.getAnalysisEngineDescription().getResourceManagerConfiguration().setExternalResources(resources);
			this.getAnalysisEngineDescription().getResourceManagerConfiguration().setExternalResourceBindings(bindings);
		}
				
		@Override
		protected NameValuePair[] getNameValuePairs() {
			/*
			NameValuePair type = this.getFactory().createNameValuePair();
			type.setName("AnnotationType");
			type.setValue(TermAnnotation.class.getCanonicalName());
			NameValuePair size = this.getFactory().createNameValuePair();
			size.setName("NeighbourSize");
			size.setValue(7);
			return new NameValuePair[] { type , size };
			*/
			return new NameValuePair[0];
		}

		@Override
		protected void setConfigurationParameterDeclarations() {
			// this.setParameter("AnnotationType", ConfigurationParameter.TYPE_STRING);
			// this.setParameter("NeighbourSize", ConfigurationParameter.TYPE_INTEGER);
		}
				
		@Override
		protected String[] getFlow() {
			String[] flows = new String[2];
			// flows[0] = NeighbourAnnotator.class.getCanonicalName();
			flows[0] = TermBankIndexer.class.getCanonicalName();
			flows[1] = TermBankWriter.class.getCanonicalName();
			return flows;
		}
		
		}
	
}
