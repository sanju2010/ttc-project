package uima.sandbox.indexer.engines;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import uima.sandbox.indexer.resources.Index;


public class Writer extends JCasAnnotator_ImplBase {
		
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
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException { }
	
	private static Set<String> done;
	
	static {
		Writer.done = new HashSet<String>();
	}
	
	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		if (!Writer.done.contains(this.getFile())) {
			Writer.done.add(this.getFile());
			try {
				OutputStream stream = new FileOutputStream(this.getFile());
				try {
					this.getContext().getLogger().log(Level.INFO, "Serializing " + this.getFile());
					this.getIndex().store(stream);
					// this.getContext().getLogger().log(Level.INFO, "Written " + this.getFile());
				} finally {
					stream.close();
				}
			} catch (Exception e) {
				throw new AnalysisEngineProcessException(e);
			}
		}
	}
}
