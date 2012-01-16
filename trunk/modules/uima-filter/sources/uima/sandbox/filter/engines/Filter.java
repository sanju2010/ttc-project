package uima.sandbox.filter.engines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class Filter extends JCasAnnotator_ImplBase {

	private uima.sandbox.filter.resources.Filter filter;
	
	private void setFilter(uima.sandbox.filter.resources.Filter filter) {
		this.filter = filter;
	}
	
	private uima.sandbox.filter.resources.Filter getFilter() {
		return this.filter;
	}
	
	private String path;
	
	private void setPath(String path) throws ResourceInitializationException {
		try {
			this.path = path;
			InputStream stream = new FileInputStream(path);
			this.getFilter().load(stream);
		} catch (IOException e ) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private String getPath() {
		return this.path;
	}
	
	private String feature;
	
	private void setFeature(String feature) {
		this.feature = feature;
	}

	private Type type;
	private Feature feat;
	
	private void setFeature(JCas cas) throws AnalysisEngineProcessException {
		if (this.type == null || this.feat == null) {
			try {
				String[] elements = this.feature.split(":");
				if (elements.length == 2) {
					this.type = cas.getRequiredType(elements[0].trim());
					String feat = elements[1];
					if (feat.equals("coveredText")) {
						this.feat = null;
					}
				} else {
					this.type = cas.getRequiredType(this.feature);
					this.feat = null;
				}
			} catch (CASException e) {
				throw new AnalysisEngineProcessException(e);
			}
		}
	}
	
	private Type getType() {
		return this.type;
	}
	
	private Feature getFeature() {
		return this.feat;
	}
	
	private boolean filterIn;
	
	private void enableFilterIn(boolean filterIn,boolean filterOut) throws Exception {
		if (filterIn && !filterOut) {
			this.filterIn = false;
		} else if (!filterIn && filterOut) {
			this.filterIn = true;
		} else {
			throw new Exception("Incompatible parameter setting: 'Filter In' boolean value should be different as that of 'Filter Out'");
		}
	}
	
	private boolean doFilterIn() {
		return this.filterIn;
	}
		
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			uima.sandbox.filter.resources.Filter filter = (uima.sandbox.filter.resources.Filter) context.getResourceObject("Filter");
			this.setFilter(filter);
			
			String feature = (String) context.getConfigParameterValue("Feature");
			this.setFeature(feature);
			
			Boolean filterIn = (Boolean) context.getConfigParameterValue("FilterIn");
			Boolean filterOut = (Boolean) context.getConfigParameterValue("FilterOut");
			this.enableFilterIn(filterIn.booleanValue(),filterOut.booleanValue());
			
			String path = (String) context.getConfigParameterValue("File");
			if (path != null && this.getPath() == null) {
				this.setPath(path);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
		
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		this.setFeature(cas);
		List<Annotation> annotations = new ArrayList<Annotation>();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(this.getType());
		FSIterator<Annotation> iter = index.iterator();
		while(iter.hasNext()) {
			Annotation annotation = iter.next();
			String value = null;
			if (this.getFeature() == null) { 
				value = annotation.getCoveredText();
			} else {	
				value = annotation.getStringValue(this.getFeature());
			}
			if (value == null) {
				continue;
			} else {
				if (this.getFilter().get().contains(value)) {
					if (this.doFilterIn()) {
						annotations.add(annotation);
					}
				} else {
					if (!this.doFilterIn()) {
						annotations.add(annotation);
					}
				}
			}
		}
		for (Annotation annotation : annotations) {
			annotation.removeFromIndexes();
		}
	}
	
}
