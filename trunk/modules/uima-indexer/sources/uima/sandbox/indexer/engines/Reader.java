package uima.sandbox.indexer.engines;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import uima.sandbox.indexer.resources.Index;


public class Reader extends JCasAnnotator_ImplBase {
	
	private Index index;
	
	private void setIndex(Index index) {
		this.index = index;
	}
	
	private Index getIndex() {
		return this.index;
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
		super.initialize(context);
		try {
			Index index = (Index) context.getResourceObject("Index");
			this.setIndex(index);
			String file = (String) context.getConfigParameterValue("File");
			this.setFile(file);
			this.collectionProcessComplete();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException { }

	private static boolean done = false;

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		try {
			if (!done) {
				done = true;
				InputStream stream = new FileInputStream(this.getFile());
				try {
					this.getIndex().load(stream);
				} finally {
					stream.close();
				}
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}
