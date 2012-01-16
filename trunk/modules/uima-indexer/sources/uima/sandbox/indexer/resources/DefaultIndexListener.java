package uima.sandbox.indexer.resources;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

public class DefaultIndexListener implements IndexListener {

	@Override 
	public void configure(UimaContext context){ }
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) { }

	@Override
	public void release(JCas cas) { }

}
