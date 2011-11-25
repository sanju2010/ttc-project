package eu.project.ttc.models;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.ResourceInitializationException;

public interface TermContextIndex {

	public void setLanguage(String language);

	public String getLanguage();

	public Map<String, Integer> getOccurrences();

	public Map<String, TermContext> getTermContexts();

	public void doShrink(Set<String> terms);

	public void addOccurrences(String term, Integer occurrences);

	public void setOccurrences(String term, Integer occurrences);

	public void setCoOccurrences(String term, String context, Double coOccurrences, int mode);

	public void addCoOccurrences(String term, String context, Double coOccurrences);

	public void doLoad(URI uri) throws ResourceInitializationException;

}