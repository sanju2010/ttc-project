package eu.project.ttc.engines;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.models.TermContextIndex;
import eu.project.ttc.models.TermContextSpace;
import eu.project.ttc.types.TermContextEntryAnnotation;
import eu.project.ttc.types.TermContextIndexAnnotation;
import eu.project.ttc.types.TermContextVectorAnnotation;

public class TermContextComputer extends JCasAnnotator_ImplBase {

	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {		
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);
			// context.getLogger().log(Level.INFO,"Loading Term Context Space " + space);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String indexID = this.getIndexID(cas);
			this.getTermContextSpace().addIndex(indexID);
			this.getTermContextSpace().getIndex(indexID).setLanguage(cas.getDocumentLanguage());
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermContextVectorAnnotation.type);
			FSIterator<Annotation> iter = index.iterator();
			while (iter.hasNext()) {
				TermContextVectorAnnotation annotation = (TermContextVectorAnnotation) iter.next();
				this.doIndex(cas,indexID,annotation);
			}
		} catch (CASRuntimeException e) {
			this.getContext().getLogger().log(Level.WARNING,e.getMessage());
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private String getIndexID(JCas cas) throws CASRuntimeException {
		String indexID = null;
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermContextIndexAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			TermContextIndexAnnotation annotation = (TermContextIndexAnnotation) iterator.next();
			indexID = annotation.getName();
		}
		if (indexID == null) {
			throw new CASRuntimeException(new Exception("Annotation " + TermContextIndexAnnotation.class.getCanonicalName() + " not found"));
		} else {
			return indexID;
		}
	}
	
	private void doIndex(JCas cas,String indexID,TermContextVectorAnnotation annotation) throws Exception {
		TermContextIndex index = this.getTermContextSpace().getIndex(indexID);
		// System.out.println(annotation.getTerm().getLemma() + " " + annotation.getContextVector().size());
		int length = annotation.getContextVector().size();
		String term = annotation.getTerm().getLemma();
		index.addOccurrences(term, null);
		for (int i = 0; i < length; i++) {
			TermContextEntryAnnotation entry = annotation.getContextVector(i);
			String context = entry.getLemma();
			index.addCoOccurrences(term, context, new Double(1));
		}
	}
	
	@Override
	public void collectionProcessComplete() {
		/*
		try {
			FileOutputStream src = new FileOutputStream("/home/rocheteau-j/src.w7-vcxt");
			this.getTermContextSpace().getIndex("Source").doStore(src);
			FileOutputStream tgt = new FileOutputStream("/home/rocheteau-j/tgt-w7.vcxt");
			this.getTermContextSpace().getIndex("Target").doStore(tgt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
}
