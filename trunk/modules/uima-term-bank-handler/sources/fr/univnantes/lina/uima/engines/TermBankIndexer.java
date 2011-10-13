package fr.univnantes.lina.uima.engines;

import org.apache.uima.CompoundWordTermAnnotation;
import org.apache.uima.MultiWordTermAnnotation;
import org.apache.uima.SingleWordTermAnnotation;
import org.apache.uima.TermAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.models.TermBank;

public class TermBankIndexer extends JCasAnnotator_ImplBase {
		
	private TermBank termBank;
	
	private void setTermBank(TermBank termBank) {
		this.termBank = termBank;
	}
	
	private TermBank getTermBank() {
		return this.termBank;
	}
	
	private boolean singleWordIndexingRequired;
	
	private void requireSingleWordIndexing(boolean required) {
		this.singleWordIndexingRequired = required;
	}
	
	private boolean isSingleWordIndexingRequired() {
		return this.singleWordIndexingRequired;
	}
	
	private boolean multiWordIndexingRequired;
	
	private void requireMultiWordIndexing(boolean required) {
		this.multiWordIndexingRequired = required;
	}
	
	private boolean isMultiWordIndexingRequired() {
		return this.multiWordIndexingRequired;
	}
	
	private boolean compoundWordIndexingRequired;
	
	private void requireCompoundWordIndexing(boolean required) {
		this.compoundWordIndexingRequired = required;
	}
	
	private boolean isCompoundWordIndexingRequired() {
		return this.compoundWordIndexingRequired;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			TermBank termBank = (TermBank) context.getResourceObject("TermBank");
			this.setTermBank(termBank);
			Boolean singleWord = (Boolean) context.getConfigParameterValue("IndexSingleWordTerms");
			this.requireSingleWordIndexing(singleWord.booleanValue());
			Boolean multiWord = (Boolean) context.getConfigParameterValue("IndexMultiWordTerms");
			this.requireMultiWordIndexing(multiWord.booleanValue());
			Boolean compoundWord = (Boolean) context.getConfigParameterValue("IndexCompoundWordTerms");
			this.requireCompoundWordIndexing(compoundWord.booleanValue());
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	private void doIndex(JCas cas,Type type) throws Exception {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation term = (TermAnnotation) iterator.next();
			if (term.getBegin() < term.getEnd()) {
				this.getTermBank().index(
						term.getCoveredText(),
						term.getCategory(),
						term.getLemma(),
						term.getComplexity(),
						cas.getDocumentLanguage(),
						term.getDocument(),
						term.getBegin(),
						term.getEnd()
				);
			}
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			if (this.isSingleWordIndexingRequired()) {
				String name = SingleWordTermAnnotation.class.getCanonicalName();
				Type type = cas.getTypeSystem().getType(name);
				this.doIndex(cas, type);
			} 
			if (this.isCompoundWordIndexingRequired()) {
				String name = CompoundWordTermAnnotation.class.getCanonicalName();
				Type type = cas.getTypeSystem().getType(name);
				this.doIndex(cas, type);
			}
			if (this.isMultiWordIndexingRequired()) {
				String name = MultiWordTermAnnotation.class.getCanonicalName();
				Type type = cas.getTypeSystem().getType(name);
				this.doIndex(cas, type);
			}
		} catch(Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
