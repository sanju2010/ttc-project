package eu.project.ttc.resources;

import org.apache.uima.resource.SharedResourceObject;

import eu.project.ttc.models.Tree;

public interface Bank extends SharedResourceObject {

	public Tree<Character> getPrefixTree();
	
	public Tree<Character> getSuffixTree();
	
}
