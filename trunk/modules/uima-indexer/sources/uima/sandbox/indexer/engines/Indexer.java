package uima.sandbox.indexer.engines;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.indexer.resources.Index;
import uima.sandbox.indexer.resources.IndexListener;

public class Indexer extends JCasAnnotator_ImplBase {
		
	private Index index;
	
	private void setIndex(Index index) {
		this.index = index;
	}
	
	private Index getIndex() {
		return this.index;
	}
	
	private IndexListener listener;
	
	private void setListener(IndexListener listener) {
		this.listener = listener;
	}
	
	private IndexListener getListener() {
		return this.listener;
	}
	
	private String type;
		
	private void setType(String type) {
		this.type = type;
	}
	
	private String getType() {
		return this.type;
	}
	
	private Type getType(JCas cas) throws CASException {
		return cas.getRequiredType(this.type);
	}
		
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getIndex() == null) {
				Index index = (Index) context.getResourceObject("Index");
				this.setIndex(index);				
			}
			if (this.getListener() == null) {
				IndexListener listener = (IndexListener) context.getResourceObject("Listener");
				this.setListener(listener);
				listener.configure(context);				
			}
			if (this.getType() == null) {
				String type = (String) context.getConfigParameterValue("Type");
				this.setType(type);				
			}			
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			if (this.getType() != null) {
				Type type = this.getType(cas);
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
				FSIterator<Annotation> iterator = index.iterator();
				while (iterator.hasNext()) {
					Annotation annotation = iterator.next();
					if (this.getListener() != null) {
						this.getListener().index(annotation);						
					}
				}
			}
		} catch(Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			if (this.getListener() != null) {
				this.getListener().release(this.getIndex().get());				
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
}
