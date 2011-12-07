package eu.project.ttc.tools.converter;

import java.util.concurrent.ExecutionException;

import org.apache.uima.collection.metadata.CpeDescription;

public interface ConverterEngine {

	public void execute();
	
	public CpeDescription get() throws InterruptedException, ExecutionException;
}
