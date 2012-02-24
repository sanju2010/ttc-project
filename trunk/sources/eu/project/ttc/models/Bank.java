package eu.project.ttc.models;

import org.apache.uima.resource.SharedResourceObject;

public interface Bank extends SharedResourceObject {

	public Tree<Character> getPrefixTree();
	
	public Tree<Character> getSuffixTree();
	
}
