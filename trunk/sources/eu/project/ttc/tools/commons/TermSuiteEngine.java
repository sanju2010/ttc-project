package eu.project.ttc.tools.commons;

import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import eu.project.ttc.tools.commons.InputSource.InputSourceTypes;

public interface TermSuiteEngine {

    // FIXME
//    /**
//     * Compute the analysis engine descriptor to use.
//     *
//     * @return
//     *      absolute path to the resource corresponding to the description
//     *      of the corresponding analysis engine
//     * @throws Exception
//     */
//	public String getEngineDescriptor() throws Exception;
//
//    /**
//     * Compute the analysis engine configuration parameter settings to be used,
//     * that is to be directly passed to the corresponding UIMA engine.
//     *
//     * @return
//     *      instance of ConfigurationParameterSettings to be directly passed to
//     *      the analysis engine, that is to the descriptor running the engine.
//     * @throws Exception
//     */
//	public ConfigurationParameterSettings getAESettings() throws Exception;
//
//    /**
//     * Compute the description of where and what the files to be processed are.
//     */
//	public InputSource getInputSource();
	
//	public String language();
//
//	public String encoding();
//
//	public void callBack(CAS cas) throws Exception;

	/** Default encoding for TermSuite engines */
	public static final String DEFAULT_ENCODING = "utf-8";

}