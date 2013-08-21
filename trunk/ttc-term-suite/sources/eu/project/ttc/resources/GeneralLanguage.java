package eu.project.ttc.resources;

import org.apache.uima.resource.SharedResourceObject;

public interface GeneralLanguage extends SharedResourceObject {

	public double get(String entry);

	public void set(String entry, int frequency);
	
}
