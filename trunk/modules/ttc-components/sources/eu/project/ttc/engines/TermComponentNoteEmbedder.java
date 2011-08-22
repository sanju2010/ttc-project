package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.TermComponentAnnotation;
import org.apache.uima.TermNoteAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class TermComponentNoteEmbedder extends JCasAnnotator_ImplBase {

	private String typeName;
	
	private void setTypeName(String name) {
		this.typeName = name;
	}
	
	private String getTypeName() {
		return this.typeName;
	}
	
	private Type type;
	
	private void setType(Type type) {
		this.type = type;
	}
	
	private Type getType() {
		return this.type;
	}
		
	private String featureName;
	
	private void setFeatureName(String name) {
		this.featureName = name;
	}
	
	private String getFeatureName() {
		return this.featureName;
	}
	
	private Feature feature;
	
	private void setFeature(Feature feature) {
		this.feature = feature;
	}
	
	private Feature getFeature() {
		return this.feature;
	}
	
	private void doCheck(JCas cas) throws Exception {
		Type type = cas.getTypeSystem().getType(this.getTypeName());
		if (type == null) {
			throw new Exception("Type '" + this.getTypeName() + "' not found");
		} else {
			this.setType(type);
		}
		Feature feature = type.getFeatureByBaseName(this.getFeatureName());
		if (feature == null) {
			throw new Exception("Feature '" + this.getTypeName() + ":" + this.getFeatureName() + "' not found");
		} else if (feature.getRange().getName().equals("uima.cas.String")) {
			this.setFeature(feature);
		} else {
			String message = "Feature '" + this.getTypeName() + ":" + this.getFeatureName();
			message += "' type is '" + feature.getRange().getName();
			message += "' but should be 'uima.cas.String'";
			throw new Exception(message);
		}
	}
	
	private String name;
	
	private void setName(String name) {
		this.name = name;
	}
	
	private String getName() {
		return this.name;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			String name = (String) context.getConfigParameterValue("Name");
			this.setName(name);
			String typeName = (String) context.getConfigParameterValue("AnnotationType");
			this.setTypeName(typeName);
			String featureName = (String) context.getConfigParameterValue("FeatureName");
			this.setFeatureName(featureName);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			this.doCheck(cas);
			AnnotationIndex<Annotation> termIndex = cas.getAnnotationIndex(TermComponentAnnotation.type);
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(this.getType());
			FSIterator<Annotation> termIterator = termIndex.iterator();
			while (termIterator.hasNext()) {
				TermComponentAnnotation component = (TermComponentAnnotation) termIterator.next();
				FSIterator<Annotation> iterator = index.subiterator(component);
				List<TermNoteAnnotation> notes= new ArrayList<TermNoteAnnotation>();
				while (iterator.hasNext()) {
					Annotation annotation = iterator.next();
					TermNoteAnnotation noteAnnotation = this.doAnnotate(cas,annotation);
					notes.add(noteAnnotation);
				}
				this.addNotes(cas,component,notes);				
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private void addNotes(JCas cas,TermComponentAnnotation component,List<TermNoteAnnotation> notes) {
		int length = notes.size();
		int offset = 0;
		if (component.getNotes() == null) {
			FSArray array = new FSArray(cas,offset + length);
			component.setNotes(array);
		} else {
			offset = component.getNotes().size();
			FSArray array = new FSArray(cas,offset + length);
			array.copyFromArray(component.getNotes().toArray(),0,0,offset);
			component.setNotes(array);
		}
		for (int i = 0; i < notes.size(); i++) {
			component.setNotes(offset + i, notes.get(i));
		}
	}

	private TermNoteAnnotation doAnnotate(JCas cas,Annotation annotation) {
		int begin = annotation.getBegin();
		int end = annotation.getEnd();
		String kind = this.getName();
		String value = annotation.getFeatureValueAsString(this.getFeature());
		TermNoteAnnotation noteAnnotation = new TermNoteAnnotation(cas, begin, end);
		noteAnnotation.setKind(kind);
		noteAnnotation.setValue(value);
		noteAnnotation.addToIndexes();
		return noteAnnotation;
	}

}
