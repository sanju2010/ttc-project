package eu.project.ttc.models;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.EditDistance;

import uima.sandbox.indexer.resources.IndexListener;

public class EditDistanceGathering implements IndexListener {
	
	private EditDistance editDistance;
	
	private void setEditDistance(String name) throws Exception {
		if (this.editDistance == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof EditDistance) {
				this.editDistance = (EditDistance) obj;
				UIMAFramework.getLogger().log(Level.INFO,"Setting Edit Distance: " + this.editDistance.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + EditDistance.class.getCanonicalName());
			}
		}
	}
	
	private EditDistance getEditDistance() {
		return this.editDistance;
	}
	
	@Override 
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			
			String editDistance = (String) context.getConfigParameterValue("EditDistanceClassName");
			this.setEditDistance(editDistance);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) { }
	
	private boolean done = false;

	@Override
	public void release(JCas cas) { 
		if (!this.done) {
			this.done = true;
		}
	}
	
}
