package eu.project.ttc.engines.aligner;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.models.aligner.TermContextSpace;

public class TermContextAligner extends JCasAnnotator_ImplBase {
	
	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}
		
	private void setSimilarityDistance(String name) throws Exception {
		this.getTermContextSpace().setSimilarityDistance(name);
	}
	
	private String sourceName;
	
	private void setSourceName(String name) {
		this.sourceName = name;
	}
	
	private String getSourceName() {
		return this.sourceName;
	}
	
	private String targetName;
	
	private void setTargetName(String name) {
		this.targetName = name;
	}
	
	private String getTargetName() {
		return this.targetName;
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
		try {
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);
			// context.getLogger().log(Level.INFO,"Loading Term Context Space " + space);
			String name = (String) context.getConfigParameterValue("SimilarityDistanceClassName");
			this.setSimilarityDistance(name);
			String sourceName = (String) context.getConfigParameterValue("SourceName");
			String targetName = (String) context.getConfigParameterValue("TargetName");
			String resultName = (String) context.getConfigParameterValue("ResultName");
			this.setSourceName(sourceName);
			this.setTargetName(targetName);
			this.setResultName(resultName);
			
			String language = this.getTermContextSpace().getIndex(this.getSourceName()).getLanguage();
			this.getTermContextSpace().addIndex(this.getResultName());
			this.getTermContextSpace().getIndex(this.getResultName()).setLanguage(language);
			
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			this.getTermContextSpace().doAlign(this.getSourceName(),this.getTargetName(),this.getResultName());
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
