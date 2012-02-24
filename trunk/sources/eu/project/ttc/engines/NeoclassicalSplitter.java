package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.models.Bank;
import eu.project.ttc.models.Tree;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

public class NeoclassicalSplitter extends JCasAnnotator_ImplBase {

	private Bank bank;
	
	private void setBank(Bank bank) {
		this.bank = bank;
	}

	private Bank getBank() {
		return this.bank;
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
			Bank resource = (Bank) context.getResourceObject("Bank");
			this.setBank(resource);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		} 
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SingleWordTermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();
			int begin = annotation.getBegin();
			int end = annotation.getEnd();
			this.getComponents().clear();
			this.getPrefixes(cas,begin,end,begin,this.getBank().getPrefixTree());
			this.getSuffixes(cas,begin,end,end,this.getBank().getSuffixTree());
			if (!this.getComponents().isEmpty()) {
				this.doFill(cas, annotation, begin, end);
				this.doAnnotate(annotation);
			}
		}
	}

	private void getPrefixes(JCas cas,int begin,int end,int index,Tree<Character> current) {
		if (index < end) {
			char ch = cas.getDocumentText().charAt(index);
			Character c = Character.toLowerCase(ch);
			Tree<Character> tree = current.get(c);
			if (tree == null) {
				if (current.leaf()) { 
					this.addRootComponent(cas,begin,index,true);
					tree = this.getBank().getPrefixTree().get(c);
					if (tree != null) {  
						this.getPrefixes(cas,index,end,index + 1,tree);					
					}
				}
			} else {
				this.getPrefixes(cas,begin,end,index + 1,tree);
			}
		}
	}
	
	private void getSuffixes(JCas cas,int begin,int end,int index,Tree<Character> current) {
		if (index > begin) {
			char ch = cas.getDocumentText().charAt(index - 1);
			Character c = Character.toLowerCase(ch);
			Tree<Character> tree = current.get(c);
			if (tree == null) {
				if (current.leaf()) {
					this.addRootComponent(cas,index,end,false);
					tree = this.getBank().getSuffixTree().get(c);
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
	
	private void doAnnotate(SingleWordTermAnnotation annotation) {
		annotation.setCompound(true);
		annotation.setNeoclassical(true);
	}
	
	private void doFill(JCas cas, SingleWordTermAnnotation annotation, int begin,int end) {
		int last = begin;
		List<Annotation> annotations = new ArrayList<Annotation>(this.getComponents());
		Collections.sort(annotations,this.getComparator());
		for (Annotation a : annotations) {
			int index = a.getBegin();
			if (last < index) {
				this.addComponent(cas, annotation.getCategory(), last, index);
			}
			last = a.getEnd();
		}
		if (last < end) {
			this.addComponent(cas, annotation.getCategory(), last, end);
		}
	}

	private void addComponent(JCas cas,String category,int begin,int end) {
		TermComponentAnnotation annotation = new TermComponentAnnotation(cas,begin,end);
		annotation.setCategory(category);
		annotation.addToIndexes();
		this.getComponents().add(annotation);
	}
	
	private void addRootComponent(JCas cas,int begin,int end,boolean prefix) {
		TermComponentAnnotation annotation = new TermComponentAnnotation(cas,begin,end);
		annotation.setLemma(annotation.getCoveredText());
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
