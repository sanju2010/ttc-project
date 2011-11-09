package eu.project.ttc.models;

import org.apache.uima.resource.SharedResourceObject;

public interface GeneralLanguage extends SharedResourceObject {

	public double get(String entry);

	public void set(String entry, int frequency);
	
}
