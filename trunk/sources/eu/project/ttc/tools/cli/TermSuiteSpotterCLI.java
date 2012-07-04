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
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import eu.project.ttc.tools.InputSourceTypes;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.spotter.SpotterSettings;

/**
 * Command line interface for the Indexer engines.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class TermSuiteSpotterCLI {

	/** Name of the file storing saved preferences */
	private static final String PREFERENCES_FILE_NAME = "SpotterCLI.properties";

	/** Short usage description of the CLI */
	private static final String USAGE = "java -Xms1g -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteSpotterCLI";

	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			InputSourceTypes inputType = null;

			Properties storedProps = TermSuiteCLIUtils
					.readFromUserHome(PREFERENCES_FILE_NAME);

			// If this is the first time, create empty properties
			if (storedProps == null)
				storedProps = new Properties();

			// create the command line parser
			PosixParser parser = new PosixParser();

			// create the Options
			Options options = new Options();

			OptionGroup optionGroup = new OptionGroup();
			OptionBuilder.hasArg(false);
			optionGroup.addOption(OptionBuilder.create("txt"));
			optionGroup.addOption(OptionBuilder.create("xmi"));
			optionGroup.addOption(OptionBuilder.create("url"));
			optionGroup.isRequired();
			options.addOptionGroup(optionGroup);

			options.addOption(TermSuiteCLIUtils.createOption("directory", null,
					true, "input directory",
					storedProps.getProperty("directory") == null));
			options.addOption(TermSuiteCLIUtils.createOption("language", null,
					true, "language of the input files",
					storedProps.getProperty("language") == null));
			options.addOption(TermSuiteCLIUtils.createOption("encoding", null,
					true, "encoding of the input files",
					storedProps.getProperty("encoding") == null));

			// Indexer specific options
			// spotter specific options
			options.addOption(TermSuiteCLIUtils.createOption(null, "Directory",
					true, "output directory",
					storedProps.getProperty("Directory") == null));
			options.addOption(TermSuiteCLIUtils.createOption(
					null,
					SpotterSettings.P_TREETAGGER_HOME_DIRECTORY,
					true,
					"TreeTagger home directory",
					storedProps
							.getProperty(SpotterSettings.P_TREETAGGER_HOME_DIRECTORY) == null));

			try {
				// Parse and set CL options
				CommandLine line = parser.parse(options, args, false);
				if (line.hasOption("txt"))
					inputType = InputSourceTypes.TXT;
				else if (line.hasOption("uri"))
					inputType = InputSourceTypes.URI;
				else if (line.hasOption("xmi"))
					inputType = InputSourceTypes.XMI;

				for (Option myOption : line.getOptions()) {
					System.out.println(myOption.getOpt() + " : "
							+ myOption.getLongOpt() + " : "
							+ myOption.getValue());

					if (myOption.hasArg()) {
						
						String optionKey = TermSuiteCLIUtils
								.getOptionKey(myOption);

						if (!myOption.hasArg())
							storedProps.setProperty(optionKey, "true");
						else
							storedProps.setProperty(optionKey, myOption.getValue());
					}
				}

				// Save props for next run
				TermSuiteCLIUtils.saveToserHome(PREFERENCES_FILE_NAME,
						storedProps);

				// Create AE and configure
				AnalysisEngineDescription description = TermSuiteCLIUtils
						.getSpotterAEDescription(storedProps
								.getProperty("language"));
				TermSuiteCLIUtils.setConfigurationParameters(description,
						storedProps);

				TermSuiteRunner runner = new TermSuiteRunner(description,
						storedProps.getProperty("directory"), inputType,
						storedProps.getProperty("language"),
						storedProps.getProperty("encoding"));

				// Run
				runner.execute();
				if (!SwingUtilities.isEventDispatchThread())
					runner.get();

			} catch (ParseException e) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(80);
				formatter
						.setOptionComparator(new ShortOptionsFirstComparator());
				formatter.printHelp(USAGE, options);
			}

		} catch (Exception e) {
			TermSuiteRunner.error(e, 1);
		}
	}

}
