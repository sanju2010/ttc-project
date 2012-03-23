package uima.sandbox.contextualizer.engines;

import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.contextualizer.models.Context;


public abstract class Contextualizer extends JCasAnnotator_ImplBase {
	
	private int scope;
	
	private void setSize(Integer size) {
		this.scope = size;
	}
	
	private int getSize() {
		return this.scope;
	}
	
	private String type;
	
	private void setType(String type) {
		this.type = type;
	}
	
	private String getType() {
		return this.type;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		String type = (String) context.getConfigParameterValue("Type");
		this.setType(type);
		Integer size = (Integer) context.getConfigParameterValue("Size");
		this.setSize(size);
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			Context context = new Context(cas,this.getType(),this.getSize());
			List<List<Annotation>> lists = context.make();
			for (List<Annotation> list : lists) {
				if (!list.isEmpty()) {
					Annotation annotation = list.get(0);
					Annotation[] annotations = new Annotation[list.size() - 1];
					for (int i = 1; i < list.size(); i++) {
						annotations[i - 1] = list.get(i);
					}
					this.annotate(cas, annotation, annotations);
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	protected abstract void annotate(JCas cas,Annotation annotation, Annotation[] annotations); 
	
}
