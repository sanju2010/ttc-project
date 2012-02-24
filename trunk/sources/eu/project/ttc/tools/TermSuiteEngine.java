package eu.project.ttc.tools;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

public interface TermSuiteEngine {
	
	public String get() throws Exception;
	
	public ConfigurationParameterSettings settings() throws Exception;
	
	public String data();
	
	public int input();
	
	public String language();
	
	public String encoding();
	
	public void callBack(CAS cas) throws Exception;

	public void callBack();
	
}