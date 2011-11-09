package eu.project.ttc.models.aligner;

import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.SharedResourceObject;

public interface TermContextBench extends SharedResourceObject {

	/**
	 * define a language to an index given bu its name name. 
	 * @param entry
	 */
	public void assoc(String language, String name);
	
	/**
	 * insert an entry in the current document. 
	 * @param entry
	 */
	public void index(String id, String term, String category, String lemma);

	/**
	 * create a new current document
	 */
	public void start(String id, String language);

	/**
	 * store the current document into documents
	 */
	public void stop(String id, String language);

	/**
	 * remove hapax entries from all documents.
	 */
	public void release();
	
	/**
	 * provide the common analysis structure that correpsonds to this bench.
	 * 
	 * @return the bench CAS
	 */
	public boolean hasNext();
	
	/**
	 * provide the common analysis structure that correpsonds to this bench.
	 * 
	 * @return the bench CAS
	 */
	public JCas next(JCas cas);
	
}
