package eu.project.ttc.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.swing.SwingWorker;

import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
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
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ExternalResourceBinding;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;
import org.apache.uima.util.InvalidXMLException;
import org.xml.sax.SAXException;

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
	
	private void setTermSuiteCollector() throws CpeDescriptorException, URISyntaxException {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/engines/TermSuiteCollector.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("SourceLanguage",this.getTermSuite().getParameters().getSourceLanguage());
		settings.setParameterValue("SourceDirectories",this.getTermSuite().getParameters().getSourceDirectories());
		settings.setParameterValue("TargetLanguage",this.getTermSuite().getParameters().getTargetLanguage());
		settings.setParameterValue("TargetDirectories",this.getTermSuite().getParameters().getTargetDirectories());
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCollectionReader(termSuiteCollector);
	}
	
	private void setTermSuiteExtractor() throws CpeDescriptorException, URISyntaxException, InvalidXMLException, IOException, ResourceInitializationException, ResourceConfigurationException, SAXException {		
		File file = File.createTempFile("TermSuiteExtractor-",".xml");
		file.deleteOnExit();
		this.setTermSuiteExtractorDescriptor(file);
		CpeIntegratedCasProcessor termSuiteExtractor = CpeDescriptorFactory.produceCasProcessor("Term Suite Extractor");
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(file.toURI().toString());
		termSuiteExtractor.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		termSuiteExtractor.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCasProcessor(termSuiteExtractor);
	}

	private void setTermSuiteExtractorDescriptor(File file) throws CpeDescriptorException, IOException, InvalidXMLException, SAXException, ResourceInitializationException, ResourceConfigurationException {
		ResourceSpecifierFactory factory = UIMAFramework.getResourceSpecifierFactory();		
		AnalysisEngineDescription ae = factory.createAnalysisEngineDescription();
		ae.setPrimitive(false);
		this.setMetaData(factory, ae, "Term Suite Extractor");
		this.setOperationalProperties(factory,ae);
		this.setParameters(factory, ae);
		this.setTermSuiteExtractorParameterSettings(factory, ae);
		this.setCapabilities(factory, ae);
		// this.setTermSuiteAnnotatorFlowController(factory, ae);
		this.setTermSuiteExtractorFlowConstraints(factory, ae);
		this.setTermSuiteExtractorResource(factory, ae);
		ae.validate();
		this.doStore(ae,file);
		ae.doFullValidation();
	}
	
	private void setTermSuiteAnnotators() throws CpeDescriptorException, InvalidXMLException, ResourceInitializationException, ResourceConfigurationException, IOException, SAXException {
		File file = File.createTempFile("TermSuiteAnnotators-",".xml");
		file.deleteOnExit();
		this.setTermSuiteAnnotatorDescriptor(file);
		CpeIntegratedCasProcessor termSuiteAnnotators = CpeDescriptorFactory.produceCasProcessor("Term Suite Annotators");
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(file.toURI().toString());
		termSuiteAnnotators.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		termSuiteAnnotators.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCasProcessor(termSuiteAnnotators);
	}

	private void setTermSuiteAnnotatorDescriptor(File file) throws CpeDescriptorException, IOException, InvalidXMLException, SAXException, ResourceInitializationException, ResourceConfigurationException {
		ResourceSpecifierFactory factory = UIMAFramework.getResourceSpecifierFactory();		
		AnalysisEngineDescription ae = factory.createAnalysisEngineDescription();
		ae.setPrimitive(false);
		this.setMetaData(factory, ae, "Term Suite Annotators");
		this.setOperationalProperties(factory,ae);
		this.setParameters(factory, ae);
		this.setTermSuiteAnnotatorParameterSettings(factory, ae);
		this.setCapabilities(factory, ae);
		this.setTermSuiteAnnotatorFlowController(factory, ae);
		this.setTermSuiteAnnotatorFlowConstraints(factory, ae);
		this.setTreeTaggerHomeDirectory(factory, ae);
		ae.validate();
		this.doStore(ae,file);
		ae.doFullValidation();
	}
	
	private void setMetaData(ResourceSpecifierFactory factory,AnalysisEngineDescription ae,String name) {
		AnalysisEngineMetaData md = factory.createAnalysisEngineMetaData();
		md.setName(name);
		ae.setMetaData(md);
	}
	
	private void setCapabilities(ResourceSpecifierFactory factory,AnalysisEngineDescription main) {
		Capability capability = factory.createCapability();
		Capability[] capabilities = new Capability[] { capability };
		main.getAnalysisEngineMetaData().setCapabilities(capabilities);
	}

	private void setParameters(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameterDeclarations parameters = factory.createConfigurationParameterDeclarations();
		ae.getAnalysisEngineMetaData().setConfigurationParameterDeclarations(parameters);
	}
		
	private void setTreeTaggerHomeDirectory(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameter parameter = factory.createConfigurationParameter();
		parameter.setName("TreeTaggerHomeDirectory");
		parameter.setMultiValued(false);
		parameter.setType(ConfigurationParameter.TYPE_STRING);
		Set<String> keys = ae.getDelegateAnalysisEngineSpecifiersWithImports().keySet();
		String[] overrides = new String[keys.size()];
		int index = 0;
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			overrides[index] = key + "/TreeTaggerHomeDirectory";
			index++;
		}
		parameter.setOverrides(overrides);
		ae.getAnalysisEngineMetaData().getConfigurationParameterDeclarations().addConfigurationParameter(parameter);
	}

	private void setTermSuiteExtractorParameterSettings(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameterSettings settings = factory.createConfigurationParameterSettings();
		ae.getAnalysisEngineMetaData().setConfigurationParameterSettings(settings);
		settings.setParameterSettings(new NameValuePair[] { });
	}
	
	private void setTermSuiteAnnotatorParameterSettings(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameterSettings settings = factory.createConfigurationParameterSettings();
		ae.getAnalysisEngineMetaData().setConfigurationParameterSettings(settings);
		NameValuePair home = factory.createNameValuePair();
		home.setName("TreeTaggerHomeDirectory");
		home.setValue(this.getTermSuite().getParameters().getTreeTaggerHomeDirectory());
		settings.setParameterSettings(new NameValuePair[] { home });
	}
	
	private void setTermSuiteAnnotatorFlowController(ResourceSpecifierFactory factory,AnalysisEngineDescription main) {
		Import fcImport = factory.createImport();
		fcImport.setName("eu/project/ttc/controllers/LanguageFlowController");
		FlowControllerDeclaration fcDecl = factory.createFlowControllerDeclaration(); 
		fcDecl.setImport(fcImport);
		fcDecl.setKey("LanguageFlowController");
		main.setFlowControllerDeclaration(fcDecl);
	}

	private void setOperationalProperties(ResourceSpecifierFactory factory,	AnalysisEngineDescription ae) {
		OperationalProperties opProp = factory.createOperationalProperties();
		opProp.setModifiesCas(true);
		opProp.setMultipleDeploymentAllowed(true);
		opProp.setOutputsNewCASes(false);
		ae.getAnalysisEngineMetaData().setOperationalProperties(opProp);
	}

	private void setTermSuiteExtractorFlowConstraints(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		String[] flows = new String[2];
		this.setTermBaseIndexerFlowConstraint(factory, ae, flows);
		this.setTermBaseWriterFlowConstraint(factory, ae, flows);		
		FixedFlow constraints = factory.createFixedFlow();
		constraints.setFixedFlow(flows);
		ae.getAnalysisEngineMetaData().setFlowConstraints(constraints);
	}
	
	private void setTermBaseIndexerFlowConstraint(ResourceSpecifierFactory factory,AnalysisEngineDescription ae,String[] flows) {
		Import aeImport = factory.createImport();
		aeImport.setName("fr.univnantes.lina.uima.engines.TermBaseIndexer");
		String key = "TermBaseIndexer";
		flows[0] = key;
		ae.getDelegateAnalysisEngineSpecifiersWithImports().put(key,aeImport);
	}

	private void setTermBaseWriterFlowConstraint(ResourceSpecifierFactory factory,AnalysisEngineDescription ae,String[] flows) {
		Import aeImport = factory.createImport();
		aeImport.setName("fr.univnantes.lina.uima.engines.TermBaseWriter");
		String key = "TermBaseWriter";
		flows[1] = key;
		ae.getDelegateAnalysisEngineSpecifiersWithImports().put(key,aeImport);
	}

	
	private void setTermSuiteAnnotatorFlowConstraints(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		String[] flows = new String[2];
		this.setTermSuiteSourceAnnotatorFlowConstraint(factory, ae, flows);
		this.setTermSuiteTargetAnnotatorFlowConstraint(factory, ae, flows);		
		FixedFlow constraints = factory.createFixedFlow();
		constraints.setFixedFlow(flows);
		ae.getAnalysisEngineMetaData().setFlowConstraints(constraints);
	}
	
	private void setTermSuiteSourceAnnotatorFlowConstraint(ResourceSpecifierFactory factory,AnalysisEngineDescription ae,String[] flows) {
		String code = this.getTermSuite().getParameters().getSourceLanguage();
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		ae.getAnalysisEngineMetaData().getCapabilities()[0].addSupportedLanguage(code);
		Import aeImport = factory.createImport();
		aeImport.setName("eu.project.ttc.engines." + language.toLowerCase() + "." + language + "LinguisticAnalysis");
		String key = language + "LinguisticAnalysis";
		flows[0] = key;
		ae.getDelegateAnalysisEngineSpecifiersWithImports().put(key,aeImport);
	}
	
	private void setTermSuiteTargetAnnotatorFlowConstraint(ResourceSpecifierFactory factory,AnalysisEngineDescription ae,String[] flows) {
		String code = this.getTermSuite().getParameters().getTargetLanguage();
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		ae.getAnalysisEngineMetaData().getCapabilities()[0].addSupportedLanguage(code);
		Import aeImport = factory.createImport();
		aeImport.setName("eu.project.ttc.engines." + language.toLowerCase() + "." + language + "LinguisticAnalysis");
		String key = language + "LinguisticAnalysis";
		flows[1] = key;
		ae.getDelegateAnalysisEngineSpecifiersWithImports().put(key,aeImport);
	}

	private void setTermSuiteExtractorResource(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ExternalResourceDescription desc = factory.createExternalResourceDescription();
		desc.setName("TermBaseResource");
		desc.setImplementationName("fr.univnantes.lina.uima.models.TermBaseResource");
		FileResourceSpecifier spec = factory.createFileResourceSpecifier();
		URL url = this.getClass().getClassLoader().getResource("fr/univnantes/lina/uima/models/TermBaseResource.class");
		spec.setFileUrl(url.toString());
		desc.setResourceSpecifier(spec);
		ExternalResourceDependency dep = factory.createExternalResourceDependency();
		dep.setKey("TermBase");
		dep.setInterfaceName("fr.univnantes.lina.uima.models.TermBase");
		ExternalResourceBinding bind = factory.createExternalResourceBinding();
		bind.setKey("TermBase");
		bind.setResourceName("TermBaseResource");
		ae.setExternalResourceDependencies(new ExternalResourceDependency[] { dep });
		ResourceManagerConfiguration cfg = factory.createResourceManagerConfiguration();
		ae.setResourceManagerConfiguration(cfg);
		ae.getResourceManagerConfiguration().setExternalResources(new ExternalResourceDescription[] { desc });
		ae.getResourceManagerConfiguration().setExternalResourceBindings(new ExternalResourceBinding[] { bind });
	}
	
	private void doStore(AnalysisEngineDescription ae,File file) throws FileNotFoundException, SAXException, IOException {
		FileOutputStream stream = new FileOutputStream(file);
		ae.toXML(stream,true);
		stream.close();
	}
	
}
