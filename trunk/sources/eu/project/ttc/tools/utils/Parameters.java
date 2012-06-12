package eu.project.ttc.tools.utils;

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

public abstract class Parameters {

	protected void addParameter(
			ConfigurationParameterDeclarations declarations, String name,
			String type, boolean multiValued, boolean mandatory) {
		ConfigurationParameter parameter = UIMAFramework
				.getResourceSpecifierFactory().createConfigurationParameter();
		parameter.setName(name);
		parameter.setType(type);
		parameter.setMultiValued(multiValued);
		parameter.setMandatory(mandatory);
		declarations.addConfigurationParameter(parameter);
	}

	protected void addParameter(
			ConfigurationParameterDeclarations declarations, String name,
			String type, boolean multiValued, boolean mandatory,
			String description) {
		ConfigurationParameter parameter = UIMAFramework
				.getResourceSpecifierFactory().createConfigurationParameter();
		parameter.setName(name);
		parameter.setType(type);
		parameter.setMultiValued(multiValued);
		parameter.setMandatory(mandatory);
		parameter.setDescription(description);
		declarations.addConfigurationParameter(parameter);
	}

	private ResourceMetaData metaData;

	protected abstract void setMetaData(
			ConfigurationParameterDeclarations declarations);

	protected abstract String[][] getParameterGroups();

	public ResourceMetaData getMetaData() {
		return this.metaData;
	}

	public void validate() throws ResourceConfigurationException {
		this.getMetaData().validateConfigurationParameterSettings();
	}

	private SettingViewer viewer;

	private void setViewer() {
		this.viewer = new SettingViewer();
		this.setGroups();
		this.viewer.update(this.getMetaData());
	}

	private void setGroups() {
		String[][] groups = this.getParameterGroups();
		if (groups == null)
			return;

		for (int i = 0; i < groups.length; i += 2) {
			viewer.addParameterGroup(groups[i][0], groups[i][1],
					Boolean.parseBoolean(groups[i][2]), groups[i + 1]);
		}
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

	public Parameters(String resource) {
		this.setResource(resource);
		this.doLoad();
		this.setViewer();
		this.setComponent();
	}

	private String resource;

	private void setResource(String resource) {
		this.resource = resource;
	}

	private String getResource() {
		return this.resource;
	}

	public void doSave() throws IOException, SAXException {
		this.doUpdate();
		// String path = System.getProperty("user.home") + File.separator + "."
		// + this.getResource();
		OutputStream out = new FileOutputStream(this.getResource());
		this.getMetaData().toXML(out);
		out.close();
	}

	private void doLoad() {
		// String path = System.getProperty("user.home") + File.separator +
		// this.getResource();
		try {
			File file = new File(this.getResource());
			if (file.exists()) {
				XMLInputSource input = new XMLInputSource(this.getResource());
				this.metaData = UIMAFramework.getXMLParser()
						.parseResourceMetaData(input);
			} else {
				ConfigurationParameterDeclarations declarations = UIMAFramework
						.getResourceSpecifierFactory()
						.createConfigurationParameterDeclarations();
				this.setMetaData(declarations);
				this.metaData = UIMAFramework.getResourceSpecifierFactory()
						.createResourceMetaData();
				this.metaData
						.setConfigurationParameterDeclarations(declarations);
				ConfigurationParameterSettings settings = UIMAFramework
						.getResourceSpecifierFactory()
						.createConfigurationParameterSettings();
				this.metaData.setConfigurationParameterSettings(settings);
			}
		} catch (Exception e) {
			UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
		}
	}

	public void doUpdate() {
		ConfigurationParameterSettings settings = this.getMetaData()
				.getConfigurationParameterSettings();
		List<Field> fields = this.getViewer().getFields();
		for (Field field : fields) {
			if (field.isModified()) {
				String name = field.getName();
				Object value = field.getValue();
				settings.setParameterValue(name, value);
			}
		}
	}

}
