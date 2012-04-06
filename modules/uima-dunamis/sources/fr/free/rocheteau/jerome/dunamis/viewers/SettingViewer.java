package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.uima.collection.metadata.CasProcessorConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceMetaData;

import fr.free.rocheteau.jerome.dunamis.fields.Field;
import fr.free.rocheteau.jerome.dunamis.fields.FieldFactory;
import fr.free.rocheteau.jerome.dunamis.fields.FloatField;
import fr.free.rocheteau.jerome.dunamis.fields.IntegerField;
import fr.free.rocheteau.jerome.dunamis.fields.StringItemField;
import java.awt.Graphics;
import java.awt.Graphics2D;   

public class SettingViewer {
	

	public class MyJPanelGradient extends JPanel {
		
		@Override
		protected void paintComponent( Graphics g ) {

		if ( !isOpaque( ) ) {

		super.paintComponent( g );

		return;

		}
		Graphics2D g2d = (Graphics2D) g;

		//to get height and width of the component

		int w = getWidth();

		int h = getHeight();

		//generating two colors for gradient pattern

		/*parameters are consentration of Red, Blue and Green color in HEX  format*/

		Color color1 = new Color(0xeb,0xeb,0xeb);

		//Color color2 = new Color(0xa2,0xbd,0xd8);

		/*generating gradient pattern from two colors*/

		GradientPaint gp = new GradientPaint( 0, 0, color1, 0, h, Color.lightGray );

		g2d.setPaint( gp ); //set gradient color to graphics2D object

		g2d.fillRect( 0, 0, w, h ); //filling color

		setOpaque( false );
		super.paintComponent( g );

		setOpaque( true );
		}
	}
	private List<Field> fields;
	
	private void setFields() {
		this.fields = new ArrayList<Field>();
	}
	
	public List<Field> getFields() {
		return this.fields;
	}
	
	private GridBagConstraints constraint;
	private MyJPanelGradient component;
	
	private Field cb;
	private Field f1;
	private Field f2;
	private Field f3;
	
	private void setComponent() {
		this.component = new MyJPanelGradient();
		this.component.setLayout(new GridBagLayout());
		this.constraint = new GridBagConstraints();
		this.constraint.fill = GridBagConstraints.VERTICAL;
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
	    		/*if (field != null) {

    				if (name.endsWith("EnableTermConflating"))
					{
					cb=field;
					}
    				else if (name.endsWith("EditDistanceClassName"))
    				{
    				f1=field;
    				}
    				else if (name.endsWith("EditDistanceThreshold"))
    				{
    				f2=field;	
    				}
    				else if (name.endsWith("EditDistanceNgrams"))
    				{
    				f3=field;
    				cb.setListener(f1,f2,f3);
    				}
	   
		    		this.getFields().add(field);
		    		this.constraint.gridy++;
		    		this.getComponent().add(field.getComponent(),this.constraint);
		    	}*/
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
	    	/*	if (field != null) {
    				if (name.endsWith("EnableTermConflating"))
					{
					cb=field;
					JCheckBox c=(JCheckBox) cb.getComponent();
					c.setSelected(false);
					}
    				else if (name.endsWith("EditDistanceClassName"))
    				{
    				f1=field;
    				StringItemField f=(StringItemField)f1;
    				f.getText().setEnabled(false);
    				
    				}
    				else if (name.endsWith("EditDistanceThreshold"))
    				{
    				f2=field;	
    				FloatField f=(FloatField)f2;
    				f.getSpinner().setEnabled(false);
    				}
    				else if (name.endsWith("EditDistanceNgrams"))
    				{
    				f3=field;
    				IntegerField f=(IntegerField)f3;
    				f.getSpinner().setEnabled(false);
    				cb.setListener(f1,f2,f3);
    				} 
		    		this.getFields().add(field);
		    		this.constraint.gridy++;
		    		this.getComponent().add(field.getComponent(),this.constraint);
		    	}*/
	    	}

	    }
	}
    
}
