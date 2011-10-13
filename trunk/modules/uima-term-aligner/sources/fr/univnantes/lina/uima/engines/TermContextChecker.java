package fr.univnantes.lina.uima.engines;

import java.io.File;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.dictionaries.Dictionary;
import fr.univnantes.lina.uima.models.TermContextSpace;

public class TermContextChecker extends JCasAnnotator_ImplBase {
	
	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}
	
	private String indexName;
	
	private void setIndexName(String name) {
		this.indexName = name;
	}
	
	private String getIndexName() {
		return this.indexName;
	}
		
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary translations) {
		this.dictionary = translations;
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
			String indexName = (String) context.getConfigParameterValue("IndexName");
			this.setIndexName(indexName);
			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);
			String path = (String) context.getConfigParameterValue("DictionaryFile");
			if (path != null) {
				File file = new File(path);
				this.getDictionary().doLoad(file.toURI());
			}
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
			this.getTermContextSpace().doCheck(this.getIndexName(), this.getDictionary().map());			
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

}
