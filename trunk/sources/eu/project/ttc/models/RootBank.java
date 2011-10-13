package eu.project.ttc.models;

import java.io.File;
import java.io.IOException;

import org.apache.uima.resource.SharedResourceObject;

public interface RootBank extends SharedResourceObject {

	public RootTree getPrefixTree();
	
	public RootTree getSuffixTree();
	
	public void doLoad(File file) throws IOException;
	
}
