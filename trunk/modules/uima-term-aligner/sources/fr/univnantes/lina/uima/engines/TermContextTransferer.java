package fr.univnantes.lina.uima.engines;

import java.io.File;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.dictionaries.Dictionary;
import fr.univnantes.lina.uima.models.TermContextSpace;

public class TermContextTransferer extends JCasAnnotator_ImplBase {
		
	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
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
		
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);
			// context.getLogger().log(Level.INFO,"Loading Term Context Space " + space);
			String sourceName = (String) context.getConfigParameterValue("SourceName");
			this.setSourceName(sourceName);
			String targetName = (String) context.getConfigParameterValue("TargetName");
			this.setTargetName(targetName);
			String resultName = (String) context.getConfigParameterValue("ResultName");
			this.setResultName(resultName);
			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);
			String path = (String) context.getConfigParameterValue("DictionaryFile");
			if (path != null) {
				File file = new File(path);
				this.getDictionary().doLoad(file.toURI());
			}
			this.getTermContextSpace().addIndex(this.getResultName());
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
			this.getTermContextSpace().doTransfer(this.getSourceName(), this.getTargetName(), this.getResultName(), this.getDictionary().map());
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
