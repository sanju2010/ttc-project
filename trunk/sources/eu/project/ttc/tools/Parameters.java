package eu.project.ttc.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JScrollPane;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

import fr.free.rocheteau.jerome.dunamis.fields.Field;
import fr.free.rocheteau.jerome.dunamis.viewers.SettingViewer;

public class Parameters {
	
	/*private ConfigurationParameter sourceLanguage;
	
	private void setSourceLanguage() {
		this.sourceLanguage = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.sourceLanguage.setName("SourceLanguage");
		this.sourceLanguage.setType(ConfigurationParameter.TYPE_STRING);
		this.sourceLanguage.setMultiValued(false);		
	}
	
	private ConfigurationParameter getSourceLanguage() {
		return this.sourceLanguage;
	}
	
	private ConfigurationParameter targetLanguage;
	
	private void setTargetLanguage() {
		this.targetLanguage = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.targetLanguage.setName("TargetLanguage");
		this.targetLanguage.setType(ConfigurationParameter.TYPE_STRING);
		this.targetLanguage.setMultiValued(false);
	}
	
	private ConfigurationParameter getTargetLanguage() {
		return this.targetLanguage;
	}
	
	private ConfigurationParameter sourceDirectories;
	
	private void setSourceDirectories() {
		this.sourceDirectories = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.sourceDirectories.setName("SourceDirectory");
		this.sourceDirectories.setType(ConfigurationParameter.TYPE_STRING);
		this.sourceDirectories.setMultiValued(false);		
	}
	
	private ConfigurationParameter getSourceDirectories() {
		return this.sourceDirectories;
	}
	
	private ConfigurationParameter targetDirectories;
	
	private void setTargetDirectory() {
		this.targetDirectories = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.targetDirectories.setName("TargetDirectory");
		this.targetDirectories.setType(ConfigurationParameter.TYPE_STRING);
		this.targetDirectories.setMultiValued(false);
	}
	
	private ConfigurationParameter getTargetDirectory() {
		return this.targetDirectories;
	}
	
	private ConfigurationParameter treeTaggerHomeDirectory;
	
	private void setTreeTaggerHomeDirectory() {
		this.treeTaggerHomeDirectory = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.treeTaggerHomeDirectory.setName("TreeTaggerHomeDirectory");
		this.treeTaggerHomeDirectory.setType(ConfigurationParameter.TYPE_STRING);
		this.treeTaggerHomeDirectory.setMultiValued(false);
	}
	
	private ConfigurationParameter getTreeTaggerHomeDirectory() {
		return this.treeTaggerHomeDirectory;
	}
	
	private ConfigurationParameter indexSingleWordTerms;
	
	private void setIndexSingleWordTerms() {
		this.indexSingleWordTerms = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.indexSingleWordTerms.setName("IndexSingleWordTerms");
		this.indexSingleWordTerms.setType(ConfigurationParameter.TYPE_BOOLEAN);
		this.indexSingleWordTerms.setMultiValued(false);
	}
	
	private ConfigurationParameter getIndexSingleWordTerms() {
		return this.indexSingleWordTerms;
	}
	
	private ConfigurationParameter indexMultiWordTerms;
	
	private void setIndexMultiWordTerms() {
		this.indexMultiWordTerms = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.indexMultiWordTerms.setName("IndexMultiWordTerms");
		this.indexMultiWordTerms.setType(ConfigurationParameter.TYPE_BOOLEAN);
		this.indexMultiWordTerms.setMultiValued(false);
	}
	
	private ConfigurationParameter getIndexMultiWordTerms() {
		return this.indexMultiWordTerms;
	}
	
	private ConfigurationParameter indexCompoundWordTerms;
	
	private void setIndexCompoundWordTerms() {
		this.indexCompoundWordTerms = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.indexCompoundWordTerms.setName("IndexCompoundWordTerms");
		this.indexCompoundWordTerms.setType(ConfigurationParameter.TYPE_BOOLEAN);
		this.indexCompoundWordTerms.setMultiValued(false);
	}
	
	private ConfigurationParameter getIndexCompoundWordTerms() {
		return this.indexCompoundWordTerms;
	}
	
	private ConfigurationParameter enableTerminologyAlignment;
	
	private void setEnableTerminologyAlignment() {
		this.enableTerminologyAlignment = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.enableTerminologyAlignment.setName("EnableTerminolgyAlignment");
		this.enableTerminologyAlignment.setType(ConfigurationParameter.TYPE_BOOLEAN);
		this.enableTerminologyAlignment.setMultiValued(false);
	}
	
	private ConfigurationParameter getEnableTerminologyAlignment() {
		return this.enableTerminologyAlignment;
	}
	
	private ConfigurationParameter termBankFile;
	
	private void setTermBankFile() {
		this.termBankFile = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.termBankFile.setName("TermBankFile");
		this.termBankFile.setType(ConfigurationParameter.TYPE_STRING);
		this.termBankFile.setMultiValued(false);
	}
	
	private ConfigurationParameter getTermBankFile() {
		return this.termBankFile;
	}*/
	
	private void addParameter(ConfigurationParameterDeclarations declarations,String name,String type,boolean multiValued,boolean mandatory) {
		ConfigurationParameter parameter = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		parameter.setName(name);
		parameter.setType(type);
		parameter.setMultiValued(multiValued);
		parameter.setMandatory(mandatory);
		declarations.addConfigurationParameter(parameter);
	}
	
	private ResourceMetaData hiddenMetaData;
	
	private void setHiddentMetaData() throws IOException {
		ConfigurationParameterDeclarations declarations = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterDeclarations();
		this.addParameter(declarations, "TermBankFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.hiddenMetaData = UIMAFramework.getResourceSpecifierFactory().createResourceMetaData();
		this.hiddenMetaData.setConfigurationParameterDeclarations(declarations);
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		File termBankFile = File.createTempFile("term-bank-", ".obj");
		termBankFile.deleteOnExit();
		settings.setParameterValue("TermBankFile", termBankFile.getAbsolutePath());
		this.hiddenMetaData.setConfigurationParameterSettings(settings);
	}
	
	public ResourceMetaData getHiddenMetaData() {
		return this.hiddenMetaData;
	}
	
	private ResourceMetaData metaData;
	
	private void setMetaData() {
		ConfigurationParameterDeclarations declarations = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterDeclarations();
		this.addParameter(declarations, "SourceLanguage", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SourceDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetLanguage", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TargetDirectory", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "TreeTaggerHomeDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "IndexSingleWordTerms", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "IndexMultiWordTerms", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "IndexCompoundWordTerms", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "EnableTerminologyAlignment", ConfigurationParameter.TYPE_BOOLEAN, false, false);
		this.addParameter(declarations, "ScopeSize", ConfigurationParameter.TYPE_INTEGER, false, false);
		this.addParameter(declarations, "AssociationRateClassName", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "SimilarityDistanceClassName", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "DictionaryFile", ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, "EvaluationListFile", ConfigurationParameter.TYPE_STRING, false, false);
		this.metaData = UIMAFramework.getResourceSpecifierFactory().createResourceMetaData();
		this.metaData.setConfigurationParameterDeclarations(declarations);
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		this.metaData.setConfigurationParameterSettings(settings);
	}
	
	public ResourceMetaData getMetaData() {
		return this.metaData;
	}

	public void validate() throws ResourceConfigurationException {
		this.getMetaData().validateConfigurationParameterSettings();
		this.getHiddenMetaData().validateConfigurationParameterSettings();
	}
	
	private SettingViewer viewer;
	
	private void setViewer() {
		this.viewer = new SettingViewer();
		this.viewer.update(this.getMetaData());
	}
	
	private SettingViewer getViewer() {
		return this.viewer;
	}
	
	private JScrollPane component;
	
	private void setComponent() {
		this.component = new JScrollPane(this.getViewer().getComponent());
	}
	
	public JScrollPane getComponent() {
		return this.component;
	}
	
	public Parameters() {
		/*
		this.setSourceLanguage();
		this.setTargetLanguage();
		this.setSourceDirectories();
		this.setTargetDirectory();
		this.setTreeTaggerHomeDirectory();
		this.setIndexSingleWordTerms();
		this.setIndexMultiWordTerms();
		this.setIndexCompoundWordTerms();
		this.setEnableTerminologyAlignment();
		this.setTermBankFile();
		*/
		this.doLoad();
		this.setViewer();
		this.setComponent();
	}
	
	public void doSave() throws IOException, SAXException {
		this.doUpdate();
		String path = System.getProperty("user.home") + File.separator + ".term-suite.settings";
		OutputStream out = new FileOutputStream(path);
		this.getMetaData().toXML(out);
		out.close();
	}
	
	private void doLoad() {
		String path = System.getProperty("user.home") + File.separator + ".term-suite.settings";
		try {
			File file = new File(path);
			if (file.exists()) {
				XMLInputSource input = new XMLInputSource(path);
				this.metaData = UIMAFramework.getXMLParser().parseResourceMetaData(input);
			} else {
				this.setMetaData();
			}
			this.setHiddentMetaData();
		} catch (Exception e) {
			UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
		}
	}

	public void doUpdate() {
		ConfigurationParameterSettings settings = this.getMetaData().getConfigurationParameterSettings();
		List<Field> fields = this.getViewer().getFields();
		for (Field field : fields) {
			if (field.isModified()) {
				String name = field.getName();
				Object value = field.getValue();
				settings.setParameterValue(name,value);
			} 
		}
	}
	
}
