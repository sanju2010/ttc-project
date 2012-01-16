package fr.free.rocheteau.jerome.dunamis.viewers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.uima.resource.metadata.FeatureDescription;
import org.apache.uima.resource.metadata.TypeDescription;

public class TypeViewer {

	private String name;
	
	private void setName(String name) {
		int index = name.lastIndexOf(':');
		if (index == -1) {
			this.name = name;	
		} else {
			this.name = name.substring(0,index);
			String feat = name.substring(index + 1,name.length());
			this.getFeatures().add(feat);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	private List<String> features;
	
	private void setFeatures() {
		this.features = new ArrayList<String>();
	}
	
	private List<String> getFeatures() {
		return this.features;
	}
	
	private void setFeatures(FeatureDescription[] features){
		for (FeatureDescription feature : features) {
			String feat = feature.getName();
			String range = feature.getRangeTypeName();
			this.getFeatures().add(feat + " : " + range);
		}
	}
		
	public DefaultMutableTreeNode get() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.getName());
		for (String attribute : this.getFeatures()) {
			DefaultMutableTreeNode bot = new DefaultMutableTreeNode(attribute);
			top.add(bot);
		}
		return top;
	}

	public TypeViewer(String name) {
		this.setFeatures();
		this.setName(name);
	}
	
	public TypeViewer(TypeDescription type) {
		this.setFeatures();
		this.setName(type.getName());
		this.setFeatures(type.getFeatures());
	}
	
	public String toString() {
		return this.getName();
	}
	
}
