package fr.univnantes.lina.uima.dictionaries;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.SharedResourceObject;

public interface Dictionary extends SharedResourceObject {
		
	public Set<String> domain(String language);
	
	public Map<String, Set<String>> map();
		
	public void doLoad(URI uri) throws Exception;
	
}
