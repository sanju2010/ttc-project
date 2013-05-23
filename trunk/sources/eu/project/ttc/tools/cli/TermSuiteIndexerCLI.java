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

import eu.project.ttc.tools.config.IndexerSettings;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import eu.project.ttc.tools.InputSourceTypes;
import eu.project.ttc.tools.TermSuiteRunner;

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
	
	/// Parameter names
	
	/**
	 * Name of the parameter that must be set to ignore diacritics in multiword
	 * term conflating
	 */
	public static final String P_IGNORE_DIACRITICS = IndexerSettings.P_IGNORE_DIACRITICS;

	/** Name of the parameter that must be set to enable variant detection */
	public static final String P_TERM_VARIANT_DETECTION = "EnableTermGathering";

	/** Name of the parameter that must be set to the edit distance classname */
	public static final String P_EDIT_DISTANCE_CLASS = IndexerSettings.P_EDIT_DISTANCE_CLASS;

	/** Name of the parameter that must be set to the distance threshold */
	public static final String P_EDIT_DISTANCE_THRESHOLD = IndexerSettings.P_EDIT_DISTANCE_THRESHOLD;

	/** Name of the parameter that must be set to the distance ngrams */
	public static final String P_EDIT_DISTANCE_NGRAMS = IndexerSettings.P_EDIT_DISTANCE_NGRAMS;

	/** Name of the parameter that must be set to filter terms by frequency */
	public static final String P_OCC_THRESHOLD = IndexerSettings.P_FREQUENCY_THRESHOLD;

	/** Name of the parameter that must be set to filter terms by frequency */
	public static final String P_ASSOCIATION_MEASURE = "AssociationRateClassName";
	
	/** Name of the parameter that must be set to filter terms by frequency */
	public static final String P_FILTERING_THRESHOLD = IndexerSettings.P_FILTERING_THRESHOLD;

	/** Name of the parameter that must be set to filter terms by a given criteria */
	public static final String P_FILTER_RULE = IndexerSettings.P_FILTER_RULE;

	/** Name of the parameter that must be set to keep verbs and other categories in TBX output */
	public static final String P_KEEP_VERBS = IndexerSettings.P_KEEP_VERBS;
	
	/** Name of the parameter that must be set to the output directory */
	public static final String P_OUTPUT_DIR = "Directory";
	
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
			TermSuiteRunner.info("preferencesFileName : " + preferencesFileName);

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
					P_OUTPUT_DIR, true, "output directory",
					TermSuiteCLIUtils.isNull(storedProps, P_OUTPUT_DIR)));
			options.addOption(null, P_TERM_VARIANT_DETECTION, false,
					"enable term gathering");
			options.addOption(null, P_EDIT_DISTANCE_CLASS, true,
					"edit distance classname");
			options.addOption(null, P_EDIT_DISTANCE_THRESHOLD, true,
					"edit distance threshold");
			options.addOption(null, P_EDIT_DISTANCE_NGRAMS, true,
					"edit distance ngrams");
			options.addOption(null, P_IGNORE_DIACRITICS, false,
					"ignore diacritics in multiword terms");
			options.addOption(null, P_OCC_THRESHOLD, true,
					"occurence threshold");
			options.addOption(null, P_ASSOCIATION_MEASURE, true,
					"association rate class name");
			options.addOption(null, P_FILTER_RULE, true, "filter rule");
			options.addOption(null, P_FILTERING_THRESHOLD, true,
					"threshold used by the filter rule");
			options.addOption(null, P_KEEP_VERBS, false,
					"keep verbs and in TBX output");

			// Default values if necessary
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
					P_TERM_VARIANT_DETECTION, "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
					P_IGNORE_DIACRITICS, "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps, 
					P_KEEP_VERBS, "false");

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
				if (!isNoneOrNull(storedProps, P_FILTER_RULE)
						&& TermSuiteCLIUtils.isNull(storedProps, P_FILTERING_THRESHOLD))
					throw new ParseException("Missing required parameter "
							+ P_FILTERING_THRESHOLD
							+ " for the specified filter '"
							+ storedProps.getProperty(P_FILTER_RULE) + "'.");

				if ("true".equals(storedProps.getProperty(P_TERM_VARIANT_DETECTION))
						&& !TermSuiteCLIUtils.isNull(storedProps, P_EDIT_DISTANCE_CLASS)) {
					
					if (TermSuiteCLIUtils.isNull(storedProps,
							IndexerSettings.P_EDIT_DISTANCE_THRESHOLD)) {
						throw new ParseException("Missing required parameter "
								+ P_EDIT_DISTANCE_THRESHOLD
								+ " to be used with the "
								+ P_EDIT_DISTANCE_CLASS);
					}

					if (TermSuiteCLIUtils.isNull(storedProps,
							P_EDIT_DISTANCE_NGRAMS)) {
						throw new ParseException("Missing required parameter "
								+ P_EDIT_DISTANCE_NGRAMS
								+ " to be used with the "
								+ P_EDIT_DISTANCE_CLASS);
					}
				}
					
				// Save props for next run
				TermSuiteCLIUtils.saveToUserHome(PREFERENCES_FILE_NAME, storedProps);

				// Create AE and configure
				AnalysisEngineDescription description = TermSuiteCLIUtils
						.getIndexerAEDescription(storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE));
				TermSuiteCLIUtils.setConfigurationParameters(description, storedProps);
				
				TermSuiteRunner runner = new TermSuiteRunner(description,
						storedProps.getProperty(TermSuiteCLIUtils.P_INPUT_DIR),
						InputSourceTypes.XMI,
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
			TermSuiteRunner.error(e, 1);
		}
	}

	/**
	 * Determines whether <code>filter</code> is <code>null</code> or equals to
	 * {@link IndexerSettings.FilterRules#None} in the given <code>properties</code> list.
	 * 
	 * @param properties
	 *            The property list
	 * @param filter
	 *            The filter name
	 * @return <code>true</code> if <code>filter</code> is <code>null</code> or
	 *         its value equals to {@link IndexerSettings.FilterRules#None}, otherwise
	 *         <code>false</code>.
	 */
	private static boolean isNoneOrNull(Properties properties, String filter) {
		return TermSuiteCLIUtils.isNull(properties, filter)
				|| properties.getProperty(filter).equals(
						IndexerSettings.FilterRules.None.name());
	}
}
