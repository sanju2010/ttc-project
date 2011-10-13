package fr.univnantes.lina.uima.engines;

import org.apache.uima.SingleWordTermAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.models.TermContextBench;

public class TermContextIndexer extends JCasMultiplier_ImplBase {

	private TermContextBench bench;
	
	private void setBench(TermContextBench bench) {
		this.bench = bench;
	}
	
	private TermContextBench getBench() {
		return this.bench;
	}

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {		
			TermContextBench bench = (TermContextBench) context.getResourceObject("TermContextBench");
			this.setBench(bench);
			String sourceLanguage = (String) context.getConfigParameterValue("SourceLanguage");
			String targetLanguage = (String) context.getConfigParameterValue("TargetLanguage");
			String sourceName = (String) context.getConfigParameterValue("SourceName");
			String targetName = (String) context.getConfigParameterValue("TargetName");
			this.getBench().assoc(sourceLanguage, sourceName);
			this.getBench().assoc(targetLanguage, targetName);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			this.getBench().start(this.toString(),cas.getDocumentLanguage());
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SingleWordTermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();
				String lemma = annotation.getLemma();
				this.getBench().index(this.toString(),lemma.toLowerCase());
			}
			this.getBench().stop(this.toString(),cas.getDocumentLanguage());
			if (this.isLast(cas)) {
				this.getBench().release();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private boolean isLast(JCas cas) throws Exception {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation annotation = (SourceDocumentInformation) iterator.next();
			return annotation.getLastSegment();
		} else {
			throw new Exception("Missing " + SourceDocumentInformation.class.getName());
		}
	}
	
	@Override
	public boolean hasNext() throws AnalysisEngineProcessException {
		return this.getBench().hasNext();
	}

	@Override
	public AbstractCas next() throws AnalysisEngineProcessException {
		JCas cas = this.getEmptyJCas();
		return this.getBench().next(cas);
	}
	
}
