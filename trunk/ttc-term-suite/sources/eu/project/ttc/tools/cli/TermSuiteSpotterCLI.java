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
import eu.project.ttc.tools.spotter.SpotterModel;
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
 * Command line interface for the SpotterController engines.
 * 
 * @author Damien Vintache
 */
public class TermSuiteSpotterCLI {

	/** Name of the file storing saved preferences */
	private static final String PREFERENCES_FILE_NAME = "SpotterCLI.properties";

	/** Short usage description of the CLI */
	private static final String USAGE = "java [-DconfigFile=<file>] -Xms1g -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteSpotterCLI";

	/// Parameter names
	
	/** Name of the parameter that must be set to the output directory */
	public static final String P_OUTPUT_DIR = "Directory";
	
	/** Name of the parameter that must be set to the target language */
	public static final String P_TREETAGGER_HOME_DIRECTORY = SpotterModel.P_TREETAGGER_HOME_DIRECTORY;
	
	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			// usage
			// java -DconfigFile=myPropertiesFileName -Xms1g  -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteSpotterCLI
			// if the option -DconfigFile is missing preferencesFileName is set to TermSuiteCLIUtils.USER_HOME+PREFERENCES_FILE_NAME
			String preferencesFileName = System.getProperty(
					TermSuiteCLIUtils.P_PROPERTIES_FILE,
					TermSuiteCLIUtils.USER_HOME + File.separator + PREFERENCES_FILE_NAME);
            UIMAFramework.getLogger().log(Level.INFO, "preferencesFileName : " + preferencesFileName);
			
			// read properties from the preferencesFileName
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

			// spotter specific options
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_OUTPUT_DIR, true, "output directory",
					TermSuiteCLIUtils.isNull(storedProps, P_OUTPUT_DIR)));
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_TREETAGGER_HOME_DIRECTORY, true, "TreeTagger home directory", 
					TermSuiteCLIUtils.isNull(storedProps, P_TREETAGGER_HOME_DIRECTORY)));

			try {
				// Parse and set CL options
				CommandLine line = parser.parse(options, args, false);

				for (Option myOption : line.getOptions()) {
                    UIMAFramework.getLogger().log(Level.INFO, myOption.getOpt() + " : "
							+ myOption.getLongOpt() + " : "
							+ myOption.getValue());

					String optionKey = TermSuiteCLIUtils.getOptionKey(myOption);

					if (!myOption.hasArg())
						storedProps.setProperty(optionKey, "true");
					else
						storedProps.setProperty(optionKey, myOption.getValue());
				}

				// Save props for next run
				TermSuiteCLIUtils.saveToUserHome(PREFERENCES_FILE_NAME, storedProps);

				// Create AE and configure
				AnalysisEngineDescription description = TermSuiteCLIUtils
						.getSpotterAEDescription(storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE));
				TermSuiteCLIUtils.setConfigurationParameters(description, storedProps);

                // FIXME
				TermSuiteRunner runner = new TermSuiteRunner(description,
						storedProps.getProperty(TermSuiteCLIUtils.P_INPUT_DIR),
						InputSource.InputSourceTypes.TXT,
						storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE),
						storedProps.getProperty(TermSuiteCLIUtils.P_ENCODING));

				// Run
				runner.execute();
				if (!SwingUtilities.isEventDispatchThread())
					runner.get();

				UIMAFramework.getLogger().log(Level.INFO, "Termspotter running finished");
			} catch (ParseException e) {
				TermSuiteCLIUtils.printUsage(e, USAGE, options); 
			}

		} catch (Exception e) {
		    e.printStackTrace();
            UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
		}
	}

}
