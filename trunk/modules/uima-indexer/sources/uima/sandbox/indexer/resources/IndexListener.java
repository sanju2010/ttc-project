package uima.sandbox.indexer.resources;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public interface IndexListener extends SharedResourceObject {

	public void configure(UimaContext context) throws ResourceInitializationException;
	
	public void index(Annotation annotation) throws AnalysisEngineProcessException;
	
	public void release(JCas cas) throws AnalysisEngineProcessException;
	
}
