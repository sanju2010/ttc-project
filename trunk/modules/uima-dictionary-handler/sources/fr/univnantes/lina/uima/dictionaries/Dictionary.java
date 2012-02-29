package fr.univnantes.lina.uima.dictionaries;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.SharedResourceObject;

public interface Dictionary extends SharedResourceObject {
	
	public Map<String, Set<String>> get();
		
	public void load(URI uri) throws Exception;
	
}
