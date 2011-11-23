package eu.project.ttc.engines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.models.TermContextSpace;
import fr.univnantes.lina.uima.dictionaries.Dictionary;

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
	
	private String file;
	
	private void setFile(String file) {
		this.file = file;
	}
	
	private String getFile() {
		return this.file;
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
			String file = (String) context.getConfigParameterValue("File");
			this.setFile(file);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	}
	
	private static boolean done = false;
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			if (!done) {
				done = true;
				OutputStream outputStream = new FileOutputStream(this.getFile());
				try {
					this.getTermContextSpace().doCheck(this.getIndexName(), this.getDictionary().map(), outputStream);
				} finally {
					outputStream.close();
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

}
