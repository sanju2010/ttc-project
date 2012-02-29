package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;

import fr.free.rocheteau.jerome.dunamis.fields.Field;
import fr.free.rocheteau.jerome.dunamis.fields.FieldFactory;

public class SettingViewer {
	
	private List<Field> fields;
	
	private void setFields() {
		this.fields = new ArrayList<Field>();
	}
	
	public List<Field> getFields() {
		return this.fields;
	}
	
	private GridBagConstraints constraint;
	private JPanel component;
	
	private void setComponent() {
		this.component = new JPanel(new GridBagLayout());
		this.constraint = new GridBagConstraints();
		this.constraint.anchor = GridBagConstraints.LINE_START;
		this.constraint.gridx = 0;
	}
	
	public JPanel getComponent() {
		return this.component;
	}
	
	public SettingViewer() {
		this.setFields();
		this.setComponent();
	}
	
	public void update(ResourceMetaData metadata,CasProcessorConfigurationParameterSettings overrides) {
		this.constraint.gridy = 0;
		this.getFields().clear();
		ConfigurationParameterDeclarations declarations = metadata.getConfigurationParameterDeclarations();
	    ConfigurationParameter[] parameters = declarations.getConfigurationParameters();
	    ConfigurationParameterSettings settings = metadata.getConfigurationParameterSettings();
	    if (parameters == null || settings == null) {
	    	return;
	    } else {
	    	for (int index = 0; index < parameters.length; index++) {
	    		String name = parameters[index].getName();
	    		String type = parameters[index].getType();
	    		boolean multi = parameters[index].isMultiValued();
	    		Object value = settings.getParameterValue(name);
	    		Object override = overrides.getParameterValue(name);
	    		String description = parameters[index].getDescription();
	    		if (override != null) {
	    			value = override;
	    		}
	    		Field field = FieldFactory.getComponent(type,name,multi,value, description);
	    		if (field != null) {
		    		this.getFields().add(field);
		    		this.constraint.gridy++;
		    		this.getComponent().add(field.getComponent(),this.constraint);
		    	}
	    	}
	    }
	}
	
	public void update(ResourceMetaData metadata) {
		this.constraint.gridy = 0;
		this.getFields().clear();
		ConfigurationParameterDeclarations declarations = metadata.getConfigurationParameterDeclarations();
	    ConfigurationParameter[] parameters = declarations.getConfigurationParameters();
	    ConfigurationParameterSettings settings = metadata.getConfigurationParameterSettings();
	    if (parameters == null || settings == null) {
	    	return;
	    } else {
	    	for (int index = 0; index < parameters.length; index++) {
	    		String name = parameters[index].getName();
	    		String type = parameters[index].getType();
	    		boolean multi = parameters[index].isMultiValued();
	    		Object value = settings.getParameterValue(name);
	    		String description = parameters[index].getDescription();
	    		Field field = FieldFactory.getComponent(type,name,multi,value, description);
	    		if (field != null) {
		    		this.getFields().add(field);
		    		this.constraint.gridy++;
		    		this.getComponent().add(field.getComponent(),this.constraint);
		    	}
	    	}
	    }
	}
    
}
