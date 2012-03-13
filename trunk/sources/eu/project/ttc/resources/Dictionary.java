package eu.project.ttc.resources;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.SharedResourceObject;

public interface Dictionary extends SharedResourceObject {
	
	public Map<String, Set<String>> get();
	
	public void load(String id, InputStream is) throws Exception;
	
	public void load(URI uri) throws Exception;

	public void add(String source, String target);
	
}
