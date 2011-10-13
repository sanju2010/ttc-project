package fr.univnantes.lina.uima.engines;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.models.TermContextSpace;

public class TermContextWriter extends JCasAnnotator_ImplBase {
	
	private TermContextSpace space;
	
	private void setTermContextSpace(TermContextSpace space) {
		this.space = space;
	}
	
	private TermContextSpace getTermContextSpace() {
		return this.space;
	}
		
	private File indexFile; 
	
	private void setIndexFile(String path) throws IOException {
		File file = new File(path);
		this.indexFile = file;
	}
	
	private File getIndexFile() {
		return this.indexFile;
	}
	
	private String indexName;
	
	private void setIndexName(String name) {
		this.indexName = name;
	}
	
	private String getIndexName() {
		return this.indexName;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			TermContextSpace space = (TermContextSpace) context.getResourceObject("TermContextSpace");
			this.setTermContextSpace(space);
			
			String indexFile = (String) context.getConfigParameterValue("IndexFile");
			
			String indexName = (String) context.getConfigParameterValue("IndexName");
			
			this.setIndexName(indexName);
			
			this.setIndexFile(indexFile);
			
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
			OutputStream stream = new FileOutputStream(this.getIndexFile());
			this.getTermContextSpace().getIndex(this.getIndexName()).doStore(stream);
			stream.close();
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
