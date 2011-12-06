package eu.project.ttc.tools.tagger;

import java.util.concurrent.ExecutionException;

import org.apache.uima.collection.metadata.CpeDescription;

public interface TaggerEngine {

	public void execute();
	
	public CpeDescription get() throws InterruptedException, ExecutionException;
	
}