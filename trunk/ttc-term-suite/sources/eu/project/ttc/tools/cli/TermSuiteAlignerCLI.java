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

import eu.project.ttc.tools.aligner.AlignerBinding;
import eu.project.ttc.tools.aligner.AlignerModel;
import eu.project.ttc.tools.commons.InputSource;
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
 * Command line interface for the Aligner engines.
 * 
 * @author Damien Vintache
 */
public class TermSuiteAlignerCLI {

	/** Name of the file storing saved preferences */
	private static final String PREFERENCES_FILE_NAME = "AlignerCLI.properties";

	/** Short usage description of the CLI */
	private static final String USAGE = "java [-DconfigFile=<file>] -Xms1g -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteAlignerCLI";

	/// Parameter names
	/** Name of the parameter that must be set to the output dir path */
	public static final String P_OUTPUT_DIRECTORY = AlignerBinding.PRM.OUTPUT.getParameter();
	
	/** Name of the parameter that must be set to the source language */
	public static final String P_SOURCE_LANGUAGE = AlignerBinding.PRM.SRCLANGUAGE.getParameter();
	
	/** Name of the parameter that must be set to the target language */
	public static final String P_TARGET_LANGUAGE = AlignerBinding.PRM.TGTLANGUAGE.getParameter();
	
	/** Name of the parameter that must be set to the source terminology XMI file */
	public static final String P_SOURCE_TERMINOLOGY = AlignerBinding.PRM.SRCTERMINOLOGY.getParameter();
	
	/** Name of the parameter that must be set to the target terminology XMI file */
	public static final String P_TARGET_TERMINOLOGY = AlignerBinding.PRM.TGTTERMINOLOGY.getParameter();
	
	/** Name of the parameter that must be set to the bilingual dictionary */
	public static final String P_BILINGUAL_DICTIONARY = AlignerBinding.PRM.DICTIONARY.getParameter();
	
	/** Name of the parameter that must be set to the bilingual dictionary */
	public static final String P_EVALUATION_DIRECTORY = AlignerBinding.PRM.EVALDIR.getParameter();

	/** Name of the parameter that must be set to enable the compositional method */
	public static final String P_METHOD_COMPOSITIONAL = AlignerBinding.PRM.COMPOSITIONAL.getParameter();

	/** Name of the parameter that must be set to enable the semicompositional method */
    public static final String P_METHOD_SEMIDISTRIBUTIONAL = AlignerBinding.PRM.SEMIDISTRIBUTIONAL.getParameter();
    
	/** Name of the parameter that must be set to enable the distributional method */
	public static final String P_METHOD_DISTRIBUTIONAL = AlignerBinding.PRM.DISTRIBUTIONAL.getParameter();

	/** Name of the parameter that must be set to enable the distributional method */
	public static final String P_SIMILARITY_DISTANCE = AlignerBinding.PRM.SIMILARITY.getParameter();

	/** Name of the parameter that must be set to limit the number of translation candidates */
	public static final String P_MAX_CANDIDATES = AlignerBinding.PRM.MAXCANDIDATES.getParameter();
	
	/** Name of the parameter that must be set to the output directory */
	public static final String P_OUTPUT_DIR = AlignerBinding.PRM.OUTPUT.getParameter();
	
	/**
	 * Application entry point
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			// usage
			// java -DconfigFile=myPropertiesFileName -Xms1g  -Xmx2g -cp ttc-term-suite-1.3.jar eu.project.ttc.tools.cli.TermSuiteAlignerCLI
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

			// Aligner specific options
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_OUTPUT_DIR, true, "output directory",
					TermSuiteCLIUtils.isNull(storedProps, P_OUTPUT_DIR)));

			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_OUTPUT_DIRECTORY, true, "aligner output directory",
					TermSuiteCLIUtils.isNull(storedProps, P_OUTPUT_DIRECTORY)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_SOURCE_LANGUAGE, true, "source language",
					TermSuiteCLIUtils.isNull(storedProps, P_SOURCE_LANGUAGE)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_TARGET_LANGUAGE, true, "target language",
					TermSuiteCLIUtils.isNull(storedProps, P_TARGET_LANGUAGE)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_SOURCE_TERMINOLOGY, true, "source terminology file",
					TermSuiteCLIUtils.isNull(storedProps, P_SOURCE_TERMINOLOGY)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_TARGET_TERMINOLOGY, true, "target terminology file",
					TermSuiteCLIUtils.isNull(storedProps, P_TARGET_TERMINOLOGY)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_BILINGUAL_DICTIONARY, true, "bilingual dictionary file",
					TermSuiteCLIUtils.isNull(storedProps, P_BILINGUAL_DICTIONARY)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_EVALUATION_DIRECTORY, true, "evaluation inputOutput translation directory",
					TermSuiteCLIUtils.isNull(storedProps, P_EVALUATION_DIRECTORY)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_METHOD_COMPOSITIONAL, true, "CompositionalMethod",
					TermSuiteCLIUtils.isNull(storedProps, P_METHOD_COMPOSITIONAL)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_METHOD_DISTRIBUTIONAL, true, "DistributionalMethod",
					TermSuiteCLIUtils.isNull(storedProps, P_METHOD_DISTRIBUTIONAL)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
                    P_METHOD_SEMIDISTRIBUTIONAL, true, "SemidistributionalMethod",
                    TermSuiteCLIUtils.isNull(storedProps, P_METHOD_SEMIDISTRIBUTIONAL)));

			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_SIMILARITY_DISTANCE, true, "SimilarityDistanceClassName",
					TermSuiteCLIUtils.isNull(storedProps, P_SIMILARITY_DISTANCE)));

			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_MAX_CANDIDATES, true, "MaxTranslationCandidates",
					TermSuiteCLIUtils.isNull(storedProps, P_MAX_CANDIDATES)));
			
			options.addOption(TermSuiteCLIUtils.createOption(null,
					P_OUTPUT_DIR, true, "Directory",
					TermSuiteCLIUtils.isNull(storedProps, P_OUTPUT_DIR)));
		
				
			try {
				// Parse and set CL options
				CommandLine line = parser.parse(options, args, false);

				for (Option myOption : line.getOptions()) {
					System.out.println(myOption.getOpt() + " : "
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
						.getAlignerAEDescription(storedProps.getProperty(TermSuiteCLIUtils.P_LANGUAGE));
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

			} catch (ParseException e) {
				TermSuiteCLIUtils.printUsage(e, USAGE, options); 
			}

		} catch (Exception e) {
            UIMAFramework.getLogger().log(Level.SEVERE, e.getMessage());
		}
	}

}
