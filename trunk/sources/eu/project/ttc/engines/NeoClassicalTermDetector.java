package eu.project.ttc.engines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FSStringConstraint;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.models.Root;
import eu.project.ttc.models.RootBank;
import eu.project.ttc.models.RootTree;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

public class NeoClassicalTermDetector extends JCasAnnotator_ImplBase {

	private RootBank bank;
	
	private void setBank(RootBank bank) {
		this.bank = bank;
	}

	private void setBank(String path) throws IOException {
		if (path != null) {
			File file = new File(path);
			this.getBank().doLoad(file);
		}
	}
	
	private RootBank getBank() {
		return this.bank;
	}

	private String typeName;
	
	private Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.typeName);
	}
	
	private String featureName;
	
	private Feature getFeature(Type type) {
		return type.getFeatureByBaseName(this.featureName);
	}
	
	private void setFeature(String qualifiedFeature) throws Exception {
		String[] typeAndFeature = qualifiedFeature.split(":");
		if (typeAndFeature.length == 2) {
			this.typeName = typeAndFeature[0];
			this.featureName = typeAndFeature[1];
		} else {
			throw new Exception("Wrong Qualified Feature Format: " + qualifiedFeature);
		}
	}
	
	private String value;
	
	private void setValue(String value) {
		this.value = value;
	}
	
	private String getValue() {
		return this.value;
	}
	
	private AnnotationComparator comparator;
	
	private void setComparator() {
		this.comparator = new AnnotationComparator();
	}
	
	private AnnotationComparator getComparator() {
		return this.comparator;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			this.setComponents();
			this.setComparator();
			RootBank resource = (RootBank) context.getResourceObject("RootBank");
			this.setBank(resource);
			String file = (String) context.getConfigParameterValue("RootBankFile");
			this.setBank(file);
			String feature = (String) context.getConfigParameterValue("Feature");
			this.setFeature(feature);
			String value = (String) context.getConfigParameterValue("Value");
			this.setValue(value);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		} 
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		Type type = this.getType(cas);
		FSMatchConstraint filter = this.getConstraint(cas, type);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		FSIterator<Annotation> filteredIterator = cas.createFilteredIterator(iterator,filter);
		while (filteredIterator.hasNext()) {
			Annotation annotation = filteredIterator.next();
			int begin = annotation.getBegin();
			int end = annotation.getEnd();
			this.getComponents().clear();
			this.getPrefixes(cas,begin,end,begin,this.getBank().getPrefixTree());
			this.getSuffixes(cas,begin,end,end,this.getBank().getSuffixTree());
			if (!this.getComponents().isEmpty()) {
				this.doFill(cas, begin, end);
				/*
				Collections.sort(this.getComponents(),this.getComparator());
				int length = this.getComponents().size();
				Annotation[] components = new Annotation[length];
				for (int i = 0; i< length; i++) {
					components[i] = this.getComponents().get(i);
				}
				*/
				this.doAnnotate(cas, annotation.getBegin(), annotation.getEnd());
			}
		}
	}

	private FSMatchConstraint getConstraint(JCas cas, Type type) {
		ConstraintFactory factory = cas.getConstraintFactory();
		Feature feature = this.getFeature(type);
		FSStringConstraint constraint = factory.createStringConstraint();
		constraint.equals(this.getValue());
		FeaturePath path = cas.createFeaturePath();
	    path.addFeature(feature);
	    FSMatchConstraint filter = factory.embedConstraint(path,constraint);
		return filter;
	}

	private void getPrefixes(JCas cas,int begin,int end,int index,RootTree current) {
		if (index < end) {
			char ch = cas.getDocumentText().charAt(index);
			Character c = Character.toLowerCase(ch);
			RootTree tree = current.getChild(c);
			if (tree == null) {
				Root root = current.getRoot();
				if (root != null) { 
					this.addRootComponent(cas,root,begin,index,true);
					tree = this.getBank().getPrefixTree().getChild(c);
					if (tree != null) {  
						this.getPrefixes(cas,index,end,index + 1,tree);					
					}
				}
			} else {
				this.getPrefixes(cas,begin,end,index + 1,tree);
			}
		}
	}
	
	private void getSuffixes(JCas cas,int begin,int end,int index,RootTree current) {
		if (index > begin) {
			char ch = cas.getDocumentText().charAt(index - 1);
			Character c = Character.toLowerCase(ch);
			RootTree tree = current.getChild(c);
			if (tree == null) {
				Root root = current.getRoot();
				if (root != null) {
					this.addRootComponent(cas,root,index,end,false);
					tree = this.getBank().getSuffixTree().getChild(c);
					if (tree != null) { 
						this.getSuffixes(cas,begin,index,index - 1,tree);
					}
				}
			} else {
				this.getSuffixes(cas,begin,end,index - 1,tree);
			}
		}
	}
	
	private List<TermComponentAnnotation> components;
	
	private void setComponents() {
		this.components = new ArrayList<TermComponentAnnotation>();
	}
	
	private List<TermComponentAnnotation> getComponents() {
		return this.components;
	}
	
	private void doAnnotate(JCas cas,int begin,int end) {
		NeoClassicalCompoundTermAnnotation annotation = new NeoClassicalCompoundTermAnnotation(cas,begin,end);
		annotation.setBegin(begin);
		annotation.setEnd(end);
		annotation.setComplexity("neo-classical-compound");
		annotation.setLemma(annotation.getCoveredText());
		annotation.setCategory(this.getValue());
		annotation.addToIndexes();
	}
	
	private void doFill(JCas cas,int begin,int end) {
		int last = begin;
		List<Annotation> annotations = new ArrayList<Annotation>(this.getComponents());
		Collections.sort(annotations,this.getComparator());
		for (Annotation annotation : annotations) {
			int index = annotation.getBegin();
			if (last < index) {
				this.addComponent(cas, last, index);
			}
			last = annotation.getEnd();
		}
		if (last < end) {
			this.addComponent(cas, last, end);
		}
	}

	private void addComponent(JCas cas,int begin,int end) {
		TermComponentAnnotation annotation = new TermComponentAnnotation(cas,begin,end);
		annotation.setCategory("word");
		annotation.addToIndexes();
		this.getComponents().add(annotation);
	}
	
	private void addRootComponent(JCas cas,Root root,int begin,int end,boolean prefix) {
		TermComponentAnnotation annotation = new TermComponentAnnotation(cas,begin,end);
		annotation.setLemma(root.getRoot());
		if (prefix) {
			annotation.setCategory("initial");
		} else {

			annotation.setCategory("final");
		}
		annotation.addToIndexes();
		this.getComponents().add(annotation);
	}
	
	private class AnnotationComparator implements Comparator<Annotation> {

		@Override
		public int compare(Annotation fst, Annotation snd) {
			int diff = fst.getBegin() - snd.getBegin();
			if (diff == 0) {
				diff = snd.getEnd() - fst.getEnd();
				if (diff == 0) {
					return fst.getType().getName().compareTo(snd.getType().getName());
				} else {
					return diff;
				}
			} else {
				return diff;
			}
		}
		
	}
	
}
