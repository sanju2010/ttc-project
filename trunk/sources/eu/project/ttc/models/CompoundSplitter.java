package eu.project.ttc.models;

import java.net.URI;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.dictionaries.Dictionary;

import uima.sandbox.indexer.resources.IndexListener;

public class CompoundSplitter implements IndexListener {
		
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	private String path;
	
	private void setPath(String path) throws Exception {
		this.path = path;
		URI uri = URI.create(path);
		this.getDictionary().doLoad(uri);
	}
	
	private String getPath() {
		return this.path;
	}
	
	@Override 
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);

			String path = (String) context.getConfigParameterValue("File");
			if (path != null && this.getPath() == null) {
				this.setPath(path);
			}
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
		}
	}
	
}
