package eu.project.ttc.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.util.InvalidXMLException;
import org.xml.sax.SAXException;

import eu.project.ttc.tools.Corpora.Corpus;
import eu.project.ttc.tools.Languages.Language;

public class Processor extends SwingWorker<CpeDescription,Void> {

	private String[] directories;
	
	private void setDirectories(List<Corpus> corpora) {
		int size = corpora.size();
		this.directories = new String[size];
		for (int index = 0; index < size; index++) {
			Corpus corpus = corpora.get(index);
			String directory = corpus.getFile().getAbsolutePath();
			this.directories[index] = directory;
		}
	}
	
	private String[] getDirectories() {
		return this.directories;
	}
	
	private Map<String,String> languages;
	
	private void setLanguages(List<Language> languages) {
		this.languages = new HashMap<String,String>();
		for (Language language : languages) {
			this.languages.put(language.toString(),language.getCode());
		}
	}
	
	private String[] getLanguages() {
		String[] languages = new String[this.languages.size()];
		return this.languages.keySet().toArray(languages);
	}
	
	private String[] getCodes() {
		String[] codes = new String[this.languages.size()];
		return this.languages.values().toArray(codes);
	}
	
	public Processor(List<Corpus> corpora,List<Language> languages) {
		this.setDirectories(corpora);
		this.setLanguages(languages);
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
		File file = File.createTempFile("TermSuiteProcessor-",".xml");
		file.deleteOnExit();
		OutputStream stream = new FileOutputStream(file);
		this.termSuiteProcessor.toXML(stream);
		return this.termSuiteProcessor;
	}
	
	private void setTermSuiteCollector() throws CpeDescriptorException, URISyntaxException {
		URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/TermSuiteCollector.xml");
		CpeCollectionReader termSuiteCollector = CpeDescriptorFactory.produceCollectionReader(url.toURI().toString());
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("Directories",this.getDirectories());
		settings.setParameterValue("SofaName","_InitialView");
		termSuiteCollector.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCollectionReader(termSuiteCollector);
	}

	private void setTermSuiteAnnotators() throws CpeDescriptorException, InvalidXMLException, ResourceInitializationException, ResourceConfigurationException, IOException, SAXException {
		File file = File.createTempFile("TermSuiteAnnotators-",".xml");
		file.deleteOnExit();
		this.setDescriptor(file);
		CpeIntegratedCasProcessor termSuiteAnnotators = CpeDescriptorFactory.produceCasProcessor("Term Suite Annotators");
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(file.toURI().toString());
		termSuiteAnnotators.setCpeComponentDescriptor(desc);
		CasProcessorConfigurationParameterSettings settings = CpeDescriptorFactory.produceCasProcessorConfigurationParameterSettings();
		settings.setParameterValue("SofaName",new String[]{"_InitialView"});
		termSuiteAnnotators.setConfigurationParameterSettings(settings);
		this.termSuiteProcessor.addCasProcessor(termSuiteAnnotators);
	}

	private void setDescriptor(File file) throws CpeDescriptorException, IOException, InvalidXMLException, SAXException, ResourceInitializationException, ResourceConfigurationException {
		ResourceSpecifierFactory factory = UIMAFramework.getResourceSpecifierFactory();		
		AnalysisEngineDescription ae = factory.createAnalysisEngineDescription();
		ae.setPrimitive(false);
		this.setMetaData(factory, ae);
		this.setOperationalProperties(factory,ae);
		this.setParameters(factory, ae);
		this.setSofaNames(factory, ae);
		this.setHome(factory, ae);
		this.setExpand(factory, ae);
		this.setParameterSettings(factory, ae);
		this.setCapabilities(factory, ae);
		this.setFlowController(factory, ae);
		this.setFlowConstraints(factory, ae);
		ae.validate();
		this.doStore(ae,file);
		ae.doFullValidation();
	}
	
	private void setMetaData(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		AnalysisEngineMetaData md = factory.createAnalysisEngineMetaData();
		md.setName("Term Suite Annotators");
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
	
	private void setSofaNames(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameter parameter = factory.createConfigurationParameter();
		parameter.setName("SofaNames");
		parameter.setMultiValued(true);
		parameter.setType(ConfigurationParameter.TYPE_STRING);
		Set<String> keys = ae.getDelegateAnalysisEngineSpecifiersWithImports().keySet();
		String[] overrides = new String[keys.size()];
		int index = 0;
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			overrides[index] = key + "/SofaNames";
			index++;
		}
		parameter.setOverrides(overrides);
		ae.getAnalysisEngineMetaData().getConfigurationParameterDeclarations().addConfigurationParameter(parameter);
	}
	
	private void setHome(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
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
	
	private void setExpand(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameter parameter = factory.createConfigurationParameter();
		parameter.setName("ExpandPartOfSpeech");
		parameter.setMultiValued(false);
		parameter.setType(ConfigurationParameter.TYPE_BOOLEAN);
		Set<String> keys = ae.getDelegateAnalysisEngineSpecifiersWithImports().keySet();
		String[] overrides = new String[keys.size()];
		int index = 0;
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			overrides[index] = key + "/ExpandPartOfSpeech";
			index++;
		}
		parameter.setOverrides(overrides);
		ae.getAnalysisEngineMetaData().getConfigurationParameterDeclarations().addConfigurationParameter(parameter);
	}
	
	private void setParameterSettings(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		ConfigurationParameterSettings settings = factory.createConfigurationParameterSettings();
		ae.getAnalysisEngineMetaData().setConfigurationParameterSettings(settings);
		NameValuePair pair = factory.createNameValuePair();
		pair.setName("SofaNames");
		pair.setValue(new String[]{"_InitialView"});
		NameValuePair home = factory.createNameValuePair();
		home.setName("TreeTaggerHomeDirectory");
		home.setValue(new File("/usr/local/share/tree-tagger").getAbsolutePath());
		NameValuePair expand = factory.createNameValuePair();
		expand.setName("ExpandPartOfSpeech");
		expand.setValue(new Boolean(true));
		settings.setParameterSettings(new NameValuePair[] { pair, home, expand });
	}
	
	private void setFlowController(ResourceSpecifierFactory factory,AnalysisEngineDescription main) {
		Import fcImport = factory.createImport();
		fcImport.setName("fr/univnantes/lina/uima/engines/LanguageFlowController");
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

	private void setFlowConstraints(ResourceSpecifierFactory factory,AnalysisEngineDescription ae) {
		String[] flows = new String[this.getLanguages().length];
		for (int index = 0; index < this.getLanguages().length; index++) {
			String language = this.getLanguages()[index];
			String code = this.getCodes()[index];
			ae.getAnalysisEngineMetaData().getCapabilities()[0].addSupportedLanguage(code);
			Import aeImport = factory.createImport();
			aeImport.setName("eu.project.ttc.engines." + language + "LinguisticAnalysis");
			String key = language + "LinguisticAnalysis";
			flows[index] = key;
			ae.getDelegateAnalysisEngineSpecifiersWithImports().put(key,aeImport);
		}
		FixedFlow constraints = factory.createFixedFlow();
		constraints.setFixedFlow(flows);
		ae.getAnalysisEngineMetaData().setFlowConstraints(constraints);
	}

	private void doStore(AnalysisEngineDescription ae,File file) throws FileNotFoundException, SAXException, IOException {
		FileOutputStream stream = new FileOutputStream(file);
		ae.toXML(stream,true);
		stream.close();
	}
	
}
