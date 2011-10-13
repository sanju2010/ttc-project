package fr.univnantes.lina.uima.engines;

import java.io.File;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.uima.dictionaries.Dictionary;
import fr.univnantes.lina.uima.models.TermBank;

public class TermBankFilter extends JCasAnnotator_ImplBase {
	
	private TermBank termBank;
	
	private void setTermBank(TermBank termBank) {
		this.termBank = termBank;
	}
	
	private TermBank getTermBank() {
		return this.termBank;
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
			TermBank termBank = (TermBank) context.getResourceObject("TermBank");
			this.setTermBank(termBank);
			context.getLogger().log(Level.CONFIG,"Loading Term Bank " + termBank);
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
	
}
