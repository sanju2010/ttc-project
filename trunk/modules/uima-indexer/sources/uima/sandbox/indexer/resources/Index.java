package uima.sandbox.indexer.resources;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.SharedResourceObject;

public interface Index extends SharedResourceObject, Serializable {
	
	public void load(InputStream inputStream) throws Exception;
	
	public void store(OutputStream outputStream) throws Exception;
	
	public JCas get();

}
