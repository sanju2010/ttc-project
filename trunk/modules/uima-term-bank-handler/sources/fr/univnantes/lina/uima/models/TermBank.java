package fr.univnantes.lina.uima.models;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.uima.resource.SharedResourceObject;

public interface TermBank extends SharedResourceObject, Serializable {
			
	public void index(String term,String category,String lemma,String complexity,String language,String document,int begin,int end);
	
	public void update(InputStream inputStream) throws Exception;
	
	public void release(OutputStream outputStream) throws Exception;
	
}
