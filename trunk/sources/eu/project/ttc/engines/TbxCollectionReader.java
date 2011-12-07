package eu.project.ttc.engines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class TbxCollectionReader extends CollectionReader_ImplBase {
	
	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	private File file;

	private void setFile(File file) throws IOException {
		if (file.exists()) {
			if (file.isFile()) {
				this.file = file;
				this.setSize(1);
			} else {
				throw new IOException(file.getAbsolutePath());
			}
		} else {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
	}

	private File getFile() {
		return this.file;
	}
	
	private int size;
	
	private void setSize(int size) {
		this.size = size;
	}
		
	private int getSize() {
		return this.size;
	}
	
	private int index;
	
	private void setIndex() {
		this.index = 0;
	}
	
	private void incrIndex() {
		this.index++;
	}
	
	private int getIndex() {
		return this.index;
	}
				
	@Override
	public void initialize() throws ResourceInitializationException {
		try {
			this.setIndex();
			String language = (String) this.getConfigParameterValue("Language");
			this.setLanguage(language);
			String path = (String) this.getConfigParameterValue("File");
			File file = new File(path);
			this.setFile(file);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	private void populate(JCas cas,File file) throws IOException {
		// TODO
		cas.setDocumentText("TODO");
		cas.setDocumentLanguage(this.getLanguage());
	}
	
	/**
	 * This method loads the next element from the collection in order to pass
	 * it to the processing.
	 * 
	 * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
	 */
	@Override
	public void getNext(CAS cas) throws CollectionException {
		try {
			JCas jcas = cas.getJCas();
			File file = this.getFile();
			this.incrIndex();
			this.populate(jcas, file);
			SourceDocumentInformation sdi = new SourceDocumentInformation(jcas);
			sdi.setBegin(0);
			sdi.setEnd(cas.getDocumentText().length());
			sdi.setDocumentSize(cas.getDocumentText().length());
			sdi.setLastSegment(!this.hasNext());
			sdi.setOffsetInSource(0);
			sdi.setUri(file.toURI().toURL().toString());
			sdi.addToIndexes();
			this.getLogger().log(Level.INFO,"Processing " + file.getAbsolutePath());
		} catch (Exception e) {
			throw new CollectionException(e);
		} 
	}

	/**
	 * This method returns a boolean value that is true if and only if their still have a resource 
	 * to populate as CAS in the collection.
	 * 
	 * @return true if all resources are not populated into CAS. 
	 */
	@Override
	public boolean hasNext() {
		return this.getIndex() < this.getSize();
	}

	/**
	 * This method returns the state of the CAS populate process from collection resources   
	 * as a progress array. 
	 * 
	 * @return a progress array of CAS populate process among the collection resources.  
	 */
	@Override
	public Progress[] getProgress() {
		Progress progress = new ProgressImpl(this.getIndex(),this.getSize(),Progress.ENTITIES);
		Progress[] result = { progress } ;
		return result ;
	}

	@Override
	public void close() throws IOException { }
	
}
