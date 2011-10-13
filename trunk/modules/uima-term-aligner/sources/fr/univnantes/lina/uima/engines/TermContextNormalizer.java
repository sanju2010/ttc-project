package fr.univnantes.lina.uima.engines;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.models.TermContextSpace;

public class TermContextNormalizer extends JCasAnnotator_ImplBase {

	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}

	private void setAssociationRate(String name) throws Exception {
		this.getTermContextSpace().setAssociationRate(name);
	}
	
	private String indexName;
	
	private void setIndexName(String name) {
		this.indexName = name;
	}
	
	private String getIndexName() { 
		return this.indexName;
	}

	private String resultName;
	
	private void setResultName(String name) {
		this.resultName = name;
	}
	
	private String getResultName() { 
		return this.resultName;
	}

	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {			
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);
						
			// context.getLogger().log(Level.INFO,"Loading Term Context Space " + space);
			
			String className = (String) context.getConfigParameterValue("AssociationRateClassName");
			this.setAssociationRate(className);
			
			String indexName = (String) context.getConfigParameterValue("IndexName");
			String resultName = (String) context.getConfigParameterValue("ResultName");
			this.setIndexName(indexName);
			this.setResultName(resultName);
			
			this.getTermContextSpace().addIndex(this.getResultName());			

		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException{
		try {
			this.getTermContextSpace().doNormalize(this.getIndexName(),this.getResultName());
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
