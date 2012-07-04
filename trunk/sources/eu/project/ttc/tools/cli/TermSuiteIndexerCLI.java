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

import java.util.Properties;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import eu.project.ttc.tools.InputSourceTypes;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.indexer.IndexerAdvancedSettings;
import eu.project.ttc.tools.indexer.TBXSettings;

/**
 * Command line interface for the Indexer engines.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class TermSuiteIndexerCLI {

	/** Name of the file storing saved preferences */
	private static final String PREFERENCES_FILE_NAME = "IndexerCLI.properties";

	/** Short usage description of the CLI */
	private static final String USAGE = "java -Xms1g -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteIndexerCLI";
	
	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {

			Properties storedProps = TermSuiteCLIUtils
					.readFromUserHome(PREFERENCES_FILE_NAME);

			// If this is the first time, create empty properties
			if (storedProps == null)
				storedProps = new Properties();

			// create the command line parser
			PosixParser parser = new PosixParser();

			// create the Options
			Options options = new Options();
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"directory", null, true, "input directory"));
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"language", null, true, "language of the input files"));
			options.addOption(TermSuiteCLIUtils.createMandatoryOption(
					"encoding", null, true, "encoding of the input files"));

			// Indexer specific options
			options.addOption(TermSuiteCLIUtils.createMandatoryOption("",
					"Directory", true, "output directory"));
			options.addOption("", "EnableTermGathering", false,
					"enable term gathering");
			options.addOption("",
					IndexerAdvancedSettings.P_EDIT_DISTANCE_CLASS, true,
					"edit distance classname");
			options.addOption("",
					IndexerAdvancedSettings.P_EDIT_DISTANCE_THRESHOLD, true,
					"edit distance threshold");
			options.addOption("",
					IndexerAdvancedSettings.P_EDIT_DISTANCE_NGRAMS, true,
					"edit distance ngrams");
			options.addOption("", IndexerAdvancedSettings.P_IGNORE_DIACRITICS,
					false, "ignore diacritics in multiword terms");
			options.addOption("",
					IndexerAdvancedSettings.P_FREQUENCY_THRESHOLD, true,
					"occurence threshold");
			options.addOption("", "AssociationRateClassName", true,
					"association rate class name");
			options.addOption("", TBXSettings.P_FILTER_RULE, true,
					"filter rule");
			options.addOption("", TBXSettings.P_FILTERING_THRESHOLD, true,
					"threshold used by the filter rule");
			options.addOption("", TBXSettings.P_KEEP_VERBS, false,
					"keep verbs and other categories in TBX output");

			// Default values if necessary
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
					"EnableTermGathering", "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
					IndexerAdvancedSettings.P_IGNORE_DIACRITICS, "false");
			TermSuiteCLIUtils.setToValueIfNotExists(storedProps,
					TBXSettings.P_KEEP_VERBS, "false");

			try {
				// Parse and set CL options
				CommandLine line = parser.parse(options, args, false);
				for (Option myOption : line.getOptions()) {
					String optionKey = TermSuiteCLIUtils.getOptionKey(myOption);
					if (!myOption.hasArg())
						storedProps.setProperty(optionKey, "true");

					System.out.println(myOption.getOpt() + " : "
							+ myOption.getLongOpt() + " : "
							+ myOption.getValue());

					storedProps.setProperty(optionKey, myOption.getValue());
				}

				// Save props for next run
				TermSuiteCLIUtils.saveToserHome(PREFERENCES_FILE_NAME, storedProps);

				// Create AE and configure
				AnalysisEngineDescription description = TermSuiteCLIUtils
						.getIndexerAEDescription(storedProps.getProperty("language"));
				TermSuiteCLIUtils.setConfigurationParameters(description, storedProps);
				
				TermSuiteRunner runner = new TermSuiteRunner(description,
						storedProps.getProperty("directory"),
						InputSourceTypes.XMI,
						storedProps.getProperty("language"),
						storedProps.getProperty("encoding"));

				// Run
				runner.execute();
				if (!SwingUtilities.isEventDispatchThread())
					runner.get();

			} catch (ParseException e) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(USAGE, options);
			}

		} catch (Exception e) {
			TermSuiteRunner.error(e, 1);
		}
	}

}
