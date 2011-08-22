package eu.project.ttc.engines;

import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.flow.Flow;
import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.JCasFlowController_ImplBase;
import org.apache.uima.flow.JCasFlow_ImplBase;
import org.apache.uima.flow.ParallelStep;
import org.apache.uima.flow.Step;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;

public class TermSuiteLanguageController extends JCasFlowController_ImplBase {

	private Set<String> processors;
	
	private void setProcessors(Set<String> processors) {
		this.processors = processors;
	}
	
	private Set<String> getProcessors() {
		return this.processors;
	}
	
	@Override
	public void initialize(FlowControllerContext context) throws ResourceInitializationException {
	    super.initialize(context);
	    this.setProcessors(context.getAnalysisEngineMetaDataMap().keySet());
	}
	
	@Override
	public Flow computeFlow(JCas aJCas) throws AnalysisEngineProcessException {
		return new TermSuiteLanguageFlow();
	}
	
	private class TermSuiteLanguageFlow extends JCasFlow_ImplBase {

		private boolean done = false;
		
		@Override
		public Step next() throws AnalysisEngineProcessException {
			if (this.done) {
				return new FinalStep();
			} else {
				this.done = true;
				JCas cas = this.getJCas();
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(DocumentAnnotation.type);
				FSIterator<Annotation> iterator = index.iterator();
				while (iterator.hasNext()) {
					DocumentAnnotation annotation = (DocumentAnnotation) iterator.next();
					if (annotation.getLanguage() == null || annotation.getLanguage().isEmpty() || annotation.getLanguage().startsWith("x-")) {
						return new ParallelStep(getProcessors());
					}
				}
				return new FinalStep();				
			}
		}
		
	}

}
