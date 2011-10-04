package eu.project.ttc.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JScrollPane;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.xml.sax.SAXException;

import fr.univnantes.lina.uima.tools.dunamis.fields.Field;
import fr.univnantes.lina.uima.tools.dunamis.viewers.SettingViewer;

public class Parameters {
	
	private ConfigurationParameter sourceLanguage;
	
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
		this.sourceDirectories.setName("SourceDirectories");
		this.sourceDirectories.setType(ConfigurationParameter.TYPE_STRING);
		this.sourceDirectories.setMultiValued(true);		
	}
	
	private ConfigurationParameter getSourceDirectories() {
		return this.sourceDirectories;
	}
	
	private ConfigurationParameter targetDirectories;
	
	private void setTargetDirectories() {
		this.targetDirectories = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.targetDirectories.setName("TargetDirectories");
		this.targetDirectories.setType(ConfigurationParameter.TYPE_STRING);
		this.targetDirectories.setMultiValued(true);
	}
	
	private ConfigurationParameter getTargetDirectories() {
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
	
	private ConfigurationParameter termBankFile;
	
	private void setTermBankFile() {
		this.termBankFile = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.termBankFile.setName("TermBankFile");
		this.termBankFile.setType(ConfigurationParameter.TYPE_STRING);
		this.termBankFile.setMultiValued(false);
	}
	
	private ConfigurationParameter getTermBankFile() {
		return this.termBankFile;
	}
	
	private ConfigurationParameter termContextBenchFile;
	
	private void setTermContextBenchFile() {
		this.termContextBenchFile = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameter();
		this.termContextBenchFile.setName("TermContextBenchFile");
		this.termContextBenchFile.setType(ConfigurationParameter.TYPE_STRING);
		this.termContextBenchFile.setMultiValued(false);
	}
	
	private ConfigurationParameter getTermContextBenchFile() {
		return this.termContextBenchFile;
	}
	
	private ResourceMetaData hiddenMetaData;
	
	private void setHiddentMetaData() throws IOException {
		ConfigurationParameterDeclarations declarations = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterDeclarations();
		declarations.addConfigurationParameter(this.getTermBankFile());
		declarations.addConfigurationParameter(this.getTermContextBenchFile());
		this.hiddenMetaData = UIMAFramework.getResourceSpecifierFactory().createResourceMetaData();
		this.hiddenMetaData.setConfigurationParameterDeclarations(declarations);
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		File termBankFile = File.createTempFile("term-bank-", ".obj");
		termBankFile.deleteOnExit();
		File termContextBenchFile = File.createTempFile("term-context-", ".xmi");
		termContextBenchFile.deleteOnExit();
		settings.setParameterValue("TermBankFile", termBankFile.getAbsolutePath());
		settings.setParameterValue("TermContextBenchFile", termContextBenchFile.getAbsolutePath());
		this.hiddenMetaData.setConfigurationParameterSettings(settings);
	}
	
	public ResourceMetaData getHiddentMetaData() {
		return this.hiddenMetaData;
	}
	
	private ResourceMetaData metaData;
	
	private void setMetaData() {
		ConfigurationParameterDeclarations declarations = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterDeclarations();
		declarations.addConfigurationParameter(this.getSourceLanguage());
		declarations.addConfigurationParameter(this.getSourceDirectories());
		declarations.addConfigurationParameter(this.getTargetLanguage());
		declarations.addConfigurationParameter(this.getTargetDirectories());
		declarations.addConfigurationParameter(this.getTreeTaggerHomeDirectory());
		this.metaData = UIMAFramework.getResourceSpecifierFactory().createResourceMetaData();
		this.metaData.setConfigurationParameterDeclarations(declarations);
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		this.metaData.setConfigurationParameterSettings(settings);
	}
	
	public ResourceMetaData getMetaData() {
		return this.metaData;
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
		this.setSourceLanguage();
		this.setTargetLanguage();
		this.setSourceDirectories();
		this.setTargetDirectories();
		this.setTreeTaggerHomeDirectory();
		this.setTermBankFile();
		this.setTermContextBenchFile();
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
