/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.project.ttc.tools.cli;

import java.io.File;
import java.util.Properties;

import javax.swing.SwingUtilities;

import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.indexer.IndexerBinding;
import eu.project.ttc.tools.indexer.IndexerModel;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import eu.project.ttc.tools.TermSuiteRunner;
import org.apache.uima.util.Level;

/**
 * Command line interface for the Indexer engines.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class TermSuiteIndexerCLI {

	/** Name of the file storing saved preferences */
	private static final String PREFERENCES_FILE_NAME = "IndexerCLI.properties";

	/** Short usage description of the CLI */
	private static final String USAGE = "java [-DconfigFile=<file>]  -Xms1g -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteIndexerCLI";
	
//	/// Parameter names
//
//	/**
//	 * Name of the parameter that must be set to ignore diacritics in multiword
//	 * term conflating
//	 */
//	public static final String P_IGNORE_DIACRITICS = IndexerModel.P_IGNORE_DIACRITICS;
//
//	/** Name of the parameter that must be set to enable variant detection */
//	public static final String P_TERM_VARIANT_DETECTION = "EnableTermGathering";
//
//	/** Name of the parameter that must be set to the edit distance classname */
//	public static final String P_EDIT_DISTANCE_CLASS = IndexerModel.P_EDIT_DISTANCE_CLASS;
//
//	/** Name of the parameter that must be set to the distance threshold */
//	public static final String P_EDIT_DISTANCE_THRESHOLD = IndexerModel.P_EDIT_DISTANCE_THRESHOLD;
//
//	/** Name of the parameter that must be set to the distance ngrams */
//	public static final String P_EDIT_DISTANCE_NGRAMS = IndexerModel.P_EDIT_DISTANCE_NGRAMS;
//
//	/** Name of the parameter that must be set to filter terms by frequency */
//	public static final String P_OCC_THRESHOLD = IndexerModel.P_FREQUENCY_THRESHOLD;
//
//	/** Name of the parameter that must be set to filter terms by frequency */
//	public static final String P_ASSOCIATION_MEASURE = "AssociationRateClassName";
//
//	/** Name of the parameter that must be set to filter terms by frequency */
//	public static final String P_FILTERING_THRESHOLD = IndexerModel.P_FILTERING_THRESHOLD;
//
//	/** Name of the parameter that must be set to filter terms by a given criteria */
//	public static final String P_FILTER_RULE = IndexerModel.P_FILTER_RULE;
//
//	/** Name of the parameter that must be set to keep verbs and other categories in TBX output */
//	public static final String P_KEEP_VERBS = IndexerModel.P_KEEP_VERBS;
//
//	/** Name of the parameter that must be set to the output directory */
//	public static final String P_OUTPUT_DIR = "Directory";
	
	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			// usage
			// java -DconfigFile=myPropertiesFileName -Xms1g  -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteIndexerCLI
			// if the option -DconfigFile is missing preferencesFileName is set to TermSuiteCLIUtils.USER_HOME+PREFERENCES_FILE_NAME
			String preferencesFileName = System.getProperty(
					TermSuiteCLIUtils.P_PROPERTIES_FILE,
					TermSuiteCLIUtils.USER_HOME + File.separator + PREFERENCES_FILE_NAME);
            UIMAFramework.getLogger().log(Level.INFO, "preferencesFileName : " + preferencesFileName);

			Properties storedProps = TermSuiteCLIUtils
					.readPropertiesFileName(preferencesFileName);

			// If this is the first time, create empty properties
			if (storedProps == null)
				storedProps = new Properties();

			// create the command line parser
			PosixParser parser = new PosixParser();

			// create the Options
			Options options = new Options();
			options.addOption(TermSuiteCLIUtils.createOption(
					TermSuiteCLIUtils.P_INPUT_DIR, null, true, "input directory", 
					TermSuiteCLIUtils.isNull(storedProps, TermSuiteCLIUtils.P_INPUT_DIR)));
			options.addOption(TermSuiteCLIUtils.createOption(
					TermSuiteCLIUtils.P_LANGUAGE, null, true, "language of the input files",
					TermSuiteCLIUtils.isNull(storedProps, TermSuiteCLIUtils.P_LANGUAGE)));
			options.addOption(TermSuiteCLIUtils.createOption(
					TermSuiteCLIUtils.P_ENCODING, null, true, "encoding of the input files", 
					TermSuiteCLIUtils.isNull(storedProps, TermSuiteCLIUtils.P_ENCODING)));

			// Indexer specific options
			options.addOption(TermSuiteCLIUtils.createOption(null,
                    IndexerBinding.CFG.OUTPUT.getParameter(), true, "output directory",
					TermSuiteCLIUtils.isNull(storedProps, IndexerBinding.CFG.OUTPUT.getParameter())));
			options.addOption(null, IndexerBinding.CFG.VARIANTDETECTION.getParameter(), false,
					"enable term gathering");
			options.addOption(null, IndexerBinding.CFG.EDITDISTANCECLS.getParameter(), true,
					"edit distance classname");
			options.addOption(null, IndexerBinding.CFG.EDITDISTANCETLD.getParameter(), true,
					"edit distance threshold");
			options.addOption(null, IndexerBinding.CFG.EDITDISTANCENGRAMS.getParameter(), true,
					"edit distance ngrams");
			options.addOption(null, IndexerBinding.CFG.IGNOREDIACRITICS.getParameter(), false,
					"ignore diacritics in multiword terms");
			options.addOption(null, IndexerBinding.CFG.FREQUENCYTLD.getParameter(), true,
					"occurence threshold");
			options.addOption(null, IndexerBinding.CFG.ASSOCIATIONMEASURE.getParameter(), true,
					"association rate class name");
			options.addOption(null, IndexerBinding.CFG.FILTERRULE.getParameter(), true, "filter rule");
			options.addOption(null, IndexerBinding.CFG.FILTERINGTLD.getParameter(), true,
					"threshold used by the filter rule");
			options.addOption(null, IndexerBinding.CFG.KEEPVERBS.getParameter(), false,
					"keep verbs and in TBX output");

			// Default values if necessary
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
                    IndexerBinding.CFG.VARIANTDETECTION.getParameter(), "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
                    IndexerBinding.CFG.IGNOREDIACRITICS.getParameter(), "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
                    IndexerBinding.CFG.KEEPVERBS.getParameter(), "false");

			try {
				// Parse and set CL options
				CommandLine line = parser.parse(options, args, false);
				for (Option myOption : line.getOptions()) {
					String optionKey = TermSuiteCLIUtils.getOptionKey(myOption);

					if (!myOption.hasArg())
						storedProps.setProperty(optionKey, "true");
					else
						storedProps.setProperty(optionKey, myOption.getValue());
				}

				// Check options that are required if other options are present
				if (!isNoneOrNull(storedProps, IndexerBinding.CFG.FILTERRULE.getParameter())
						&& TermSuiteCLIUtils.isNull(storedProps, IndexerBinding.CFG.FILTERINGTLD.getParameter()))
					throw new ParseException("Missing required parameter "
							+ IndexerBinding.CFG.FILTERINGTLD.getParameter()
							+ " for the specified filter '"
							+ storedProps.getProperty(IndexerBinding.CFG.FILTERRULE.getParameter()) + "'.");

				if ("true".equals(storedProps.getProperty(IndexerBinding.CFG.VARIANTDETECTION.getParameter()))
						&& !TermSuiteCLIUtils.isNull(storedProps, IndexerBinding.CFG.EDITDISTANCECLS.getParameter())) {
					
					if (TermSuiteCLIUtils.isNull(storedProps,
                            IndexerBinding.CFG.EDITDISTANCETLD.getParameter())) {
						throw new ParseException("Missing required parameter "
								+ IndexerBinding.CFG.EDITDISTANCETLD.getParameter()
								+ " to be used with the "
								+ IndexerBinding.CFG.EDITDISTANCECLS.getParameter());
					}

					if (TermSuiteCLIUtils.isNull(storedProps,
                            IndexerBinding.CFG.EDITDISTANCENGRAMS.getParameter())) {
						throw new ParseException("Missing required parameter "
								+ IndexerBinding.CFG.EDITDISTANCENGRAMS.getParameter()
								+ " to be used with the "
								+ IndexerBinding.CFG.EDITDISTANCECLS.getParameter());
					}
				}
					
				// Save props for next run
				TermSuiteCLIUtils.saveToUserHome(PREFERENCES_FILE_NAME, storedProps);

				// Create AE and configure
				AnalysisEngineDescription description = TermSuiteCLIUtils
						.getIndexerAEDescription(storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE));
				TermSuiteCLIUtils.setConfigurationParameters(description, storedProps);

                // FIXME
				TermSuiteRunner runner = new TermSuiteRunner(description,
						storedProps.getProperty(TermSuiteCLIUtils.P_INPUT_DIR),
						InputSource.InputSourceTypes.XMI,
						storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE),
						storedProps.getProperty(TermSuiteCLIUtils.P_ENCODING));

				// Run
				runner.execute();
				if (!SwingUtilities.isEventDispatchThread())
					runner.get();

			} catch (ParseException e) {
				TermSuiteCLIUtils.printUsage(e, USAGE, options); 
			}

		} catch (Exception e) {
            UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Determines whether <code>filter</code> is <code>null</code> or equals to
	 * {@link eu.project.ttc.tools.indexer.IndexerModel.FilterRules#None} in the given <code>properties</code> list.
	 * 
	 * @param properties
	 *            The property list
	 * @param filter
	 *            The filter name
	 * @return <code>true</code> if <code>filter</code> is <code>null</code> or
	 *         its value equals to {@link eu.project.ttc.tools.indexer.IndexerModel.FilterRules#None}, otherwise
	 *         <code>false</code>.
	 */
	private static boolean isNoneOrNull(Properties properties, String filter) {
		return TermSuiteCLIUtils.isNull(properties, filter)
				|| properties.getProperty(filter).equals(
						IndexerModel.FilterRules.None.name());
	}
}
