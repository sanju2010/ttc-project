package eu.project.ttc.tools;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

public interface TermSuiteEngine {
	
	public String get() throws Exception;
	
	public ConfigurationParameterSettings settings() throws Exception;
	
	public String data();
	
	public InputSourceTypes input();
	
	public String language();
	
	public String encoding();
	
	public void callBack(CAS cas) throws Exception;

	/** Default encoding for TermSuite engines */
	public static final String DEFAULT_ENCODING = "utf-8";
}