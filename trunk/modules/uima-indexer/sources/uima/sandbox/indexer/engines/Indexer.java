package uima.sandbox.indexer.engines;

import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;

public abstract class Indexer extends JCasMultiplier_ImplBase {
				
	private String feature;
	
	private void setFeature(String feature) {
		this.feature = feature;
	}
	
	private String getFeature() {
		return this.feature;
	}
	
	private Feature getFeature(JCas cas) throws Exception {
		String[] items = this.feature.split(":");
		if (items.length == 2) {
			Type type = cas.getRequiredType(items[0]); 
			return cas.getRequiredFeature(type, items[1]);
		} else {
			throw new Exception(this.feature);
		}
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getFeature() == null) {
				String feature = (String) context.getConfigParameterValue("Feature");
				this.setFeature(feature);
			}
			this.initialize();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	protected abstract void initialize() throws Exception;

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			boolean hasNext = this.retrieve(cas);
			this.enableHasNext(hasNext);
			this.update(cas);
		} catch(Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	protected abstract void update(JCas cas) throws Exception;

	private boolean retrieve(JCas cas) throws Exception {
		Feature feature = this.getFeature(cas);
		String range = feature.getRange().getName();
		String bool = CAS.TYPE_NAME_BOOLEAN;
		if (range.equals(bool)) {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(feature.getDomain());
			Iterator<Annotation> iterator = index.iterator();
			if (iterator.hasNext()) {
				Annotation annotation = iterator.next();
				return annotation.getBooleanValue(feature);
			} else {
				throw new Exception("No annotation of type " + feature.getDomain().getName());
			}
		} else {
			throw new Exception(this.feature + " should rande over " + bool + " but ranges over " + range);
		}
	}

	private boolean hasNext;
	
	private void enableHasNext(boolean enabled) {
		this.hasNext = enabled;
	}
	
	@Override
	public boolean hasNext() throws AnalysisEngineProcessException {
		return this.hasNext;
	}

	@Override
	public JCas next() throws AnalysisEngineProcessException {
		try {
			this.enableHasNext(false);
			JCas cas = this.getEmptyJCas();
			this.release(cas);
			return cas;
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	protected abstract void release(JCas cas) throws Exception;
	
}
