package eu.project.ttc.models;

import java.util.Map;

public interface AlignmentMethod {

	/**
	 * This method returns the set of translations of a given term 
	 * @param term
	 * @param translations
	 * @return
	 */
	public Map<String, Double> align(String term);
	
}
