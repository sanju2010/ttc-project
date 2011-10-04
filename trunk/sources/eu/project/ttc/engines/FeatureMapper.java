package eu.project.ttc.engines;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.models.FeatureMapping;

public class FeatureMapper extends JCasAnnotator_ImplBase {

	private FeatureMapping mapping;
	
	private void setMapping(FeatureMapping conversion) {
		this.mapping = conversion;
	}
	
	private FeatureMapping getMapping() {
		return this.mapping;
	}
	
	private void setMapping(String path) throws IOException {
		FileInputStream inputStream = new FileInputStream(path);
		this.getMapping().doLoad(inputStream);
	}
	
	private String annotationType;
	
	private void setAnnotationType(String type) {
		this.annotationType = type;
	}
	
	private Type getAnnotationType(JCas cas) {
		return cas.getTypeSystem().getType(this.annotationType);
	}
	
	private String featureName;
	
	private void setFeatureName(String name) {
		this.featureName = name;
	}
	
	private String getFeatureName() {
		return this.featureName;
	}
	
	private void setFeature(String feature) throws ResourceInitializationException {
		String[] elements = feature.split(":");
		if (elements.length == 2) {
			this.setAnnotationType(elements[0]);
			this.setFeatureName(elements[1]);
		} else {
			throw new ResourceInitializationException("Wrong feature declaration: " + feature, elements);
		}
	}
		
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			String feature = (String) context.getConfigParameterValue("Feature");			this.setFeature(feature);
			FeatureMapping mapping = (FeatureMapping) context.getResourceObject("Mapping");
            this.setMapping(mapping);
            String path = (String) context.getConfigParameterValue("MappingFile");
            if (path != null) {
                this.setMapping(path);            	
            }
		} catch (ResourceAccessException e) {
            throw new ResourceInitializationException(e);
		} catch (IOException e) {
            throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		Type type = this.getAnnotationType(cas);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
        FSIterator<Annotation> iter = index.iterator();
        while (iter.hasNext()) {
        	Annotation annotation = iter.next();
        	Feature feature = annotation.getType().getFeatureByBaseName(this.getFeatureName());
        	if (feature != null) {
        		String source = annotation.getStringValue(feature);
        		String target = this.getMapping().get(source);
        		annotation.setStringValue(feature, target);
            }
        }
	}

}
