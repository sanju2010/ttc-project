package uima.sandbox.matcher.models.trees;

import java.util.Set;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.jcas.JCas;

public interface Tree {

	public FSMatchConstraint getConstraint(JCas cas);
		
	public String id();
	
	public boolean terminal();
	
	public boolean optional();
		
	public Set<? extends Tree> get();
	
}
